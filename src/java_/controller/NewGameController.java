package java_.controller;

import java_.game.controller.GameService;
import java_.game.controller.PurchaseHandler;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.util.Reader;
import java_.util.security.LoginHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Controls the new game user interface for creating a new game with up to four players and on a game board of the user's choice.
 */
public class NewGameController implements Initializable {

    @FXML
    private VBox mainBox;

    @FXML
    private ChoiceBox<String> gameBoardSelect;

    @FXML
    private VBox player1SetUpVBox;

    @FXML
    private TextField player1Username;

    @FXML
    private PasswordField player1Password;

    @FXML
    private Label player1LogInStatusLabel;

    @FXML
    private VBox player1PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<String> player1PlayerPieceSelect;

    @FXML
    public Label player1PlayerPieceStatus;

    @FXML
    private ImageView player1PlayerPieceImage;

    @FXML
    private VBox player2SetUpVBox;

    @FXML
    private TextField player2Username;

    @FXML
    private PasswordField player2Password;

    @FXML
    private Label player2LogInStatusLabel;

    @FXML
    private VBox player2PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<String> player2PlayerPieceSelect;

    @FXML
    public Label player2PlayerPieceStatus;

    @FXML
    private ImageView player2PlayerPieceImage;

    @FXML
    private VBox player3SetUpVBox;

    @FXML
    private TextField player3Username;

    @FXML
    private PasswordField player3Password;

    @FXML
    private Label player3LogInStatusLabel;

    @FXML
    private VBox player3PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<String> player3PlayerPieceSelect;

    @FXML
    public Label player3PlayerPieceStatus;

    @FXML
    private ImageView player3PlayerPieceImage;

    @FXML
    private VBox player4SetUpVBox;

    @FXML
    private TextField player4Username;

    @FXML
    private PasswordField player4Password;

    @FXML
    private Label player4LogInStatusLabel;

    @FXML
    private VBox player4PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<String> player4PlayerPieceSelect;

    @FXML
    public Label player4PlayerPieceStatus;

    @FXML
    private ImageView player4PlayerPieceImage;

    @FXML
    private Label startGameStatusLabel;

    private final static String LOG_IN_SUCCESS_STRING = "Login Successful!";
    private final static String LOG_IN_FAILURE_STRING = "Invalid/Duplicate Credentials!";
    private final static String NO_PLAYER_PIECE_SELECTED_MSG = "Player Piece Not Selected!";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";
    private final static int MIN_NUMBER_OF_PLAYERS = 2;
    private final static int MAX_NUMBER_OF_PLAYERS = 4;

    private final File[] playerPieceImageFiles = new File[MAX_NUMBER_OF_PLAYERS];

    private final boolean[] isPlayerReady = new boolean[MAX_NUMBER_OF_PLAYERS];
    private final String[] usernames = new String[MAX_NUMBER_OF_PLAYERS];

    //private final Set<URL> currentlySelectedPlayerPieces = new HashSet<>();

    /**
     * Determines if the same player is attempting to log in more than once to be more than one of the players in the
     * game which should not be allowed.
     *
     * @param username    The username of the user that is checked to see if they are trying to log in twice.
     * @param playerIndex The index of the user that is checked to see if they are trying to log in twice.
     * @return True if the user is trying to log in more than once, Otherwise, returns false.
     */
    private boolean isUserTryingToLogInTwice(String username, int playerIndex) {
        for (int i = 0; i < MAX_NUMBER_OF_PLAYERS; i++) {
            if (usernames[i] != null && usernames[i].equals(username) && i != playerIndex) {
                System.out.println(i); //todo remove
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the same player piece is being used more than once.
     * This should not be allowed as it would be hard to differentiate between players in the game.
     *
     * @param playerPiece The player piece that is checked to see if it is being selected twice.
     * @param playerIndex The player index that a player piece belongs to that is being checked.
     * @return
     */
    private boolean isPlayerPieceBeingSelectedTwice(File playerPiece, int playerIndex) {
        for (int i = 0; i < MAX_NUMBER_OF_PLAYERS; i++) {
            if (playerPieceImageFiles[i] != null && playerPieceImageFiles[i].equals(playerPiece) && i != playerIndex) {
                System.out.println(i);  //todo remove
                return true;
            }
        }
        return false;
    }

    /**
     * Initialises the NewGame screen, giving it background, a list of available game boards, allows PlayerPiece selection and Players to log in.
     *
     * @param location  The location (not used).
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
        mainBox.setBackground(new Background(backgroundImage));
        mainBox.setMinWidth(614);

        Reader reader = new Reader();
        try {
            List<String> gameBoardNames = reader.readGameBoardNames();
            addGameBoardNames(gameBoardNames);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        player1SetUpVBox.setDisable(true);
        player1PlayerPieceSelectionVBox.setDisable(true);
        player2SetUpVBox.setDisable(true);
        player2PlayerPieceSelectionVBox.setDisable(true);
        player3SetUpVBox.setDisable(true);
        player3PlayerPieceSelectionVBox.setDisable(true);
        player4SetUpVBox.setDisable(true);
        player4PlayerPieceSelectionVBox.setDisable(true);

        gameBoardSelect.setOnAction(event -> {
            if (gameBoardSelect.getValue() != null) {
                player1SetUpVBox.setDisable(false);
            }
        });

        player1PlayerPieceSelect.setOnAction(event -> {
            if (player1PlayerPieceSelect.getValue() != null) {
                try {
                    File playerPieceFile = new File("src/view/res/img/player_piece/" + player1PlayerPieceSelect.getValue());
                    player1PlayerPieceImage.setImage(new Image(String.valueOf(playerPieceFile.toURI().toURL())));
                    playerPieceImageFiles[0] = playerPieceFile;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        player2PlayerPieceSelect.setOnAction(event -> {
            if (player2PlayerPieceSelect.getValue() != null) {
                try {
                    File playerPieceFile = new File("src/view/res/img/player_piece/" + player2PlayerPieceSelect.getValue());
                    player2PlayerPieceImage.setImage(new Image(String.valueOf(playerPieceFile.toURI().toURL())));
                    playerPieceImageFiles[1] = playerPieceFile;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        player3PlayerPieceSelect.setOnAction(event -> {
            if (player3PlayerPieceSelect.getValue() != null) {
                try {
                    File playerPieceFile = new File("src/view/res/img/player_piece/" + player3PlayerPieceSelect.getValue());
                    player3PlayerPieceImage.setImage(new Image(String.valueOf(playerPieceFile.toURI().toURL())));
                    playerPieceImageFiles[2] = playerPieceFile;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        player4PlayerPieceSelect.setOnAction(event -> {
            if (player4PlayerPieceSelect.getValue() != null) {
                try {
                    File playerPieceFile = new File("src/view/res/img/player_piece/" + player4PlayerPieceSelect.getValue());
                    player4PlayerPieceImage.setImage(new Image(String.valueOf(playerPieceFile.toURI().toURL())));
                    playerPieceImageFiles[3] = playerPieceFile;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Adds all available gameboards to the interface allowing players to select their desired board.
     *
     * @param gameBoardNames The name of the gameboards to be added.
     */
    private void addGameBoardNames(List<String> gameBoardNames) {
        gameBoardSelect.setItems(FXCollections.observableArrayList(gameBoardNames));
    }

    /**
     * Handles Player1 log in credentials, allowing them to log in and have authorisation to play the game.
     *
     * @param actionEvent When the Player1LogIn button is clicked.
     */
    @FXML
    public void onPlayer1LogInButtonPressed(ActionEvent actionEvent) {
        String username = (isUserTryingToLogInTwice(player1Username.getText(), 0)
                ? "" : player1Username.getText());
        String password = player1Password.getText();
        if (LoginHandler.login(username, password)) {
            player1LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            player1PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player1PlayerPieceSelect, player1LogInStatusLabel, username);
            usernames[0] = username;
        } else {
            //check if the user accidentally double clicked... todo change to list from set
            player1LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[0] = false;
        }
    }

    /**
     * Displays the player piece for player 1 after they have confirmed their choice.
     *
     * @param actionEvent The action of the button being clicked.
     */
    @FXML
    public void onPlayer1PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player1PlayerPieceSelect.getValue() != null) {

            File playerPieceFile = new File("src/view/res/img/player_piece/" + player1PlayerPieceSelect.getValue());
            //URL playerPieceSelected = playerPieceFile.toURI().toURL();
            if (isPlayerPieceBeingSelectedTwice(playerPieceFile, 0)) {
                player1PlayerPieceStatus.setText("Same player piece selected twice.");
                isPlayerReady[0] = false;
            } else {
                playerPieceImageFiles[0] = playerPieceFile;
                player2SetUpVBox.setDisable(false);
                player1PlayerPieceStatus.setText(player1PlayerPieceSelect.getValue() + " has been selected!");
                isPlayerReady[0] = true;
            }

        } else {
            player1PlayerPieceStatus.setText(NO_PLAYER_PIECE_SELECTED_MSG);
            isPlayerReady[0] = false;
        }
    }

    /**
     * Handles Player2 log in credentials, allowing them to log in and have authorisation to play the game.
     *
     * @param actionEvent When the Player2LogIn button is clicked.
     */
    @FXML
    public void onPlayer2LogInButtonPressed(ActionEvent actionEvent) {
        String username = (isUserTryingToLogInTwice(player2Username.getText(), 1)
                ? "" : player2Username.getText());
        String password = player2Password.getText();
        if (LoginHandler.login(username, password)) {
            player2LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            player2PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player2PlayerPieceSelect, player2LogInStatusLabel, username);
            usernames[1] = username;
        } else {
            player2LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[1] = false;
        }
    }

    /**
     * Displays the player piece for player 2 after they have confirmed their choice.
     *
     * @param actionEvent The action of the button being clicked.
     */
    @FXML
    public void onPlayer2PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player2PlayerPieceSelect.getValue() != null) {

            File playerPieceFile = new File("src/view/res/img/player_piece/" + player2PlayerPieceSelect.getValue());
            //URL playerPieceSelected = playerPieceFile.toURI().toURL();
            if (isPlayerPieceBeingSelectedTwice(playerPieceFile, 1)) {
                player2PlayerPieceStatus.setText("Same player piece selected twice.");
                isPlayerReady[1] = false;
            } else {
                playerPieceImageFiles[1] = playerPieceFile;
                player3SetUpVBox.setDisable(false);
                player2PlayerPieceStatus.setText(player2PlayerPieceSelect.getValue() + " has been selected!");
                isPlayerReady[1] = true;
            }

        } else {
            player2PlayerPieceStatus.setText(NO_PLAYER_PIECE_SELECTED_MSG);
            isPlayerReady[1] = false;
        }
    }

    /**
     * Handles Player3 log in credentials, allowing them to log in and have authorisation to play the game.
     *
     * @param actionEvent When the Player3LogIn button is clicked.
     */
    @FXML
    public void onPlayer3LogInButtonPressed(ActionEvent actionEvent) {
        String username = (isUserTryingToLogInTwice(player3Username.getText(), 2)
                ? "" : player3Username.getText());
        String password = player3Password.getText();
        if (LoginHandler.login(username, password)) {
            player3LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            player3PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player3PlayerPieceSelect, player3LogInStatusLabel, username);
            usernames[2] = username;
        } else {
            player3LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[2] = false;
        }
    }

    /**
     * Displays the player piece for player 3 after they have confirmed their choice.
     *
     * @param actionEvent The action of the button being clicked.
     */
    @FXML
    public void onPlayer3PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player3PlayerPieceSelect.getValue() != null) {

            File playerPieceFile = new File("src/view/res/img/player_piece/" + player3PlayerPieceSelect.getValue());
            //URL playerPieceSelected = playerPieceFile.toURI().toURL();
            if (isPlayerPieceBeingSelectedTwice(playerPieceFile, 2)) {
                player3PlayerPieceStatus.setText("Same player piece selected twice.");
                isPlayerReady[2] = false;
            } else {
                playerPieceImageFiles[2] = playerPieceFile;
                player4SetUpVBox.setDisable(false);
                player3PlayerPieceStatus.setText(player3PlayerPieceSelect.getValue() + " has been selected!");
                isPlayerReady[2] = true;
            }

        } else {
            player3PlayerPieceStatus.setText(NO_PLAYER_PIECE_SELECTED_MSG);
            isPlayerReady[2] = false;
        }
    }

    /**
     * Handles Player1 log in credentials, allowing them to log in and have authorisation to play the game.
     *
     * @param actionEvent When the Player1LogIn button is clicked.
     */
    @FXML
    public void onPlayer4LogInButtonPressed(ActionEvent actionEvent) {
        String username = (isUserTryingToLogInTwice(player4Username.getText(), 3)
                ? "" : player4Username.getText());
        String password = player4Password.getText();
        if (LoginHandler.login(username, password)) {
            player4LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            player4PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player4PlayerPieceSelect, player4LogInStatusLabel, username);
            usernames[3] = username;
        } else {
            player4LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[3] = false;
        }
    }

    /**
     * Displays the player piece for player 4 after they have confirmed their choice.
     *
     * @param actionEvent The action of the button being clicked.
     */
    @FXML
    public void onPlayer4PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player4PlayerPieceSelect.getValue() != null) {

            File playerPieceFile = new File("src/view/res/img/player_piece/" + player4PlayerPieceSelect.getValue());
            //URL playerPieceSelected = playerPieceFile.toURI().toURL();
            if (isPlayerPieceBeingSelectedTwice(playerPieceFile, 3)) {
                player4PlayerPieceStatus.setText("Same player piece selected twice.");
                isPlayerReady[3] = false;
            } else {
                playerPieceImageFiles[3] = playerPieceFile;
                player4PlayerPieceStatus.setText(player4PlayerPieceSelect.getValue() + " has been selected!");
                isPlayerReady[3] = true;
            }

        } else {
            player4PlayerPieceStatus.setText(NO_PLAYER_PIECE_SELECTED_MSG);
            isPlayerReady[3] = false;
        }
    }


    /**
     * Populates the choice box with all player pieces available to the player / all purchased player pieces by that player.
     *
     * @param playerPieceSelect The choice box that is to contain the player pieces available for selection.
     * @param loginStatusLabel  The label to show that the player has correctly logged in.
     * @param username          The username of the player whose owned player pieces are to be displayed.
     */
    private void populateWithPlayerPieces(ChoiceBox<String> playerPieceSelect, Label loginStatusLabel, String username) {
        try {
            ArrayList<String> purchasedPlayerPieces = PurchaseHandler.getPlayersPurchasedPlayerPieces(username);
            playerPieceSelect.setItems(FXCollections.observableArrayList(purchasedPlayerPieces));
        } catch (FileNotFoundException e) {
            loginStatusLabel.setText("error finding player piece file.");
        }
    }

    /**
     * Loads a new game with the players and player pieces on the new game interface.
     *
     * @param e The event of the start game button being clicked.
     * @throws IOException If the player piece image file path is incorrect.
     */
    @FXML
    private void onStartGameButtonClicked(ActionEvent e) throws IOException {
        // Make player array...
        int nPlayers = 0;

        nPlayers = (isPlayerReady[0] ? nPlayers + 1 : nPlayers);
        nPlayers = (isPlayerReady[1] ? nPlayers + 1 : nPlayers);
        nPlayers = (isPlayerReady[2] ? nPlayers + 1 : nPlayers);
        nPlayers = (isPlayerReady[3] ? nPlayers + 1 : nPlayers);

        if (nPlayers >= MIN_NUMBER_OF_PLAYERS) {
            Player[] players = new Player[nPlayers];

            int nPlayersCreated = 0;
            int i = 0;

            while (nPlayersCreated < nPlayers) {
                if (isPlayerReady[i]) {
                    players[nPlayersCreated] = new Player(usernames[i],
                            new PlayerPiece(playerPieceImageFiles[i])); //todo test
                    nPlayersCreated++;
                }
                i++;
            }
            GameService.getInstance().loadNewGame(players, gameBoardSelect.getValue());

            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/game.fxml"));
            currentStage.setScene(new Scene(game));
        } else {
            startGameStatusLabel.setText("Ensure min. 2 players are logged in, with confirmed player pieces.");
        }
    }

    /**
     * Returns the user to the main menu.
     *
     * @param e The action of the back button being clicked.
     * @throws IOException If the main menu file path is incorrect.
     */
    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }
}
