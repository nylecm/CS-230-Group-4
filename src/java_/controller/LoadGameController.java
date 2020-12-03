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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoadGameController implements Initializable {

    @FXML
    private HBox logInFormHBox;
    @FXML
    private ChoiceBox<File> loadGameSelect;

    private static final String SAVES_FOLDER_DIRECTORY = "data/saves";
    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Reader r = new Reader();
        File[] fileNames = r.readFileNames(SAVES_FOLDER_DIRECTORY);
        addSaveFileNames(fileNames);

        loadGameSelect.setOnAction(event -> {
            if (loadGameSelect.getValue() != null) {
                readFileForVerificationDetails(loadGameSelect.getValue());
            }
        });
    }

    private void readFileForVerificationDetails(File file) {
        // Read username
        // Read number of Players
        // Read player pieces used

        // Create log in form v boxes.
    }

    private void addSaveFileNames(File[] fileNames) {
        loadGameSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    public void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    public void onLoadGameButtonClicked(ActionEvent actionEvent) {
    }
}
