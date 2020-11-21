package java_.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class NewGameController implements Initializable {

    @FXML
    private ChoiceBox gameBoardSelect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Reader reader = new Reader();
        try {
            List<String> gameBoardNames = reader.readGameBoardNames();
            addGameBoardNames(gameBoardNames);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/game.fxml"));
        currentStage.setScene(new Scene(game));
    }
}
