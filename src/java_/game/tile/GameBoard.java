package java_.game.tile;

import java_.game.player.PlayerPiece;
import java_.util.Position;

import java.util.HashMap;

public class GameBoard {

    private final int width;
    private final int height;
    private String name;
    private PlayerPiece[] playerPiece;
    //private HashMap<PlayerPiece, Position> playerPositionMap;
    private int[][] coordinate = new int[2][4];
    private SilkBag silkBag;
    private FloorTile[][] board;


    public GameBoard(PlayerPiece[] playerPiece, int width, int height, String name, SilkBag silkBag) {
        this.playerPiece = playerPiece;
        this.name = name;
        this.silkBag = silkBag;
        this.width = width;
        this.height = height;
        this.board = new FloorTile[width][height];
    }


    private boolean isRowFixed(int posY) {

        for (int x = 0; x < width; x++) {

            if (board[x][posY].isFixed()) {
                return true;
            }
        }
        return false;
    }
    private boolean isColumnFixed(int posX) {

        for (int y = 0; y < height; y++) {

            if (board[posX][y].isFixed()) {
                return true;
            }
        }
        return false;
    }

    private void slideTiles(int posX, int posY) {

        FloorTile temp1;
        FloorTile temp2;
        FloorTile tileReturn = null;

        if (posX == -1) {
            tileReturn = board[width][posY];

            for (int i = width-1; i == 0; i--) {

                board[i][posY] = board[i-1][posY];
                board[i-1][posY] = null;

            }
        } else if (posX == width) {
            tileReturn = board[0][posY];

            for (int i = 0; i < width ; i++) {
                board[i][posY] = board[i+1][posY];
                board[i+1][posY] = null;
            }
        } else if (posY == -1) {
            tileReturn = board[posX][height];

            for(int i = height-1; i == 0; i--) {
                board[posX][i] = board[posX][i-1];
                board[posX][i-1] = null;
            }
        } else if (posY == height) {
            tileReturn = board[posX][0];

            for (int i = 0; i < height; i++) {
                board[posX][i] = board[posX][i+1];
                board[posX][i+1] = null;
            }
        }

        silkBag.put(tileReturn);


    }


    public void insertAt(int positionX, int positionY, FloorTile tile) throws IndexOutOfBoundsException{
        if (tile.isFixed() && board[positionX][positionY] == null) {
            board[positionX][positionY] = tile;
        } else {

            if (positionX == -1 || positionX == width +1 || positionY == -1 || positionY == height + 1) {

               if (!isColumnFixed(positionX + 1) && !isRowFixed(positionY + 1)) {


                   board[positionX][positionY] = tile;




               }




                board[positionX][positionY] = tile;






            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }


    public void insertTile(int positionX, int positionY, FloorTile tile) {

    }

  /*  public static void main(String[] args) {

        PlayerPiece[] playerPiece = new PlayerPiece[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);
        GameBoard firstgame = new GameBoard(playerPiece, 4, 1, "hello", silkBag);

        firstgame.board[0][0];


    }
 */
}
