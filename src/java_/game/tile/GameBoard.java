package java_.game.tile;

import java_.game.player.PlayerPiece;
import java_.util.Position;

import java.util.EnumSet;
import java.util.HashMap;

public class GameBoard {

    private final int nRows; //(height)
    private final int nCols; //(width)
    private String name;
    private PlayerPiece[] playerPieces;
    private Position[] playerPiecePositions;
    private HashMap<Position, EnumSet<EffectType>> activeEffects;
    private SilkBag silkBag;
    private FloorTile[][] board;

    public GameBoard(PlayerPiece[] playerPieces, Position[] playerPiecePositions, int nCols, int nRows, String name, SilkBag silkBag) {
        this.playerPieces = playerPieces;
        this.playerPiecePositions = playerPiecePositions;
        this.name = name;
        this.silkBag = silkBag;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];
    }

    private boolean isRowFixed(int posY) {

        for (int x = 0; x < nCols; x++) {

            if (board[x][posY].isFixed()) {
                return true;
            }
        }
        return false;
    }

    private boolean isColumnFixed(int posX) {

        for (int y = 0; y < nRows; y++) {

            if (board[posX][y].isFixed()) {
                return true;
            }
        }
        return false;
    }

    private void slideTiles(int posX, int posY) {

        FloorTile tileReturn = null;

        if (posX == -1) {
            tileReturn = board[nCols-1][posY];

            for (int i = nCols - 1; i != 0; i--) {

                board[posY][i] = board[posY][i-1];
                board[posY][i-1] = null;

            }
        } else if (posX == nCols) {
            tileReturn = board[0][posY];

            for (int i = 0; i < nCols; i++) {
                board[posY][i] = board[i + 1][posY];
                board[posY][i+1] = null;
            }
        } else if (posY == -1) {
            tileReturn = board[posX][nRows-1];

            for (int i = nRows - 1; i != 0; i--) {
                board[i][posX] = board[posX][i - 1];
                board[i-1][posX] = null;
            }
        } else if (posY == nRows) {
            tileReturn = board[posX][0];

            for (int i = 0; i < nRows; i++) {
                board[i][posX] = board[posX][i + 1];
                board[i+1][posX] = null;
            }
        }

        silkBag.put(tileReturn);


    }

    public void insertAt(int positionX, int positionY, FloorTile tile) throws IndexOutOfBoundsException {
        if (tile.isFixed() && board[positionX][positionY] == null) {
            board[positionX][positionY] = tile;
        } else {

            if (positionX == -1 || positionX == nRows || positionY == -1 || positionY == nCols) {

                if (!isColumnFixed(positionX + 1) && !isRowFixed(positionY + 1)) {


                    //board[positionX][positionY] = tile;


                }


               // board[positionX][positionY] = tile;


            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    public void insertTile(int positionX, int positionY, FloorTile tile) {

    }

  /**public static void main(String[] args) {

        PlayerPiece[] playerPiece = new PlayerPiece[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);
        GameBoard firstgame = new GameBoard(playerPiece, 4, 1, "hello", silkBag);

        FloorTile A = new FloorTile(TileType.CORNER, false, false);
        firstgame.board[0][0]


    }**/
}

