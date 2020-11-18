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
        //insertFixedTiles(fixedTiles, fixedTilePositions);
        //fillGaps(tiles);
    }

    private boolean isRowFixed(int rowNum) {

        if (rowNum == -1) {
            rowNum += 1;
        } else if (rowNum == nRows) {
            rowNum -= 1;
        }
        for (int x = 0; x < nCols; x++) {

            if (board[rowNum][x].isFixed()) { //Check for frozen etc.
                return true;
            }
        }
        return false;

        /**
        if (rowNum == -1) {
            for (int x = 0; x < nCols; x++) {

                if (board[rowNum+1][x].isFixed()) { //Check for frozen etc.
                    return true;
                }
            }
            return false;
        } else if (rowNum == nRows) {
            for (int x = 0; x < nCols; x++) {

                if (board[rowNum-1][x].isFixed()) {
                    return true;
                }
            }
            return false;
        } else {
            for (int x = 0; x < nCols; x++) {

                if (board[rowNum][x].isFixed()) {
                    return true;
                }
            }
            return false;
        }


    **/
    }

    private boolean isColumnFixed(int colNum) {


        if (colNum == -1) {
            colNum += 1;
        } else if (colNum == nCols) {
            colNum -= 1;
        }
        for (int y = 0; y < nRows; y++) {

            if (board[y][colNum].isFixed()) {
                return true;
            }
        }
        return false;
        /**
        if (colNum == -1) {
            for (int y = 0; y < nRows; y++) {

                if (board[y][colNum+1].isFixed()) {
                    return true;
                }
            }
            return false;
        } else if (colNum == nCols) {
            for (int y = 0; y < nRows; y++) {

                if (board[y][colNum-1].isFixed()) {
                    return true;
                }
            }
            return false;
        } else {
            for (int y = 0; y < nRows; y++) {

                if (board[y][colNum].isFixed()) {
                    return true;
                }
            }
            return false;
        }
        **/
    }

    private void insertFixedTiles(FloorTile[] fixedTiles, Position[] fixedTilePositions) {

        for (int i = 0; i < fixedTiles.length; i++ ) {
            int rowNum = fixedTilePositions[i].getY();
            int colNum = fixedTilePositions[i].getX();

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
                    nextFloorTile +=1;
                }
            }
        }
    }


                //insertAt
    private void insert(int colNum /**x**/, int rowNum /**y**/, FloorTile tile) {

        FloorTile pushedOffTile = null; //Being pushed off

        if (colNum == -1) {
            pushedOffTile = board[rowNum][nCols-1];

            for (int i = nCols - 1; i != 0; i--) {

                board[rowNum][i] = board[rowNum][i-1];
                board[rowNum][i-1] = null;

            }

            board[rowNum][colNum+1] = tile;
        } else if (colNum == nCols) {
            pushedOffTile = board[rowNum][0];

            for (int i = 0; i < nCols - 1; i++) {
                board[rowNum][i] = board[rowNum][i+1];
                board[rowNum][i+1] = null;
            }
            board[rowNum][colNum-1] = tile;
        } else if (rowNum == -1) {
            pushedOffTile = board[nRows-1][colNum];

            for (int i = nRows - 1; i != 0; i--) {
                board[i][colNum] = board[i-1][colNum];
                board[i-1][colNum] = null;
            }
            board[rowNum+1][colNum] = tile;
        } else if (rowNum == nRows) {
            pushedOffTile = board[0][colNum];

            for (int i = 0; i < nRows-1; i++) {
                board[i][colNum] = board[i+1][colNum];
                board[i+1][colNum] = null;
            }
            board[rowNum-1][colNum] = tile;
        }

        silkBag.put(pushedOffTile);

    }


    // private  insertAt()
    /**
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
    **/
    //to string

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

        PlayerPiece[] playerPieces = new PlayerPiece[0];
        Tile[] newTiles = new Tile[0];
        SilkBag silkBag = new SilkBag(newTiles);
        FloorTile[] fixedTiles = null;
        FloorTile[] tiles = null;
        Position[] fixedTilePositions = null;
        GameBoard firstgame = new GameBoard(playerPieces, fixedTiles, fixedTilePositions,tiles, 3, 3, "hello", silkBag);








        FloorTile A = new FloorTile(TileType.CORNER, true, false);
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

        System.out.println(firstgame);



        /**
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

        **/

        FloorTile insert = new FloorTile(TileType.STRAIGHT, false, false);

        //System.out.println("");
        firstgame.insert(1, 3, insert);
        /**
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
         **/
    }
}

