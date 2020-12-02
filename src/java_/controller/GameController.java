package java_.controller;

import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.tile.ActionTile;
import java_.game.tile.GameBoard;
import java_.util.Position;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController {

//    private GameEventsHandler gameEventsHandler;
//
//    private GameAnimationsController gameAnimationsController;
//
//    private GameMovesHandler gameMovesHandler;
//
//    private GameService gameService;
//
//    private GameBoard gameBoard;
//
//    @FXML
//    private Dimension2D gameBoardView;
//
//    @FXML
//    private ScrollPane scrollPane;
//
//    @FXML
//    private StackPane content;
//
//    @FXML
//    private Group edgeTileGroup;
//
//    @FXML
//    private Group tileGroup;
//
//    @FXML
//    private Group playerPieceGroup;
//
//    @FXML
//    private Group effectGroup;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//        gameService = GameService.getInstance();
//
//        gameBoard = gameService.getGameBoard();
//
//        gameBoardView = new Dimension2D(gameBoard.getnCols(), gameBoard.getnRows());
//
//        //TODO: Replace with isometric view
//        displayGameView(gameBoard);
//    }
//
//    private void displayGameView(GameBoard gameBoard) {
//        edgeTileGroup = new Group();
//        tileGroup = new Group();
//        playerPieceGroup = new Group();
//        effectGroup = new Group();
//
//        //Build GameBoard
//        displayEdges();
//        displayFloorTiles(gameBoard);
//        displayPlayerPieces(gameBoard);
//        setEffectBorders();
//
//        //Build Player queue
//        //TODO Implement
//
//        //Build ActionTiles list
//        displayActionTiles(GameService.getInstance().getCurrentPlayer());
//
//        content = new StackPane();
//        content.getChildren().addAll(tileGroup, edgeTileGroup, effectGroup, playerPieceGroup);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setContent(content);
//    }
//
//    private void displayEdges() {
//        ImageView edgeTileDisplayTop;
//        ImageView edgeTileDisplayBottom;
//        ImageView edgeTileDisplayLeft;
//        ImageView edgeTileDisplayRight;
//
//        for (int i = 0; i < gameBoardView.getWidth(); i++) {
//            edgeTileDisplayTop = getFloorTileImageView(edgeTileImage);
//            edgeTileDisplayBottom = getFloorTileImageView(edgeTileImage);
//
//            edgeTileDisplayTop.setLayoutY(- TILE_HEIGHT);
//            edgeTileDisplayTop.setLayoutX(i * TILE_WIDTH);
//
//
//            edgeTileDisplayBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
//            edgeTileDisplayBottom.setLayoutX(i * TILE_WIDTH);
//
//            edgeTileGroup.getChildren().add(edgeTileDisplayTop);
//            edgeTileGroup.getChildren().add(edgeTileDisplayBottom);
//
//            setEdgeTileEventHandlers(edgeTileDisplayTop);
//            setEdgeTileEventHandlers(edgeTileDisplayBottom);
//
//            if (GameService.getInstance().getGameBoard().isRowFixed(getTileCol(edgeTileDisplayTop)) || GameService.getInstance().getGameBoard().isRowFixed(getTileCol(edgeTileDisplayBottom))) {
//                edgeTileDisplayTop.setVisible(false);
//                edgeTileDisplayBottom.setVisible(false);
//            }
//        }
//
//        for (int i = 0; i < gameBoardView.getHeight(); i++) {
//            edgeTileDisplayLeft = getFloorTileImageView(edgeTileImage);
//            edgeTileDisplayRight = getFloorTileImageView(edgeTileImage);
//
//            edgeTileDisplayLeft.setLayoutY(i * TILE_HEIGHT);
//            edgeTileDisplayLeft.setLayoutX(- TILE_WIDTH);
//
//            edgeTileDisplayRight.setLayoutY(i * TILE_HEIGHT);
//            edgeTileDisplayRight.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
//
//            edgeTileGroup.getChildren().add(edgeTileDisplayLeft);
//            edgeTileGroup.getChildren().add(edgeTileDisplayRight);
//
//            setEdgeTileEventHandlers(edgeTileDisplayLeft);
//            setEdgeTileEventHandlers(edgeTileDisplayRight);
//
//            if (GameService.getInstance().getGameBoard().isRowFixed(getTileRow(edgeTileDisplayLeft)) || GameService.getInstance().getGameBoard().isRowFixed(getTileRow(edgeTileDisplayRight))) {
//                edgeTileDisplayLeft.setVisible(false);
//                edgeTileDisplayRight.setVisible(false);
//            }
//        }
//    }
//
//    private void displayFloorTiles(GameBoard gameBoard) {
//        for (int row = 0; row < gameBoardView.getHeight(); row++) {
//            for (int col = 0; col < gameBoardView.getWidth(); col++) {
//
//                Image floorTileImage = new Image(getFloorTileTypeImage(gameBoard.getTileAt(row, col)));
//                ImageView floorTileDisplay = getFloorTileImageView(floorTileImage);
//                floorTileDisplay.setLayoutX(col * TILE_WIDTH);
//                floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
//                floorTileDisplay.setRotate(- gameBoard.getTileAt(row, col).getRotation() * 90);
//
//                setFloorTileEventHandlers(floorTileDisplay);
//
//                tileGroup.getChildren().add(floorTileDisplay);
//            }
//        }
//    }
//
//    private void displayPlayerPieces(GameBoard gameBoard) {
//        Image leftTopImage = new Image("leftTop.png");
//        ImageView leftTop = new ImageView(leftTopImage);
//        leftTop.setFitWidth(TILE_WIDTH);
//        leftTop.setFitHeight(TILE_HEIGHT);
//        leftTop.setLayoutX(- TILE_WIDTH);
//        leftTop.setLayoutY(- TILE_HEIGHT);
//        playerPieceGroup.getChildren().add(leftTop);
//
//        Image rightTopImage = new Image("rightTop.png");
//        ImageView rightTop = new ImageView(rightTopImage);
//        rightTop.setFitWidth(TILE_WIDTH);
//        rightTop.setFitHeight(TILE_HEIGHT);
//        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
//        rightTop.setLayoutY(- TILE_HEIGHT);
//        playerPieceGroup.getChildren().add(rightTop);
//
//        Image leftBottomImage = new Image("leftBottom.png");
//        ImageView leftBottom = new ImageView(leftBottomImage);
//        leftBottom.setFitWidth(TILE_WIDTH);
//        leftBottom.setFitHeight(TILE_HEIGHT);
//        leftBottom.setLayoutX(- TILE_WIDTH);
//        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
//        playerPieceGroup.getChildren().add(leftBottom);
//
//        Image rightBottomImage = new Image("rightBottom.png");
//        ImageView rightBottom = new ImageView(rightBottomImage);
//        rightBottom.setFitWidth(TILE_WIDTH);
//        rightBottom.setFitHeight(TILE_HEIGHT);
//        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
//        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
//        playerPieceGroup.getChildren().add(rightBottom);
//
//        for (int i = 0; i < gameBoard.getNumOfPlayerPieces(); i++) {
//            Position playerPiecePosition = gameBoard.getPlayerPiecePosition(i);
//            int row = playerPiecePosition.getRowNum();
//            int col = playerPiecePosition.getColNum();
//
//            Image playerPieceImage = gameBoard.getPlayerPiece(i).getImage();
//            ImageView playerPieceDisplay = new ImageView(playerPieceImage);
//            //TODO Remove magical calculations
//            playerPieceDisplay.setFitWidth(28);
//            playerPieceDisplay.setFitHeight(28);
//            playerPieceDisplay.setLayoutX((col - 1) * TILE_WIDTH + TILE_WIDTH + 6);
//            playerPieceDisplay.setLayoutY((row - 1) * TILE_HEIGHT + TILE_HEIGHT + 6);
//            playerPieceDisplay.toFront();
//            playerPieceDisplay.setId("Col: " + col + ", Row: " + row);
//            playerPieceGroup.getChildren().add(playerPieceDisplay);
//
//            playerPieceDisplay.setOnDragDetected(event -> {
//                Dragboard dragboard = playerPieceDisplay.startDragAndDrop(TransferMode.MOVE);
//                ClipboardContent content = new ClipboardContent();
//                content.putImage(playerPieceDisplay.getImage());
//                dragboard.setContent(content);
//                event.consume();
//            });
//
//            playerPieceDisplay.setOnMouseClicked(event -> {
//                System.out.println("Col: " + getTileCol(playerPieceDisplay));
//                System.out.println("Row: " + getTileRow(playerPieceDisplay));
//            });
//        }
//    }
//
//    public void setEffectBorders() {
//        Image leftTopImage = new Image("leftTop.png");
//        ImageView leftTop = new ImageView(leftTopImage);
//        leftTop.setFitWidth(TILE_WIDTH);
//        leftTop.setFitHeight(TILE_HEIGHT);
//        leftTop.setLayoutX(- TILE_WIDTH);
//        leftTop.setLayoutY(- TILE_HEIGHT);
//        effectGroup.getChildren().add(leftTop);
//
//        Image rightTopImage = new Image("rightTop.png");
//        ImageView rightTop = new ImageView(rightTopImage);
//        rightTop.setFitWidth(TILE_WIDTH);
//        rightTop.setFitHeight(TILE_HEIGHT);
//        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
//        rightTop.setLayoutY(- TILE_HEIGHT);
//        effectGroup.getChildren().add(rightTop);
//
//        Image leftBottomImage = new Image("leftBottom.png");
//        ImageView leftBottom = new ImageView(leftBottomImage);
//        leftBottom.setFitWidth(TILE_WIDTH);
//        leftBottom.setFitHeight(TILE_HEIGHT);
//        leftBottom.setLayoutX(- TILE_WIDTH);
//        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
//        effectGroup.getChildren().add(leftBottom);
//
//        Image rightBottomImage = new Image("rightBottom.png");
//        ImageView rightBottom = new ImageView(rightBottomImage);
//        rightBottom.setFitWidth(TILE_WIDTH);
//        rightBottom.setFitHeight(TILE_HEIGHT);
//        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
//        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
//        effectGroup.getChildren().add(rightBottom);
//    }
//
//    public void displayActionTiles(Player player) {
//        List<ActionTile> playersActionTiles = GameService.getInstance().getPlayerService().getDrawnActionTiles(player);
//        if (!playersActionTiles.isEmpty()) {
//            for (ActionTile actionTile : playersActionTiles) {
//                Image actionTileImage = new Image(getActionTileTypeImage(actionTile));
//                ImageView actionTileDisplay = new ImageView(actionTileImage);
//                drawnActionTiles.getChildren().add(actionTileDisplay);
//            }
//        }
//    }
}