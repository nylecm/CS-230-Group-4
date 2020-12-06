package java_.game.controller;

import java_.game.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Handles the process of awarding coins to players.
 */
public class CoinHandler {
    //todo testing
    private static final double FACTOR = 0.2;
    private static final int WINNING_MULTIPLIER = 5;
    private static final int LOSING_MULTIPLIER = 1;
    private static final String PLAYER_COINS_FILEPATH = "data/user_coins.txt";
    private static final String DELIMITER = "`";
    private static final int DAILY_LOGIN_COIN_INCREMENT = 10;
    private static final double DAILY_LOGIN_MODIFIER = 1.2;
    private static final int DAILY_LOGIN_REWARD_CAP = 146;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Updates the coins of players that participated in the game.
     * The amount of coins is determined ((nTurns / nPlayers) * 0.2 * 5) for a winning player and
     * ((nTurns / nPlayers) * 0.2 * 1) for a losing player.
     * @param players The players that participated in the game.
     * @param winningPlayerIndex The index of the player who won the game.
     * @throws FileNotFoundException If the file containing the user coin details cannot be found.
     */
    public static void updateCoins(Player[] players, int winningPlayerIndex) throws FileNotFoundException {
        int nTurns = GameService.getInstance().getTurnCount(); //todo uncomment
        //int nTurns = 100; //testing only.

        // Calculate number of coins that ought to be given out:
        int nPlayers = players.length;
        int effort = nTurns / nPlayers;
        int winningCoins = (int) (effort * FACTOR * WINNING_MULTIPLIER); //todo balancing ...
        int losingCoins = (int) (effort * FACTOR * LOSING_MULTIPLIER);

        // Gives player's their earned coins:
        Scanner in = new Scanner(new File(PLAYER_COINS_FILEPATH));
        in.useDelimiter(DELIMITER);
        ArrayList<String> newFileLines = new ArrayList<>();

        while (in.hasNextLine()) {
            //Store each attribute of the file line.
            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);
            String username = ln.next();
            int nCoins = ln.nextInt();
            int dailyStreak = ln.nextInt();
            String lastLoginDate = ln.next();
            int nPlayerPieces = ln.nextInt();
            String ownedPlayerPieces = "";

            //Loop though all owned player pieces and store them
            while (ln.hasNext()) {
                ownedPlayerPieces += ln.next() + DELIMITER;
            }

            for (int i = 0; i < players.length; i++) {
                if (players[i].getUsername().equals(username) && i == winningPlayerIndex) {
                    nCoins += winningCoins; //If the player won give them the winning amount of coins
                } else if (players[i].getUsername().equals(username) && i != winningPlayerIndex) {
                    nCoins += losingCoins; //If the player lost give them a losing amount of coins
                }
            }
            //After changes to number of coins (if there were any), store the new file line.
            newFileLines.add(username + DELIMITER + nCoins + DELIMITER + dailyStreak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + DELIMITER + ownedPlayerPieces);
            ln.close();
        }

        in.close();
        //todo remove array list after fixing printWriter
        //Write all the new file lines
        PrintWriter lineWriter = new PrintWriter(new File(PLAYER_COINS_FILEPATH));
        for (String newFileLine : newFileLines) {
            lineWriter.println(newFileLine);
        }
        lineWriter.flush();
        lineWriter.close();
    }

    /**
     * Gives a player a specified amount of coins.
     * @param username The username of the player who the coins are to be given to.
     * @param coins The amount of coins to be given to the player.
     * @throws FileNotFoundException If the file containing the user coin details cannot be found.
     */
    public static void giveCoins(String username, int coins) throws FileNotFoundException {
        File coinFile = new File(PLAYER_COINS_FILEPATH);
        Scanner in = new Scanner(coinFile);
        in.useDelimiter(DELIMITER);
        ArrayList<String> newFileLines = new ArrayList<>();

        while (in.hasNextLine()) {

            //Store all file line attributes
            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);
            String fileUsername = ln.next();
            int nCoins = ln.nextInt();
            int streak = ln.nextInt();
            String lastLoginDate = ln.next();
            int nPlayerPieces = ln.nextInt();
            String playerPieces = "";
            //Store all owned player pieces
            while (ln.hasNext()) {
                playerPieces += ln.next() + DELIMITER;
            }
            //Increase coins by specified amount
            if (fileUsername.equals(username)) {
                nCoins += coins;
            }

            //The new file line with updated coins is stored
            newFileLines.add(fileUsername + DELIMITER + nCoins + DELIMITER + streak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + DELIMITER + playerPieces);
            ln.close();
        }
        in.close();

        //Write all new file lines
        PrintWriter lineWriter = new PrintWriter(coinFile);
        for (String newFileLine : newFileLines) {
            lineWriter.println(newFileLine);
        }
        lineWriter.close();
    }

    /**
     * Increases the daily streak of players if they have played the game two days in a row. Otherwise, resets their streak to 1.
     * @param username The username of the player who has the daily streak.
     * @throws FileNotFoundException If the file containing the user coin details cannot be found.
     * @throws ParseException If the date today string cannot be parsed.
     */
    public static void increaseDailyStreak(String username) throws FileNotFoundException, ParseException {
        File coinFile = new File(PLAYER_COINS_FILEPATH);
        Scanner in = new Scanner(coinFile);
        in.useDelimiter(DELIMITER);
        ArrayList<String> newFileLines = new ArrayList<>();
        while (in.hasNextLine()) {

            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);

            //Store all file line attributes
            String fileUsername = ln.next();
            int nCoins = ln.nextInt();
            int streak = ln.nextInt();
            String lastLoginDate = ln.next();
            int nPlayerPieces = ln.nextInt();
            String playerPieces = "";
            while (ln.hasNext()) {
                playerPieces += ln.next() + DELIMITER;
            }

            //Calculating day after previous login date
            SimpleDateFormat yyyyddmm = new SimpleDateFormat(DATE_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(yyyyddmm.parse(lastLoginDate));
            calendar.add(Calendar.DATE, 1);
            String dayAfterLastLogin = yyyyddmm.format(calendar.getTime());

            //Today's date
            calendar = Calendar.getInstance();
            String dateToday = yyyyddmm.format(calendar.getTime());

            if (username.equals(fileUsername)) {

                if (dayAfterLastLogin.equals(dateToday)) { // consecutive days logged in
                    streak += 1; //increase streak
                    nCoins += streakCoins(streak);
                } else if (!lastLoginDate.equals(dateToday)) { //Logged in two different days that are more than a day apart.
                    streak = 1; //reset streak
                    nCoins += streakCoins(streak);
                }
                lastLoginDate = dateToday;
            }

            String newFileLine = fileUsername + DELIMITER + nCoins + DELIMITER + streak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + DELIMITER + playerPieces;
            newFileLines.add(newFileLine);

            ln.close();
        }
        in.close();
        PrintWriter lineWriter = new PrintWriter(coinFile);
        for (String newLine : newFileLines) {
            lineWriter.println(newLine);
        }
        lineWriter.flush();
        lineWriter.close();

    }

    /**
     * Determines the amount of coins a player should receive for their daily streak.
     * The amount starts at 10 for a single day logged in.
     * Every successive day, you add 10 and multiply by 1.2 to get the amount of coins received for the next successive day logged in.
     * @param streak The number of successive days the user has logged in to play the game.
     * @return The amount of coins to be given for the user's streak.
     */
    private static int streakCoins(int streak) {
        int streakValue = 10;
        int i = 1;
        while (streakValue < DAILY_LOGIN_REWARD_CAP && i < streak) {
            streakValue = (int) ((streakValue + DAILY_LOGIN_COIN_INCREMENT) * DAILY_LOGIN_MODIFIER);
            i++;
        }
        return streakValue;
    }
}
