package java_.game.controller;

import java_.game.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CoinHandler {

    private static final double FACTOR = 0.2;
    private static final int WINNING_MULTIPLIER = 5;
    private static final int LOSING_MULTIPLIER = 1;
    private static final String PLAYER_COINS_FILEPATH = "data/user_coins.txt";
    private static final String DELIMITER = "`";

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
        //PrintWriter lineWriter = new PrintWriter(coinFile);;
        ArrayList<String> newFileLines = new ArrayList<>();
        while (in.hasNextLine()) {

            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);
            String username = ln.next();
            int nCoins = ln.nextInt();
            int dailyStreak = ln.nextInt();
            String lastLoginDate = ln.next();
            int nPlayerPieces = ln.nextInt();
            String ownedPlayerPieces = DELIMITER;
            while (ln.hasNext()) {
                ownedPlayerPieces += ln.next() + "`";
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
            newFileLines.add(username + DELIMITER + nCoins + DELIMITER + dailyStreak + DELIMITER + lastLoginDate + DELIMITER + nPlayerPieces + ownedPlayerPieces);
        }

        //todo remove array list after fixing printWriter
        PrintWriter lineWriter = new PrintWriter(coinFile);
        for (String newFileLine: newFileLines) {
            lineWriter.println(newFileLine);
        }
        lineWriter.flush();
        lineWriter.close();
    }

    public static void increaseStreak(String username) throws FileNotFoundException {

        Scanner in = new Scanner(new File(PLAYER_COINS_FILEPATH));
        in.useDelimiter(DELIMITER);
        while (in.hasNextLine()) {

            String fileUsername = in.next();
            int nCoins = in.nextInt();
            int streak = in.nextInt();
            String lastLoginDate = in.next();
            in.nextLine();



            /*
            System.out.print(in.next());
            System.out.print(in.nextInt());
            System.out.print(in.nextInt());
            System.out.println(in.next());
            */







        }




    }


    public static void main(String[] args) throws FileNotFoundException {
        /*
        Player player1 = new Player("nylecm", null);
        Player player2 = new Player("bob101", null);
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        updateCoins(players, 0);
        */
        increaseStreak("samcox");
    }
}
