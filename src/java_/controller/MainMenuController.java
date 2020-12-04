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
     * Sets the message of the day in the main menu.
     * @throws IOException If the message of the day cannot be retrieved.
     */
    private void setMessageOfTheDay() throws IOException {
        messageOfTheDay.setText(MessageOfTheDayService.getMessage());
    }

    /**
     * Initialises the main menu, setting its background and setting the message of the day.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File(URANUS_BACKGROUND_PATH).toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainBox.setBackground(new Background(backgroundFill));

        try {
            setMessageOfTheDay();
        } catch (IOException e) {
            e.printStackTrace();
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
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource(NEW_GAME_PATH));
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
        Pane loadGame = (Pane) FXMLLoader.load(getClass().getResource(LOAD_GAME_PATH));
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
        Pane customiseProfile = (Pane) FXMLLoader.load(getClass().getResource(CUSTOMISE_PROFILE_PATH));
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
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource(LEADERBOARD_PATH));
        currentStage.setScene(new Scene(newGame));
    }

    /**
     * Opens the settings screen.
     * @param e When the settings button is clicked.
     * @throws IOException If the setting file path is incorrect
     */
    @FXML
    private void onSettingsButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource(SETTINGS_PATH));
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
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource(REGISTER_PATH));
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
