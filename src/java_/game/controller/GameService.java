package java_.game.controller;

import java_.game.player.PlayerService;
import java_.game.tile.FloorTile;
import java_.game.tile.GameBoard;
import java_.game.tile.Tile;
import java_.game.tile.TileType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
        int w;
        int h;

        while (in.hasNextLine()) {
            boardName = in.next();
            int nRow = in.nextInt();
            int nCol = in.nextInt();
            int nFixedTiles = in.nextInt();

            FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
            for (int i = 0; i < nFixedTiles; i++) {
                int rotation = in.nextInt();
                int row = in.nextInt();
                int col = in.nextInt();
                TileType tileType = TileType.valueOf(in.next().toUpperCase());
                FloorTile t = new FloorTile(tileType, true, false, rotation);
                fixedTiles[i] = t;
            }
            System.out.println(Arrays.toString(fixedTiles));
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

    public static void main(String[] args) throws FileNotFoundException {
        GameService gs = new GameService();
        gs.loadNewGame(new File("C:\\Users\\micha\\IdeaProjects\\CS-230-Group-4\\data\\game_board.txt"));
        //System.out.println(GameService.getInstance().getS());
    }
}
