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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
    private ChoiceBox player1PlayerPieceSelect;

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

    private Set<File> currentlySelectedPlayerPieces;

    private final String LOG_IN_SUCCESS_STRING = "Login Successful!";

    private final String LOG_IN_FAILURE_STRING = "Invalid Credentials!";

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

    public void onPlayer1LogInButtonPressed(ActionEvent actionEvent) {
        String username = player1Username.getText();
        String password = player1Password.getText();
        if (LoginHandler.login(username, password)) {
            player1LogInStatusLabel.setText(LOG_IN_SUCCESS_STRING);
            player1PlayerPieceSelectionVBox.setDisable(false);
            populateWithPlayerPieces(player1PlayerPieceSelect);
        } else {
            player1LogInStatusLabel.setText(LOG_IN_FAILURE_STRING);
        }
    }

    private void populateWithPlayerPieces(ChoiceBox playerPieceSelect) {
        Reader reader = new Reader();
        File[] playerPieces = reader.readFileNames("src/res/img/player_piece");
        playerPieceSelect.setItems(FXCollections.observableArrayList(playerPieces));
    }

    public void onPlayer1PlayerPieceConfirmClicked(ActionEvent actionEvent) {

        player1PlayerPieceSelectionVBox.setDisable(false);

    }
}
