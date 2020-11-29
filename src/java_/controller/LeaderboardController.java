package java_.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import java_.game.player.Table;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class LeaderboardController implements Initializable {

    @FXML
    private TableView<Table> tableID;
    @FXML
    private TableColumn<Table, String> name;
    @FXML
    private TableColumn<Table, Integer> wins;
    @FXML
    private TableColumn<Table, Integer> losses;
    @FXML
    private ChoiceBox gameBoardSelect;

    private Table tableOne;
    private Object Table;

    //Un-comment if using file reader.
    //@FXML ObservableList<Table> data = FXCollections.observableArrayList();

    public LeaderboardController() {
    }

    //Reading data in manually
    @FXML
    ObservableList<Table> data = FXCollections.observableArrayList();

    //todo add ability to see stats for all game boards.

    //Reading data with file reader
    private void readStatFile(File statFile) throws FileNotFoundException {
        data.clear();

        Scanner in = new Scanner(statFile);
        in.useDelimiter("`");

        while (in.hasNextLine()) {
            String name = in.next();
            int wins = in.nextInt();
            String lossesStr = in.next();
            int losses = Integer.parseInt(lossesStr);
            data.add(new Table(name, wins, losses));
            in.nextLine();
        }
        in.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Reader r = new Reader();
        File[] fileNames = r.readFileNames("data/user_stats");
        addGameBoardStatFileNames(fileNames);

        //Uncomment for file reader
        //FileReader();
        name.setCellValueFactory(new PropertyValueFactory<>("rName"));
        wins.setCellValueFactory(new PropertyValueFactory<>("rWins"));
        losses.setCellValueFactory(new PropertyValueFactory<>("rLosses"));

        tableID.setItems(data);
    }

    private void addGameBoardStatFileNames(File[] fileNames) {
        gameBoardSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    @FXML
    private void onViewStatsForGameBoardButton(ActionEvent e) {
        try {
            readStatFile(new File(String.valueOf(gameBoardSelect.getValue())));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
