package java_.controller;

import java.awt.event.ActionEvent;
import java.io.File;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class LeaderboardController implements Initializable {

    @FXML
    private TableView<Table> tableID;
    @FXML
    private TableColumn<Table, String> name;
    @FXML
    private TableColumn<Table, Integer> wins;
    @FXML
    private TableColumn<Table, Integer> losses;

    private Table tableOne;
    private Object Table;

    //Un-comment if using file reader.
    //@FXML ObservableList<Table> data = FXCollections.observableArrayList();

    public LeaderboardController() {
    }

    //Reading data in manually
    @FXML
    ObservableList<Table> data = FXCollections.observableArrayList(
            new Table("Waleed", 8, 3),
            new Table("Ioan", 12, 12),
            new Table("Michal", 15, 3),
            new Table("Sam", 13, 3),
            new Table("Mateo", 23, 9),
            new Table("Mohammad", 18, 14),
            new Table("Bishwo", 11, 18)
    );


    //Reading data with file reader
    /* private void FileReader() {
        try {
            Scanner x = new Scanner(new File("C:\\Users\\Waleed's PC\\Desktop\\Leaderboard\\src\\users.txt")).useDelimiter("\\s");
            while(x.hasNext()) {
                String name = x.next();
                int wins = x.nextInt();
                int losses = x.nextInt();
                data.add(new Table(name, wins, losses));
            }
        } catch (Exception e) {
            System.out.println("");
        }
    } */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Uncomment for file reader
        //FileReader();
        name.setCellValueFactory(new PropertyValueFactory<Table, String>("rName"));
        wins.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rWins"));
        losses.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rLosses"));

        tableID.setItems(data);
    }
}
