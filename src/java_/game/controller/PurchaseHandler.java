package java_.game.controller;

import java_.util.generic_data_structures.Link;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class PurchaseHandler {
    public static final String DELIMITER = "`";
    private static final String USER_COINFILE_DIRECTORY = "data/user_coins.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PLAYERPIECE_PRICE_DIRECTORY = "data/player_piece_cost.txt";


    public static int buyPlayerPiece(String username, String newPlayerPiece, int userAmountOfCoins) throws IOException {
        ArrayList<String> users = new ArrayList<>();

        int newPlayerPiecePrice = 0;
        Scanner in = new Scanner(new File(PLAYERPIECE_PRICE_DIRECTORY));
        in.useDelimiter(DELIMITER);
        boolean isPlayerPiecePriceFound = false;

        while (in.hasNextLine() && !isPlayerPiecePriceFound) {
            newPlayerPiecePrice = in.nextInt();
            String playerPiece = in.next();
            if (playerPiece.equals(newPlayerPiece)) {
                isPlayerPiecePriceFound = true;
            }
            /*if (in.hasNextLine()) {
                in.nextLine();
            }*/
        }
        in.close();

        if (!isPlayerPiecePriceFound) {
            throw new IllegalArgumentException("Player Piece with such name not Found.");
        }

        int newCoinAmount = userAmountOfCoins - newPlayerPiecePrice;
        in = new Scanner(new File(USER_COINFILE_DIRECTORY));
        in.useDelimiter(DELIMITER);
        int indexOfNeededLine = 0;

        while (in.hasNextLine()) {
            String tempUser = in.next();
            if (username.equals(tempUser)) {
                indexOfNeededLine = users.size();
            }
            in.next();
            in.next();
            in.next();
            String numberOfPlayerPiecesStr = in.next();
            int numberOfPlayerPieces = Integer.parseInt(numberOfPlayerPiecesStr);
            HashSet<String> currentlyOwnedPlayerPieces = new HashSet<>();
            for (int i = 0; i < numberOfPlayerPieces; i++) {
                String playerPiece = in.next();
                if (currentlyOwnedPlayerPieces.contains(playerPiece)) {

                }
                currentlyOwnedPlayerPieces.add(playerPiece);
            }

            //loop through each player piece to check for duplicates.
            users.add(tempUser + in.nextLine());
        }
        in.close();

        String currentLine = users.get(indexOfNeededLine);
        String[] parts = currentLine.split(DELIMITER);

        String newRowData = parts[0] + DELIMITER + newCoinAmount + DELIMITER +
                parts[2] + DELIMITER + parts[3] + DELIMITER + (Integer.parseInt(parts[4]) + 1) + DELIMITER;

        for (int i = 5; i < parts.length; i++) {
            newRowData = newRowData + parts[i] + DELIMITER;
        }
        newRowData = newRowData + newPlayerPiece + DELIMITER;
        users.remove(indexOfNeededLine);
        users.add(newRowData);

        FileWriter writer = new FileWriter(USER_COINFILE_DIRECTORY);
        for (String user : users) {
            writer.write(user + System.lineSeparator());
        }
        writer.close();

        return newCoinAmount;
    }

    public static void buyAllFreePlayerPieces(String username) throws IOException {
        ArrayList<String> freePlayerPieces = getFreePlayerPiecesNames();
        for (String freePlayerPiece : freePlayerPieces) {
            buyPlayerPiece(username, freePlayerPiece, 0);
        }
    }

    private static ArrayList<String> getFreePlayerPiecesNames() throws FileNotFoundException {
        Scanner in = new Scanner(new File(PLAYERPIECE_PRICE_DIRECTORY));
        in.useDelimiter(DELIMITER);
        ArrayList<String> freePlayerPiecesNames = new ArrayList<>();

        while (in.hasNextLine()) {
            String costStr = in.next();
            if (costStr.equals("")) break;
            costStr = costStr.replace("\n","");
            costStr = costStr.replace("\r","");
            System.out.println(costStr);
            String playerPieceName = in.next();
            if (Integer.parseInt(costStr) == 0) {
                freePlayerPiecesNames.add(playerPieceName);
            }
        }
        in.close();
        return freePlayerPiecesNames;
    }

    public static void addNewPlayer(String username, boolean isFirstPlayer) throws IOException {
        SimpleDateFormat yyyyddmm = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String dateToday = yyyyddmm.format(calendar.getTime());

        String userRecord = username + DELIMITER + 0 + DELIMITER + 1 + DELIMITER +
                dateToday + DELIMITER + 0 + DELIMITER + "\n";

        byte[] userRecordBytes = userRecord.getBytes();

        Files.write(Paths.get(USER_COINFILE_DIRECTORY), userRecordBytes, StandardOpenOption.APPEND);
        buyAllFreePlayerPieces(username);
    }
}