import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The type java.java_.Main.
 */
public class LogInMain extends Application {
    private final static int WIDTH = 280;
    private final static int HEIGHT = 220 + 20; //todo discuss windows height problem...

    public void start(Stage primaryStage) {
        try {
            Pane root = (Pane) FXMLLoader.load(getClass().getResource("view/layout/log_in.fxml"));
            Scene scene = new Scene(root,WIDTH,HEIGHT);

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
