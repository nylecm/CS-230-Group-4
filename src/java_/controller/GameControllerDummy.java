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
        gameBoardView = new Dimension2D(8, 8);

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
        ImageView edgeTileDisplayTop = null;
        ImageView edgeTileDisplayBottom = null;
        ImageView edgeTileDisplayLeft = null;
        ImageView edgeTileDisplayRight = null;

        for (int i = 0; i < gameBoardView.getWidth(); i++) {
            edgeTileDisplayTop = getFloorTileImageView(edgeTileImage);
            edgeTileDisplayBottom = getFloorTileImageView(edgeTileImage);

            edgeTileDisplayTop.setLayoutY(- TILE_HEIGHT);
            edgeTileDisplayTop.setLayoutX(i * TILE_WIDTH);


            edgeTileDisplayBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
            edgeTileDisplayBottom.setLayoutX(i * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayTop);
            edgeTileGroup.getChildren().add(edgeTileDisplayBottom);

            setEdgeTileEventHandlers(edgeTileDisplayTop);
            setEdgeTileEventHandlers(edgeTileDisplayBottom);

        }
        for (int i = 0; i < gameBoardView.getHeight(); i++) {
            edgeTileDisplayLeft = getFloorTileImageView(edgeTileImage);
            edgeTileDisplayRight = getFloorTileImageView(edgeTileImage);

            edgeTileDisplayLeft.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayLeft.setLayoutX(- TILE_WIDTH);

            edgeTileDisplayRight.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayRight.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayLeft);
            edgeTileGroup.getChildren().add(edgeTileDisplayRight);

            setEdgeTileEventHandlers(edgeTileDisplayLeft);
            setEdgeTileEventHandlers(edgeTileDisplayRight);
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

                setFloorTileEventHandlers(floorTileDisplay);

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

    public void setEdgeTileEventHandlers(ImageView edgeTileDisplay) {
        edgeTileDisplay.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });

        edgeTileDisplay.setOnDragDropped(event -> {
            int tileCol = getTileCol(edgeTileDisplay);
            int tileRow = getTileRow(edgeTileDisplay);

            System.out.println("Col: " + tileCol);
            System.out.println("Row: " + tileRow );


            if (tileCol == - 1 || tileCol == gameBoardView.getWidth()) {
                //slideCol(tileCol, tileRow);
            } else if ((tileRow == - 1 || tileRow == gameBoardView.getHeight())) {
                slideCol(tileCol, tileRow);
            }

//            ImageView floorTileDisplay = getFloorTileImageView(floorTileImage);
//            floorTileDisplay.setLayoutX((tileCol) * TILE_WIDTH);
//            floorTileDisplay.setLayoutY((tileRow) * TILE_HEIGHT);
//            tileGroup.getChildren().add(floorTileDisplay);

            //TODO Fix pushed off tile

            //TODO Fix first tile

        });
    }

    public void setFloorTileEventHandlers(ImageView floorTileDisplay) {
        ColorAdjust highlight = new ColorAdjust();

        floorTileDisplay.setOnMouseEntered(event -> {
            highlight.setBrightness(0.2);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnMouseExited(event -> {
            highlight.setBrightness(0);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnMouseClicked(event -> {
            System.out.println("Col: " + getTileCol(floorTileDisplay));
            System.out.println("Row: " + getTileRow(floorTileDisplay));
        });
    }

    //TODO Without animation, testing
    private void slideColTemp(int col, int row) {
        List<Node> floorTilesToMove;
        if (row < col) {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileCol((ImageView) t) == col)
                    .collect(Collectors.toList());
        } else {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileCol((ImageView) t) == col)
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
                    .filter(t -> getTileRow((ImageView) t) == row &&
                            getTileCol((ImageView) t) != gameBoardView.getWidth() - 1)
                    .collect(Collectors.toList());
        } else {
            floorTilesToMove = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileRow((ImageView) t) == row &&
                            getTileCol((ImageView) t) != 0)
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
        ImageView floorTileDisplay = getFloorTileImageView(floorTileImage);
        floorTileDisplay.setLayoutX(col * TILE_WIDTH);
        floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
        tileGroup.getChildren().add(floorTileDisplay);

        List<Node> floorTilesToMove;
        double startPosition;
        double endPosition;
        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getTileCol((ImageView) t) == col)
                .collect(Collectors.toList());

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        Node lastTile = null;

        for (Node floorTile : floorTilesToMove) {
            DoubleProperty property = floorTile.translateYProperty();
            if (row < col) {
                startPosition = row * TILE_HEIGHT;
                endPosition = startPosition + TILE_HEIGHT;
                lastTile = floorTilesToMove.get(floorTilesToMove.size() - 1);
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

    private int getTileCol(ImageView tileDisplay) {
        return (int) (tileDisplay.getLayoutX() / TILE_WIDTH);
    }

    private int getTileRow(ImageView tileDisplay) {
        return (int) (tileDisplay.getLayoutY() / TILE_HEIGHT);
    }
}
