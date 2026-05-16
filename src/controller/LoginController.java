package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.DatabaseHandler;
import util.Session;

import java.sql.*;

public class LoginController {
    private Stage stage;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            updateStatus("Please enter credentials.", "red");
            return;
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { 
                Session.setUser(user);
                updateStatus("Welcome, " + user + "! Redirecting...", "green");
                
                // 1.5 second pause to show the welcome message
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> {
                    try {
                        // Transitions to the Game Menu (Hub)
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameMenu.fxml"));
                        Parent root = loader.load();
                        
                        GameMenuController controller = loader.getController();
                        controller.setStage(stage);
                        
                        stage.getScene().setRoot(root);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        updateStatus("Error loading Game Menu.", "red");
                    }
                });
                pause.play();
                
            } else {
                updateStatus("Invalid username or password.", "red");
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateStatus("Database connection error.", "red");
        }
    }

    private void updateStatus(String message, String color) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setStyle("-fx-text-fill: " + color + ";");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Signup.fxml"));
            Parent root = loader.load();
            SignupController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();
            MainMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}