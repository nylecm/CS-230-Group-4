package java_;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main Class used to run the game.
 *
 * @author nylecm
 */
public class MainMenuMain extends Application {

    private static final int WIDTH = 720;
    private static final int HEIGHT = 576;
    private static final String MAIN_MENU_FXML = "/resources/mainMenu.fxml";

    /**
     * Starts the game, showing the main menu.
     *
     * @param primaryStage not used
     */
    public void start(Stage primaryStage) {
        try {
            Pane root = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_FXML));
            Scene scene = new Scene(root, WIDTH, HEIGHT);

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
