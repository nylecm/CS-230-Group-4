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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameControllerDummy implements Initializable {

    private static final int TILE_WIDTH = 40;

    private static final int TILE_HEIGHT = 40;

    private static final int PLAYER_PIECE_WIDTH = 28;

    private static final int PLAYER_PIECE_HEIGHT = 28;

    private static final int BORDER_OFFSET_HORIZONTAL = 3;

    private static final int BORDER_OFFSET_VERTICAL = 1;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private StackPane content = new StackPane();

    @FXML
    private Dimension2D gameBoardView;

    @FXML
    private ImageView floorTileToBeInserted;

    @FXML
    private Group edgeTileGroup;

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

        edgeTileGroup = new Group();
        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();

        GameBoard gameBoard = gameService.getGameBoard();
        PlayerService playerService = gameService.getPlayerService();

        //TODO: Replace width and height with values from GameBoard
        gameBoardView = new Dimension2D(gameBoard.getnCols() + 2, gameBoard.getnRows() + 2);

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

    private void displayGameBoardFlat(GameBoard gameBoard) {
        displayEdges();
        displayFloorTiles();
        displayPlayerPieces(gameBoard);

        content.getChildren().addAll(edgeTileGroup, tileGroup);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(content);
    }

    private void displayEdges() {
        for (int i = 0; i < gameBoardView.getWidth(); i++) {
            ImageView edgeTileDisplayTop = getFloorTileImageView(edgeTileImage);
            ImageView edgeTileDisplayBottom = getFloorTileImageView(edgeTileImage);

            edgeTileDisplayTop.setLayoutY(- TILE_HEIGHT);
            edgeTileDisplayTop.setLayoutX(i * TILE_WIDTH);


            edgeTileDisplayBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
            edgeTileDisplayBottom.setLayoutX(i * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayTop);
            edgeTileGroup.getChildren().add(edgeTileDisplayBottom);

        }
        for (int i = 0; i < gameBoardView.getHeight(); i++) {
            ImageView edgeTileDisplayRight = getFloorTileImageView(edgeTileImage);
            ImageView edgeTileDisplayLeft = getFloorTileImageView(edgeTileImage);

            edgeTileDisplayLeft.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayLeft.setLayoutX(- TILE_WIDTH);

            edgeTileDisplayRight.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayRight.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayLeft);
            edgeTileGroup.getChildren().add(edgeTileDisplayRight);
        }
    }

    private void displayFloorTiles() {
        for (int row = 0; row < gameBoardView.getHeight(); row++) {
            for (int col = 0; col < gameBoardView.getWidth(); col++) {

                ColorAdjust highlight = new ColorAdjust();
                highlight.setBrightness(row * 0.05);

                ImageView floorTileDisplay = getFloorTileImageView(floorTileImage);
                floorTileDisplay.setLayoutX(col * TILE_WIDTH);
                floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
                floorTileDisplay.setEffect(highlight);

                setEventHandlers(floorTileDisplay);

                tileGroup.getChildren().add(floorTileDisplay);
            }
        }
    }

    //TODO: Implement
    private void displayPlayerPieces(GameBoard gameBoard) {
        for (int i = 0; i < gameBoard.getNumOfPlayerPieces(); i++) {
            Position playerPiecePosition = gameBoard.getPlayerPiecePosition(i);
            int row = playerPiecePosition.getRowNum();
            int col = playerPiecePosition.getColNum();

            Image playerPieceImage = new Image("playerPiece.png");
            ImageView playerPieceDisplay = new ImageView(playerPieceImage);
            playerPieceDisplay.setFitWidth(PLAYER_PIECE_WIDTH);
            playerPieceDisplay.setFitHeight(PLAYER_PIECE_HEIGHT);

            playerPieceDisplay.setLayoutX((col) * PLAYER_PIECE_WIDTH + 200);
            playerPieceDisplay.setLayoutY((row) * PLAYER_PIECE_HEIGHT);

            playerPieceGroup.getChildren().add(playerPieceDisplay);
        }
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
            int tileCol = getFloorTileCol(floorTileDisplay);
            int tileRow = getFloorTileRow(floorTileDisplay);

            ImageView newFloorTileDisplay = getFloorTileImageView(floorTileImage);
            newFloorTileDisplay.setLayoutX((tileCol) * TILE_WIDTH);
            newFloorTileDisplay.setLayoutY((tileRow) * TILE_HEIGHT);
            newFloorTileDisplay.toFront();
            tileGroup.getChildren().add(newFloorTileDisplay);

            if (tileRow == 0 || tileRow == gameBoardView.getHeight() - 1) { //Top or bottom edge
                slideCol(tileCol, tileRow);
            } else if (tileCol == 0 || tileCol == gameBoardView.getWidth() - 1) { //Left or right edge
                slideRowTemp(tileRow, tileCol);
            }

            //TODO Fix pushed off tile

            //TODO Fix first tile

        });
    }

    //TODO Without animation, testing
    private void slideColTemp(int col, int row) {
        List<Node> floorTilesToMove;
        if (row < col) {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileCol((ImageView) t) == col &&
                            ((ImageView) t).getImage() != edgeTileImage)
                    .collect(Collectors.toList());
        } else {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileCol((ImageView) t) == col &&
                            ((ImageView) t).getImage() != edgeTileImage)
                    .collect(Collectors.toList());
        }
        for (Node floorTile : floorTilesToMove) {
            if (row < col) {
                floorTile.setLayoutY(floorTile.getLayoutY() + TILE_HEIGHT);
            } else {
                floorTile.setLayoutY(floorTile.getLayoutY() - TILE_HEIGHT);
            }
        }
        Node lastTile = null;
        if (row < col) {
            lastTile = floorTilesToMove.get(floorTilesToMove.size() - 2);
        } else {
            lastTile = floorTilesToMove.get(0);
        }
//        tileGroup.getChildren().remove(lastTile);
    }

    private void slideRowTemp(int row, int col) {
        List<Node> floorTilesToMove;
        if (col < row) {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileRow((ImageView) t) == row &&
                            getFloorTileCol((ImageView) t) != gameBoardView.getWidth() - 1)
                    .collect(Collectors.toList());
        } else {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getFloorTileRow((ImageView) t) == row &&
                            getFloorTileCol((ImageView) t) != 0)
                    .collect(Collectors.toList());
        }
        for (Node floorTile : floorTilesToMove) {
            if (col < row) {
                floorTile.setLayoutX(floorTile.getLayoutX() + TILE_WIDTH);
            } else {
                floorTile.setLayoutX(floorTile.getLayoutX() - TILE_WIDTH);
            }
        }
        Node lastTile = null;
        if (col < row) {
            lastTile = floorTilesToMove.get(floorTilesToMove.size() - 2);
        } else {
            lastTile = floorTilesToMove.get(0);
        }
        tileGroup.getChildren().remove(lastTile);
    }

    private void slideCol(int col, int row) {
        List<Node> floorTilesToMove;
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
        return (int) (floorTileDisplay.getLayoutX() / TILE_WIDTH);
    }

    private int getFloorTileRow(ImageView floorTileDisplay) {
        return (int) (floorTileDisplay.getLayoutY() / TILE_HEIGHT);
    }
}
