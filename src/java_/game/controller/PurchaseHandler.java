package java_.game.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseHandler {
    public static final String DELIMITER = "`";
    private static final String USER_COINFILE_DIRECTORY = "data/user_coins.txt";

    private static final String PLAYERPIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";


    public static int buyPlayerPiece(String username, String newPlayerPiece, int userAmountOfCoins) throws IOException {
        ArrayList<String> users = new ArrayList<String>();

        int piecePrice = 0;
        String playerPieceBought = "";
        File priceFile = new File(PLAYERPIECE_PRICE_DIRECTORY);
        Scanner in = new Scanner(priceFile);
        in.useDelimiter(DELIMITER);
        while (in.hasNextLine()) {
            String playerPiece = in.next();
            if (playerPiece.equals(newPlayerPiece)) {
                piecePrice = in.nextInt();
                playerPieceBought = playerPiece;
            }
            in.nextLine();
        }
        in.close();

        int newCoinAmount = userAmountOfCoins - piecePrice;
        File userFile = new File(USER_COINFILE_DIRECTORY);
        in = new Scanner(userFile);
        in.useDelimiter(DELIMITER);
        int indexOfNeededLine = 0;
        while (in.hasNextLine()) {
            String tempUser = in.next();
            if (username.equals(tempUser)) {
                indexOfNeededLine = users.size();
            }
            users.add(tempUser + in.nextLine());
        }
        in.close();

        String currentLine = users.get(indexOfNeededLine);
        String[] parts = currentLine.split(DELIMITER);


        String newRowData = parts[0] + DELIMITER + newCoinAmount + DELIMITER;

        for (int i = 2; i < parts.length; i++) {
            newRowData = newRowData + parts[i] + DELIMITER;
        }
        newRowData = newRowData + playerPieceBought + DELIMITER;
        users.remove(indexOfNeededLine);
        users.add(newRowData);

        FileWriter writer = new FileWriter(USER_COINFILE_DIRECTORY);
        for (String user : users) {
            writer.write(user + System.lineSeparator());
        }
        writer.close();

        return newCoinAmount;
    }
}
