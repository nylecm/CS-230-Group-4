package java_.controller.reworkedGuiFiles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.io.FileWriter;

public class PlayerPiecePurchaseController {

    private final String USER_COINFILE_DIRECTORY = "data/user_coins.txt";
    private final String PLAYERPIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";
    @FXML private Boolean userFound = false;
    @FXML private TextField usernameField;
    @FXML private String username = "";
    @FXML private String userCoinAmount;
    @FXML private Label coinNumber;
    @FXML private ListView preOwnedPieces;
    @FXML private ChoiceBox<String> affordablePlayerPieces;
    private int userNoCoins;

    @FXML
    private void onLoginButtonClicked(ActionEvent e) throws IOException {
        preOwnedPieces.getItems().clear();
        username = usernameField.getText();
        File userCoinFile = new File(USER_COINFILE_DIRECTORY);
        Scanner coinFileScanner = new Scanner(userCoinFile);
        coinFileScanner.useDelimiter("`");
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
            File priceFile = new File(PLAYERPIECE_PRICE_DIRECTORY);
            Scanner in = new Scanner(priceFile);
            in.useDelimiter("`");
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
        ArrayList<String> users = new ArrayList<String>();
        int tilePrice = 0;
        String playerPieceBought = "";
        File priceFile = new File(PLAYERPIECE_PRICE_DIRECTORY);
        Scanner in = new Scanner(priceFile);
        in.useDelimiter("`");
        while (in.hasNextLine()) {
            String playerPiece = in.next();
            if (playerPiece.equals(getChoice(affordablePlayerPieces))) {
                tilePrice = in.nextInt();
                playerPieceBought = playerPiece;
            }
            in.nextLine();
        }
        in.close();
        int newCoinAmount = userNoCoins - tilePrice;
        coinNumber.setText(String.valueOf(newCoinAmount));
        File userFile = new File(USER_COINFILE_DIRECTORY);
        Scanner sc = new Scanner(userFile);
        sc.useDelimiter("`");
        int indexOfNeededLine = 0;
        while(sc.hasNextLine()) {
            String tempUser = sc.next();
            if (username.equals(tempUser)) {
                indexOfNeededLine = users.size();
            }
            users.add(tempUser + sc.nextLine());
        }
        sc.close();

        String currentLine = users.get(indexOfNeededLine);
        String[] parts = currentLine.split("`");
        String newRowData = parts[0] + "`" + newCoinAmount + "`";
        for (int i = 2; i < parts.length; i++) {
            newRowData = newRowData + parts[i] + "`";
        }
        newRowData = newRowData + playerPieceBought + "`";
        users.remove(indexOfNeededLine);
        users.add(newRowData);

        FileWriter writer = new FileWriter(USER_COINFILE_DIRECTORY);
        for (int j = 0; j < users.size(); j++) {
            writer.write(users.get(j) + System.lineSeparator());
        }
        writer.close();

    }

    private String getChoice(ChoiceBox<String> affordablePlayerPieces) {
        String purchaseTile = affordablePlayerPieces.getValue();
        return purchaseTile;
    }
    @FXML
    private void onOwnedPiecesButtonClicked(ActionEvent e) throws FileNotFoundException {
        preOwnedPieces.getItems().clear();
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(USER_COINFILE_DIRECTORY);
        Scanner in = new Scanner(file);
        in.useDelimiter("`");
        while (in.hasNextLine()) {
            String playerPiece = in.next();
            String[] parts = in.nextLine().split("`");
            if (playerPiece.equals(username)) {
                for (int i = 5; i < parts.length; i++) {
                    list.add(parts[i]);
                }
            }
        }
        preOwnedPieces.getItems().addAll(list);
    }
}
