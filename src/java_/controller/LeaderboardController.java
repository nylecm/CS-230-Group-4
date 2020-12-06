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

/**
 * Controller for a leaderboard that tracks player stats including number of wins and losses on each game board.
 *
 * @author Waleed Ashraf
 */
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

    /**
     * Initialises the LeaderboardController
     */
    public LeaderboardController() {
    }

    //Reading data in manually
    @FXML
    ObservableList<LeaderboardTable> data = FXCollections.observableArrayList();

    //todo add ability to see stats for all game boards.
    //
    //Reading data with file reader

    /**
     * Reads a file containing the stats of players that have played on that game board.
     *
     * @param statFile The file of a specific game board containing the stats (number of wins and losses ) of each player that has played on that map.
     * @throws FileNotFoundException If the stat file cannot be found.
     */
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

    /**
     * Initialises the leaderboard interface, giving it a background and displaying all user stats.
     *
     * @param location  the location used to resolve paths or null if the location is not known. (not used)
     * @param resources the resources used to localize object, or null if object was not localized. (not used)
     */
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

    /**
     * Adds the names of the files containing the players' stats.
     *
     * @param fileNames The names of the files to be added as stat file names.
     */
    private void addGameBoardStatFileNames(File[] fileNames) {
        gameBoardSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    /**
     * Returns the user to the main menu
     *
     * @param e The event of the back button being clicked by the user.
     * @throws IOException If the main menu file path is incorrect.
     */
    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Displays the stats for all players of a specific game board.
     *
     * @param e The event of a player clicking on the button to view player stats for a particular game board.
     */
    @FXML
    private void onViewStatsForGameBoardButton(ActionEvent e) {
        try {
            readStatFile(new File(String.valueOf(gameBoardSelect.getValue())));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    /**
     * Displays all player stats (not for a specific game board)
     *
     * @param e The user clicked on the button to display all player stats.
     * @throws IOException If the user stats file path is incorrect.
     */
    @FXML
    private void onTotalStatsButtonClicked(ActionEvent e) throws IOException {
        List<LeaderboardTable> playerStats = new LinkedList<>();
        ArrayList<Integer> playerWins = new ArrayList<>();
        ArrayList<Integer> playerLosses = new ArrayList<>();
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<String> statFiles = new ArrayList<>();
        File folder = new File(USER_STATS_FOLDER_DIRECTORY);
        statFiles.addAll(listOfFiles(folder));

        for (int i = 0; i < statFiles.size(); i++) {
            String dirStats = USER_STATS_FOLDER_DIRECTORY + "/" + statFiles.get(i);
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
            } catch (Exception e1) {
                e1.printStackTrace();
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

    /**
     * Returns a list of all files in a specified folder.
     *
     * @param folder The folder which contains the files that are to be returned.
     * @return The ArrayList if all files in the folder.
     */
    private ArrayList listOfFiles(final File folder) {
        ArrayList statFilesTemp = new ArrayList();

        for (final File fileEntry : folder.listFiles()) {
            statFilesTemp.add(fileEntry.getName());
        }

        return statFilesTemp;
    }

}
