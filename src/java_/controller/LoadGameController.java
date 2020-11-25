package java_.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoadGameController implements Initializable {

    @FXML
    private ChoiceBox loadGameSelect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Reader r = new Reader();
        File[] fileNames = r.readFileNames("data/saves");
        addSaveFileNames(fileNames);
    }

    private void addSaveFileNames(File[] fileNames) {
        loadGameSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    public void onBackButtonClicked(ActionEvent actionEvent) {
    }

    public void onLoadGameButtonClicked(ActionEvent actionEvent) {
    }
}
