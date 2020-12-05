package java_.game.tile;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.util.Position;
import javafx.geometry.Pos;

import java.util.*;

public class GameBoard {
    public static final String FIRE_EFFECT_WHERE_THERE_IS_A_PLAYER = "Cannot apply fire effect where there is a player.";
    public static final String INVALID_ROW_COLUMN_COMBO_MSG = "Invalid row/column combination entered.";
    private final int nRows; // height
    private final int nCols; // width
    private final String name;
    private final PlayerPiece[] playerPieces;
    private final Position[] playerPiecePositions;
    private final HashMap<Position, AreaEffect> activeEffects = new HashMap<>();
    private final FloorTile[][] board;

    public FloorTile getTileAt(int row, int col) {
        return board[row][col];
    }

    public GameBoard(PlayerPiece[] playerPieces, Position[] playerPiecePositions,
                     FloorTile[] fixedTiles, Position[] fixedTilePositions,
                     FloorTile[] tiles, int nCols, int nRows, String name) {
        this.playerPieces = playerPieces;
        this.playerPiecePositions = playerPiecePositions;
        this.name = name;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];

        insertTilesAtPositions(fixedTiles, fixedTilePositions);
        insertTiles(tiles);
    }

    public GameBoard(PlayerPiece[] playerPieces, Position[] playerPiecePositions,
                     FloorTile[] tiles, int nCols, int nRows, String name) {
        this.playerPieces = playerPieces;
        this.playerPiecePositions = playerPiecePositions;
        this.name = name;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];

        insertTiles(tiles);
    }

    private void insertTilesAtPositions(FloorTile[] tiles, Position[] positions) {
        for (int i = 0; i < tiles.length; i++) {
            int rowNum = positions[i].getRowNum();
            int colNum = positions[i].getColNum();

            if (board[rowNum][colNum] == null) {
                board[rowNum][colNum] = tiles[i];
            }
        }
    }

    public void insertTiles(FloorTile[] tiles) {
        int nextFloorTile = 0;

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {

                if (board[i][j] == null) {
                    board[i][j] = tiles[nextFloorTile];
                    nextFloorTile++;
                }
            }
        }
    }

    public boolean validateMove(int sourceCol, int sourceRow, int targetCol, int targetRow, int sourceBitmask, int oppositeBitmask) {
        if (activeEffects.containsKey(new Position(targetRow, targetCol))) {
            return false;
        }
        FloorTile sourceFloorTile = board[sourceRow][sourceCol];
        FloorTile targetFloorTile = board[targetRow][targetCol];

        int sourceFloorTilePaths = sourceFloorTile.getPathsBits();
        int targetFloorTilePaths = targetFloorTile.getPathsBits();

        Position targetFloorTilePosition = new Position(targetRow, targetCol);
//        boolean targetFloorTileOnFire = activeEffects.get(targetFloorTilePosition).getEffectType() == EffectType.FIRE;

        //Making sure the move is only to the adjacent row/column
        boolean isAdjacentFloorTile = (targetCol == sourceCol - 1 || targetCol == sourceCol + 1) || (targetRow == sourceRow - 1 || targetRow == sourceRow + 1);

        return (sourceFloorTilePaths & sourceBitmask) == sourceBitmask && (targetFloorTilePaths & oppositeBitmask) == oppositeBitmask && isAdjacentFloorTile;
    }

    public void movePlayerPiece(int newRow, int newCol) {
        int currentPlayerNumber = GameService.getInstance().getCurrentPlayerNum();
        playerPieces[currentPlayerNumber].addPreviousPlayerPosition(playerPiecePositions[currentPlayerNumber]);
        playerPiecePositions[currentPlayerNumber] = new Position(newRow, newCol);
    }

    @Deprecated
    public void movePlayerPieceUp(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum() - 1, curPos.getColNum());
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    @Deprecated
    public void movePlayerPieceRight(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum(), curPos.getColNum() + 1);
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    @Deprecated
    public void movePlayerPieceDown(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum() + 1, curPos.getColNum());
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    @Deprecated
    public void movePlayerPieceLeft(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum(), curPos.getColNum() - 1);
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    public void insert(int colNum, int rowNum, FloorTile tile, int rotation)
            throws IllegalArgumentException {
        FloorTile pushedOffTile; // Tile being pushed off

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
            throw new IllegalArgumentException(INVALID_ROW_COLUMN_COMBO_MSG);
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
                playerPiecePositions[i] = nextPositionLeftToRight(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    private Position nextPositionLeftToRight(Position pos) {
        return (pos.getColNum() == nCols - 1
                ? new Position(pos.getRowNum(), 0)
                : new Position(pos.getRowNum(), pos.getColNum() + 1));
    }

    private void shiftTilesLeftToRight(int colNum, int rowNum, FloorTile tile, int rotation) {
        tile.rotate(rotation);
        for (int i = nCols - 1; i != 0; i--) {
            board[rowNum][i] = board[rowNum][i - 1]; // Right tile is now the tile to its left.

            if (activeEffects.get(new Position(rowNum, i - 1)) != null) {
                activeEffects.put(new Position(rowNum, i), activeEffects.get(new Position(rowNum, i - 1)));
                activeEffects.remove(new Position(rowNum, i - 1));
            }
        }
        board[rowNum][colNum + 1] = tile;
    }

    private void shiftRightToLeft(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesRightToLeft(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesRightToLeft(rowNum);
    }

    private void shiftPlayerPiecesRightToLeft(int rowNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getRowNum() == rowNum) {
                playerPiecePositions[i] = nextPositionRightToLeft(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    private Position nextPositionRightToLeft(Position pos) {
        return (pos.getColNum() == 0
                ? new Position(pos.getRowNum(), nCols - 1)
                : new Position(pos.getRowNum(), pos.getColNum() - 1));
    }

    private void shiftTilesRightToLeft(int colNum, int rowNum, FloorTile tile, int rotation) {
        tile.rotate(rotation);
        for (int i = 0; i < nCols - 1; i++) {
            board[rowNum][i] = board[rowNum][i + 1];
            if (activeEffects.get(new Position(rowNum, i + 1)) != null) {
                activeEffects.put(new Position(rowNum, i), activeEffects.get(new Position(rowNum, i + 1)));
                activeEffects.remove(new Position(rowNum, i + 1));
            }
        }
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
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    private Position nextPositionTopToBottom(Position pos) {
        return (pos.getRowNum() == nRows - 1
                ? new Position(0, pos.getColNum())
                : new Position(pos.getRowNum() + 1, pos.getColNum()));
    }

    private void shiftTilesTopToBottom(int colNum, int rowNum, FloorTile tile, int rotation) {
        tile.rotate(rotation);
        for (int i = nRows - 1; i != 0; i--) {
            board[i][colNum] = board[i - 1][colNum];
            if (activeEffects.get(new Position(i - 1, colNum)) != null) {
                activeEffects.put(new Position(i, colNum), activeEffects.get(new Position(i - 1, colNum)));
                activeEffects.remove(new Position(i - 1, colNum));
            }
        }
        board[rowNum + 1][colNum] = tile;
    }

    private void shiftBottomToTop(int colNum, int rowNum, FloorTile tile, int rotation) {
        //Shift Player Piece:
        shiftTilesBottomToTop(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesBottomToTop(colNum);
    }

    private void shiftPlayerPiecesBottomToTop(int colNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getColNum() == colNum) {
                playerPiecePositions[i] = nextPositionBottomToTop(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    private Position nextPositionBottomToTop(Position pos) {
        return (pos.getRowNum() == 0
                ? new Position(nRows - 1, pos.getColNum())
                : new Position(pos.getRowNum() - 1, pos.getColNum()));
    }

    private void shiftTilesBottomToTop(int colNum, int rowNum, FloorTile tile, int rotation) {
        tile.rotate(rotation);
        for (int i = 0; i < nRows - 1; i++) {
            board[i][colNum] = board[i + 1][colNum];
            if (activeEffects.get(new Position(i + 1, colNum)) != null) {
                activeEffects.put(new Position(i, colNum), activeEffects.get(new Position(i + 1, colNum)));
                activeEffects.remove(new Position(i + 1, colNum));
            }
        }
        board[rowNum - 1][colNum] = tile;
    }

    public boolean isRowFixed(int rowNum) { // todo consider Matej's GUI based sol...
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

    public boolean isColumnFixed(int colNum) { //handled by gui.
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

    public void applyEffect(AreaEffect effect, Position pos) throws IllegalStateException {
        int effectRadius = effect.getRadius();
        int diameter = effectRadius * 2;
        int effectWidth = 1 + diameter; // Includes centre.

        Set<Position> playerPiecePos = new HashSet<>(Arrays.asList(playerPiecePositions));

        Position effectStartPos = new Position
                (pos.getRowNum() - effectRadius, pos.getColNum() - effectRadius);

        for (int i = effectStartPos.getRowNum(); i < effectStartPos.getRowNum() + effectWidth; i++) {
            for (int j = effectStartPos.getColNum(); j < effectStartPos.getColNum() + effectWidth; j++) {

                if ((i >= 0 && i < nRows) && (j >= 0 && j < nCols)) {
                    assert board[i][j] != null;
                    Position affectedPos = new Position(i, j);
                    if (effect.effectType == EffectType.FIRE && playerPiecePos.contains(affectedPos)) {
                        throw new IllegalStateException(FIRE_EFFECT_WHERE_THERE_IS_A_PLAYER);
                    }
                    activeEffects.put(affectedPos, effect);
                }
            }
        }
    }

    public Position backtrack(int playerNum, int targetNumberOfBacktracks) throws IllegalStateException {
        if (isBacktrackPossible(playerNum) && targetNumberOfBacktracks == 1) {
            Position previousPosition = playerPieces[playerNum].getPreviousPlayerPosition();
            playerPiecePositions[playerNum] = previousPosition;
            playerPieces[playerNum].getPreviousPlayerPiecePositions().pop();
            return previousPosition;
        } else if (isBacktrackPossible(playerNum) && targetNumberOfBacktracks > 1) {
            playerPiecePositions[playerNum] = playerPieces[playerNum].getPreviousPlayerPosition();
            playerPieces[playerNum].getPreviousPlayerPiecePositions().pop();
            Position output;
            output = backtrack(playerNum, targetNumberOfBacktracks - 1);
            return output;
        }
        return null;
    }

    public boolean isBacktrackPossible(int playerNum) {
        return (getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()) == null)
                || ((getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()) != null)
                && (!getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()).getEffectType().equals(EffectType.FIRE)));
    }

    public void refreshEffects() {//todo check if broken
        for (AreaEffect effect : activeEffects.values()) {
            if (effect.getRemainingDuration() == 1) {
                activeEffects.remove(effect);
            } else {
                effect.decrementRemainingDuration();
            }
        }
    }

    public AreaEffect getEffectAt(Position pos) {
        return activeEffects.get(pos);
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

    public int getPlayerPieceIndexByPosition(Position position) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            if (playerPiecePositions[i].equals(position)) {
                return i;
            }
        }
        return -1;
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

    public PlayerPiece getPlayerPiece(int i) {
        return playerPieces[i];
    }

    @Deprecated
    public HashMap<Position, AreaEffect> getActiveEffects() {
        return activeEffects;
    }

    public int getNumOfPlayerPieces() {
        return playerPiecePositions.length;
    }

    public Set<Position> getPositionsWithActiveEffects() {
        return activeEffects.keySet();
    }
}
