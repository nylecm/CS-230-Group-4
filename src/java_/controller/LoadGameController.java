package java_.controller;

import java_.game.controller.GameService;
import java_.game.tile.GameBoard;
import java_.util.generic_data_structures.Link;
import java_.util.security.LoginHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoadGameController implements Initializable {

    @FXML
    private VBox mainPane;

    @FXML
    private HBox logInFormHBox;

    @FXML
    private ChoiceBox<File> loadGameSelect;

    private static final String SAVES_FOLDER_DIRECTORY = "data/saves";
    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";
    private static final String DELIMITER = "`";

    private final LinkedList<VBox> logInVBoxes = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File(URANUS_BACKGROUND_PATH).toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainPane.setBackground(new Background(backgroundFill));
        mainPane.setMinWidth(614);

        Reader r = new Reader();
        File[] fileNames = r.readFileNames(SAVES_FOLDER_DIRECTORY);
        addSaveFileNames(fileNames);

        loadGameSelect.setOnAction(event -> {
            if (loadGameSelect.getValue() != null) {
                try {
                    logInVBoxes.clear();
                    logInFormHBox.getChildren().clear();
                    readFileForPlayerInfo(loadGameSelect.getValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void readFileForPlayerInfo(File file) throws FileNotFoundException {
        // Read username
        // Read number of Players
        // Read player pieces used

        // Create log in form v boxes.

        Scanner in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        String nPlayersStr = in.next();
        int nPlayers = Integer.parseInt(nPlayersStr);
        System.out.println(nPlayers);
        in.nextLine();
        in.nextLine();
        Image[] playerPieces = new Image[nPlayers];

        for (int i = 0; i < nPlayers; i++) {
            try {
                playerPieces[i] = new Image(String.valueOf(new File(in.next()).toURI().toURL())); // check this
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        in.nextLine();


        String[] playerUsernames = new String[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            playerUsernames[i] = in.next();
            in.nextLine();
        }
        in.close();

        for (int i = 0; i < nPlayers; i++) {
            createPasswordEntryForm(playerUsernames[i], playerPieces[i]);
        }
    }

    private void createPasswordEntryForm(String playerUsername, Image playerPiece) {
        VBox userLogInBox = new VBox();
        Label usernameLabel = new Label(playerUsername);
        PasswordField passwordField = new PasswordField();
        ImageView playerPieceView = new ImageView(playerPiece);

        userLogInBox.getChildren().add(usernameLabel);
        userLogInBox.getChildren().add(passwordField);
        userLogInBox.getChildren().add(playerPieceView);

        logInFormHBox.getChildren().add(userLogInBox);
        logInVBoxes.addLast(userLogInBox);
    }

    private void addSaveFileNames(File[] fileNames) {
        loadGameSelect.setItems(FXCollections.observableArrayList(fileNames));
    }

    public void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    public void onLoadGameButtonClicked(ActionEvent e) throws IOException {
        boolean isIncorrectPasswordEntered = false;
        int i = 0;

        while (!isIncorrectPasswordEntered && i < logInVBoxes.size()) {
            VBox userLoginForm = logInVBoxes.get(i);
            Label userUsername = (Label) userLoginForm.getChildren().get(0);
            PasswordField userPassword = (PasswordField) userLoginForm.getChildren().get(1);
            if (!LoginHandler.login(userUsername.getText(), userPassword.getText())) {
                isIncorrectPasswordEntered = true;
            }
            i++;
        }

        if (!isIncorrectPasswordEntered) {
            try {
                GameService.getInstance().loadSavedInstance(loadGameSelect.getValue());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/gameDummy.fxml"));
            currentStage.setScene(new Scene(game));
        } else {
            System.out.println("WRONG PW");
            //Handle incorrect password
        }
    }
}
