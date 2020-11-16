package java_.game.tile;

import java_.game.player.PlayerPiece;

public class GameBoard {

    private int width;
    private int height;
    private String name;
    private PlayerPiece[] playerPiece;
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

    public void insertAt(int positionX, int positionY, FloorTile tile) throws IndexOutOfBoundsException{
        if (tile.isFixed() && board[positionX][positionY] == null) {
            board[positionX][positionY] = tile;
        } else {

            if ((positionX == 0 || positionX == width) || positionY == 0 || positionY == height) {

               if (!isColumnFixed(positionX) && !isRowFixed(positionY)) {



                   board[positionX][positionY] = tile;




               }




                board[positionX][positionY] = tile;






            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }


}
