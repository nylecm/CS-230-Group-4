package java_.game.tile;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.util.Position;
import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameBoard {

    private final int nRows; // height
    private final int nCols; // width
    private final String name; // todo may not be needed.
    private PlayerPiece[] playerPieces;
    private Position[] playerPiecePositions; // ex-position tracking...
    private final FloorTile[] fixedTiles; // todo reconsider may only need to be local.
    private final Position[] fixedTilePositions; // todo reconsider may only need to be local.
    private final FloorTile[] tiles;
    private Set<Position> positionsWithActiveEffects;
    private HashMap<Position, Set<Effect>> activeEffects;
    private final FloorTile[][] board;

    public GameBoard(Position[] playerPiecePositions, FloorTile[] fixedTiles, Position[] fixedTilePositions, FloorTile[] tiles, int nCols, int nRows, String name) {
        this.playerPiecePositions = playerPiecePositions;
        this.fixedTiles = fixedTiles;
        this.fixedTilePositions = fixedTilePositions;
        this.tiles = tiles;
        this.name = name;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];
        insertFixedTiles(fixedTiles, fixedTilePositions);
        fillGaps(tiles);
        activeEffects = new HashMap<Position, Set<Effect>>();
        positionsWithActiveEffects = new HashSet<Position>();
    }

    public void applyEffect(AreaEffect effect, Position p) {
        int effectRadius = effect.getRadius();
        int diameter = effectRadius * 2;
        int effectWidth = 1 + effectRadius * 2;

        Position effectStartPos = new Position(p.getRowNum() - effectRadius, p.getColNum() - effectRadius);
        for (int i = effectStartPos.getRowNum(); i < effectStartPos.getRowNum() + effectWidth; i++) {
            for (int j = effectStartPos.getColNum(); j < effectStartPos.getColNum() + effectWidth; j++) {

                if ((i >= 0 && i < nRows) && (j >= 0 && j < nCols)) {
                    if (board[i][j] != null) {
                        Position affectedPos = new Position(i, j);

                        Set<Effect> effectSet = null;
                        //positionsWithActiveEffects.contains(affectedPos)
                        //if (activeEffects.get(affectedPos) == null
                            if (!positionsWithActiveEffects.contains(affectedPos)) {
                                effectSet = new HashSet<Effect>();
                            } else {
                                effectSet = activeEffects.get(affectedPos);
                            }
                        effectSet.add(effect);
                        activeEffects.put(affectedPos, effectSet);
                        positionsWithActiveEffects.add(affectedPos);

                        /**
                        Set effectSet = activeEffects.get(affectedPos);
                        effectSet.add(effect);
                        activeEffects.put(affectedPos, effectSet);
                        positionsWithActiveEffects.add(affectedPos);
                         **/
                    }
                }
            }
        }




        Set effectSet = activeEffects.get(p); //For positions that are r+radius c+radius or both:
        effectSet.add(effect);
        activeEffects.put(p, effectSet);
        positionsWithActiveEffects.add(p);
    }

    public Set<Effect> getEffects(int row, int col) {
        return activeEffects.get(new Position(row, col));
    }

    private boolean isTileFixed(int row, int col) {
        if (board[row][col].isFixed()) {
            return true;
        }
        for (Effect effect : getEffects(row, col)) {
            if (effect.getEffectType() == EffectType.ICE) {
                return true;
            }
        }
        return false;
    }

    private boolean isRowFixed(int rowNum) {
        if (rowNum == -1) {
            rowNum += 1;
        } else if (rowNum == nRows) {
            rowNum -= 1;
        }
        for (int x = 0; x < nCols; x++) {
            if (isTileFixed(rowNum, x)) { //Check for frozen etc.
                return true;
            }
        }
        return false; // See bottom for commented out code...
    }

    private boolean isColumnFixed(int colNum) {
        if (colNum == -1) {
            colNum += 1;
        } else if (colNum == nCols) {
            colNum -= 1;
        }
        for (int y = 0; y < nRows; y++) {
            if (isTileFixed(y,colNum)) {
                return true;
            }
        }
        return false;
    }

    private void insertFixedTiles(FloorTile[] fixedTiles, Position[] fixedTilePositions) {
        for (int i = 0; i < fixedTiles.length; i++) {
            int rowNum = fixedTilePositions[i].getRowNum();
            int colNum = fixedTilePositions[i].getColNum();

            if (board[rowNum][colNum] == null) {
                board[rowNum][colNum] = fixedTiles[i];
            }
        }
    }

    private void fillGaps(FloorTile[] tiles) {
        int nextFloorTile = 0;

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {

                if (board[i][j] == null) {
                    board[i][j] = tiles[nextFloorTile];
                    nextFloorTile += 1;
                }
            }
        }
    }

    public void insert(int colNum, int rowNum, FloorTile tile) { //fixme check if row is fixed...
        FloorTile pushedOffTile = null; //Being pushed off

        if (colNum == -1 && !isRowFixed(rowNum)) {
            pushedOffTile = board[rowNum][nCols - 1];

            for (int i = nCols - 1; i != 0; i--) {

                board[rowNum][i] = board[rowNum][i - 1];
                board[rowNum][i - 1] = null;
            }
            board[rowNum][colNum + 1] = tile;
        } else if (colNum == nCols && !isRowFixed(rowNum)) {
            pushedOffTile = board[rowNum][0];

            for (int i = 0; i < nCols - 1; i++) {
                board[rowNum][i] = board[rowNum][i + 1];
                board[rowNum][i + 1] = null;
            }
            board[rowNum][colNum - 1] = tile;
        } else if (rowNum == -1 && !isColumnFixed(rowNum)) {
            pushedOffTile = board[nRows - 1][colNum];

            for (int i = nRows - 1; i != 0; i--) {
                board[i][colNum] = board[i - 1][colNum];
                board[i - 1][colNum] = null;
            }
            board[rowNum + 1][colNum] = tile;
        } else if (rowNum == nRows && !isColumnFixed(rowNum)) {
            pushedOffTile = board[0][colNum];

            for (int i = 0; i < nRows - 1; i++) {
                board[i][colNum] = board[i + 1][colNum];
                board[i + 1][colNum] = null;
            }
            board[rowNum - 1][colNum] = tile;
        }

        assert pushedOffTile != null;
        GameService.getInstance().getSilkBag().put(pushedOffTile); //todo ?? Null pointer
    }

    public Boolean isWin() {
        return true;
    }

    public String toString() {
        String boardString = "";
        for (int j = 0; j < nRows; j++) {
            String row = "";
            for (int i = 0; i < nCols; i++) {
                if (board[j][i] == null) {
                    row = row + "Empty ";
                } else {
                    row = row + board[j][i].getType() + " ";
                }
            }
            boardString = boardString + row + "\n";
        }
        return boardString;
    }


    public static void main(String[] args) {

        //PlayerPiece[] playerPieces = new PlayerPiece[0];
        Position[] playerPiecePositions = new Position[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);

        FloorTile A = new FloorTile(TileType.CORNER, true, false);
        FloorTile B = new FloorTile(TileType.STRAIGHT, true, false);
        FloorTile C = new FloorTile(TileType.T_SHAPED, true, false);

        FloorTile[] fixedTiles = new FloorTile[3];
        fixedTiles[0] = A;
        fixedTiles[1] = B;
        fixedTiles[2] = C;

        Position[] fixedTilePositions = new Position[3];
        fixedTilePositions[0] = new Position(0, 0);
        fixedTilePositions[1] = new Position(1, 1);
        fixedTilePositions[2] = new Position(2, 2);

        FloorTile D = new FloorTile(TileType.CORNER, false, false);
        FloorTile E = new FloorTile(TileType.CORNER, false, false);
        FloorTile F = new FloorTile(TileType.T_SHAPED, false, false);
        FloorTile G = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile H = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile I = new FloorTile(TileType.CORNER, false, false);
        FloorTile J = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile K = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile L = new FloorTile(TileType.STRAIGHT, false, false);


        FloorTile[] tiles = new FloorTile[9];
        tiles[0] = D;
        tiles[1] = E;
        tiles[2] = F;
        tiles[3] = G;
        tiles[4] = H;
        tiles[5] = I;
        tiles[6] = J;
        tiles[7] = K;
        tiles[8] = L;

       GameBoard firstgame = new GameBoard(playerPiecePositions, fixedTiles, fixedTilePositions, tiles, 4, 3, "hello");

        System.out.println(firstgame);

        AreaEffect effect = new AreaEffect(EffectType.ICE, 1, 3);

        firstgame.applyEffect(effect, new Position(1, 1));



        /**
        FloorTile insert1 = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile insert2 = new FloorTile(TileType.CORNER, false, false);
        FloorTile insert3 = new FloorTile(TileType.T_SHAPED, false, false);
        FloorTile insert4 = new FloorTile(TileType.CORNER, false, false);
         **/

        /**
        firstgame.insert(-1, 0, insert1);
        System.out.println(firstgame);
        firstgame.insert(4, 1, insert2);
        System.out.println(firstgame);
        firstgame.insert(1, -1, insert3);
        System.out.println(firstgame);
        firstgame.insert(0, 4, insert4);
        System.out.println(firstgame);
        **/
        /*
         firstgame.board[1][0] = D;
         firstgame.board[1][1] = E;
         firstgame.board[1][2] = F;
         firstgame.board[2][0] = G;
         firstgame.board[2][1] = H;
         firstgame.board[2][2] = I;

         System.out.println(firstgame);
         **/


        /*
         for (int j = 0; j < firstgame.nRows; j++) {
         String row = "";
         for (int i = 0; i < firstgame.nCols; i++) {
         if (firstgame.board[j][i] == null) {
         row = row + "Empty ";
         } else {
         row = row + firstgame.board[j][i].getType() + " ";
         }

         }
         System.out.println(row);
         }
         */

        //FloorTile insert = new FloorTile(TileType.STRAIGHT, false, false);

        //System.out.println("");
        //firstgame.insert(1, 3, insert);
        /*
         for (int j = 0; j < firstgame.nRows; j++) {
         String row = "";
         for (int i = 0; i < firstgame.nCols; i++) {
         if (firstgame.board[j][i] == null) {
         row = row + "Empty ";
         } else {
         row = row + firstgame.board[j][i].getType() + " ";
         }

         }
         System.out.println(row);
         }
         */
    }
}


/*
 * // from isRowFixed
 * if (rowNum == -1) {
 * for (int x = 0; x < nCols; x++) {
 * <p>
 * if (board[rowNum+1][x].isFixed()) { //Check for frozen etc.
 * return true;
 * }
 * }
 * return false;
 * } else if (rowNum == nRows) {
 * for (int x = 0; x < nCols; x++) {
 * <p>
 * if (board[rowNum-1][x].isFixed()) {
 * return true;
 * }
 * }
 * return false;
 * } else {
 * for (int x = 0; x < nCols; x++) {
 * <p>
 * if (board[rowNum][x].isFixed()) {
 * return true;
 * }
 * }
 * return false;
 * }
 **/