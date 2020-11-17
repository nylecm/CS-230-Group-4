package java_.game.controller;

import java_.game.player.PlayerService;
import java_.game.tile.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameService {
    private static GameService instance = null;

    private GameBoard gb;
    private PlayerService ps;
    private int turnCount;

    private GameService() {
        //ps = PlayerService.getInstance();
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void loadNewGame(File f) throws FileNotFoundException {
        remake();

        Scanner in = new Scanner(f);
        in.useDelimiter("` ");

        String boardName;
        int w;
        int h;

        while (in.hasNextLine()) {
            //todo to be completed fully when other classes complete...

            boardName = in.next();

            System.out.println(boardName);

            int nRow = in.nextInt();
            int nCol = in.nextInt();
            //create game board...
            int nFixedTiles = in.nextInt(); //todo remove

            FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
            for (int i = 0; i < nFixedTiles; i++) {
                int rotation = in.nextInt();
                int row = in.nextInt();
                int col = in.nextInt();
                TileType tileType = TileType.valueOf(in.next().toUpperCase());
                FloorTile t = new FloorTile(tileType, true, false, rotation);
                fixedTiles[i] = t; //todo replace with gb.insert at...
            }
            System.out.println(Arrays.toString(fixedTiles)); //todo remove

            ArrayList<FloorTile> floorTiles = new ArrayList<>();

            for (int i = 0; i < FloorTile.FLOOR_TILE_TYPES.size(); i++) {
                TileType tileType = TileType.valueOf(in.next().toUpperCase());
                int n = in.nextInt();
                FloorTile t = new FloorTile(tileType, false, false);

                for (int j = 0; j < n; j++) {
                    floorTiles.add(t); //todo replace with gb.insert at...
                }
            }
            System.out.println(floorTiles);
            Collections.shuffle(floorTiles);
            System.out.println(floorTiles);

            //Action tiles:
            ArrayList<ActionTile> actionTiles = new ArrayList<>();

            for (int i = 0; i < ActionTile.ACTION_TILE_TYPES.size(); i++) {
                TileType tileType = TileType.valueOf(in.next().toUpperCase());
                int n = in.nextInt();
                ActionTile t = new ActionTile(tileType);

                for (int j = 0; j < n; j++) {
                    actionTiles.add(t); //todo replace with gb.insert at...
                }
            }
            System.out.println(actionTiles);

            int p1StartRow = in.nextInt();
            int p1StartCol = in.nextInt();
            int p2StartRow = in.nextInt();
            int p2StartCol = in.nextInt();
            int p3StartRow = in.nextInt();
            int p3StartCol = in.nextInt();
            int p4StartRow = in.nextInt();
            int p4StartCol = in.nextInt();
        }
        in.close();

        // Player Serv create...
        // Silk bag create ...
        // Gamebaord create ...
        // UI Data Bundle...
    }

    public void loadSavedInstance(File f) throws FileNotFoundException {
        remake();

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

    public void remake() {
        instance = new GameService();
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameService gs = new GameService();
        gs.loadNewGame(new File("C:\\Users\\micha\\IdeaProjects\\CS-230-Group-4\\data\\game_board.txt"));
        //System.out.println(GameService.getInstance().getS());
    }
}
