import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The type java.Main.
 */
public class Main extends Application {
    public void start(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("view/layout/mainMenu.fxml"));
            Scene scene = new Scene(root, 780, 576);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
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
