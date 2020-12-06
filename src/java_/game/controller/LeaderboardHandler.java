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

    public static void updateLeaderboard(Player[] players, int winningPlayerIndex, String gameboardName) throws FileNotFoundException { //todo remove gameboardName (just for testing)

        //String gameboardName = GameService.getInstance().getGameBoard().getName(); //todo un-comment
        Reader r = new Reader();
        File[] fileNames = r.readFileNames(USER_STATS_FOLDER_DIRECTORY);
        for (File file : fileNames) {
            boolean[] hasUserBeenFound = new boolean[players.length];
            if (file.getName().equals(gameboardName)) {

                File gameboardFile = new File(USER_STATS_FOLDER_DIRECTORY + "/" + gameboardName);
                Scanner in = new Scanner(gameboardFile);

                ArrayList<String> newFileLines = new ArrayList<>();

                while (in.hasNextLine()) {

                    String[] currentPlayerStats = in.nextLine().split(DELIMITER);
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].getUsername().equals(currentPlayerStats[0]) && i == winningPlayerIndex) { //The current line contains the stats of the winning player of the game
                            currentPlayerStats[1] = String.valueOf(Integer.valueOf(currentPlayerStats[1]) + 1); //todo probably being stupid - it's 2am
                            hasUserBeenFound[i] = true;
                        } else if (i != winningPlayerIndex && players[i].getUsername().equals(currentPlayerStats[0])) { //The current line stores the stats of someone who was in the game but lost.
                            currentPlayerStats[2] = String.valueOf(Integer.valueOf(currentPlayerStats[2]) + 1); //todo probably being stupid - it's 2am
                            hasUserBeenFound[i] = true;
                        }
                    }
                    String newFileLine = currentPlayerStats[0] + DELIMITER + currentPlayerStats[1] + DELIMITER + currentPlayerStats[2] + DELIMITER;
                    newFileLines.add(newFileLine);
                }
                in.close();
                PrintWriter fileWriter = new PrintWriter(gameboardFile);
                for (String line : newFileLines) { //todo
                    fileWriter.println(line);
                }

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

    public static void main(String[] args) {
        Player newPlayer = new Player("james", null);
        Player newPlayer2 = new Player("roy", null);

        Player[] playerArray = new Player[2];
        playerArray[0] = newPlayer;
        playerArray[1] = newPlayer2;

        String gameboardName = "oberon_2.txt";

        try {
            updateLeaderboard(playerArray, 0, gameboardName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
