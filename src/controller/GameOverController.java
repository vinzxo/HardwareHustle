package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.DatabaseHandler;
import util.Session;
import java.sql.*;

public class GameOverController {
    private Stage stage;

    @FXML private Label finalScoreLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initData(int score) {
        finalScoreLabel.setText("FINAL SCORE: " + score);
        saveScoreToDatabase(score);
    }

    private void saveScoreToDatabase(int score) {
        String username = Session.getUser();
        if (username == null) return;

        String sql = "INSERT INTO leaderboard (username, score) VALUES (?, ?)";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            System.out.println("Score saved for " + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameMenu.fxml"));
            Parent root = loader.load();
            GameMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}