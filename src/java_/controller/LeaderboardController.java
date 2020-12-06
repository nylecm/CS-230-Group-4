package java_.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java_.game.player.LeaderboardTable;
import java_.util.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import java.util.ArrayList;


public class LeaderboardController implements Initializable {

    @FXML
    private BorderPane mainBox;
    @FXML
    private TableView<LeaderboardTable> tableID;
    @FXML
    private TableColumn<LeaderboardTable, String> name;
    @FXML
    private TableColumn<LeaderboardTable, Integer> wins;
    @FXML
    private TableColumn<LeaderboardTable, Integer> losses;
    @FXML
    private ChoiceBox gameBoardSelect;

    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";
    private static final String USER_STATS_FOLDER_DIRECTORY = "data/user_stats";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";

    private LeaderboardTable leaderboardTableOne;
    private Object Table;

    //Un-comment if using file reader.
    //@FXML ObservableList<Table> data = FXCollections.observableArrayList();

    public LeaderboardController() {
    }

    //Reading data in manually
    @FXML
    ObservableList<LeaderboardTable> data = FXCollections.observableArrayList();

    //todo add ability to see stats for all game boards.
    //
    //Reading data with file reader
    private void readStatFile(File statFile) throws FileNotFoundException {
        data.clear();

        List<LeaderboardTable> playerStats = new LinkedList<>();

        Scanner in = new Scanner(statFile);
        in.useDelimiter("`");

        while (in.hasNextLine()) {
            String name = in.next();
            int wins = in.nextInt();
            String lossesStr = in.next();
            int losses = Integer.parseInt(lossesStr);
            playerStats.add(new LeaderboardTable(name, wins, losses));
            in.nextLine();
        }
        in.close();

        playerStats.sort(Comparator.comparingInt(LeaderboardTable::getrWins));
        Collections.reverse(playerStats);

        data.addAll(playerStats);
    }

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


        Reader r = new Reader();
        File[] fileNames = r.readFileNames(USER_STATS_FOLDER_DIRECTORY);
        addGameBoardStatFileNames(fileNames);

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
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
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

    @FXML
    private void onTotalStatsButtonClicked(ActionEvent e) throws IOException {

        List<LeaderboardTable> playerStats = new LinkedList<>();
        ArrayList<Integer> playerWins = new ArrayList<Integer>();
        ArrayList<Integer> playerLosses = new ArrayList<Integer>();
        ArrayList<String> playerNames = new ArrayList<String>();
        ArrayList<String> statFiles = new ArrayList<String>();
        File folder = new File("data/user_stats");
        statFiles.addAll(listOfFiles(folder));

        for(int i = 0; i < statFiles.size(); i++) {
            String dirStats = "data/user_stats/" + statFiles.get(i);
            File file = new File(dirStats);
            try {
                Scanner in = new Scanner(file);
                in.useDelimiter("`");
                int a = 0;
                while (in.hasNextLine()) {
                    String newName = in.next();
                    int newWinCount = in.nextInt();
                    int newLossCount = in.nextInt();

                    try {
                        playerNames.get(a);
                    } catch (IndexOutOfBoundsException c) {
                        playerNames.add(newName);
                    }

                    try {
                        int tempWins = playerWins.get(a);
                        tempWins = tempWins + newWinCount;
                        playerWins.set(a, tempWins);
                    } catch (IndexOutOfBoundsException c) {
                        playerWins.add(newWinCount);
                    }

                    try {
                        int tempLosses = playerLosses.get(a);
                        tempLosses = tempLosses + newLossCount;
                        playerLosses.set(a, tempLosses);
                    } catch (IndexOutOfBoundsException c) {
                        playerLosses.add(newLossCount);
                    }

                    a++;
                }
                in.close();
            } catch (Exception b) {
                b.printStackTrace();
            }

            }

        playerStats.clear();
        data.clear();
        for (int k = 0; k < playerNames.size(); k++) {
            String name = playerNames.get(k);
            int wins = playerWins.get(k);
            int losses = playerLosses.get(k);

            playerStats.add(new LeaderboardTable(name, wins, losses));
        }
        data.addAll(playerStats);
        tableID.setItems(data);

    }
    private ArrayList listOfFiles(final File folder) {
        ArrayList statFilesTemp = new ArrayList();

        for (final File fileEntry : folder.listFiles()) {
                statFilesTemp.add(fileEntry.getName());
            }

        return statFilesTemp;
    }

}
