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
            tileReturn = board[posY][nCols-1];

            for (int i = nCols - 1; i != 0; i--) {

                board[posY][i] = board[posY][i-1];
                board[posY][i-1] = null;

            }
        } else if (posX == nCols) {
            tileReturn = board[posY][0];

            for (int i = 0; i < nCols - 1; i++) {
                board[posY][i] = board[posY][i+1];
                board[posY][i+1] = null;
            }
        } else if (posY == -1) {
            tileReturn = board[nRows-1][posX];

            for (int i = nRows - 1; i != 0; i--) {
                board[i][posX] = board[i-1][posX];
                board[i-1][posX] = null;
            }
        } else if (posY == nRows) {
            tileReturn = board[0][posX];

            for (int i = 0; i < nRows-1; i++) {
                board[i][posX] = board[i+1][posX];
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

  public static void main(String[] args) {

        PlayerPiece[] playerPieces = new PlayerPiece[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);
        Position[] positions = null;
        GameBoard firstgame = new GameBoard(playerPieces, positions, 3, 3, "hello", silkBag);

        FloorTile A = new FloorTile(TileType.CORNER, false, false);
        FloorTile B = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile C = new FloorTile(TileType.T_SHAPED, false, false);
        FloorTile D = new FloorTile(TileType.CORNER, false, false);
        FloorTile E = new FloorTile(TileType.CORNER, false, false);
        FloorTile F = new FloorTile(TileType.T_SHAPED, false, false);
        FloorTile G = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile H = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile I = new FloorTile(TileType.CORNER, false, false);

        firstgame.board[0][0] = A;
        firstgame.board[0][1] = B;
        firstgame.board[0][2] = C;
        firstgame.board[1][0] = D;
        firstgame.board[1][1] = E;
        firstgame.board[1][2] = F;
        firstgame.board[2][0] = G;
        firstgame.board[2][1] = H;
        firstgame.board[2][2] = I;



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

        System.out.println("");
        firstgame.slideTiles(1, 3);

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

    }
}

