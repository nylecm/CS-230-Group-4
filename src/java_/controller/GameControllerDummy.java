package java_.controller;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.game.player.PlayerService;
import java_.game.tile.FloorTile;
import java_.game.tile.GameBoard;
import java_.game.tile.Tile;
import java_.game.tile.TileType;
import java_.util.Position;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameControllerDummy implements Initializable {

    private static final int TILE_WIDTH = 40;

    private static final int TILE_HEIGHT = 40;

    private static final int BORDER_OFFSET_HORIZONTAL = 3;

    private static final int BORDER_OFFSET_VERTICAL = 1;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Dimension2D gameBoardView;

    @FXML
    private ImageView floorTileToBeInserted;

    @FXML
    private Group tileGroup;

    @FXML
    private Group playerPieceGroup;

    @FXML
    private Group effectGroup;

    @FXML
    private Image floorTileImage = new Image("fullFlat.png");

    @FXML
    private Image edgeTileImage = new Image("emptyFlat.png");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameService gameService = GameService.getInstance();

        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();

        //TODO: Remove, only for testing
        GameBoard gameBoard = gameService.getGameBoard();


        PlayerService playerService = gameService.getPlayerService();

        int nPlayers = playerService.getPlayers().length;

        //TODO: Replace width and height with values from GameBoard
        gameBoardView = new Dimension2D(gameService.getGameBoard().getnCols() + 3, gameService.getGameBoard().getnRows() + 1);




        // Place player pieces:

        drawPlayerPiece();

        //TODO: Replace with isometric view
        displayGameBoardFlat(gameBoard);

        floorTileToBeInserted.setOnDragDetected(event -> {
            Dragboard dragboard = floorTileToBeInserted.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(floorTileToBeInserted.getImage());
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void drawPlayerPiece() {
        Image playerPieceImage = new Image("playerPiece.png");
        ImageView playerPiece = new ImageView(playerPieceImage);
        playerPiece.setFitWidth(28);
        playerPiece.setFitHeight(28);
        playerPiece.setX(1 + 6);
        playerPiece.setY(1 + 6);
        playerPieceGroup.getChildren().add(playerPiece);
        playerPiece.setId("playerPiece");

        playerPiece.setOnDragDetected(event -> {
            Dragboard dragboard = playerPiece.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(playerPiece.getImage());
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void displayGameBoardFlat(GameBoard gameBoard) {
        displayFloorTiles();
        displayPlayerPieces(gameBoard);
    }

    private void displayFloorTiles() {
        for (int row = 0; row < gameBoardView.getWidth(); row++) {
            for (int col = 0; col < gameBoardView.getHeight(); col++) {
                Image tileImage = floorTileImage;
                if (row == 0 || col == 0 || row == gameBoardView.getWidth() - 1 || col == gameBoardView.getHeight() - 1) {
                    if ((row == 0 && col == 0) || (row == 0 && col == gameBoardView.getHeight() - 1) || (row == gameBoardView.getWidth() - 1 && col == 0) || (row == gameBoardView.getWidth() - 1 && col == gameBoardView.getHeight() - 1)) {
                        tileImage = null;
                    } else {
                        tileImage = edgeTileImage;
                    }
                }

                ImageView floorTileDisplay = getFloorTileImageView(tileImage);
                floorTileDisplay.setX((col) * TILE_WIDTH);
                floorTileDisplay.setY((row) * TILE_HEIGHT);

                setEventHandlers(floorTileDisplay);

                tileGroup.getChildren().add(floorTileDisplay);
            }
        }
        StackPane playerPieceViewHolder = new StackPane(playerPieceGroup);
        StackPane gameBoardViewHolder = new StackPane(tileGroup);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(playerPieceViewHolder);
        scrollPane.setContent(gameBoardViewHolder);
    }

    //TODO: Implement
    private void displayPlayerPieces(GameBoard gameBoard) {

    }

    public void setEventHandlers(ImageView floorTileDisplay) {
        ColorAdjust highlight = new ColorAdjust();

        floorTileDisplay.setOnMouseEntered(event -> {
            highlight.setBrightness(0.2);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnMouseExited(event -> {
            highlight.setBrightness(0);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });

        floorTileDisplay.setOnDragDropped(event -> {
            floorTileDisplay.setImage(event.getDragboard().getImage());
            int tileCol = getFloorTileCol(floorTileDisplay);
            int tileRow = getFloorTileRow(floorTileDisplay);

            ImageView newFloorTile = getFloorTileImageView(floorTileImage);
            newFloorTile.setX(tileCol * TILE_WIDTH);
            newFloorTile.setY(tileRow * TILE_HEIGHT);
            tileGroup.getChildren().add(newFloorTile);

            if (tileRow == 0 || tileRow == gameBoardView.getHeight() - 1) {
                slideCol(tileCol, tileRow);
            } else if (tileCol == 0 || tileCol == gameBoardView.getWidth() - 1) {
                System.out.println("I will slide the row!");
            }
        });
    }

    private void slideCol(int col, int row) {
        List<Node> floorTilesToMove = null;
        double startPosition;
        double endPosition;
        if (row < col) {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileCol((ImageView) t) == col &&
                            getFloorTileRow((ImageView) t) != gameBoardView.getHeight() - 1)
                    .collect(Collectors.toList());
        } else {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileCol((ImageView) t) == col &&
                            getFloorTileRow((ImageView) t) != 0)
                    .collect(Collectors.toList());
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        Node lastTile = null;

        for (Node floorTile : floorTilesToMove) {
            DoubleProperty property = floorTile.translateYProperty();
            System.out.println(property);
            if (row < col) {
                startPosition = row * TILE_HEIGHT;
                endPosition = startPosition + TILE_HEIGHT;
                lastTile = floorTilesToMove.get(floorTilesToMove.size() - 2); //TODO: Replace 2 with border offset
            } else {
                startPosition = row / TILE_HEIGHT;
                endPosition = startPosition - TILE_HEIGHT;
                lastTile = floorTilesToMove.get(0);
            }
            timeline.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(property, startPosition)),
                    new KeyFrame(new Duration(1000), new KeyValue(property, endPosition))
            );
        }
        timeline.play();
        Node pushedOfTile = lastTile;

        timeline.setOnFinished(event -> {
            tileGroup.getChildren().remove(pushedOfTile);
        });
    }

    private ImageView getFloorTileImageView(Image floorTileImage) {
        ImageView output = new ImageView(floorTileImage);
        output.setFitWidth(TILE_WIDTH);
        output.setFitHeight(TILE_HEIGHT);
        return output;
    }

    private int getFloorTileCol(ImageView floorTileDisplay) {
        return (int) (floorTileDisplay.getX() / TILE_WIDTH);
    }

    private int getFloorTileRow(ImageView floorTileDisplay) {
        return (int) (floorTileDisplay.getY() / TILE_HEIGHT);
    }

    private GameBoard loadGameboard() {
        PlayerPiece playerPiece1 = new PlayerPiece();
        PlayerPiece playerPiece2 = new PlayerPiece();
        Position[] playerPiecesPosition = {
                new Position(1, 5),
                new Position(3, 3)
        };

        Tile[] newTiles = new Tile[0];

        FloorTile A = new FloorTile(TileType.CORNER, true);
        FloorTile B = new FloorTile(TileType.STRAIGHT, true);
        FloorTile C = new FloorTile(TileType.T_SHAPED, true);

        FloorTile[] fixedTiles = new FloorTile[3];
        fixedTiles[0] = A;
        fixedTiles[1] = B;
        fixedTiles[2] = C;

        Position[] fixedTilePositions = new Position[3];
        fixedTilePositions[0] = new Position(0, 0);
        fixedTilePositions[1] = new Position(1, 1);
        fixedTilePositions[2] = new Position(2, 2);

        FloorTile D = new FloorTile(TileType.CORNER, false);
        FloorTile E = new FloorTile(TileType.CORNER, false);
        FloorTile F = new FloorTile(TileType.T_SHAPED, false);
        FloorTile G = new FloorTile(TileType.STRAIGHT, false);
        FloorTile H = new FloorTile(TileType.STRAIGHT, false);
        FloorTile I = new FloorTile(TileType.CORNER, false);

        FloorTile[] tiles = new FloorTile[6];
        tiles[0] = D;
        tiles[1] = E;
        tiles[2] = F;
        tiles[3] = G;
        tiles[4] = H;
        tiles[5] = I;


        return new GameBoard(playerPiecesPosition, fixedTiles, fixedTilePositions, tiles, 3, 3, "GameBoard1");
    }
}
