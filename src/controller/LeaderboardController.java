package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import util.DatabaseHandler;
import java.sql.*;

public class LeaderboardController {
    private Stage stage;
    private String previousScene = "/view/MainMenu.fxml"; 

    @FXML private ListView<String> leaderboardListView;
    public void setStage(Stage stage) {
        this.stage = stage;
        loadLeaderboard();
    }
    public void setPreviousScene(String path) {
        this.previousScene = path;
    }

    private void loadLeaderboard() {
        if (leaderboardListView != null) {
            leaderboardListView.getItems().clear();
        }

        String sql = "SELECT username, score FROM leaderboard ORDER BY score DESC LIMIT 10";

        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                String entry = String.format("%d.  %-15s %d pts", 
                                rank, 
                                rs.getString("username"), 
                                rs.getInt("score"));
                leaderboardListView.getItems().add(entry);
                rank++;
            }

            if (rank == 1) {
                leaderboardListView.getItems().add("No high scores yet. Be the first!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(previousScene));
            Parent root = loader.load();
            
            // Define 'controller' so we can use it below
            Object nextController = loader.getController();
            
            if (nextController instanceof MainMenuController) {
                ((MainMenuController) nextController).setStage(stage);
            } else if (nextController instanceof GameMenuController) {
                ((GameMenuController) nextController).setStage(stage);
            }
            
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}