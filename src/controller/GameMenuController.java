package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class GameMenuController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        switchScene("/view/GamePlay.fxml");
    }

    @FXML
    private void handleLeaderboard(ActionEvent event) {
        switchScene("/view/Leaderboard.fxml");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Object controller = loader.getController();
            
            if (controller instanceof GamePlayController) {
                ((GamePlayController) controller).setStage(stage);
            }
            if (controller instanceof LeaderboardController) {
                LeaderboardController lc = (LeaderboardController) controller;
                lc.setStage(stage);
                lc.setPreviousScene("/view/GameMenu.fxml");
            } else if (controller instanceof LoginController) {
                ((LoginController) controller).setStage(stage);
            }

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("FATAL ERROR: Could not load " + fxmlPath);
            e.printStackTrace();
        }
    }
}