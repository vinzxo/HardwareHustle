import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MainMenuController;
import util.DatabaseHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Initialize DB first to make sure tables exist
            DatabaseHandler.getConnection();
            util.DatabaseHandler.seedQuestions();

            // 2. Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            // 3. Set the Controller's stage
            MainMenuController controller = loader.getController();
            controller.setStage(primaryStage);

            // 4. Create and Show the Scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Hardware Hustle");
            
            // This line makes sure the window actually appears!
            primaryStage.show();
            
            // Optional: Start maximized to fix the "white bars"
            primaryStage.setMaximized(true);

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Game launched but failed to load UI.");
            e.printStackTrace(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}