package java_.game.controller;

import java_.game.player.PlayerService;
import java_.game.tile.GameBoard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameService {
    private static GameService instance = null;

    private GameBoard gb;
    private PlayerService ps;
    private int turnCount;

    private GameService() {
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void loadNewGame(File f) throws FileNotFoundException {
        Scanner in = new Scanner(f);
        in.useDelimiter("` ");

        String boardName;

        while (in.hasNextLine()) {
            boardName = in.next();

        }



        in.close();

        // Player Serv create...
        // Silk bag create ...
        // Gamebaord create ...
        // UI Data Bundle...
    }

    public void loadSavedInstance(File f) throws FileNotFoundException {
        Scanner in = new Scanner(f);
        in.useDelimiter("` ");

        while (in.hasNextLine()) {

        }

        in.close();

        /*
         * file reader reads level file and creates a new game...
         */
    }

    public void save() {
        /*
         * file writer...
         */
    }

    public void destroy() {
        instance = null;
    }

    public static void main(String[] args) {
        //GameService gs = new GameService();

        //System.out.println(GameService.getInstance().getS());
    }
}
