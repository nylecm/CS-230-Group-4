package java_.controller;

import java_.game.controller.CoinHandler;
import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.util.Reader;
import java_.util.security.LoginHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * The controller for the load game interface.
 */
public class LoadGameController implements Initializable {
    @FXML
    private Label loadGameStatusLabel;

    @FXML
    private VBox mainPane;

    @FXML
    private HBox logInFormHBox;

    @FXML
    private ChoiceBox<File> loadGameSelect;

    private static final String SAVES_FOLDER_DIRECTORY = "data\\saves";
    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";
    private static final String GAME_PATH = "../../view/layout/game.fxml";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";
    private static final String DELIMITER = "`";

    private static final String WRONG_PASSWORD_MESSAGE = "Wrong Password for player(s): ";
    private static final String MULTIPLE_WRONG_PASSWORD_SEPARATOR = " & ";
    public static final String GAME_SAVE_FILE_NOT_FOUND_MSG = "Game save file not found!";
    public static final String PLAYER_PIECE_URL_ERROR_MSG =
            "Problem handling player piece images, ensure they are still in the player piece directory.";

    private final LinkedList<VBox> logInVBoxes = new LinkedList<>();

    /**
     * Initialises the load game interface, giving it a background, displaying available game saved that can be loaded.
     *
     * @param location  The location (not used).
     * @param resources the resources (not used).
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

        Reader r = new Reader();
        File[] fileNames = r.readFileNames(SAVES_FOLDER_DIRECTORY);
        addSaveFileNames(fileNames);

        loadGameSelect.setOnAction(event -> {
            if (loadGameSelect.getValue() != null) {
                try {
                    logInVBoxes.clear();
                    logInFormHBox.getChildren().clear();
                    readFileForPlayerInfo(loadGameSelect.getValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Reads a specific file to give player info on the players in that game save.
     * This data includes usernames, player pieces, and passwords so the users can login to authorise themselves to play the game.
     *
     * @param file The file that contains the player data.
     * @throws FileNotFoundException If the file containing the player data cannot be found.
     */
    private void readFileForPlayerInfo(File file) throws FileNotFoundException {
        System.out.println(file);
        Scanner in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        String nPlayersStr = in.next();
        int nPlayers = Integer.parseInt(nPlayersStr);
        System.out.println(nPlayers);
        in.nextLine();
        in.nextLine();
        Image[] playerPieces = new Image[nPlayers];

        for (int i = 0; i < nPlayers; i++) {
            try {
                playerPieces[i] = new Image(String.valueOf(new File(in.next()).toURI().toURL())); // check this
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        in.nextLine();

        String[] playerUsernames = new String[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            playerUsernames[i] = in.next();
            in.nextLine();
        }
        in.close();

        for (int i = 0; i < nPlayers; i++) {
            createPasswordEntryForm(playerUsernames[i], playerPieces[i]);
        }
    }

    /**
     * Creates a form for password entry so the user can verify themselves and gain authorisation to play a loaded saved game.
     *
     * @param playerUsername The username of the player who the form is being generated for so they can enter their password.
     * @param playerPiece    The player piece of the player who needs to enter their credentials.
     */
    private void createPasswordEntryForm(String playerUsername, Image playerPiece) {
        VBox userLogInBox = new VBox();
        Label usernameLabel = new Label(playerUsername);
        PasswordField passwordField = new PasswordField();
        ImageView playerPieceView = new ImageView(playerPiece);

        userLogInBox.getChildren().add(usernameLabel);
        userLogInBox.getChildren().add(passwordField);
        userLogInBox.getChildren().add(playerPieceView);

        logInFormHBox.getChildren().add(userLogInBox);
        logInVBoxes.addLast(userLogInBox);
    }

    /**
     * Adds the names of save files to the interface so the user can select which save they want to load.
     *
     * @param fileNames The names of the files to be displayed on the interface
     */
    private void addSaveFileNames(File[] fileNames) {
        loadGameSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    /**
     * Returns the user to the main menu.
     *
     * @param e The event that the user has clicked on the back button
     * @throws IOException If the main menu file path is incorrect.
     */
    public void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Loads the saved game selected by the user.
     *
     * @param e The user clicks on the load game button
     * @throws IOException If no save game is selected and the user attempts to load a game.
     */
    public void onLoadGameButtonClicked(ActionEvent e) throws IOException {
        int numberOfIncorrectPasswordsEntered = 0;

        for (int i = 0; i < logInVBoxes.size(); i++) {
            VBox userLoginForm = logInVBoxes.get(i);
            Label userUsername = (Label) userLoginForm.getChildren().get(0);
            PasswordField userPassword = (PasswordField) userLoginForm.getChildren().get(1);
            if (!LoginHandler.login(userUsername.getText(), userPassword.getText())) {
                numberOfIncorrectPasswordsEntered++;
                if (numberOfIncorrectPasswordsEntered == 1) {
                    loadGameStatusLabel.setText(WRONG_PASSWORD_MESSAGE + (i + 1));
                } else {
                    loadGameStatusLabel.setText(loadGameStatusLabel.getText().concat
                            (MULTIPLE_WRONG_PASSWORD_SEPARATOR + (i + 1)));
                }
            }
        }

        if (numberOfIncorrectPasswordsEntered < 1) {
            try {
                GameService.getInstance().loadSavedInstance(loadGameSelect.getValue());
                for (Player player : GameService.getInstance().getPlayerService().getPlayers()) {
                    CoinHandler.increaseDailyStreak(player.getUsername());
                }
            } catch (FileNotFoundException ex) {
                loadGameStatusLabel.setText(GAME_SAVE_FILE_NOT_FOUND_MSG);
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                loadGameStatusLabel.setText(PLAYER_PIECE_URL_ERROR_MSG);
                ex.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }

            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Pane game = (Pane) FXMLLoader.load(getClass().getResource(GAME_PATH));
            currentStage.setScene(new Scene(game));
        }
    }
}
