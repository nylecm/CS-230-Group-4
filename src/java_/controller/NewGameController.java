package java_.controller;

import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.util.security.LoginHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NewGameController implements Initializable {
    @FXML
    private ChoiceBox gameBoardSelect;


    @FXML
    private VBox player1SetUpVBox;

    @FXML
    private ToggleButton togglePlayer1;

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
    private ToggleButton togglePlayer2;

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
    private ToggleButton togglePlayer3;

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
    private ToggleButton togglePlayer4;

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

    private final Set<File> currentlySelectedPlayerPieces = new HashSet<>();

    private final Set<String> currentUserNames = new HashSet<>();

    private final String LOG_IN_SUCCESS_STRING = "Login Successful!";

    private final String LOG_IN_FAILURE_STRING = "Invalid/Duplicate Credentials!";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        } else {
            player1LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
        }
    }

    @FXML
    public void onPlayer1PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player1PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player1PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player2SetUpVBox.setDisable(false);
            player1PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
        } else {
            player1PlayerPieceStatus.setText("Player Piece Not Selected!");
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
        } else {
            player2LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
        }
    }

    @FXML
    public void onPlayer2PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player2PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player2PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player3SetUpVBox.setDisable(false);
            player2PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
        } else {
            player2PlayerPieceStatus.setText("Player Piece Not Selected!");
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
        } else {
            player3LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
        }
    }

    @FXML
    public void onPlayer3PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player3PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player3PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player4SetUpVBox.setDisable(false);
            player3PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
        } else {
            player3PlayerPieceStatus.setText("Player Piece Not Selected!");
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
        } else {
            player4LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
        }
    }

    @FXML
    public void onPlayer4PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player4PlayerPieceSelect.getValue() != null) {
            File playerPieceSelected = player4PlayerPieceSelect.getValue();
            currentlySelectedPlayerPieces.add(playerPieceSelected);
            player4PlayerPieceStatus.setText(playerPieceSelected.getName() + " has been selected!");
        } else {
            player4PlayerPieceStatus.setText("Player Piece Not Selected!");
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
        GameService.getInstance().loadNewGame(new Player[]{new Player("bob", 0, 0, new PlayerPiece()), new Player("bob", 0, 0, new PlayerPiece())}, (String) gameBoardSelect.getValue());

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/gameDummy.fxml"));
        currentStage.setScene(new Scene(game));
    }

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }
}
