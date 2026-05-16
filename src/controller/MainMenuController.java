package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainMenuController {
    private Stage stage;
    public String previousScene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setPreviousScene(String path) {
    this.previousScene = path;
}

    @FXML
    private void handleLogin(ActionEvent event) {
        switchScene("/view/Login.fxml", "Login");
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        switchScene("/view/Signup.fxml", "Sign Up");
    }
    
    @FXML
    private void handleLeaderboard(ActionEvent event) {
        switchScene("/view/Leaderboard.fxml", "Leaderboard");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        this.previousScene = "/view/MainMenu.fxml";
        switchScene("/view/MainMenu.fxml", "Main Menu");
    }

    private void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof LeaderboardController) {
            LeaderboardController lc = (LeaderboardController) controller;
            lc.setStage(stage);
            lc.setPreviousScene("/view/MainMenu.fxml"); // This is the "Directions"
            }
            if (controller instanceof LoginController) ((LoginController) controller).setStage(stage);
            if (controller instanceof SignupController) ((SignupController) controller).setStage(stage);
            if (controller instanceof MainMenuController) ((MainMenuController) controller).setStage(stage);
            else if (controller instanceof GameMenuController) {
                ((GameMenuController) controller).setStage(stage);
            }
            
            stage.getScene().setRoot(root);
            stage.setTitle("Hardware Hustle - " + title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}