package java_.game.controller;

import java_.game.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CoinHandler {

    private static final double FACTOR = 0.2;
    private static final int WINNING_MULTIPLIER = 5;
    private static final int LOSING_MULTIPLIER = 1;
    private static final String PLAYER_COINS_FILEPATH = "data/user_coins.txt";
    private static final String DELIMITER = "`";

    public static void updateCoins(Player[] players, int winningPlayerIndex) throws FileNotFoundException {
        int nTurns = GameService.getInstance().getTurnCount();
        int nPlayers = players.length;

        int effort = nTurns / nPlayers;

        int winningCoins = (int)(effort * FACTOR * WINNING_MULTIPLIER);
        int losingCoins = (int)(effort * FACTOR * LOSING_MULTIPLIER);


        File coinFile = new File(PLAYER_COINS_FILEPATH);
        Scanner in = new Scanner(coinFile);
        in.useDelimiter(DELIMITER);
        while (in.hasNextLine()) {

            Scanner ln = new Scanner(in.nextLine());
            ln.useDelimiter(DELIMITER);
            String username = ln.next();
            int nCoins = ln.nextInt();
            int nPlayerPieces = ln.nextInt();
            String ownedPlayerPieces = DELIMITER;
            while (ln.hasNext()) {
                ownedPlayerPieces += ln.next() + "`";
            }
            for (int i = 0; i < players.length; i++) {




            }
            //System.out.println(ownedPlayerPieces);


        }


    }

    public static void main(String[] args) throws FileNotFoundException {
        Player player1 = new Player("samcox", null);
        Player player2 = new Player("bob01", null);
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        updateCoins(players, 0);
    }







}
