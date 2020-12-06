package java_.game.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class PurchaseHandler {
    public static final String DELIMITER = "`";
    private static final String USER_COIN_FILE = "data/user_coins.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PLAYER_PIECE_PRICE_FILE = "data/player_piece_cost.txt";
    private static final String PLAYER_PIECE_ALREADY_OWNED = "Player piece already owned!";
    private static final int FIRST_INDEX_WITH_PLAYER_PIECE = 5;


    /**
     * Purchases a new player piece for the specified player. Their coin balance is updated to reflect this purchase.
     *
     * @param username          The username of th player making the purchase.
     * @param newPlayerPiece    The new player piece being purchased.
     * @param userAmountOfCoins The user's current coin balance
     * @return The updated coin balance of the user after the purchase has been made.
     * @throws IOException If the player piece the player is trying to buy is already owned by the player.
     */
    public static int buyPlayerPiece(String username, String newPlayerPiece, int userAmountOfCoins) throws IOException {
        ArrayList<String> users = new ArrayList<>();

        int piecePrice = 0;
        File priceFile = new File(PLAYER_PIECE_PRICE_FILE);
        Scanner in = new Scanner(priceFile);
        in.useDelimiter(DELIMITER);
        //Loops through file until it finds the player piece player piece the player wants to buy and stores its price.
        while (in.hasNextLine()) {
            in.next();
            String playerPiece = in.next();
            if (playerPiece.equals(newPlayerPiece)) {
                piecePrice = in.nextInt();
            }
            if (in.hasNextLine()) {
                in.nextLine();
            }
        }
        in.close();

        int newCoinAmount = userAmountOfCoins - piecePrice; //New balance after transaction
        File userFile = new File(USER_COIN_FILE);
        in = new Scanner(userFile);
        in.useDelimiter(DELIMITER);


        //Loops through the user coin file until it finds the user making the purchase and stores the line number
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

        //Loops through all owned player pieces of the player making the purchase.
        //Throws an error if they already own the piece they are trying to purchase.
        for (int i = FIRST_INDEX_WITH_PLAYER_PIECE; i < parts.length; i++) {
            if (parts[i].equals(newPlayerPiece)) {
                throw new IllegalArgumentException(PLAYER_PIECE_ALREADY_OWNED);
            }
        }

        //Updates the file line of the player making the purchase to reflect new balance
        String newRowData = parts[0] + DELIMITER + newCoinAmount + DELIMITER +
                parts[2] + DELIMITER + parts[3] + DELIMITER + (Integer.parseInt(parts[4]) + 1) + DELIMITER;

        for (int i = FIRST_INDEX_WITH_PLAYER_PIECE; i < parts.length; i++) {
            newRowData = newRowData + parts[i] + DELIMITER;
        }
        //Adds the new purchased player piece to all owned player pieces.
        newRowData = newRowData + newPlayerPiece + DELIMITER;
        users.remove(indexOfNeededLine);
        users.add(newRowData);

        //Writes the updated line to the file.
        FileWriter writer = new FileWriter(USER_COIN_FILE);
        for (String user : users) {
            writer.write(user + System.lineSeparator());
        }
        writer.close();

        return newCoinAmount;
    }

    /**
     * Returns all owned player pieces for a specified player.
     *
     * @param username The username of the player who the player pieces belong to.
     * @return The ArrayList of all player pieces owned by the user.
     * @throws FileNotFoundException If the user coin file containing the player pieces cannot be found.
     */
    public static ArrayList<String> getPlayersPurchasedPlayerPieces(String username) throws FileNotFoundException {
        ArrayList<String> ownedPlayerPieces = new ArrayList<>();

        Scanner in = new Scanner(new File(USER_COIN_FILE));
        in.useDelimiter(DELIMITER);
        boolean targetUserFound = false;

        //Loops through file until target user is found and then reads all owned player pieces
        while (in.hasNextLine() && !targetUserFound) {
            String curUsername = in.next();
            System.out.println(curUsername);
            if (curUsername.equals(username)) {
                in.next();
                in.next();
                in.next();
                String numberOfPlayerPiecesOwnedStr = in.next();
                int numberOfPlayerPiecesOwned = Integer.parseInt(numberOfPlayerPiecesOwnedStr);
                for (int i = 0; i < numberOfPlayerPiecesOwned; i++) {
                    ownedPlayerPieces.add(in.next());
                }
                targetUserFound = true;
            } else {
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }
        }
        in.close();
        return ownedPlayerPieces;
    }

    /**
     * Adds all free player pieces (player pieces with a coin cost of 0) to the player pieces owned by the player.
     *
     * @param username The username of the player who is buying all the free player pieces.
     * @throws IOException If the free player piece is already owned by the player.
     */
    public static void buyAllFreePlayerPieces(String username) throws IOException {
        ArrayList<String> freePlayerPieces = getFreePlayerPiecesNames();
        for (String freePlayerPiece : freePlayerPieces) {
            buyPlayerPiece(username, freePlayerPiece, 0);
        }
    }

    /**
     * Returns the names of all free player pieces
     *
     * @return The ArrayList containing the names of all free player pieces.
     * @throws FileNotFoundException
     */
    private static ArrayList<String> getFreePlayerPiecesNames() throws FileNotFoundException {
        Scanner in = new Scanner(new File(PLAYER_PIECE_PRICE_FILE));
        in.useDelimiter(DELIMITER);
        ArrayList<String> freePlayerPiecesNames = new ArrayList<>();

        while (in.hasNextLine()) {
            in.next();
            String playerPieceName = in.next();
            String costStr = in.next();
            if (Integer.parseInt(costStr) == 0) {
                freePlayerPiecesNames.add(playerPieceName);
            }
        }
        in.close();
        return freePlayerPiecesNames;
    }

    /**
     * Adds a new player to the file containing all players and their owned player pieces.
     * The new player will be given all free playe pieces.
     *
     * @param username      the username of the new player to be added to the file.
     * @param isFirstPlayer True if the player is the first playr of the game.
     * @throws IOException If the player already exists.
     */
    public static void addNewPlayer(String username, boolean isFirstPlayer) throws IOException { //todo remove boolean parameter?
        SimpleDateFormat yyyyddmm = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String dateToday = yyyyddmm.format(calendar.getTime());

        String userRecord = username + DELIMITER + 0 + DELIMITER + 1 + DELIMITER + dateToday + DELIMITER + 0 + DELIMITER + "\n";

        byte[] userRecordBytes = userRecord.getBytes();

        Files.write(Paths.get(USER_COIN_FILE), userRecordBytes, StandardOpenOption.APPEND);
        buyAllFreePlayerPieces(username);
    }
}