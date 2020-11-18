package java_.game.controller;

import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.game.player.PlayerService;
import java_.game.tile.*;
import java_.util.Position;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GameService {
    private static GameService instance = null;

    private GameBoard gb;
    private PlayerService ps;
    private int turnCount;

    private GameService() {
        //ps = PlayerService.getInstance().remake();
        //gb = GameBoard.getInstance().remake();
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void loadNewGame(File f, String boardName, int nPlayers) throws FileNotFoundException {
        remake();

        Scanner in = new Scanner(f);
        in.useDelimiter("` ");

        int nRows;
        int nCols;
        PlayerPiece[] playerPieces = new PlayerPiece[nPlayers];

        while (in.hasNextLine() && in.next().equals(boardName)) {
            //todo to be completed fully when other classes complete...

            System.out.println(boardName);

            nRows = in.nextInt();
            nCols = in.nextInt();

            int nFixedTiles = in.nextInt();
            FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
            Position[] fixedTilePositions = new Position[nFixedTiles];

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

            // Player Pieces
            for (int i = 0; i < nPlayers; i++) {
                int startRow = in.nextInt();
                int startCol = in.nextInt();
                playerPieces[i] = new PlayerPiece(new Position(startRow, startCol));
            }

            gb = new GameBoard(playerPieces, fixedTiles, fixedTilePositions, );
        }
        in.close();

        // Player Serv create...
        // Silk bag create ...
        // Game board create ...
        // UI Data Bundle...
    }

    public void loadSavedInstance(File f) throws FileNotFoundException {
        remake(); //todo future homer's problem

        Scanner in = new Scanner(f);
        in.useDelimiter("` ");

        while (in.hasNextLine()) {

        }

        in.close();

        /*
         * file reader reads level file and creates a new game...
         */
    }

    public void gameplayLoop() {
        System.out.println("Have fun!");
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
        gs.loadNewGame(new File("C:\\Users\\micha\\IdeaProjects\\CS-230-Group-4\\data\\game_board.txt"), "oberon_1", 3);
        //System.out.println(GameService.getInstance().getS());
    }
}
