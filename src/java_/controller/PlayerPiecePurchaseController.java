package java_.controller;

import java_.game.controller.PurchaseHandler;
import java_.util.security.LoginHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Controls the player piece purchase window.
 *
 * @author Waleed Ashraf, edited by nylecm.
 */
public class PlayerPiecePurchaseController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label loginStatus;

    @FXML
    private Label purchaseStatus;

    @FXML
    private ImageView playerPiecePreview;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label coinNumber;

    @FXML
    private ListView<String> preOwnedPieces;

    @FXML
    private ChoiceBox<String> affordablePlayerPieces;

    private static final String USER_COIN_FILE_DIRECTORY = "data/user_coins.txt";
    private static final String PLAYER_PIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";
    public static final String DELIMITER = "`";
    private String currentUser;

    /**
     * Logs the player into the store when correct user credentials are entered,
     * when the login button is clicked. Sets the number of coins the player has,
     * displays player pieces the user, and displays player pieces owned by given
     * player. If log in attempt is unsuccessful, notifies user.
     *
     * @param e The action of the login button being clicked.
     * @throws IOException If the user coin file path is incorrect.
     */
    @FXML
    private void onLoginButtonClicked(ActionEvent e) {
        // Security check:
        if (LoginHandler.login(usernameField.getText(), passwordField.getText())) {
            preOwnedPieces.getItems().clear();
            currentUser = usernameField.getText();
            playerPiecePreview.setImage(null);
            try {
                boolean isUserFound = findUserAndGetUserCoinAmount();
                if (isUserFound) {
                    updateAffordablePlayerPieces();
                } else {
                    loginStatus.setText("Invalid username.");
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        } else {
            loginStatus.setText("Wrong password entered.");
        }
    }

    /**
     * When clicked, buys the currently selected player piece, and refreshed
     * coin count and the table of owned player pieces.
     *
     * @param e action event (not used)
     */
    @FXML
    private void onBuyButtonClicked(ActionEvent e) {
        int newCoinBalance = 0;
        try {
            newCoinBalance = PurchaseHandler.buyPlayerPiece(currentUser, affordablePlayerPieces.getValue(), Integer.parseInt(coinNumber.getText()));
            coinNumber.setText(String.valueOf(newCoinBalance));
            showOwnedPlayerPieces();
            updateAffordablePlayerPieces();
        } catch (IOException | IllegalArgumentException ex) {
            purchaseStatus.setText(ex.getMessage());
        }
    }

    /**
     * Shows player pieces owned by currently logged in player.
     *
     * @throws FileNotFoundException the file not found exception
     */
    private void showOwnedPlayerPieces() throws FileNotFoundException {
        preOwnedPieces.getItems().clear();
        ArrayList<String> list = new ArrayList<>();
        File file = new File(USER_COIN_FILE_DIRECTORY);
        Scanner in = null;

        in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        while (in.hasNextLine()) {
            String playerPiece = in.next();
            String[] parts = in.nextLine().split(DELIMITER);
            if (playerPiece.equals(currentUser)) {
                list.addAll(Arrays.asList(parts).subList(5, parts.length));
            }
        }
        preOwnedPieces.getItems().addAll(list);
        in.close();
    }

    /**
     * Goes back to the main menu when the back button is clicked.
     *
     * @param e the event
     * @throws IOException the io exception, if main menu is not found (unlikely)
     */
    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Initialises the form, setting background image, and setting up onAction
     * events.
     *
     * @param location  the location (not used)
     * @param resources the resources (not used)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundImage backgroundImage = null;
        try {
            backgroundImage = new BackgroundImage(new Image(String.valueOf(new File(URANUS_BACKGROUND_PATH).toURI().toURL())),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(0, 0, false, false, false, true));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainPane.setBackground(new Background(backgroundImage));
        mainPane.setMinWidth(614);

        affordablePlayerPieces.setOnAction(event -> {
            if (affordablePlayerPieces.getValue() != null) {
                try {
                    playerPiecePreview.setImage(new Image(String.valueOf(new File("src/view/res/img/player_piece/" + affordablePlayerPieces.getValue()).toURI().toURL())));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Updates the player pieces that are available for purchase by the player with their new balance.
     *
     * @throws FileNotFoundException if the player piece price file cannot be found.
     */
    private void updateAffordablePlayerPieces() throws FileNotFoundException {
        affordablePlayerPieces.getItems().clear();
        Scanner in = new Scanner(new File(PLAYER_PIECE_PRICE_DIRECTORY));
        in.useDelimiter(DELIMITER);

        while (in.hasNextLine()) {
            in.next();
            String tileAvailable = in.next();
            int tilePrice = in.nextInt();
            if (tilePrice <= Integer.parseInt(coinNumber.getText())) {
                affordablePlayerPieces.getItems().add(tileAvailable);
            }
            //in.nextLine();
        }
        in.close();

        try {
            showOwnedPlayerPieces();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        usernameField.setText("");
        passwordField.setText("");
        loginStatus.setText("You are logged in as: " + currentUser + ".");
    }


    /**
     * Returns the username and coin balance for that user as specified in the username field.
     *
     * @return The target username and coin balance.
     * @throws FileNotFoundException If the user coin file cannot be found.
     */
    private boolean findUserAndGetUserCoinAmount() throws FileNotFoundException {
        affordablePlayerPieces.getItems().clear();
        String targetUsername = usernameField.getText();
        Scanner in = new Scanner(new File(USER_COIN_FILE_DIRECTORY));
        in.useDelimiter(DELIMITER);
        boolean isUserFound = false;

        while (in.hasNextLine() && !isUserFound) {
            String tempUser = in.next();
            if (tempUser.equals(targetUsername)) {
                coinNumber.setText(in.next());
                isUserFound = true;
            }
            in.nextLine();
        }
        in.close();
        return isUserFound;
    }
}
