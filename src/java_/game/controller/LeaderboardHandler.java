package java_.game.controller;

import java_.controller.Reader;
import java_.game.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LeaderboardHandler {

    private static final String USER_STATS_FOLDER_DIRECTORY = "data/user_stats";
    private static final String DELIMITER = "`";

    public static void updateLeaderboard(Player[] players, int winningPlayerIndex, String gameboardName) throws FileNotFoundException { //todo remove gameboardName (just for testing)

        //String gameboardName = GameService.getInstance().getGameBoard().getName(); //todo un-comment
        Reader r = new Reader();
        File[] fileNames = r.readFileNames(USER_STATS_FOLDER_DIRECTORY);
        for (File file: fileNames) {
           //System.out.println(file.getName());
            if (file.getName().equals(gameboardName)) {

                File gameboardFile = new File(USER_STATS_FOLDER_DIRECTORY + "/" + gameboardName);
                Scanner in = new Scanner(gameboardFile);

                while (in.hasNextLine()) {

                    String[] currentPlayerStats = in.nextLine().split(DELIMITER);
                    for (String w : currentPlayerStats) { //todo remove
                        System.out.println(w);
                    }
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].getUsername().equals(currentPlayerStats[0]) && i == winningPlayerIndex) { //The current line contains the stats of the winning player of the game
                            currentPlayerStats[1] = String.valueOf(Integer.valueOf(currentPlayerStats[1]) + 1); //todo probably being stupid - it's 2am
                        } else if (i != winningPlayerIndex && players[i].getUsername().equals(currentPlayerStats[0])){ //The current line stores the stats of someone who was in the game but lost.
                            currentPlayerStats[2] = String.valueOf(Integer.valueOf(currentPlayerStats[2]) + 1); //todo probably being stupid - it's 2am
                        }
                    }


                    //todo printwriter stuff

                    for (String w : currentPlayerStats) { //todo remove
                        System.out.println(w);
                    }


                }

                //System.out.println(in.nextLine());



            }
        }


    }

    public static void main(String[] args) {


        Player newPlayer = new Player("samcox", null);
        Player newPlayer2 = new Player("bob101", null);

        Player[] playerArray = new Player[2];
        playerArray[0] = newPlayer;
        playerArray[1] = newPlayer2;

        String gameboardName = "oberon_2.txt";

        try {
            updateLeaderboard(playerArray, 0, gameboardName);
        } catch (Exception e) {

        }

    }


}
