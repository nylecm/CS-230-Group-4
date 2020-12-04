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

    public static void updateCoins(Player[] players, int winningPlayerIndex) throws FileNotFoundException {
        //int nTurns = GameService.getInstance().getTurnCount(); todo uncomment
        int nTurns = 100; //todo remove
        int nPlayers = players.length;

        int effort = nTurns / nPlayers;

        int winningCoins = (int)(effort * FACTOR * WINNING_MULTIPLIER); //todo balancing ...
        int losingCoins = (int)(effort * FACTOR * LOSING_MULTIPLIER);

        File coinFile = new File(PLAYER_COINS_FILEPATH);
        Scanner in = new Scanner(coinFile);
        in.useDelimiter(DELIMITER);
        ArrayList<String> newFileLines = new ArrayList<>();
        while (in.hasNextLine()) {

            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);
            String username = ln.next();
            int nCoins = ln.nextInt();
            int dailyStreak = ln.nextInt();
            String lastLoginDate = ln.next();
            int nPlayerPieces = ln.nextInt();
            String ownedPlayerPieces = "";
            while (ln.hasNext()) {
                ownedPlayerPieces += ln.next() + DELIMITER;
            }
            for (int i = 0; i < players.length; i++) {
                if (players[i].getUsername().equals(username) && i == winningPlayerIndex) {
                    nCoins += winningCoins;
                } else if (players[i].getUsername().equals(username) && i != winningPlayerIndex) {
                    nCoins += losingCoins;
                }
            }
            //lineWriter.println(username + DELIMITER + nCoins + DELIMITER + nPlayerPieces + ownedPlayerPieces); //todo fix print writer
            //System.out.println(ownedPlayerPieces);
            newFileLines.add(username + DELIMITER + nCoins + DELIMITER + dailyStreak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + DELIMITER + ownedPlayerPieces);
        }

        //todo remove array list after fixing printWriter
        PrintWriter lineWriter = new PrintWriter(coinFile);
        for (String newFileLine: newFileLines) {
            lineWriter.println(newFileLine);
        }
        lineWriter.flush();
        lineWriter.close();
    }

    public static void increaseStreak(String username) throws FileNotFoundException, ParseException {
        File coinFile = new File(PLAYER_COINS_FILEPATH);
        Scanner in = new Scanner(coinFile);
        in.useDelimiter(DELIMITER);
        ArrayList<String> newFileLines = new ArrayList<>();
        while (in.hasNextLine()) {

            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);

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
            SimpleDateFormat yyyyddmm = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(yyyyddmm.parse(lastLoginDate));
            calendar.add(Calendar.DATE, 1);
            String dayAfterLastLogin = yyyyddmm.format(calendar.getTime());
           // System.out.print(dayAfterLastLogin);


            //Today's date
            calendar = Calendar.getInstance();
            String dateToday = yyyyddmm.format(calendar.getTime());


            //System.out.print(dayAfterLastLogin);
            //System.out.println();

            if (username.equals(fileUsername)) {
                lastLoginDate = dateToday;
                if (dayAfterLastLogin.equals(dateToday)) { // consecutive days logged in
                    streak += 1; //increase streak
                    nCoins += streakCoins(streak);
                } else if (!lastLoginDate.equals(dateToday) && !dayAfterLastLogin.equals(dateToday)){ //Logged in two different days that are more than a day apart.
                    streak = 1; //reset streak
                    nCoins += streakCoins(streak);
                }
            }

            String newFileLine = fileUsername + DELIMITER + nCoins + DELIMITER + streak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + DELIMITER + playerPieces;
            newFileLines.add(newFileLine);

        }
        PrintWriter lineWriter = new PrintWriter(coinFile);
        for (String newLine: newFileLines) {
            lineWriter.println(newLine);
        }
        lineWriter.flush();
        lineWriter.close();

    }

    private static int streakCoins(int streak) {
        int streakValue = 10;
        int i = 1;
        while (streakValue < DAILY_LOGIN_REWARD_CAP && i < streak) {
            streakValue = (int) ((streakValue + 10) * 1.2);
            i++;
        }
        return streakValue;
    }





    public static void main(String[] args) throws FileNotFoundException, ParseException {
        /*
        Player player1 = new Player("nylecm", null);
        Player player2 = new Player("bob101", null);
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        updateCoins(players, 0);
        */
        increaseStreak("nylecm2");
        //System.out.println(streakCoins(1));
    }
}
