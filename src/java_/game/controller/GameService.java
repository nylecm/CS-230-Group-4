package java_.game.controller;

import java_.game.player.PlayerPiece;
import java_.game.player.PlayerService;
import java_.game.tile.*;
import java_.util.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameService {
    private static GameService instance = null;

    private GameBoard gb;
    private PlayerService ps;
    private int turnCount;

    private static final String DELIMITER = "` ";
    private static final String GAME_BOARD_FILE_PATH = "data/game_board.txt";

    private GameService() {
        PlayerService.getInstance().remake();
        //gb = GameBoard.getInstance().remake();
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void loadNewGame(File f, String boardName, int nPlayers)
            throws FileNotFoundException {
        remake();

        Scanner in = new Scanner(f);
        in.useDelimiter(DELIMITER);
        gb = readSelectGameBoard(boardName, nPlayers, in);
        in.close();

        gameplayLoop();
    }

    private GameBoard readSelectGameBoard(String boardName, int nPlayers, Scanner in) throws IllegalArgumentException {
        while (in.hasNextLine() && in.next().equals(boardName)) {//todo to be completed fully when other classes complete...
            int nRows = in.nextInt();
            int nCols = in.nextInt();

            // Dealing with fixed tiles:
            int nFixedTiles = in.nextInt();
            FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
            Position[] fixedTilePositions = new Position[nFixedTiles];

            for (int i = 0; i < nFixedTiles; i++) {
                int rotation = in.nextInt();
                int row = in.nextInt();
                int col = in.nextInt();
                TileType tileType = TileType.valueOf(in.next().toUpperCase());

                Position p = new Position(row, col);
                FloorTile t = new FloorTile(tileType, true, false, rotation);
                fixedTiles[i] = t;
                fixedTilePositions[i] = p;
            }

            // Dealing with non-fixed floor tiles:
            ArrayList<FloorTile> floorTiles = readFloorTiles(in);
            Collections.shuffle(floorTiles);

            // Taking first floor tiles for the initial set to populate game board.
            FloorTile[] floorTilesForGameBoard = getFloorTilesForGameBoard
                    (nRows, nCols, nFixedTiles, floorTiles);

            //Action tiles:
            ArrayList<ActionTile> actionTiles = readActionTiles(in);
            Collections.shuffle(actionTiles);

            // Silk Bag:
            ArrayList<Tile> tilesForSilkBag = new ArrayList<>();
            tilesForSilkBag.addAll(floorTiles);
            tilesForSilkBag.addAll(actionTiles);

            SilkBag sb = new SilkBag(tilesForSilkBag.toArray(new Tile[0]));

            // Player Pieces:
            PlayerPiece[] playerPieces = readPlayerPieces(nPlayers, in);
            // todo player service...

            return new GameBoard(playerPieces, fixedTiles, fixedTilePositions,
                    floorTilesForGameBoard, nCols, nRows, boardName, sb); // todo consider keeping silk bag in game service...
        }
        throw new IllegalArgumentException("No level with such name found!");
    }

    private ArrayList<FloorTile> readFloorTiles(Scanner in) {
        ArrayList<FloorTile> floorTiles = new ArrayList<>();

        for (int i = 0; i < FloorTile.FLOOR_TILE_TYPES.size(); i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            int nOfThisType = in.nextInt();
            FloorTile t = new FloorTile(tileType, false, false);

            for (int j = 0; j < nOfThisType; j++) {
                floorTiles.add(t);
            }
        }
        return floorTiles;
    }

    private FloorTile[] getFloorTilesForGameBoard
            (int nRows, int nCols, int nFixedTiles, ArrayList<FloorTile> floorTiles) {
        FloorTile[] floorTilesForGameBoard = new FloorTile[(nRows * nCols) - nFixedTiles]; // todo check if nFixed Tiles is greater than nCol * nRow

        for (int i = 0; i < (nRows * nCols) - nFixedTiles; i++) { //todo check if there are enough tiles for the game board...
            floorTilesForGameBoard[i] = floorTiles.get(0);
            floorTiles.remove(0);
        }
        return floorTilesForGameBoard;
    }

    private ArrayList<ActionTile> readActionTiles(Scanner in) {
        ArrayList<ActionTile> actionTiles = new ArrayList<>();

        for (int i = 0; i < ActionTile.ACTION_TILE_TYPES.size(); i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            int n = in.nextInt();
            ActionTile t = new ActionTile(tileType);

            for (int j = 0; j < n; j++) {
                actionTiles.add(t);
            }
        }
        return actionTiles;
    }

    private PlayerPiece[] readPlayerPieces(int nPlayers, Scanner in) {
        PlayerPiece[] playerPieces = new PlayerPiece[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int startRow = in.nextInt(); //todo load player piece positions...
            int startCol = in.nextInt();
            playerPieces[i] = new PlayerPiece();
        }
        return playerPieces;
    }


    public void loadSavedInstance(File f) throws FileNotFoundException {
        remake(); //todo future homer's problem

        Scanner in = new Scanner(f);
        in.useDelimiter(DELIMITER);

        while (in.hasNextLine()) {

        }

        in.close();

        /*
         * file reader reads level file and creates a new game...
         */
    }

    public void gameplayLoop() { // todo gameplay loop...
        /*while (!gb.isWin()) {
            // ps mk mv
            // ...
        }*/
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
        gs.loadNewGame(
                new File(GAME_BOARD_FILE_PATH), "oberon_1", 3);
        System.out.println(gs.gb);
        gs.gb.insert(0,-1, new FloorTile(TileType.STRAIGHT, false,false));
        System.out.println(gs.gb);
        //System.out.println(GameService.getInstance().getS());
    }

    /*private class FloorTilePositionBundle {
        private FloorTile floorTile;
        private Position position;

        public FloorTilePositionBundle(FloorTile floorTile, Position position) {
            this.floorTile = floorTile;
            this.position = position;
        }

        public FloorTile getFloorTile() {
            return floorTile;
        }

        public void setFloorTile(FloorTile floorTile) {
            this.floorTile = floorTile;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }
    }*/
}
