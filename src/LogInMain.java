import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The type Main.
 */
public class LogInMain extends Application {
    public void start(Stage primaryStage) {
        try {
            Pane root = (Pane) FXMLLoader.load(getClass().getResource("log_in.fxml"));
            Scene scene = new Scene(root,400,200);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
