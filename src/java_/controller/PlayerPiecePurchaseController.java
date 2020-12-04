package java_.controller;

import java_.game.controller.PurchaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerPiecePurchaseController {

    private static final String USER_COIN_FILE_DIRECTORY = "data/user_coins.txt";
    private static final String PLAYER_PIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";
    public static final String DELIMITER = "`";

    @FXML
    private TextField usernameField;

    @FXML
    private Label coinNumber;

    @FXML
    private ListView preOwnedPieces;

    @FXML
    private ChoiceBox<String> affordablePlayerPieces;

    private Boolean userFound = false;
    private String username = "";
    private String userCoinAmount;
    private int userNoCoins;

    @FXML
    private void onLoginButtonClicked(ActionEvent e) throws IOException {
        //todo use log in handler

        preOwnedPieces.getItems().clear();
        username = usernameField.getText();
        File userCoinFile = new File(USER_COIN_FILE_DIRECTORY);
        Scanner coinFileScanner = new Scanner(userCoinFile);
        coinFileScanner.useDelimiter(DELIMITER);
        while (coinFileScanner.hasNextLine()) {
            String tempUser = coinFileScanner.next();
            if (tempUser.equals(username)) {
                userCoinAmount = coinFileScanner.next();
                userNoCoins = Integer.parseInt(userCoinAmount);
                userFound = true;
            }
            coinFileScanner.nextLine();
        }
        coinFileScanner.close();
        if (userFound) {
            coinNumber.setText(userCoinAmount);
            File priceFile = new File(PLAYER_PIECE_PRICE_DIRECTORY);
            Scanner in = new Scanner(priceFile);
            in.useDelimiter(DELIMITER);
            while (in.hasNextLine()) {
                String tileAvailable = in.next();
                int tilePrice = in.nextInt();
                if (tilePrice <= Integer.parseInt(userCoinAmount)) {
                    affordablePlayerPieces.getItems().add(tileAvailable);
                }
                in.nextLine();
            }
            in.close();
        }
    }

    @FXML
    private void onBuyButtonClicked(ActionEvent e) throws IOException {
        PurchaseHandler.buyPlayerPiece(username, affordablePlayerPieces.getValue(), Integer.parseInt(coinNumber.getText()));
    }

    private String getChoice(ChoiceBox<String> affordablePlayerPieces) {
        return affordablePlayerPieces.getValue();
    }

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
            if (playerPiece.equals(username)) {
                for (int i = 5; i < parts.length; i++) {
                    list.add(parts[i]);
                }
            }
        }
        preOwnedPieces.getItems().addAll(list);
    }
}
