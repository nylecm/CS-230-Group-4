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

        if (posX == -1) {

            temp1 = board[0][posY];
            temp2 = board[1][posY];
            board[1][posY] = temp1;
            board[0][posY] = null;

            for (int i = 2; i < width; i +=2) {
                temp1 = board[i][posY];
                board[i][posY] = temp2;
                temp2 = board[i+1][posY];
                board[i+1][posY] = temp1;
            }
        }



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


}
