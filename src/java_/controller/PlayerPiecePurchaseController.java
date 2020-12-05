package java_.controller;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import java_.game.controller.PurchaseHandler;
import java_.util.security.LoginHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PlayerPiecePurchaseController {

    private static final String USER_COIN_FILE_DIRECTORY = "data/user_coins.txt";
    private static final String PLAYER_PIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";
    public static final String DELIMITER = "`";

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

    @FXML
    private void onLoginButtonClicked(ActionEvent e) throws IOException {
        if (LoginHandler.login(usernameField.getText(), passwordField.getText())) {
            preOwnedPieces.getItems().clear();
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

            if (isUserFound) {
                in = new Scanner(new File(PLAYER_PIECE_PRICE_DIRECTORY));
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
            } else {
                //todo notify user... refresh number of coins
            }
        } else {
            System.out.println("wp");
        }
    }

    @FXML
    private void onBuyButtonClicked(ActionEvent e) {
        try {
            PurchaseHandler.buyPlayerPiece(usernameField.getText(), affordablePlayerPieces.getValue(), Integer.parseInt(coinNumber.getText()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex2) {
            System.out.println(ex2.getMessage()); //todo notify user of error
        }
    }

    /*private String getChoice(ChoiceBox<String> affordablePlayerPieces) {
        return affordablePlayerPieces.getValue();
    }*/

    @FXML
    private void onOwnedPiecesButtonClicked(ActionEvent e) throws FileNotFoundException {
        preOwnedPieces.getItems().clear();
        ArrayList<String> list = new ArrayList<>();
        File file = new File(USER_COIN_FILE_DIRECTORY);
        Scanner in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        while (in.hasNextLine()) {
            String playerPiece = in.next();
            String[] parts = in.nextLine().split(DELIMITER);
            if (playerPiece.equals(usernameField.getText())) {
                list.addAll(Arrays.asList(parts).subList(5, parts.length));
            }
        }
        preOwnedPieces.getItems().addAll(list);
    }
}
