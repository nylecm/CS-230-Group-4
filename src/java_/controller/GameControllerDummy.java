package java_.controller;

import java_.game.controller.GameService;
import java_.game.tile.*;
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
import javafx.scene.control.Button;
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

    private static final int TILE_WIDTH = 80;

    private static final int TILE_HEIGHT = 80;

    private static final int PLAYER_PIECE_WIDTH = 28;

    private static final int PLAYER_PIECE_HEIGHT = 28;

    private Tile drawnTile;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private StackPane content = new StackPane();

    @FXML
    private Dimension2D gameBoardView;

    @FXML
    private ImageView drawnFloorTile;

    @FXML
    private ImageView drawnActionTile;

    @FXML
    private Button drawTileButton;

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

    @FXML
    private Image actionTileImage = new Image("actionTile.png");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameService gameService = GameService.getInstance();

        GameBoard gameBoard = gameService.getGameBoard();

        //TODO: Replace width and height with values from GameBoard
        gameBoardView = new Dimension2D(gameBoard.getnCols(), gameBoard.getnRows());

        //TODO: Replace with isometric view
        displayGameBoardFlat(gameBoard);

        drawnFloorTile.setOnDragDetected(event -> {
            Dragboard dragboard = drawnFloorTile.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(floorTileImage);
            dragboard.setContent(content);
            event.consume();
        });

        drawnActionTile.setOnDragDetected(event -> {
            Dragboard dragboard = drawnActionTile.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(actionTileImage);
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void displayGameBoardFlat(GameBoard gameBoard) {
        edgeTileGroup = new Group();
        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();

        displayEdges();
        displayFloorTiles(gameBoard);
        displayPlayerPieces(gameBoard);
        setEffectBorders();

        content.getChildren().addAll(tileGroup, effectGroup, edgeTileGroup, playerPieceGroup);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(content);
    }

    private void displayEdges() {
        ImageView edgeTileDisplayTop;
        ImageView edgeTileDisplayBottom;
        ImageView edgeTileDisplayLeft;
        ImageView edgeTileDisplayRight;

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

    private void displayFloorTiles(GameBoard gameBoard) {
        for (int row = 0; row < gameBoardView.getHeight(); row++) {
            for (int col = 0; col < gameBoardView.getWidth(); col++) {

                Image floorTileImage = new Image(getFloorTileTypeImage(gameBoard.getTileAt(row, col)));
                ImageView floorTileDisplay = getFloorTileImageView(floorTileImage);
                floorTileDisplay.setLayoutX(col * TILE_WIDTH);
                floorTileDisplay.setLayoutY(row * TILE_HEIGHT);

                setFloorTileEventHandlers(floorTileDisplay);

                tileGroup.getChildren().add(floorTileDisplay);
            }
        }
    }

    //TODO: Implement
    private void displayPlayerPieces(GameBoard gameBoard) {
        Image leftTopImage = new Image("leftTop.png");
        ImageView leftTop = new ImageView(leftTopImage);
        leftTop.setFitWidth(TILE_WIDTH);
        leftTop.setFitHeight(TILE_HEIGHT);
        leftTop.setLayoutX(- TILE_WIDTH);
        leftTop.setLayoutY(- TILE_HEIGHT);
        playerPieceGroup.getChildren().add(leftTop);

        Image rightTopImage = new Image("rightTop.png");
        ImageView rightTop = new ImageView(rightTopImage);
        rightTop.setFitWidth(TILE_WIDTH);
        rightTop.setFitHeight(TILE_HEIGHT);
        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightTop.setLayoutY(- TILE_HEIGHT);
        playerPieceGroup.getChildren().add(rightTop);

        Image leftBottomImage = new Image("leftBottom.png");
        ImageView leftBottom = new ImageView(leftBottomImage);
        leftBottom.setFitWidth(TILE_WIDTH);
        leftBottom.setFitHeight(TILE_HEIGHT);
        leftBottom.setLayoutX(- TILE_WIDTH);
        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        playerPieceGroup.getChildren().add(leftBottom);

        Image rightBottomImage = new Image("rightBottom.png");
        ImageView rightBottom = new ImageView(rightBottomImage);
        rightBottom.setFitWidth(TILE_WIDTH);
        rightBottom.setFitHeight(TILE_HEIGHT);
        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        playerPieceGroup.getChildren().add(rightBottom);

        for (int i = 0; i < gameBoard.getNumOfPlayerPieces(); i++) {
            Position playerPiecePosition = gameBoard.getPlayerPiecePosition(i);
            int row = playerPiecePosition.getRowNum();
            int col = playerPiecePosition.getColNum();

            Image playerPieceImage = gameBoard.getPlayerPiece(i).getImage();
            ImageView playerPieceDisplay = new ImageView(playerPieceImage);
            //TODO Remove magical calculations
            playerPieceDisplay.setFitWidth(28);
            playerPieceDisplay.setFitHeight(28);
            playerPieceDisplay.setLayoutX((col - 1) * TILE_WIDTH + TILE_WIDTH + 6);
            playerPieceDisplay.setLayoutY((row - 1) * TILE_HEIGHT + TILE_HEIGHT + 6);
            playerPieceDisplay.toFront();
            playerPieceDisplay.setId("Col: " + col + ", Row: " + row);
            playerPieceGroup.getChildren().add(playerPieceDisplay);

            playerPieceDisplay.setOnDragDetected(event -> {
                Dragboard dragboard = playerPieceDisplay.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(playerPieceDisplay.getImage());
                dragboard.setContent(content);
                event.consume();
            });

            playerPieceDisplay.setOnMouseClicked(event -> {
                System.out.println("Col: " + getTileCol(playerPieceDisplay));
                System.out.println("Row: " + getTileRow(playerPieceDisplay));
            });
        }
    }

    public void setEffectBorders() {
        Image leftTopImage = new Image("leftTop.png");
        ImageView leftTop = new ImageView(leftTopImage);
        leftTop.setFitWidth(TILE_WIDTH);
        leftTop.setFitHeight(TILE_HEIGHT);
        leftTop.setLayoutX(- TILE_WIDTH);
        leftTop.setLayoutY(- TILE_HEIGHT);
        effectGroup.getChildren().add(leftTop);

        Image rightTopImage = new Image("rightTop.png");
        ImageView rightTop = new ImageView(rightTopImage);
        rightTop.setFitWidth(TILE_WIDTH);
        rightTop.setFitHeight(TILE_HEIGHT);
        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightTop.setLayoutY(- TILE_HEIGHT);
        effectGroup.getChildren().add(rightTop);

        Image leftBottomImage = new Image("leftBottom.png");
        ImageView leftBottom = new ImageView(leftBottomImage);
        leftBottom.setFitWidth(TILE_WIDTH);
        leftBottom.setFitHeight(TILE_HEIGHT);
        leftBottom.setLayoutX(- TILE_WIDTH);
        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        effectGroup.getChildren().add(leftBottom);

        Image rightBottomImage = new Image("rightBottom.png");
        ImageView rightBottom = new ImageView(rightBottomImage);
        rightBottom.setFitWidth(TILE_WIDTH);
        rightBottom.setFitHeight(TILE_HEIGHT);
        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        effectGroup.getChildren().add(rightBottom);
    }

    public void setEdgeTileEventHandlers(ImageView edgeTileDisplay) {
        edgeTileDisplay.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });

        edgeTileDisplay.setOnMouseClicked(event -> {
            System.out.println("Col: " + getTileCol(edgeTileDisplay));
            System.out.println("Row: " + getTileRow(edgeTileDisplay));
        });

        edgeTileDisplay.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();
            if (source == drawnFloorTile) {
                int tileCol = getTileCol(edgeTileDisplay);
                int tileRow = getTileRow(edgeTileDisplay);

                System.out.println("Col: " + tileCol);
                System.out.println("Row: " + tileRow);

                if (tileCol == -1 || tileCol == gameBoardView.getWidth()) {
                    System.out.println("Yes 1");
                    slideRowTemp(tileRow, tileCol);
                    GameService.getInstance().getGameBoard().insert(tileCol, tileRow, (FloorTile) drawnTile, (int) drawnFloorTile.getRotate() / 90);
                } else if ((tileRow == -1 || tileRow == gameBoardView.getHeight())) {
                    slideColTemp(tileCol, tileRow);
                    System.out.println("Yes 2");
                    GameService.getInstance().getGameBoard().insert(tileCol, tileRow, (FloorTile) drawnTile, (int) drawnFloorTile.getRotate() / 90);
                }
                //TODO Store?
                displayGameBoardFlat(GameService.getInstance().getGameBoard());
            }
        });
    }

    public void setFloorTileEventHandlers(ImageView floorTileDisplay) {
        ColorAdjust highlight = new ColorAdjust();

        floorTileDisplay.setOnMouseEntered(event -> {
            highlight.setBrightness(0.2);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnMouseExited(event -> {
            floorTileDisplay.getEffect();
            highlight.setBrightness(0);
            floorTileDisplay.setEffect(highlight);
        });

        floorTileDisplay.setOnMouseClicked(event -> {
            System.out.println("Col: " + getTileCol(floorTileDisplay));
            System.out.println("Row: " + getTileRow(floorTileDisplay));

            int tileCol = getTileCol(floorTileDisplay);
            int tileRow = getTileRow(floorTileDisplay);

            System.out.println("Paths: " + GameService.getInstance().getGameBoard().getTileAt(tileRow, tileCol).getPathsBits());
        });

        floorTileDisplay.setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
        });

        floorTileDisplay.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();
            if (playerPieceGroup.getChildren().contains(source)) {
                int sourceFloorTileCol = getTileCol(source);
                int sourceFloorTileRow = getTileRow(source);
                System.out.println(sourceFloorTileCol + ", " + sourceFloorTileRow);

                int targetFloorTileCol = getTileCol(floorTileDisplay);
                int targetFloorTileRow = getTileRow(floorTileDisplay);
                System.out.println(targetFloorTileCol + ", " + targetFloorTileRow);

                int sourceBitmask;
                int oppositeBitmask;
                if (sourceFloorTileRow < targetFloorTileRow) {
                    System.out.println("Going south");
                    sourceBitmask = FloorTile.SOUTH_PATH_MASK;
                    oppositeBitmask = FloorTile.NORTH_PATH_MASK;
                } else if (sourceFloorTileRow > targetFloorTileRow) {
                    System.out.println("Going north");
                    sourceBitmask = FloorTile.NORTH_PATH_MASK;
                    oppositeBitmask = FloorTile.NORTH_PATH_MASK;
                } else if (sourceFloorTileCol < targetFloorTileCol) {
                    System.out.println("Going east");
                    sourceBitmask = FloorTile.EAST_PATH_MASK;
                    oppositeBitmask = FloorTile.WEST_PATH_MASK;
                } else {
                    System.out.println("Going west");
                    sourceBitmask = FloorTile.WEST_PATH_MASK;
                    oppositeBitmask = FloorTile.EAST_PATH_MASK;
                }

                if (GameService.getInstance().getGameBoard().validateMove(sourceFloorTileCol, sourceFloorTileRow, targetFloorTileCol, targetFloorTileRow, sourceBitmask, oppositeBitmask)) {
                    source.setLayoutX(floorTileDisplay.getLayoutX() + 6);
                    source.setLayoutY(floorTileDisplay.getLayoutY() + 6);
                }

                FloorTile sourceFloorTile = GameService.getInstance().getGameBoard().getTileAt(getTileRow(source), getTileCol(source));
                FloorTile targetFloorTile = GameService.getInstance().getGameBoard().getTileAt(getTileRow(floorTileDisplay), getTileCol(floorTileDisplay));


            } else if (source == drawnActionTile) {
                int area = 3; //Does not allow rectangle areas

                double centerY = floorTileDisplay.getLayoutY();
                double centerX = floorTileDisplay.getLayoutX();

                for (int row = 0; row < area; row++) {
                    for (int col = 0; col < area; col++) {
                        ImageView effectDisplay = getFloorTileImageView(actionTileImage);
                        effectDisplay.setLayoutY(centerY + TILE_HEIGHT - row * TILE_WIDTH);
                        effectDisplay.setLayoutX(centerX - TILE_WIDTH + col * TILE_WIDTH);
                        effectGroup.getChildren().add(effectDisplay);
                    }
                }
            }
        });
    }

    //TODO Without animation, testing
    private void slideColTemp(int col, int row) {
        ImageView floorTileDisplay = getFloorTileImageView(drawnFloorTile.getImage());
        floorTileDisplay.setRotate(drawnFloorTile.getRotate());
        floorTileDisplay.setLayoutX(col * TILE_WIDTH);
        floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
        floorTileDisplay.toFront();
        setFloorTileEventHandlers(floorTileDisplay);
        tileGroup.getChildren().add(floorTileDisplay);

        List<Node> floorTilesToMove;
        List<Node> playerPiecesToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getTileCol((ImageView) t) == col)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getTileCol((ImageView) t) == col)
                .collect(Collectors.toList());

        for (Node floorTile : floorTilesToMove) {
            if (row < col) {
                floorTile.setLayoutY(floorTile.getLayoutY() + TILE_HEIGHT);
            } else { //Bottom row
                floorTile.setLayoutY(floorTile.getLayoutY() - TILE_HEIGHT);
            }
        }

        for (Node playerPiece : playerPiecesToMove) {
            if (row < col) {
                playerPiece.setLayoutY(playerPiece.getLayoutY() + TILE_HEIGHT);
            } else { //Bottom row
                playerPiece.setLayoutY(playerPiece.getLayoutY() - TILE_HEIGHT);
            }

            //Return back if yeeted out
            if (playerPiece.getLayoutY() < 0) {
                playerPiece.setLayoutY((gameBoardView.getHeight() - 1) * TILE_HEIGHT + 6);
            }
            if (playerPiece.getLayoutY() > gameBoardView.getHeight() * TILE_HEIGHT) {
                playerPiece.setLayoutY(6);
            }
        }

        //TODO THIS IS A VERY BAD CODE, SCAAARY
        List<Node> lastTile = null;
        if (row < col) {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileCol((ImageView) t) == col &&
                        getTileRow((ImageView) t) == gameBoardView.getHeight())
                    .collect(Collectors.toList());
        } else {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileCol((ImageView) t) == col &&
                            getTileRow((ImageView) t) == -1)
                    .collect(Collectors.toList());
        }

        tileGroup.getChildren().remove(lastTile.get(0));
    }

    private void slideRowTemp(int row, int col) {
        ImageView floorTileDisplay = getFloorTileImageView(drawnFloorTile.getImage());
        floorTileDisplay.setRotate(drawnFloorTile.getRotate());
        floorTileDisplay.setLayoutX(col * TILE_WIDTH);
        floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
        floorTileDisplay.toFront();
        setFloorTileEventHandlers(floorTileDisplay);
        tileGroup.getChildren().add(floorTileDisplay);

        List<Node> floorTilesToMove;
        List<Node> playerPiecesToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getTileRow((ImageView) t) == row)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getTileRow((ImageView) t) == row)
                .collect(Collectors.toList());

        for (Node floorTile : floorTilesToMove) {
            if (col < row) {
                floorTile.setLayoutX(floorTile.getLayoutX() + TILE_WIDTH);
            } else {
                floorTile.setLayoutX(floorTile.getLayoutX() - TILE_WIDTH);
            }

        }

        //TODO THIS IS A VERY BAD CODE, SCAAARY
        List<Node> lastTile = null;
        if (col < row) {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileRow((ImageView) t) == row &&
                            getTileCol((ImageView) t) == gameBoardView.getWidth())
                    .collect(Collectors.toList());
        } else {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getTileRow((ImageView) t) == row &&
                            getTileCol((ImageView) t) == -1)
                    .collect(Collectors.toList());
        }

        tileGroup.getChildren().remove(lastTile.get(0));

        for (Node playerPiece : playerPiecesToMove) {
            if (col < row) {
                playerPiece.setLayoutX(playerPiece.getLayoutX() + TILE_WIDTH);
            } else { //Bottom row
                playerPiece.setLayoutX(playerPiece.getLayoutX() - TILE_WIDTH);
            }
            //Return back if yeeted out
            if (playerPiece.getLayoutX() < 0) {
                playerPiece.setLayoutX((gameBoardView.getWidth() - 1) * TILE_WIDTH + 6);
            }
            System.out.println(gameBoardView.getWidth() * TILE_WIDTH);
            if (playerPiece.getLayoutX() > gameBoardView.getWidth() * TILE_WIDTH) {
                playerPiece.setLayoutX(6);
            }
        }
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
                startPosition = row;
                endPosition = startPosition + TILE_HEIGHT;
                lastTile = floorTilesToMove.get(floorTilesToMove.size() - 1);
            } else {
                startPosition = row;
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

    @FXML
    public void onDrawTileButtonClicked() {
        drawnTile = GameService.getInstance().getSilkBag().take();
        if (drawnTile instanceof FloorTile) {
            Image newFloorTileImage = new Image((getFloorTileTypeImage((FloorTile) drawnTile)));
            drawnFloorTile.setImage(newFloorTileImage);
        } else { //Action Tile drawn
            Image newActionTileImage = new Image((getActionTileTypeImage((ActionTile) drawnTile)));
            drawnActionTile.setImage(newActionTileImage);
        }
    }

    @FXML
    public void onRotateClockwiseButtonClicked() {
        drawnFloorTile.setRotate(drawnFloorTile.getRotate() + 90);
    }

    @FXML
    public void onRotateAntiClockwiseButtonClicked() {
        drawnFloorTile.setRotate(drawnFloorTile.getRotate() - 90);
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

    private String getFloorTileTypeImage(FloorTile floorTile) {
        TileType type = floorTile.getType();
        switch (type) {
            case STRAIGHT:
                return "straightFlat.png";
            case CORNER:
                return "cornerFlat.png";
            case T_SHAPED:
                return "t-shapedFlat.png";
            case GOAL:
                return "goalFlat.png";
        }
        return null;
    }

    private String getActionTileTypeImage(ActionTile actionTile) {
        TileType type = actionTile.getType();
        switch (type) {
            case FIRE:
                return "fireFlat.png";
            case ICE:
                return "iceFlat.png";
            case BACKTRACK:
                return "backtrackFlat.png";
            case DOUBLE_MOVE:
                return "doublemoveFlat.png";
        }
        return null;
    }
}
