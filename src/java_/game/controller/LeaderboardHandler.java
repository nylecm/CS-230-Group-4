package java_.game.controller;

import java_.util.Reader;
import java_.game.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaderboardHandler {
    private static final String USER_STATS_FOLDER_DIRECTORY = "data/user_stats";
    private static final String DELIMITER = "`";
    private static final String FILE_EXTENSION_STRING = ".txt";

    /**
     * Updates the leaderboard with the players that participated in a game.
     * If the player won then their number of wins increases by 1.
     * If the player lost then their number of losses increases by 1.
     * @param players The players that participated in a game.
     * @param winningPlayerIndex The index of the player who won the game.
     * @throws FileNotFoundException If the file containing user stats cannot be found.
     */
    public static void updateLeaderboard(Player[] players, int winningPlayerIndex) throws FileNotFoundException {
        String gameboardName = GameService.getInstance().getGameBoard().getName() + FILE_EXTENSION_STRING;
        System.out.println(gameboardName);
        Reader r = new Reader();
        File[] fileNames = r.readFileNames(USER_STATS_FOLDER_DIRECTORY);
        //Loops through every game board file containing user stats
        for (File file : fileNames) {
            boolean[] hasUserBeenFound = new boolean[players.length]; //Keeps track of if the users in the game already have a file line representing their stats
            if (file.getName().equals(gameboardName)) {
                File gameboardFile = new File(USER_STATS_FOLDER_DIRECTORY + "/" + gameboardName);
                Scanner in = new Scanner(gameboardFile);

                ArrayList<String> newFileLines = new ArrayList<>();

                //Loops through the game board file and updates the stats if the player has won or lost.
                while (in.hasNextLine()) {

                    String[] currentPlayerStats = in.nextLine().split(DELIMITER);
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].getUsername().equals(currentPlayerStats[0]) && i == winningPlayerIndex) { //The current line contains the stats of the winning player of the game
                            currentPlayerStats[1] = String.valueOf(Integer.valueOf(currentPlayerStats[1]) + 1);
                            hasUserBeenFound[i] = true;
                        } else if (i != winningPlayerIndex && players[i].getUsername().equals(currentPlayerStats[0])) { //The current line stores the stats of someone who was in the game but lost.
                            currentPlayerStats[2] = String.valueOf(Integer.valueOf(currentPlayerStats[2]) + 1);
                            hasUserBeenFound[i] = true;
                        }
                    }
                    String newFileLine = currentPlayerStats[0] + DELIMITER + currentPlayerStats[1] + DELIMITER + currentPlayerStats[2] + DELIMITER;
                    newFileLines.add(newFileLine);
                }
                in.close();
                //Prints all the updated file lines to the file
                PrintWriter fileWriter = new PrintWriter(gameboardFile);
                for (String line : newFileLines) {
                    fileWriter.println(line);
                }

                //If the user has not played on this game board previously add them to the file.
                for (int i = 0; i < hasUserBeenFound.length; i++) {
                    if (!hasUserBeenFound[i] && i == winningPlayerIndex) {
                        fileWriter.println(players[i].getUsername() + DELIMITER + "1" + DELIMITER + "0" + DELIMITER);
                    } else if (!hasUserBeenFound[i] && i != winningPlayerIndex) {
                        fileWriter.println(players[i].getUsername() + DELIMITER + "0" + DELIMITER + "1" + DELIMITER);
                    }
                }
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }
}
