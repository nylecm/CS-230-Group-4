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
    private ImageView player1PlayerPieceImage;

    @FXML
    private Button player1PlayerPieceConfirm;

    @FXML
    private VBox player2SetUpVBox;


    @FXML
    private VBox player3SetUpVBox;


    @FXML
    private VBox player4SetUpVBox;

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
        player3SetUpVBox.setDisable(true);
        player4SetUpVBox.setDisable(true);

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
                //player1PlayerPieceImage.setImage(new Image(player1PlayerPieceSelect.getValue().getPath()));
            }
        });
    }

    private void addGameBoardNames(List<String> gameBoardNames) {
        gameBoardSelect.setItems(FXCollections.observableArrayList(gameBoardNames));
    }

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    @FXML
    private void onStartGameButtonClicked(ActionEvent e) throws IOException {
        GameService.getInstance().loadNewGame(new Player[]{new Player("bob", 0, 0, new PlayerPiece()), new Player("bob", 0, 0, new PlayerPiece())}, (String) gameBoardSelect.getValue());

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/gameDummy.fxml"));
        currentStage.setScene(new Scene(game));
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

    private void populateWithPlayerPieces(ChoiceBox<File> playerPieceSelect) {
        Reader reader = new Reader();
        File[] playerPieces = reader.readFileNames("src/view/res/img/player_piece");
        LinkedList<File> playerPiecesList = new LinkedList<>(Arrays.asList(playerPieces));
        playerPiecesList.removeIf(currentlySelectedPlayerPieces::contains);
        playerPieceSelect.setItems(FXCollections.observableList(playerPiecesList));
    }

    public void onPlayer1PlayerPieceConfirmClicked(ActionEvent actionEvent) {
        if (player1PlayerPieceSelect != null) {
            player2SetUpVBox.setDisable(false);
        } else {
            //todo tell user they have to select player piece
        }
    }

    public void onPlayer2LogInButtonPressed(ActionEvent actionEvent) {
    }

    public void onPlayer2PlayerPieceConfirmClicked(ActionEvent actionEvent) {
    }

    public void onPlayer3LogInButtonPressed(ActionEvent actionEvent) {
    }

    public void onPlayer3PlayerPieceConfirmClicked(ActionEvent actionEvent) {
    }

    public void onPlayer4LogInButtonPressed(ActionEvent actionEvent) {
    }

    public void onPlayer4PlayerPieceConfirmClicked(ActionEvent actionEvent) {
    }
}
