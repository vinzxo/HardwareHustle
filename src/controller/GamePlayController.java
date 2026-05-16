package controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.DatabaseHandler;
import java.sql.*;

public class GamePlayController {
    private Stage stage;
    private int score = 0;
    private int lives = 3;
    private int timeLeft = 7;
    private String correctAnswer;
    private Timeline timer;
    private AnimationTimer scrollTimer;

    @FXML private Label questionLabel, scoreLabel, timerLabel, heart1, heart2, heart3;
    @FXML private Button path1, path2, path3;
    @FXML private ImageView studentSprite, enemySprite;
    @FXML private ImageView background1, background2;

    private final double SCROLL_SPEED = 2.5; 

    public void setStage(Stage stage) {
        this.stage = stage;
        
        // 1. Force backgrounds to stretch and fill the stage
        background1.fitWidthProperty().bind(stage.widthProperty());
        background1.fitHeightProperty().bind(stage.heightProperty());
        background2.fitWidthProperty().bind(stage.widthProperty());
        background2.fitHeightProperty().bind(stage.heightProperty());

        // 2. Initialize movement
        startBackgroundScroll(); 
        startNewRound();
    }

    public void startBackgroundScroll() {
        // Position background2 immediately to the right of the screen
        // We use stage.getWidth() instead of a hardcoded 1920
        background2.setTranslateX(stage.getWidth());

        scrollTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentWidth = stage.getWidth();
                
                // Move both left
                background1.setTranslateX(background1.getTranslateX() - SCROLL_SPEED);
                background2.setTranslateX(background2.getTranslateX() - SCROLL_SPEED);

                // Loop background1: If it goes off-screen, move it to the right of background2
                if (background1.getTranslateX() <= -currentWidth) {
                    background1.setTranslateX(background2.getTranslateX() + currentWidth);
                }

                // Loop background2: If it goes off-screen, move it to the right of background1
                if (background2.getTranslateX() <= -currentWidth) {
                    background2.setTranslateX(background1.getTranslateX() + currentWidth);
                }
            }
        };
        scrollTimer.start();
    }
    
    private void startNewRound() {
        if (timer != null) timer.stop(); 
        loadNewQuestion();
        startTimer();
    }

    private void startTimer() {
        timeLeft = 7;
        updateTimerLabel();
        
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            updateTimerLabel();
            if (timeLeft <= 0) {
                timer.stop(); 
                handleWrongAnswer(); 
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void loadNewQuestion() {
        resetButtonStyles();
        String sql = "SELECT * FROM questions ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                questionLabel.setText(rs.getString("question"));
                path1.setText(rs.getString("optionA"));
                path2.setText(rs.getString("optionB"));
                path3.setText(rs.getString("optionC"));
                correctAnswer = rs.getString("correctAnswer");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handlePathSelection(ActionEvent event) {
        timer.stop(); 
        Button clicked = (Button) event.getSource();
        
        String choice = (clicked == path1) ? "A" : (clicked == path2 ? "B" : "C");

        if (choice.equalsIgnoreCase(correctAnswer)) {
            score += 10;
            scoreLabel.setText("SCORE: " + String.format("%05d", score));
            clicked.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-border-color: white;");
            
            PauseTransition pause = new PauseTransition(Duration.millis(400));
            pause.setOnFinished(e -> startNewRound());
            pause.play();
        } else {
            clicked.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
            handleWrongAnswer();
        }
    }

    private void handleWrongAnswer() {
        lives--;
        updateHeartsUI();
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), enemySprite);
        tt.setByX(60); 
        tt.play();

        if (lives <= 0) {
            if (timer != null) timer.stop();
            if (scrollTimer != null) scrollTimer.stop(); 
            showGameOver();
        } else {
            PauseTransition pause = new PauseTransition(Duration.millis(500));
            pause.setOnFinished(e -> startNewRound());
            pause.play();
        }
    }

    private void updateHeartsUI() {
        if (lives == 2) heart3.setVisible(false);
        else if (lives == 1) heart2.setVisible(false);
        else if (lives == 0) heart1.setVisible(false);
    }

    private void updateTimerLabel() {
        timerLabel.setText(timeLeft + "s");
        if (timeLeft <= 3) {
            timerLabel.setStyle("-fx-background-color: rgba(180, 0, 0, 0.4); -fx-text-fill: #ff2e63; -fx-border-color: #ff2e63; -fx-border-width: 2; -fx-padding: 5 15 5 15; -fx-font-family: 'Consolas'; -fx-font-size: 20px; -fx-font-weight: bold;");
        } else {
            timerLabel.setStyle("-fx-background-color: rgba(20, 20, 40, 0.8); -fx-text-fill: #00d2ff; -fx-border-color: #00d2ff; -fx-border-width: 2; -fx-padding: 5 15 5 15; -fx-font-family: 'Consolas'; -fx-font-size: 18px; -fx-font-weight: bold;");
        }
    }

    private void showGameOver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameOver.fxml"));
            Parent root = loader.load();
            GameOverController controller = loader.getController();
            controller.setStage(stage);
            controller.initData(this.score); 
            stage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void resetButtonStyles() {
        String style = "-fx-background-color: rgba(22, 33, 62, 0.8); -fx-text-fill: white; -fx-border-color: #00d2ff; -fx-border-width: 2;";
        path1.setStyle(style);
        path2.setStyle(style);
        path3.setStyle(style);
    }
}