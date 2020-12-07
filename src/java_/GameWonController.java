package java_;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the game won screen that is displayed when a player wins the game.
 */
public class GameWonController implements Initializable {

    @FXML
    private Label playerWhoWon;

    @FXML
    private VBox mainPane;

    private static final String WON_MSG = " won!";
    private static final String MAIN_MENU_PATH = "/resources/mainMenu.fxml";
    private static final String URANUS_BACKGROUND_PATH = "src/resources/space_uranus.png";

    /**
     * Returns the user to the main menu.
     * @param e The action of the main menu button being clicked.
     * @throws IOException If the main menu file path is incorrect.
     */
    public void onBackToMainMenuButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Initialises the game won controller, giving it a background image and displaying the player that won the game.
     * @param location The location (not used).
     * @param resources The resources (not used).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundImage backgroundImage = null;
        try {
            backgroundImage = new BackgroundImage(new Image(String.valueOf
                    (new File(URANUS_BACKGROUND_PATH).toURI().toURL())), BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize
                    (0, 0, false, false, false, true));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainPane.setBackground(new Background(backgroundImage));
        mainPane.setMinWidth(614);

        int playerNumber = GameService.getInstance().getCurrentPlayerNum();
        playerWhoWon.setText(GameService.getInstance().getPlayerService().getPlayer(playerNumber).getUsername() + WON_MSG);
    }
}
