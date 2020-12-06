package java_.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java_.util.MessageOfTheDayService;

import java.util.ResourceBundle;

/**
 * The controller for the main menu interface.
 */
public class MainMenuController implements Initializable {

    @FXML
    public VBox mainBox;

    @FXML
    private Label messageOfTheDay;

    private static final String NEW_GAME_PATH = "../../view/layout/newGame.fxml";
    private static final String LOAD_GAME_PATH = "../../view/layout/loadGame.fxml";
    private static final String CUSTOMISE_PROFILE_PATH = "../../view/layout/PlayerPiecePurchase.fxml";
    private static final String LEADERBOARD_PATH = "../../view/layout/leaderboard.fxml";
    private static final String SETTINGS_PATH = "../../view/layout/settings.fxml";
    private static final String REGISTER_PATH = "../../view/layout/register.fxml";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";

    /**
     * Initialises the main menu, setting its background and setting the message of the day.
     * @param location
     * @param resources
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
        mainBox.setBackground(new Background(backgroundImage));
        mainBox.setMinWidth(614);

        try {
            messageOfTheDay.setText(MessageOfTheDayService.getMessage());
        } catch (IOException e) {
            messageOfTheDay.setText("Unable to get the message of the day.");
        }
    }

    /**
     * Opens the new game screen.
     * @param e When the newGame button is clicked.
     * @throws IOException If the new game file path is incorrect.
     */
    @FXML
    private void onNewGameButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = FXMLLoader.load(getClass().getResource(NEW_GAME_PATH));
        currentStage.setScene(new Scene(newGame));
    }


    /**
     * Opens the load game screen.
     * @param e When the loadGame button is clicked.
     * @throws IOException If the load game file path is incorrect.
     */
    @FXML
    private void onLoadGameButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane loadGame = FXMLLoader.load(getClass().getResource(LOAD_GAME_PATH));
        currentStage.setScene(new Scene(loadGame));
    }

    /**
     * Opens the customise profile screen.
     * @param e When the customise profile button is clicked.
     * @throws IOException If the customise profile file path is incorrect.
     */
    @FXML
    private void onCustomiseProfileButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane customiseProfile = FXMLLoader.load(getClass().getResource(CUSTOMISE_PROFILE_PATH));
        currentStage.setScene(new Scene(customiseProfile));
    }

    /**
     * Opens the leaderboard screen.
     * @param e When the leaderboard button is clicked.
     * @throws IOException If the leaderboard file path is incorrect.
     */
    @FXML
    private void onLeaderboardButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = FXMLLoader.load(getClass().getResource(LEADERBOARD_PATH));
        currentStage.setScene(new Scene(newGame));
    }

    /**
     * Opens the register screen.
     * @param e When the register button is clicked.
     * @throws IOException If the register file path is incorrect
     */
    @FXML
    private void onRegisterButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = FXMLLoader.load(getClass().getResource(REGISTER_PATH));
        currentStage.setScene(new Scene(newGame));
    }

    /**
     * Quits the game. Closes all windows.
     * @param e When the quit button is clicked.
     */
    @FXML
    private void onQuitButtonClicked(ActionEvent e) {
        System.exit(0);
    }
}
