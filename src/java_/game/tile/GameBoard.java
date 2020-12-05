package java_.game.tile;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.util.Position;

import java.util.*;

/**
 * Represents a game board, which is the grid of floor tiles on which the game
 * is played. It models the floor tiles that make-up the game board, the effects
 * currently active on the game board, and the positions of the player pieces
 * on the game board.
 * <p>
 * The operations that can be performed on the game board include inserting tiles,
 * moving player pieces, and applying & refreshing area effects (ie fire & ice).
 *
 * @author nylecm, sam cox, waleed ashraf (in order of descending contribution)
 */
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

    /**
     * Instantiates a new Game board, used when loading a new game.
     *
     * @param playerPieces         the player pieces that ought to be used by the
     *                             game board for the game.
     * @param playerPiecePositions the initial positions of the player pieces
     * @param fixedTiles           the fixed tiles that will make-up the game board.
     * @param fixedTilePositions   the positions of the fixed tiles, needs to be
     *                             where the ith position corresponds to the position
     *                             of the ith floor tile.
     * @param tiles                the non-fixed tiles to be added to the game
     *                             board.
     * @param nCols                the number of column (width) of the new game board.
     * @param nRows                the number of rows (height) of the new game board.
     * @param name                 the name of the new game board.
     */
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

    /**
     * Instantiates a new Game board, used when loading a saved game.
     *
     * @param playerPieces         the player pieces that ought to be used by the
     *                             game board for the game.
     * @param playerPiecePositions the initial positions of the player pieces
     * @param tiles                the tiles to be added to the game board. The
     *                             order of the tiles corresponds to the positioning
     *                             of the tile in the game board. The first tile will
     *                             be positioned row 0 col 0, and the next tiles will
     *                             be placed row-by-row, from top-to-bottom.
     * @param nCols                the number of column (width) of the new game board.
     * @param nRows                the number of rows (height) of the new game board.
     * @param name                 the name of the new game board.
     */
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

    /**
     * Inserts tiles at given positions.
     *
     * @param tiles     the tiles to be placed at given positions.
     * @param positions the positions of the tiles that ought to be placed. The
     *                  index of the position shall correspond to the index
     *                  of the tile in the tiles array that ought to be placed
     *                  at that position.
     */
    private void insertTilesAtPositions(FloorTile[] tiles, Position[] positions) {
        for (int i = 0; i < tiles.length; i++) {
            int rowNum = positions[i].getRowNum();
            int colNum = positions[i].getColNum();

            if (board[rowNum][colNum] == null) {
                board[rowNum][colNum] = tiles[i];
            }
        }
    }

    /**
     * Inserts tiles in the first available spaces first going through each row
     * from left to right, beginning on the top row and ending on the bottom
     * row.
     *
     * @param tiles the tiles that ought to be added to the game board at the
     *              first available positions.
     */
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

    /**
     * Validate move boolean. FIXME MATEO NEEDS TO JAVA DOC THIS!!!!!!!!!!!!
     *
     * @param sourceCol       the source col
     * @param sourceRow       the source row
     * @param targetCol       the target col
     * @param targetRow       the target row
     * @param sourceBitmask   the source bitmask
     * @param oppositeBitmask the opposite bitmask
     * @return the boolean
     */
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

    /**
     * Moves player piece to a new position.
     *
     * @param newRow the row of the new position.
     * @param newCol the column of the new position.
     */
    public void movePlayerPiece(int newRow, int newCol) {
        int currentPlayerNumber = GameService.getInstance().getCurrentPlayerNum();
        playerPieces[currentPlayerNumber].addPreviousPlayerPosition(playerPiecePositions[currentPlayerNumber]);
        playerPiecePositions[currentPlayerNumber] = new Position(newRow, newCol);
    }

    /**
     * Move player piece up.
     *
     * @param playerNumber the number of the player that ought to be moved
     * @
     */
    @Deprecated
    public void movePlayerPieceUp(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum() - 1, curPos.getColNum());
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    /**
     * Move player piece right.
     *
     * @param playerNumber the player number
     */
    @Deprecated
    public void movePlayerPieceRight(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum(), curPos.getColNum() + 1);
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    /**
     * Move player piece down.
     *
     * @param playerNumber the player number
     */
    @Deprecated
    public void movePlayerPieceDown(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum() + 1, curPos.getColNum());
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    /**
     * Move player piece left.
     *
     * @param playerNumber the player number
     */
    @Deprecated
    public void movePlayerPieceLeft(int playerNumber) {
        Position curPos = playerPiecePositions[playerNumber];
        Position newPos = new Position(curPos.getRowNum(), curPos.getColNum() - 1);
        playerPiecePositions[playerNumber] = newPos;
        playerPieces[playerNumber].addPreviousPlayerPosition(curPos);
    }

    /**
     * Inserts a new tile to the edge of the game board to be slid.
     *
     * Usage: for left to right slide colNum -1
     *        for right to left slide colNum nCols
     *        for top to bottom slide rowNum -1
     *        for bottom to top slide rowNum nRows
     *
     * @param colNum   the column num where the new tile is being inserted.
     * @param rowNum   the row num where the new tile is being inserted.
     * @param tile     the tile that is being inserted.
     * @param rotation the rotation of the tile that is being inserted.
     * @throws IllegalArgumentException the illegal argument exception
     */
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

    /**
     * Initiates a left to right shift of both player pieces and tiles.
     *
     * @param colNum   the column num where the new tile is being inserted.
     * @param rowNum   the row num where the new tile is being inserted.
     * @param tile     the tile that is being inserted.
     * @param rotation the rotation of the tile that is being inserted.
     */
    private void shiftLeftToRight(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesLeftToRight(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesLeftToRight(rowNum);
    }

    /**
     * Shifts player pieces left to right, or to the start of the row if the
     * player piece is at the end prior to the insertion.
     *
     * @param rowNum the row num
     */
    private void shiftPlayerPiecesLeftToRight(int rowNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getRowNum() == rowNum) {
                playerPiecePositions[i] = nextPositionLeftToRight(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    /**
     * Next position left to right position.
     *
     * @param pos the pos
     * @return the position
     */
    private Position nextPositionLeftToRight(Position pos) {
        return (pos.getColNum() == nCols - 1
                ? new Position(pos.getRowNum(), 0)
                : new Position(pos.getRowNum(), pos.getColNum() + 1));
    }

    /**
     * Shift tiles left to right.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
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

    /**
     * Shift right to left.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
    private void shiftRightToLeft(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesRightToLeft(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesRightToLeft(rowNum);
    }

    /**
     * Shift player pieces right to left.
     *
     * @param rowNum the row num
     */
    private void shiftPlayerPiecesRightToLeft(int rowNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getRowNum() == rowNum) {
                playerPiecePositions[i] = nextPositionRightToLeft(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    /**
     * Next position right to left position.
     *
     * @param pos the pos
     * @return the position
     */
    private Position nextPositionRightToLeft(Position pos) {
        return (pos.getColNum() == 0
                ? new Position(pos.getRowNum(), nCols - 1)
                : new Position(pos.getRowNum(), pos.getColNum() - 1));
    }

    /**
     * Shift tiles right to left.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
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

    /**
     * Shift top to bottom.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
    private void shiftTopToBottom(int colNum, int rowNum, FloorTile tile, int rotation) {
        shiftTilesTopToBottom(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesTopToBottom(colNum);
    }

    /**
     * Shift player pieces top to bottom.
     *
     * @param colNum the col num
     */
    private void shiftPlayerPiecesTopToBottom(int colNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getColNum() == colNum) {
                playerPiecePositions[i] = nextPositionTopToBottom(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    /**
     * Next position top to bottom position.
     *
     * @param pos the pos
     * @return the position
     */
    private Position nextPositionTopToBottom(Position pos) {
        return (pos.getRowNum() == nRows - 1
                ? new Position(0, pos.getColNum())
                : new Position(pos.getRowNum() + 1, pos.getColNum()));
    }

    /**
     * Shift tiles top to bottom.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
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

    /**
     * Shift bottom to top.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
    private void shiftBottomToTop(int colNum, int rowNum, FloorTile tile, int rotation) {
        //Shift Player Piece:
        shiftTilesBottomToTop(colNum, rowNum, tile, rotation);
        shiftPlayerPiecesBottomToTop(colNum);
    }

    /**
     * Shift player pieces bottom to top.
     *
     * @param colNum the col num
     */
    private void shiftPlayerPiecesBottomToTop(int colNum) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            Position pos = playerPiecePositions[i];
            if (pos.getColNum() == colNum) {
                playerPiecePositions[i] = nextPositionBottomToTop(pos);
                playerPieces[i].addPreviousPlayerPosition(pos);
            }
        }
    }

    /**
     * Next position bottom to top position.
     *
     * @param pos the pos
     * @return the position
     */
    private Position nextPositionBottomToTop(Position pos) {
        return (pos.getRowNum() == 0
                ? new Position(nRows - 1, pos.getColNum())
                : new Position(pos.getRowNum() - 1, pos.getColNum()));
    }

    /**
     * Shift tiles bottom to top.
     *
     * @param colNum   the col num
     * @param rowNum   the row num
     * @param tile     the tile
     * @param rotation the rotation
     */
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

    /**
     * Is row fixed boolean.
     *
     * @param rowNum the row num
     * @return the boolean
     */
    public boolean isRowFixed(int rowNum) {
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

    /**
     * Is column fixed boolean.
     *
     * @param colNum the col num
     * @return the boolean
     */
    public boolean isColumnFixed(int colNum) {
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

    /**
     * Is tile fixed boolean.
     *
     * @param row the row
     * @param col the col
     * @return the boolean
     */
    private boolean isTileFixed(int row, int col) {
        if (board[row][col].isFixed()) {
            return true;
        } else if (activeEffects.get(new Position(row, col)) != null &&
                activeEffects.get(new Position(row, col)).getEffectType() == EffectType.ICE) {
            return true;
        }
        return false;
    }

    /**
     * Apply effect.
     *
     * @param effect the effect
     * @param pos    the pos
     * @throws IllegalStateException the illegal state exception
     */
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

    /**
     * Backtrack position.
     *
     * @param playerNum                the player num
     * @param targetNumberOfBacktracks the target number of backtracks
     * @return the position
     * @throws IllegalStateException the illegal state exception
     */
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

    /**
     * Is backtrack possible boolean.
     *
     * @param playerNum the player num
     * @return the boolean
     */
    public boolean isBacktrackPossible(int playerNum) {
        return (getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()) == null)
                || ((getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()) != null)
                && (!getEffectAt(playerPieces[playerNum].getPreviousPlayerPiecePositions().peek()).getEffectType().equals(EffectType.FIRE)));
    }

    /**
     * Refresh effects.
     */
    public void refreshEffects() {//todo check if broken
        for (AreaEffect effect : activeEffects.values()) {
            if (effect.getRemainingDuration() == 1) {
                activeEffects.remove(effect);
            } else {
                effect.decrementRemainingDuration();
            }
        }
    }

    /**
     * Gets effect at.
     *
     * @param pos the pos
     * @return the effect at
     */
    public AreaEffect getEffectAt(Position pos) {
        return activeEffects.get(pos);
    }

    /**
     * To string string.
     *
     * @return the string
     */
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

    /**
     * Gets player piece position.
     *
     * @param playerNum the player num
     * @return the player piece position
     */
    public Position getPlayerPiecePosition(int playerNum) {
        return playerPiecePositions[playerNum];
    }

    /**
     * Gets player piece index by position.
     *
     * @param position the position
     * @return the player piece index by position
     */
    public int getPlayerPieceIndexByPosition(Position position) {
        for (int i = 0; i < playerPiecePositions.length; i++) {
            if (playerPiecePositions[i].equals(position)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets rows.
     *
     * @return the rows
     */
    public int getnRows() {
        return nRows;
    }

    /**
     * Gets cols.
     *
     * @return the cols
     */
    public int getnCols() {
        return nCols;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets player piece.
     *
     * @param i the
     * @return the player piece
     */
    public PlayerPiece getPlayerPiece(int i) {
        return playerPieces[i];
    }

    /**
     * Gets num of player pieces.
     *
     * @return the num of player pieces
     */
    public int getNumOfPlayerPieces() {
        return playerPiecePositions.length;
    }

    /**
     * Gets positions with active effects.
     *
     * @return the positions with active effects
     */
    public Set<Position> getPositionsWithActiveEffects() {
        return activeEffects.keySet();
    }

    /**
     * Gets tile at.
     *
     * @param row the row
     * @param col the col
     * @return the tile at
     */
    public FloorTile getTileAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Gets active effects.
     *
     * @return the active effects
     */
    @Deprecated
    public HashMap<Position, AreaEffect> getActiveEffects() {
        return activeEffects;
    }
}
