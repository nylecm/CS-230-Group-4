package java_.controller;

import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.util.security.LoginHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NewGameController implements Initializable {

    @FXML
    private VBox mainBox;

    @FXML
    private ChoiceBox gameBoardSelect;

    @FXML
    private VBox player1SetUpVBox;

    @FXML
    private TextField player1Username;

    @FXML
    private PasswordField player1Password;

    @FXML
    private Button player1LogIn;

    @FXML
    private Label player1LogInStatusLabel;

    @FXML
    private VBox player1PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<File> player1PlayerPieceSelect;

    @FXML
    public Label player1PlayerPieceStatus;

    @FXML
    private ImageView player1PlayerPieceImage;

    @FXML
    private Button player1PlayerPieceConfirm;

    @FXML
    private VBox player2SetUpVBox;

    @FXML
    private TextField player2Username;

    @FXML
    private PasswordField player2Password;

    @FXML
    private Button player2LogIn;

    @FXML
    private Label player2LogInStatusLabel;

    @FXML
    private VBox player2PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<File> player2PlayerPieceSelect;

    @FXML
    public Label player2PlayerPieceStatus;

    @FXML
    private ImageView player2PlayerPieceImage;

    @FXML
    private Button player2PlayerPieceConfirm;

    @FXML
    private VBox player3SetUpVBox;

    @FXML
    private TextField player3Username;

    @FXML
    private PasswordField player3Password;

    @FXML
    private Button player3LogIn;

    @FXML
    private Label player3LogInStatusLabel;

    @FXML
    private VBox player3PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<File> player3PlayerPieceSelect;

    @FXML
    public Label player3PlayerPieceStatus;

    @FXML
    private ImageView player3PlayerPieceImage;

    @FXML
    private Button player3PlayerPieceConfirm;

    @FXML
    private VBox player4SetUpVBox;

    @FXML
    private TextField player4Username;

    @FXML
    private PasswordField player4Password;

    @FXML
    private Button player4LogIn;

    @FXML
    private Label player4LogInStatusLabel;

    @FXML
    private VBox player4PlayerPieceSelectionVBox;

    @FXML
    private ChoiceBox<File> player4PlayerPieceSelect;

    @FXML
    public Label player4PlayerPieceStatus;

    @FXML
    private ImageView player4PlayerPieceImage;

    @FXML
    private Button player4PlayerPieceConfirm;

    private final static String LOG_IN_SUCCESS_STRING = "Login Successful!";
    private final static String LOG_IN_FAILURE_STRING = "Invalid/Duplicate Credentials!";
    private final static int MIN_NUMBER_OF_PLAYERS = 2;
    private final static int MAX_NUMBER_OF_PLAYERS = 4;
    private final URL[] playerPieceImageURLs = new URL[MAX_NUMBER_OF_PLAYERS];

    //private boolean isPlayer1Ready = false;
    //private boolean isPlayer2Ready = false;
    //private boolean isPlayer3Ready = false;
    //private boolean isPlayer4Ready = false;
    private boolean[] isPlayerReady = new boolean[4];
    private String[] usernames = new String[4];

    private final Set<File> currentlySelectedPlayerPieces = new HashSet<>();
    private final Set<String> currentUserNames = new HashSet<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File("src/view/res/img/space_uranus.png").toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainBox.setBackground(new Background(backgroundFill));


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

        // fixme find less hacky way...
        player1PlayerPieceSelect.setOnAction(event -> {
            if (player1PlayerPieceSelect.getValue() != null) {
                try {
                    player1PlayerPieceImage.setImage(new Image(String.valueOf(player1PlayerPieceSelect.getValue().toURL())));
                    playerPieceImageURLs[0] = player1PlayerPieceSelect.getValue().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        // fixme find less hacky way...
        player2PlayerPieceSelect.setOnAction(event -> {
            if (player2PlayerPieceSelect.getValue() != null) {
                try {
                    player2PlayerPieceImage.setImage(new Image(String.valueOf(player2PlayerPieceSelect.getValue().toURL())));
                    playerPieceImageURLs[1] = player2PlayerPieceSelect.getValue().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        // fixme find less hacky way...
        player3PlayerPieceSelect.setOnAction(event -> {
            if (player3PlayerPieceSelect.getValue() != null) {
                try {
                    player3PlayerPieceImage.setImage(new Image(String.valueOf(player3PlayerPieceSelect.getValue().toURL())));
                    playerPieceImageURLs[2] = player3PlayerPieceSelect.getValue().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        // fixme find less hacky way...
        player4PlayerPieceSelect.setOnAction(event -> {
            if (player4PlayerPieceSelect.getValue() != null) {
                try {
                    player4PlayerPieceImage.setImage(new Image(String.valueOf(player4PlayerPieceSelect.getValue().toURL())));
                    playerPieceImageURLs[3] = player4PlayerPieceSelect.getValue().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addGameBoardNames(List<String> gameBoardNames) {
        gameBoardSelect.setItems(FXCollections.observableArrayList(gameBoardNames));
    }

    @FXML
    public void onPlayer1LogInButtonPressed(ActionEvent actionEvent) {
        String username = (currentUserNames.contains(player1Username.getText())
                ? "" : player1Username.getText());
        String password = player1Password.getText();
        if (LoginHandler.login(username, password)) {
            player1LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            currentUserNames.add(username);
            player1PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player1PlayerPieceSelect);
            usernames[0] = username;
        } else {
            player1LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[0] = false;
        }
    }

    @FXML
    public void onPlayer1PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player1PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player1PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player2SetUpVBox.setDisable(false);
            player1PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
            isPlayerReady[0] = true;
        } else {
            player1PlayerPieceStatus.setText("Player Piece Not Selected!");
            isPlayerReady[0] = false;
        }
    }

    @FXML
    public void onPlayer2LogInButtonPressed(ActionEvent actionEvent) {
        String username = (currentUserNames.contains(player2Username.getText())
                ? "" : player2Username.getText());
        String password = player2Password.getText();
        if (LoginHandler.login(username, password)) {
            player2LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            currentUserNames.add(username);
            player2PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player2PlayerPieceSelect);
            usernames[1] = username;
        } else {
            player2LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[1] = false;
        }
    }

    @FXML
    public void onPlayer2PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player2PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player2PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player3SetUpVBox.setDisable(false);
            player2PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
            isPlayerReady[1] = true;
        } else {
            player2PlayerPieceStatus.setText("Player Piece Not Selected!");
            isPlayerReady[1] = false;
        }
    }

    @FXML
    public void onPlayer3LogInButtonPressed(ActionEvent actionEvent) {
        String username = (currentUserNames.contains(player3Username.getText())
                ? "" : player3Username.getText());
        String password = player3Password.getText();
        if (LoginHandler.login(username, password)) {
            player3LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            currentUserNames.add(username);
            player3PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player3PlayerPieceSelect);
            usernames[2] = username;
        } else {
            player3LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[2] = false;
        }
    }

    @FXML
    public void onPlayer3PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player3PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player3PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player4SetUpVBox.setDisable(false);
            player3PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
            isPlayerReady[2] = true;
        } else {
            player3PlayerPieceStatus.setText("Player Piece Not Selected!");
            isPlayerReady[2] = false;
        }
    }

    @FXML
    public void onPlayer4LogInButtonPressed(ActionEvent actionEvent) {
        String username = (currentUserNames.contains(player4Username.getText())
                ? "" : player4Username.getText());
        String password = player4Password.getText();
        if (LoginHandler.login(username, password)) {
            player4LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            currentUserNames.add(username);
            player4PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player4PlayerPieceSelect);
            usernames[3] = username;
        } else {
            player4LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
            isPlayerReady[3] = false;
        }
    }

    @FXML
    public void onPlayer4PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player4PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player4PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player4PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
            isPlayerReady[3] = true;
        } else {
            player4PlayerPieceStatus.setText("Player Piece Not Selected!");
            isPlayerReady[3] = false;
        }
    }

    private void populateWithPlayerPieces(ChoiceBox<File> playerPieceSelect) {
        Reader reader = new Reader();
        File[] playerPieces = reader.readFileNames("src/view/res/img/player_piece");
        LinkedList<File> playerPiecesList = new LinkedList<>(Arrays.asList(playerPieces));
        playerPiecesList.removeIf(currentlySelectedPlayerPieces::contains);
        playerPieceSelect.setItems(FXCollections.observableList(playerPiecesList));
    }

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
                    players[nPlayersCreated] = new Player(usernames[i], new PlayerPiece(playerPieceImageURLs[i])); //todo test
                    nPlayersCreated++;
                }
                i++;
            }

            GameService.getInstance().loadNewGame(players, (String) gameBoardSelect.getValue());

            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/gameDummy.fxml"));
            currentStage.setScene(new Scene(game));
        } else {
            //todo... notify user of the problem...
        }
    }

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }
}
