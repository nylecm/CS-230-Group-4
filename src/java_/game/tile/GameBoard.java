package java_.game.tile;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.util.Position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameBoard {

    private final int nRows; // height
    private final int nCols; // width
    private final String name; // todo may not be needed.
    private PlayerPiece[] playerPieces;
    private final Position[] playerPiecePositions;
    //private final FloorTile[] fixedTiles; // todo reconsider may only need to be local.
    //private final Position[] fixedTilePositions; // todo reconsider may only need to be local.
    //private final FloorTile[] tiles;
    private Set<Position> positionsWithActiveEffects = new HashSet<>(); //todo consider tree set.
    private HashMap<Position, AreaEffect> activeEffects = new HashMap<>();
    private final FloorTile[][] board;

    @Deprecated
    public Set<Position> getPositionsWithActiveEffects() {
        return positionsWithActiveEffects;
    }
    //temp todo remove

    @Deprecated
    public HashMap<Position, AreaEffect> getActiveEffects() {
        return activeEffects;
    }

    public FloorTile getTileAt(int row, int col) {
        return board[row][col];
    }

    public GameBoard(Position[] playerPiecePositions, FloorTile[] fixedTiles, Position[] fixedTilePositions, FloorTile[] tiles, int nCols, int nRows, String name) {
        this.playerPiecePositions = playerPiecePositions;
        //this.fixedTiles = fixedTiles;
        //this.fixedTilePositions = fixedTilePositions;
        //this.tiles = tiles;
        this.name = name;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];

        insertFixedTiles(fixedTiles, fixedTilePositions);
        fillGaps(tiles);
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

    public void insert(int colNum, int rowNum, FloorTile tile, int rotation)
            throws IllegalArgumentException {
        FloorTile pushedOffTile = null; // Tile being pushed off

        if (colNum == -1 && !isRowFixed(rowNum)) { // Left to right horizontal shift.
            pushedOffTile = board[rowNum][nCols - 1];
            shiftLeftToRight(colNum, rowNum, tile, rotation);
        } else if (colNum == nCols && !isRowFixed(rowNum)) { // Right to left horizontal shift.
            pushedOffTile = board[rowNum][0];
            shiftRightToLeft(colNum, rowNum, tile, rotation);
        } else if (rowNum == -1 && !isColumnFixed(colNum)) { // Top to bottom vertical shift.
            pushedOffTile = board[nRows - 1][colNum];
            shiftTopToBottom(colNum, rowNum, tile, rotation);
        } else if (rowNum == nRows && !isColumnFixed(colNum)) { // Bottom to top vertical shift.
            pushedOffTile = board[0][colNum];
            shiftBottomToTop(colNum, rowNum, tile, rotation);
        } else {
            throw new IllegalArgumentException("Invalid row/ column combination entered.");
        }
        assert pushedOffTile != null;
        GameService.getInstance().getSilkBag().put(pushedOffTile.getType());
    }

    private void shiftLeftToRight(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesLeftToRight(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesLeftToRight(rowNum);
    }

    private void shiftPlayerPiecesLeftToRight(int rowNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getRowNum() == rowNum) {
                playerPiecePositions[i] = nextPositionLeftToRight(new Position(rowNum, pos.getColNum()));
            }
        }
    }

    private Position nextPositionLeftToRight(Position pos) {
        return (pos.getColNum() == nCols - 1
                ? switchPositionLeftToRight(new Position(pos.getRowNum(), 0))
                : switchPositionLeftToRight(new Position(pos.getRowNum(), pos.getColNum() + 1)));
    }

    private Position switchPositionLeftToRight(Position pos) {
        System.out.println(getEffectAt(pos));
        if (getEffectAt(pos) != null && getEffectAt(pos).getEffectType() == EffectType.FIRE) {
            return switchPositionLeftToRight(nextPositionLeftToRight(pos));
        } else {
            return pos;
        }
    }

    private void shiftTilesLeftToRight(int colNum, int rowNum, FloorTile tile, int rotation) {
        for (int i = nCols - 1; i != 0; i--) {
            board[rowNum][i] = board[rowNum][i - 1]; // Right tile is now the tile to its left.

            if (activeEffects.get(new Position(rowNum, i - 1)) != null) {
                activeEffects.put(new Position(rowNum, i), activeEffects.get(new Position(rowNum, i - 1)));

                positionsWithActiveEffects.remove(new Position(rowNum, i - 1));
                positionsWithActiveEffects.add(new Position(rowNum, i));
            }
        }
        tile.rotateClockwise(rotation);
        board[rowNum][colNum + 1] = tile;
    }

    private void shiftRightToLeft(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesRightToLeft(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesRightToLeft(rowNum);
    }

    private void shiftPlayerPiecesRightToLeft(int rowNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (playerPiecePositions[i].getRowNum() == rowNum) {
                playerPiecePositions[i] = nextPositionRightToLeft(pos);
            }
        }
    }

    private Position nextPositionRightToLeft(Position pos) {
        return (pos.getColNum() == 0
                ? switchPositionRightToLeft(new Position(pos.getRowNum(), nCols - 1))
                : switchPositionRightToLeft(new Position(pos.getRowNum(), pos.getColNum() - 1)));
    }

    private Position switchPositionRightToLeft(Position pos) {
        System.out.println(getEffectAt(pos));
        if (getEffectAt(pos) != null && getEffectAt(pos).getEffectType() == EffectType.FIRE) {
            return switchPositionLeftToRight(nextPositionRightToLeft(pos));
        } else {
            return pos;
        }
    }

    private void shiftTilesRightToLeft(int colNum, int rowNum, FloorTile tile, int rotation) {
        for (int i = 0; i < nCols - 1; i++) {
            board[rowNum][i] = board[rowNum][i + 1];
            if (activeEffects.get(new Position(rowNum, i + 1)) != null) {
                activeEffects.put(new Position(rowNum, i), activeEffects.get(new Position(rowNum, i + 1)));

                positionsWithActiveEffects.remove(new Position(rowNum, i + 1));
                positionsWithActiveEffects.add(new Position(rowNum, i));
            }
        }
        tile.rotateClockwise(rotation);
        board[rowNum][colNum - 1] = tile;
    }

    private void shiftTopToBottom(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesTopToBottom(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesTopToBottom(colNum);
    }

    private void shiftPlayerPiecesTopToBottom(int colNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getColNum() == colNum) {
                playerPiecePositions[i] = nextPositionTopToBottom(pos);
            } //todo handle fire
        }
    }

    private Position nextPositionTopToBottom(Position pos) {
        return (pos.getRowNum() == nRows - 1
                ? switchPositionTopToBottom(new Position(0, pos.getColNum()))
                : switchPositionTopToBottom(new Position(pos.getRowNum() + 1, pos.getColNum())));
    }

    private Position switchPositionTopToBottom(Position pos) {
        System.out.println(getEffectAt(pos));
        if (getEffectAt(pos) != null && getEffectAt(pos).getEffectType() == EffectType.FIRE) {
            return switchPositionLeftToRight(nextPositionTopToBottom(pos));
        } else {
            return pos;
        }
    }

    private void shiftTilesTopToBottom(int colNum, int rowNum, FloorTile tile, int rotation) {
        for (int i = nRows - 1; i != 0; i--) {
            board[i][colNum] = board[i - 1][colNum];
            if (activeEffects.get(new Position(i - 1, colNum)) != null) {
                activeEffects.put(new Position(i, colNum), activeEffects.get(new Position(i - 1, colNum)));

                positionsWithActiveEffects.remove(new Position(i - 1, colNum));
                positionsWithActiveEffects.add(new Position(i, colNum));
            }
        }
        tile.rotateClockwise(rotation);
        board[rowNum + 1][colNum] = tile;
    }

    private void shiftBottomToTop(int colNum, int rowNum, FloorTile tile, int rotation) {
        //Shift Player Piece:
        shiftPlayerPiecesBottomToTop(colNum);
        shiftTilesBottomToTop(colNum, rowNum, tile, rotation);
    }

    private void shiftPlayerPiecesBottomToTop(int colNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getColNum() == colNum) {
                playerPiecePositions[i] = nextPositionBottomToTop(pos);
            }
        }
    }

    private Position nextPositionBottomToTop(Position pos) {
        return (pos.getRowNum() == 0
                ? switchPositionBottomToTop(new Position(nRows - 1, pos.getColNum()))
                : switchPositionBottomToTop(new Position(pos.getRowNum() - 1, pos.getColNum())));
    }

    private Position switchPositionBottomToTop(Position pos) {
        System.out.println(getEffectAt(pos));
        if (getEffectAt(pos) != null && getEffectAt(pos).getEffectType() == EffectType.FIRE) {
            return switchPositionLeftToRight(nextPositionBottomToTop(pos));
        } else {
            return pos;
        }
    }

    private void shiftTilesBottomToTop(int colNum, int rowNum, FloorTile tile, int rotation) {
        for (int i = 0; i < nRows - 1; i++) {
            board[i][colNum] = board[i + 1][colNum];
            if (activeEffects.get(new Position(i + 1, colNum)) != null) {
                activeEffects.put(new Position(i, colNum), activeEffects.get(new Position(i + 1, colNum)));

                positionsWithActiveEffects.remove(new Position(i + 1, colNum));
                positionsWithActiveEffects.add(new Position(i, colNum));
            }
        }
        tile.rotateClockwise(rotation);
        board[rowNum - 1][colNum] = tile;
    }

    private boolean isRowFixed(int rowNum) { // todo consider Matej's GUI based sol...
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
            if (isTileFixed(y, colNum)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTileFixed(int row, int col) {
        if (board[row][col].isFixed()) {
            return true;
        } else if (activeEffects.get(new Position(row, col)) != null &&
                activeEffects.get(new Position(row, col)).getEffectType() == EffectType.ICE) {
            return true;
        }
        return false;
    }

    public void applyEffect(AreaEffect effect, Position p) throws IllegalStateException {
        int effectRadius = effect.getRadius();
        int diameter = effectRadius * 2;
        int effectWidth = 1 + diameter; // Includes centre.

        Set<Position> playerPiecePos = new HashSet<>();
        for (Position position : playerPiecePositions) {
            playerPiecePos.add(position);
        }

        Position effectStartPos = new Position
                (p.getRowNum() - effectRadius, p.getColNum() - effectRadius);

        for (int i = effectStartPos.getRowNum(); i < effectStartPos.getRowNum() + effectWidth; i++) {
            for (int j = effectStartPos.getColNum(); j < effectStartPos.getColNum() + effectWidth; j++) {

                if ((i >= 0 && i < nRows) && (j >= 0 && j < nCols)) {
                    assert board[i][j] != null;
                    Position affectedPos = new Position(i, j);
                    if (effect.effectType == EffectType.FIRE && playerPiecePos.contains(affectedPos)) {
                        throw new IllegalStateException();
                    }
                    activeEffects.put(affectedPos, effect);
                    positionsWithActiveEffects.add(affectedPos);
                }
            }
        }
    }

    public AreaEffect getEffectAt(Position pos) {
        return activeEffects.get(pos);
    }

    public void refreshEffects() { //todo test... .ConcurrentModificationException
        for (Position positionWithActiveEffect : positionsWithActiveEffects) {
            if (activeEffects.get(positionWithActiveEffect).getRemainingDuration() == 1) {
                activeEffects.put(positionWithActiveEffect, null);
                positionsWithActiveEffects.remove(positionWithActiveEffect);
            } else {
                activeEffects.get(positionWithActiveEffect).decrementRemainingDuration();
            }
        }
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

    public Position getPlayerPiecePosition(int playerNum) {
        return playerPiecePositions[playerNum];
    }

    public int getnRows() {
        return nRows;
    }

    public int getnCols() {
        return nCols;
    }

    public String getName() {
        return name;
    }

    /*public static void main(String[] args) {
        //PlayerPiece[] playerPieces = new PlayerPiece[0];
        Position[] playerPiecePositions = new Position[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);

        FloorTile A = new FloorTile(TileType.CORNER, true);
        FloorTile B = new FloorTile(TileType.STRAIGHT, true);
        FloorTile C = new FloorTile(TileType.T_SHAPED, true);

        FloorTile[] fixedTiles = new FloorTile[3];
        fixedTiles[0] = A;
        fixedTiles[1] = B;
        fixedTiles[2] = C;

        Position[] fixedTilePositions = new Position[3];
        fixedTilePositions[0] = new Position(0, 0);
        fixedTilePositions[1] = new Position(1, 1);
        fixedTilePositions[2] = new Position(2, 2);

        FloorTile D = new FloorTile(TileType.CORNER, false);
        FloorTile E = new FloorTile(TileType.CORNER, false);
        FloorTile F = new FloorTile(TileType.T_SHAPED, false);
        FloorTile G = new FloorTile(TileType.STRAIGHT, false);
        FloorTile H = new FloorTile(TileType.STRAIGHT, false);
        FloorTile I = new FloorTile(TileType.CORNER, false);
        FloorTile J = new FloorTile(TileType.STRAIGHT, false);
        FloorTile K = new FloorTile(TileType.STRAIGHT, false);
        FloorTile L = new FloorTile(TileType.STRAIGHT, false);


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

        AreaEffect effect = new AreaEffect(EffectType.FIRE, 1, 3);

        firstgame.applyEffect(effect, new Position(1, 0));
        for (Position pos : firstgame.positionsWithActiveEffects) {
            System.out.println(pos.getRowNum() + " " + pos.getColNum());
        }

        AreaEffect test = firstgame.activeEffects.get(new Position(0, 0));
        System.out.println(test);
        firstgame.insert(0, -1, new FloorTile(TileType.STRAIGHT, false), 0);


        System.out.println(test);


        *//*
         FloorTile insert1 = new FloorTile(TileType.STRAIGHT, false, false);
         FloorTile insert2 = new FloorTile(TileType.CORNER, false, false);
         FloorTile insert3 = new FloorTile(TileType.T_SHAPED, false, false);
         FloorTile insert4 = new FloorTile(TileType.CORNER, false, false);
         *//*

     *//*
         firstgame.insert(-1, 0, insert1);
         System.out.println(firstgame);
         firstgame.insert(4, 1, insert2);
         System.out.println(firstgame);
         firstgame.insert(1, -1, insert3);
         System.out.println(firstgame);
         firstgame.insert(0, 4, insert4);
         System.out.println(firstgame);
         *//*
    }*/
}
