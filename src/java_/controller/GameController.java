package java_.controller;


import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.game.tile.*;
import java_.util.Position;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {

    private GameService gameService;

    private GameBoard gameBoard;

    private static final int TILE_WIDTH = 80;

    private  static final int TILE_HEIGHT = 80;

    private static final int PLAYER_PIECE_WIDTH = 28;

    private static final int PLAYER_PIECE_HEIGHT = 28;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private StackPane content;

    @FXML
    private Dimension2D gameBoardView;

    @FXML
    private Group edgeTileGroup;

    @FXML
    private Group tileGroup;

    @FXML
    private Group playerPieceGroup;

    @FXML
    private Group effectGroup;

    @FXML
    private ImageView drawnFloorTile;

    @FXML
    private ImageView drawnActionTile;

    @FXML
    private HBox playersActionTiles;

    @FXML
    private HBox playerQueue;

    @FXML
    private Button endTurnButton;

    @FXML
    private Image floorTileImage = new Image("fullFlat.png");

    @FXML
    private Image edgeTileImage = new Image("emptyFlat.png");

    @FXML
    private Image actionTileImage = new Image("actionTile.png");

    //TODO Remove all below
    @FXML
    private Button drawTileButton;

    private Tile drawnTile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameService = GameService.getInstance();

        gameBoard = gameService.getGameBoard();

        gameBoardView = new Dimension2D(gameBoard.getnCols(), gameBoard.getnRows());

        //TODO Replace with isometric view
        displayGameView();

        //TODO Move somewhere else?
        drawnFloorTile.setOnDragDetected(event -> {
            if ( drawnFloorTile.getImage() != null) {
                Dragboard dragboard = drawnFloorTile.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(drawnFloorTile.getImage());
                dragboard.setContent(content);
                event.consume();
            }
        });
    }

    private void displayGameView() {
        System.out.println("Current Player Number: " + gameService.getCurrentPlayerNum());

        edgeTileGroup = new Group();
        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();

        //Build GameBoard
        displayEdges();
        displayFloorTiles();
        displayPlayerPieces();
        setEffectBorders();

        //View GameBoard
        content = new StackPane();
        content.getChildren().addAll(tileGroup, edgeTileGroup, effectGroup, playerPieceGroup);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(content);

        //Build Player Queue
        if (playerQueue.getChildren().size() != gameService.getPlayerService().getPlayers().length) {
            for (int i = 0; i < gameService.getPlayerService().getPlayers().length; i++) {
                Image playerPieceImage = GameService.getInstance().getPlayerService().getPlayer(i).getPlayerPiece().getImage();
                ImageView playerPiecePreview = new ImageView(playerPieceImage);
                playerQueue.getChildren().add(playerPiecePreview);
            }
        }


        //Build ActionTiles list of current player
        //Remove current ActionTiles for next round
        if (!playersActionTiles.getChildren().isEmpty()) {
            playersActionTiles.getChildren().clear();
        }
        int currentPlayerNum = gameService.getCurrentPlayerNum();
        displayActionTiles(gameService.getPlayerService().getPlayer(currentPlayerNum));
    }

    //TODO Remake, loop
    private void displayEdges() {
        ImageView edgeTileDisplayTop;
        ImageView edgeTileDisplayBottom;
        ImageView edgeTileDisplayLeft;
        ImageView edgeTileDisplayRight;

        for (int i = 0; i < gameBoardView.getWidth(); i++) {
            edgeTileDisplayTop = getTileImageView(edgeTileImage);
            edgeTileDisplayBottom = getTileImageView(edgeTileImage);

            edgeTileDisplayTop.setLayoutY(- TILE_HEIGHT);
            edgeTileDisplayTop.setLayoutX(i * TILE_WIDTH);


            edgeTileDisplayBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
            edgeTileDisplayBottom.setLayoutX(i * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayTop);
            edgeTileGroup.getChildren().add(edgeTileDisplayBottom);

            setEdgeTileEventHandlers(edgeTileDisplayTop);
            setEdgeTileEventHandlers(edgeTileDisplayBottom);

            if (GameService.getInstance().getGameBoard().isRowFixed(getItemCol(edgeTileDisplayTop)) || GameService.getInstance().getGameBoard().isRowFixed(getItemCol(edgeTileDisplayBottom))) {
                edgeTileDisplayTop.setVisible(false);
                edgeTileDisplayBottom.setVisible(false);
            }
        }

        for (int i = 0; i < gameBoardView.getHeight(); i++) {
            edgeTileDisplayLeft = getTileImageView(edgeTileImage);
            edgeTileDisplayRight = getTileImageView(edgeTileImage);

            edgeTileDisplayLeft.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayLeft.setLayoutX(- TILE_WIDTH);

            edgeTileDisplayRight.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayRight.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayLeft);
            edgeTileGroup.getChildren().add(edgeTileDisplayRight);

            setEdgeTileEventHandlers(edgeTileDisplayLeft);
            setEdgeTileEventHandlers(edgeTileDisplayRight);

            if (GameService.getInstance().getGameBoard().isRowFixed(getItemRow(edgeTileDisplayLeft)) || GameService.getInstance().getGameBoard().isRowFixed(getItemRow(edgeTileDisplayRight))) {
                edgeTileDisplayLeft.setVisible(false);
                edgeTileDisplayRight.setVisible(false);
            }
        }
    }

    private void displayFloorTiles() {
        for (int row = 0; row < gameBoardView.getHeight(); row++) {
            for (int col = 0; col < gameBoardView.getWidth(); col++) {

                Image floorTileImage = new Image(getFloorTileTypeImage(gameBoard.getTileAt(row, col)));
                ImageView floorTileImageView = getTileImageView(floorTileImage);
                floorTileImageView.setLayoutX(col * TILE_WIDTH);
                floorTileImageView.setLayoutY(row * TILE_HEIGHT);
                floorTileImageView.setRotate(gameBoard.getTileAt(row, col).getRotation() * 90);

                setFloorTileEventHandlers(floorTileImageView);

                tileGroup.getChildren().add(floorTileImageView);
            }
        }
    }

    private void displayPlayerPieces() {
        //TODO Extract, make a loop ------------------------------
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
        //TODO Extract, make a loop ------------------------------------

        for (int i = 0; i < gameBoard.getNumOfPlayerPieces(); i++) {
            int row = gameBoard.getPlayerPiecePosition(i).getRowNum();
            int col = gameBoard.getPlayerPiecePosition(i).getColNum();

            ImageView playerPieceImageView = getPlayerPieceImageView(gameBoard.getPlayerPiece(i).getImage());

            //TODO Remove magical calculations
            playerPieceImageView.setLayoutX((col - 1) * TILE_WIDTH + TILE_WIDTH + 6);
            playerPieceImageView.setLayoutY((row - 1) * TILE_HEIGHT + TILE_HEIGHT + 6);
            playerPieceGroup.getChildren().add(playerPieceImageView);

            setPlayerPieceEventHandlers(playerPieceImageView);
        }
    }

    private void setEffectBorders() {
        //TODO Loop, replace image values
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

    public void displayActionTiles(Player currentPlayer) {
        for (ActionTile actionTile : currentPlayer.getDrawnActionTiles()) {
            Image actionTileImage = new Image(getActionTileTypeImage(actionTile));
            ImageView actionTileDisplay = new ImageView(actionTileImage);
            playersActionTiles.getChildren().add(actionTileDisplay);
        }
    }

    private void setEdgeTileEventHandlers(ImageView edgeTileImageView) {
        edgeTileImageView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });

        edgeTileImageView.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            //Accept only FloorTiles
            if (source == drawnFloorTile) {
                int targetCol = getItemCol(edgeTileImageView);
                int targetRow = getItemRow(edgeTileImageView);

                //Left or Right edge
                if (targetCol == -1 || targetCol == gameBoardView.getWidth()) {
                    slideRowTemp(targetRow, targetCol);
                    gameBoard.insert(targetCol, targetRow, (FloorTile) drawnTile, (int) (drawnFloorTile.getRotate() / 90));
                //Top or Bottom edge
                } else if (targetRow == -1 || targetRow == gameBoardView.getHeight()) {
                    slideColTemp(targetCol, targetRow);
                    gameBoard.insert(targetCol, targetRow, (FloorTile) drawnTile, (int) (drawnFloorTile.getRotate() / 90));
                }
                //Update GameBoard                      //TODO Fix this?

                //Remove drawn FloorTile image
                drawnFloorTile.setImage(null);

                //TODO Reload gameBoardView?
                //displayGameView(GameService.getInstance().getGameBoard());
            }
        });
    }

    private void setFloorTileEventHandlers(ImageView floorTileImageView) {
        ColorAdjust highlight = new ColorAdjust();

        //TODO Remove
        floorTileImageView.setOnMouseClicked(event -> {
            System.out.println("Col: " + getItemCol(floorTileImageView));
            System.out.println("Row: " + getItemRow(floorTileImageView));

            int tileCol = getItemCol(floorTileImageView);
            int tileRow = getItemRow(floorTileImageView);

            System.out.println("Paths: " + GameService.getInstance().getGameBoard().getTileAt(tileRow, tileCol).getPathsBits());
        });

        floorTileImageView.setOnMouseEntered(event -> {
            highlight.setBrightness(0.2);
            floorTileImageView.setEffect(highlight);
        });

        floorTileImageView.setOnMouseExited(event -> {
            floorTileImageView.getEffect();
            highlight.setBrightness(0);
            floorTileImageView.setEffect(highlight);
        });

        floorTileImageView.setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
        });

        floorTileImageView.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            //PlayerPiece dropped
            if (playerPieceGroup.getChildren().contains(source)) {
                movePlayerPieceImageView(source, floorTileImageView);

            //ActionTile dropped
            } else if (playersActionTiles.getChildren().contains(source)){
                applyEffectImageView(source, floorTileImageView);
                event.setDropCompleted(true);
            }
        });

    }

    private void setPlayerPieceEventHandlers(ImageView playerPieceImageView) {
        playerPieceImageView.setOnDragDetected(event -> {
            ImageView source = (ImageView) event.getSource();

            if (!gameService.getPlayerPieceMoved()) {
                int currentPlayerNum = GameService.getInstance().getCurrentPlayerNum();
                int sourcePlayerPieceCol = getItemCol(source);
                int sourcePlayerPieceRow = getItemRow(source);
                Position draggedPlayerPiecePosition = new Position(sourcePlayerPieceRow, sourcePlayerPieceCol);

                //Check if the Player is dragging theirs PlayerPiece
                if (draggedPlayerPiecePosition.equals(GameService.getInstance().getGameBoard().getPlayerPiecePosition(currentPlayerNum))) {
                    Dragboard dragboard = playerPieceImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(playerPieceImageView.getImage());
                    dragboard.setContent(content);
                    event.consume();
                } else {
                    System.out.println("You can't move someone else's PlayerPiece dumbass!");
                }
            }
        });
    }

    private void setEffectEventHandlers(ImageView effectImageView) {
        effectImageView.setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
        });

        effectImageView.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            //PlayerPiece dropped
            if (playerPieceGroup.getChildren().contains(source)) {
                int targetFloorTileCol = getItemCol(effectImageView);
                int targetFloorTileRow = getItemRow(effectImageView);

                //Get the FloorTile under the effect (for valid move check)
                //TODO No need to filter
                Node targetFloorTile = tileGroup.getChildren()
                        .stream()
                        .filter(t -> getItemCol((ImageView) t) == targetFloorTileCol &&
                                getItemRow((ImageView) t) == targetFloorTileRow)
                        .collect(Collectors.toList()).get(0);

                movePlayerPieceImageView(source, (ImageView) targetFloorTile);
            }
        });
    }

    private void slideColTemp(int col, int row) {
        ImageView floorTileImageView = getTileImageView(drawnFloorTile.getImage());
        floorTileImageView.setRotate(drawnFloorTile.getRotate());
        floorTileImageView.setLayoutX(col * TILE_WIDTH);
        floorTileImageView.setLayoutY(row * TILE_HEIGHT);
        floorTileImageView.toFront();
        setFloorTileEventHandlers(floorTileImageView);
        tileGroup.getChildren().add(floorTileImageView);

        List<Node> floorTilesToMove;
        List<Node> playerPiecesToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getItemCol((ImageView) t) == col)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getItemCol((ImageView) t) == col)
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
                    .filter(t -> getItemCol((ImageView) t) == col &&
                            getItemRow((ImageView) t) == gameBoardView.getHeight())
                    .collect(Collectors.toList());
        } else {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getItemCol((ImageView) t) == col &&
                            getItemRow((ImageView) t) == -1)
                    .collect(Collectors.toList());
        }

        tileGroup.getChildren().remove(lastTile.get(0));
    }

    private void slideRowTemp(int row, int col) {
        ImageView floorTileImageView = getTileImageView(drawnFloorTile.getImage());
        floorTileImageView.setRotate(drawnFloorTile.getRotate());
        floorTileImageView.setLayoutX(col * TILE_WIDTH);
        floorTileImageView.setLayoutY(row * TILE_HEIGHT);
        floorTileImageView.toFront();
        setFloorTileEventHandlers(floorTileImageView);
        tileGroup.getChildren().add(floorTileImageView);

        List<Node> floorTilesToMove;
        List<Node> playerPiecesToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getItemRow((ImageView) t) == row)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getItemRow((ImageView) t) == row)
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
                    .filter(t -> getItemRow((ImageView) t) == row &&
                            getItemCol((ImageView) t) == gameBoardView.getWidth())
                    .collect(Collectors.toList());
        } else {
            lastTile = tileGroup.getChildren()
                    .stream()
                    .filter(t -> getItemRow((ImageView) t) == row &&
                            getItemCol((ImageView) t) == -1)
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

    //TODO Remove, part of the game "loop"
    @FXML
    private void onDrawTileButtonClicked() {
        drawnTile = gameService.getSilkBag().take();

        if (drawnTile instanceof FloorTile) {
            Image newFloorTileImage = new Image((getFloorTileTypeImage((FloorTile) drawnTile)));
            drawnFloorTile.setImage(newFloorTileImage);
        } else {
            Image newActionTileImage = new Image((getActionTileTypeImage((ActionTile) drawnTile)));
            ImageView drawnActionTileImageView = new ImageView(newActionTileImage);

            //Add to GUI
            playersActionTiles.getChildren().add(drawnActionTileImageView);

            //Add to Player class
            gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).addDrawnActionTile((ActionTile) drawnTile);

            drawnActionTileImageView.setOnDragDetected(event -> {
                if (!GameService.getInstance().getActionTilePlayed()) {
                    Dragboard dragboard = drawnActionTileImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(actionTileImage);
                    dragboard.setContent(content);
                    event.consume();
                } else {
                    System.out.println("You can't use ActionTile anymore you idiot");
                }
            });

            drawnActionTileImageView.setOnDragDone(event -> {

                int usedActionTileIndex = playersActionTiles.getChildren().indexOf(drawnActionTileImageView);

                playersActionTiles.getChildren().remove(drawnActionTileImageView);
                gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().remove(usedActionTileIndex);
                gameService.setActionTilePlayed(true);
            });
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

    @FXML
    private void onEndTurnButtonClicked() {
        if (playerPieceCanMove() && !GameService.getInstance().getPlayerPieceMoved()) {
            System.out.println("You have to move player piece!");
        } else {
            ImageView currentPlayerPiecePreview = (ImageView) playerQueue.getChildren().get(0);
            playerQueue.getChildren().remove(0);
            playerQueue.getChildren().add(currentPlayerPiecePreview);
            gameService.nextTurn();
            displayGameView();
        }
    }

    //TODO Loop/remake
    private boolean playerPieceCanMove() {
        int currentPlayerNum = gameService.getCurrentPlayerNum();
        int currentPlayerPieceCol = gameBoard.getPlayerPiecePosition(currentPlayerNum).getColNum();
        int currentPlayerPieceRow = gameBoard.getPlayerPiecePosition(currentPlayerNum).getRowNum();

        //Left
        if (currentPlayerPieceCol != 0) {
            if (gameBoard.validateMove(currentPlayerPieceCol, currentPlayerPieceRow, currentPlayerPieceCol - 1, currentPlayerPieceRow, FloorTile.WEST_PATH_MASK, FloorTile.EAST_PATH_MASK)) {
                return true;
            }
        }
        //Right
        if (currentPlayerPieceCol != gameBoardView.getWidth() - 1) {
            if (gameBoard.validateMove(currentPlayerPieceCol, currentPlayerPieceRow, currentPlayerPieceCol + 1, currentPlayerPieceRow, FloorTile.EAST_PATH_MASK, FloorTile.WEST_PATH_MASK)) {
                return true;
            }
        }

        //Above
        if (currentPlayerPieceRow != 0) {
            if (gameBoard.validateMove(currentPlayerPieceCol, currentPlayerPieceRow, currentPlayerPieceCol, currentPlayerPieceRow - 1, FloorTile.NORTH_PATH_MASK, FloorTile.SOUTH_PATH_MASK)) {
                return true;
            }
        }

        //Below
        if (currentPlayerPieceRow != gameBoardView.getHeight()) {
            if (gameBoard.validateMove(currentPlayerPieceCol, currentPlayerPieceRow, currentPlayerPieceCol, currentPlayerPieceRow + 1, FloorTile.SOUTH_PATH_MASK, FloorTile.NORTH_PATH_MASK)) {
                return true;
            }
        }

        return false;
    }

    private void movePlayerPieceImageView(ImageView playerPieceImageView, ImageView targetFloorTile) {
        int sourceFloorTileCol = getItemCol(playerPieceImageView);
        int sourceFloorTileRow = getItemRow(playerPieceImageView);

        int targetFloorTileCol = getItemCol(targetFloorTile);
        int targetFloorTileRow = getItemRow(targetFloorTile);

        int sourceFloorTileMask;
        int oppositeFloorTileMask;

        //Check direction and set masks needed for a valid move
        if (sourceFloorTileRow < targetFloorTileRow) {
            sourceFloorTileMask = FloorTile.SOUTH_PATH_MASK;
            oppositeFloorTileMask = FloorTile.NORTH_PATH_MASK;
        } else if (sourceFloorTileRow > targetFloorTileRow) {
            sourceFloorTileMask = FloorTile.NORTH_PATH_MASK;
            oppositeFloorTileMask = FloorTile.SOUTH_PATH_MASK;
        } else if (sourceFloorTileCol < targetFloorTileCol) {
            sourceFloorTileMask = FloorTile.EAST_PATH_MASK;
            oppositeFloorTileMask = FloorTile.WEST_PATH_MASK;
        } else {
            sourceFloorTileMask = FloorTile.WEST_PATH_MASK;
            oppositeFloorTileMask = FloorTile.EAST_PATH_MASK;
        }

        //Make a move if it's a valid move
        if (gameBoard.validateMove(sourceFloorTileCol, sourceFloorTileRow, targetFloorTileCol, targetFloorTileRow, sourceFloorTileMask, oppositeFloorTileMask)) {
            //TODO Remove magical numbers
            playerPieceImageView.setLayoutX(targetFloorTile.getLayoutX() + 6);
            playerPieceImageView.setLayoutY(targetFloorTile.getLayoutY() + 6);
            gameBoard.movePlayerPiece(targetFloorTileRow, targetFloorTileCol);

            //Disable more than one move
            gameService.setPlayerPieceMoved(true);

            //Check for win
            //TODO Implement
            if (gameBoard.getTileAt(targetFloorTileRow, targetFloorTileCol).getType() == TileType.GOAL) {
                System.exit(0);
            }
        }
    }

    private void applyEffectImageView(ImageView effectImageView, ImageView targetFloorTile) {
        //TODO Get from corresponding class
        int area = 3;

        double centerY = targetFloorTile.getLayoutY();
        double centerX = targetFloorTile.getLayoutX();

        for (int row = 0; row < area; row++) {
            for (int col = 0; col < area; col++) {
                ImageView effectDisplay = getTileImageView(effectImageView.getImage());
                effectDisplay.setLayoutY(centerY + TILE_HEIGHT - row * TILE_WIDTH);
                effectDisplay.setLayoutX(centerX - TILE_WIDTH + col * TILE_WIDTH);
                //TODO Delete them all together?
                if (getItemCol(effectDisplay) >= gameBoardView.getWidth() || getItemCol(effectDisplay) < 0
                        || getItemRow(effectDisplay) >= gameBoardView.getHeight() || getItemRow(effectDisplay) < 0) {
                    effectDisplay.setVisible(false);
                }
                setEffectEventHandlers(effectDisplay);
                effectGroup.getChildren().add(effectDisplay);
            }
        }
    }

    private ImageView getTileImageView(Image tileImage) {
        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setFitWidth(TILE_WIDTH);
        tileImageView.setFitHeight(TILE_HEIGHT);
        return tileImageView;
    }

    private ImageView getPlayerPieceImageView(Image playerPieceImage) {
        //TODO Remove magical numbers
        ImageView playerPieceImageView = new ImageView(playerPieceImage);
        playerPieceImageView.setFitWidth(28);
        playerPieceImageView.setFitHeight(28);
        return playerPieceImageView;
    }

    private int getItemCol(ImageView tileImageView) {
        return (int) (tileImageView.getLayoutX() / TILE_WIDTH);
    }

    private int getItemRow(ImageView tileImageView) {
        return (int) (tileImageView.getLayoutY() / TILE_HEIGHT);
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
