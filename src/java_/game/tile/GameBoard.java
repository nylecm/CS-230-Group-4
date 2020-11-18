package java_.game.tile;

import java_.game.player.PlayerPiece;
import java_.util.Position;

import java.util.HashMap;
import java.util.Set;

public class GameBoard {

    private final int nRows; //(height)
    private final int nCols; //(width)
    private String name;
    private PlayerPiece[] playerPieces;
    private Position[] playerPiecePositions;
    private FloorTile[] fixedTiles;
    private Position[] fixedTilePositions;
    private FloorTile[] tiles;
    private HashMap<Position, Set<Effect>> activeEffects;
    private SilkBag silkBag;
    private FloorTile[][] board;

    // todo         (pp, fixedTiles[], Position[] fixedTilePositions, tiles[], )
    public GameBoard(PlayerPiece[] playerPieces, FloorTile[] fixedTiles, Position[] fixedTilePositions, FloorTile[] tiles, int nCols, int nRows, String name, SilkBag silkBag) {
        this.playerPieces = playerPieces;
        this.fixedTiles = fixedTiles;
        this.fixedTilePositions = fixedTilePositions;
        this.tiles = tiles;
        this.name = name;
        this.silkBag = silkBag;
        this.nRows = nRows;
        this.nCols = nCols;
        this.board = new FloorTile[nRows][nCols];
    }

    private boolean isRowFixed(int posY) {
        if (posY == -1) {
            for (int x = 0; x < nCols; x++) {

                if (board[posY+1][x].isFixed()) { //Check for frozen etc.
                    return true;
                }
            }
            return false;
        } else if (posY == nRows) {
            for (int x = 0; x < nCols; x++) {

                if (board[posY-1][x].isFixed()) {
                    return true;
                }
            }
            return false;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean isColumnFixed(int posX) {

        if (posX == -1) {
            for (int y = 0; y < nRows; y++) {

                if (board[y][posX+1].isFixed()) {
                    return true;
                }
            }
            return false;
        } else if (posX == nCols) {
            for (int y = 0; y < nRows; y++) {

                if (board[y][posX-1].isFixed()) {
                    return true;
                }
            }
            return false;
        } else {
            for (int y = 0; y < nRows; y++) {

                if (board[y][posX].isFixed()) {
                    return true;
                }
            }
            return false;
        }

    }

                //insertAt
    private void slideTiles(int posX, int posY) {

        FloorTile tileReturn = null; //Being pushed off

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


    // private  insertAt()
    public void insertAt(int positionX, int positionY, FloorTile tile) throws IndexOutOfBoundsException {
        if (tile.isFixed() && board[positionX][positionY] == null) {
            board[positionX][positionY] = tile;
        } else {

            if (positionX == -1 || positionX == nCols || positionY == -1 || positionY == nRows) {

                if (!isColumnFixed(positionX) && !isRowFixed(positionY)) {

                    slideTiles(positionX, positionY);

                    if (positionX == -1) {
                        board[positionY][positionX+1] = tile;
                    } else if (positionX == nCols) {
                        board[positionY][positionX-1] = tile;
                    } else if (positionY == -1) {
                        board[positionY+1][positionX] = tile;
                    } else if (positionY == nRows) {
                        board[positionY-1][positionX] = tile;
                    }

                }

            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    //to string
    /**
    public String toString() {
        return;
    }
    **/


    public static void main(String[] args) {

        PlayerPiece[] playerPieces = new PlayerPiece[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);
        FloorTile[] fixedTiles = null;
        FloorTile[] tiles = null;
        Position[] fixedTilePositions = null;
        GameBoard firstgame = new GameBoard(playerPieces, fixedTiles, fixedTilePositions,tiles, 3, 3, "hello", silkBag);

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


        FloorTile insert = new FloorTile(TileType.STRAIGHT, false, false);

        System.out.println("");
        firstgame.insertAt(1, 3, insert);

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

