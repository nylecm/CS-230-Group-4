package java_.game.tile;

import java_.game.player.PlayerPiece;

public class GameBoard {

    private int width;
    private int height;
    private String name;
    private PlayerPiece[] playerPiece = new PlayerPiece[4];
    private int[][] coordinate = new int[2][4];
    private SilkBag silkBag;
    private FloorTile[][] board = new FloorTile[width][height];


    public GameBoard(PlayerPiece[] playerPiece, int width, int height, String name, SilkBag silkBag) {
        this.playerPiece = playerPiece;
        this.name = name;
        this.silkBag = silkBag;
    }

}
