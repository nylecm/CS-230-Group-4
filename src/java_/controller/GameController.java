package java_.controller;


import com.sun.xml.internal.ws.api.FeatureConstructor;
import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.tile.*;
import java_.util.Position;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameController implements Initializable {

    private GameService gameService;

    private GameBoard gameBoard;

    private static final int TILE_WIDTH = 70;

    private  static final int TILE_HEIGHT = 70;

    private static final int PLAYER_PIECE_WIDTH = 28;

    private static final int PLAYER_PIECE_HEIGHT = 28;

    @FXML
    private AnchorPane background;

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
    private AnchorPane playersActionTilesHolder;

    @FXML
    private VBox playerQueue;

    @FXML
    private Button endTurnButton;

    @FXML
    private ComboBox dropDownMenu;

    @FXML
    private Label infoBox;

    @FXML
    private Image floorTileImage = new Image("fullFlat.png");

    @FXML
    private Image edgeTileImage = new Image("emptyFlat.png");

    @FXML
    private Image actionTileImage = new Image("actionTile.png");

    private final String ERROR_SOUND = "src/view/res/sfx/error.mp3";

    private final String LOADING_SOUND = "src/view/res/sfx/load.mp3";

    private final String ICE_ACTION_TILE_SOUND = "src/view/res/sfx/iceActionTileSound.mp3";

    private boolean actionTilePlayed; //TODO Move somewhere else?

    private int numberOfMoves; //TODO Move somewhere else?

    private boolean floorTileInserted;

    //TODO Remove all below / Move somewhere else

    private Tile drawnTile;

    @FXML
    private GridPane mainBox;

    @FXML
    private Group playersActionTiles;

    private final String FLOORTILE_NOT_INSERTED_MSG = "ERROR!\nYOU HAVE TO INSERT FLOOR TILE FIRST!";

    private final String CANT_SET_LOCATION_ON_FIRE_MSG = "ERROR!\nYOU CAN SET FIRE ON LOCATION WITH PLAYERS!";

    private final String ACTIONTILE_ALREADY_USED_MSG = "ERROR!\nYOU CAN USE ONLY ONE ACTION TILE PER ROUND!";

    private final String PLAYEREFFECT_USED_ON_FLOORTILE_MSG = "ERROR!\nYOU CAN'T USE PLAYER EFFECT ON A FLOOR TILE!";

    private final String AREAEFFECT_USED_ON_PLAYER_MSG = "ERROR!\nYOU CAN'T USE AREA EFFECT ON A FLOOR PLAYER!";

    private final String PLAYERPIECE_NOT_MOVED_MSG = "ERROR!\nYOU HAVE TO MOVE YOUR PLAYER PIECE!";

    private final String INVALID_TARGET_MSG = "ERROR!\nTHAT IS NOT A VALID TARGET!";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameService = GameService.getInstance();

        gameBoard = gameService.getGameBoard();

        gameBoardView = new Dimension2D(gameBoard.getnCols(), gameBoard.getnRows());

        actionTilePlayed = false;
        numberOfMoves = 1;
        floorTileInserted = false;

        //TODO Move somewhere else...
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File("src/view/res/img/oberon_from_discord.png").toURI().toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        background.setBackground(new Background(backgroundFill));

        //TODO Replace with isometric view
        displayGameView();

        createDropDownMenu();

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
        animateBackground();

        edgeTileGroup = new Group();
        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();
        playersActionTiles = new Group();

        //Build GameBoard
        displayEdges();
        displayFloorTiles();
        setEffectBorders();
        displayEffects();
        displayPlayerPieces();

        //View GameBoard
//        content = new StackPane();
        content = new StackPane();
        content.getChildren().addAll(tileGroup, effectGroup, edgeTileGroup, playerPieceGroup);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(content);

        //Build Player Queue
        if (playerQueue.getChildren().size() != gameService.getPlayerService().getPlayers().length) {
            for (int i = 0; i < gameService.getPlayerService().getPlayers().length; i++) {
                Image playerPieceImage = gameService.getPlayerService().getPlayer(i).getPlayerPiece().getImage();
                ImageView playerPiecePreview = new ImageView(playerPieceImage);
                playerQueue.getChildren().add(playerPiecePreview);
            }
        }
        displayPlayerQueue();

        drawTile();

        displayActionTiles();
    }

    //TODO Isometric view (don't use)
    private void displayGameBoardIsometric(FloorTile[][] gameBoard) {
        edgeTileGroup = new Group();
        tileGroup = new Group();
        playerPieceGroup = new Group();
        effectGroup = new Group();

        displayEdges();
        displayPlayerPieces();

        for (int row = 0; row < gameBoardView.getWidth(); row++) {
            for (int col = 0; col < gameBoardView.getHeight(); col++) {
                String tileType = getFloorTileTypeImage(gameBoard[row][col]);
                Image tileImage = new Image(tileType);

                ImageView tileDisplay = new ImageView(tileImage);

                tileDisplay.setFitWidth(TILE_WIDTH);
                tileDisplay.setFitHeight(TILE_HEIGHT);
                tileDisplay.setX((col - row) * (TILE_WIDTH / 2));
                tileDisplay.setY((col + row) * (TILE_HEIGHT / 2));

                tileGroup.getChildren().add(tileDisplay);

            }
        }
        content = new StackPane();
        content.getChildren().addAll(tileGroup, edgeTileGroup, effectGroup, playerPieceGroup);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(content);
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

            if (gameBoard.isColumnFixed(getItemCol(edgeTileDisplayTop)) || gameBoard.isColumnFixed(getItemCol(edgeTileDisplayBottom))) {
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

            if (gameBoard.isRowFixed(getItemRow(edgeTileDisplayLeft)) || gameBoard.isRowFixed(getItemRow(edgeTileDisplayRight))) {
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

    private void displayEffects() {
        for (Position position : gameBoard.getPositionsWithActiveEffects()) {
            AreaEffect effect = gameBoard.getEffectAt(position);
            Image effectImage = new Image(getEffectTypeImage(effect.getEffectType()));
            ImageView effectImageView = getTileImageView(effectImage);
            effectImageView.setLayoutX(position.getColNum() * TILE_WIDTH);
            effectImageView.setLayoutY(position.getRowNum() * TILE_HEIGHT);

            effectGroup.getChildren().add(effectImageView);
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
            setPlayerPieceImageViewPosition(playerPieceImageView, row, col);
//            playerPieceImageView.setLayoutX((col - 1) * TILE_WIDTH + TILE_WIDTH + 6);
//            playerPieceImageView.setLayoutY((row - 1) * TILE_HEIGHT + TILE_HEIGHT + 6);
            playerPieceGroup.getChildren().add(playerPieceImageView);

            setPlayerPieceEventHandlers(playerPieceImageView);
        }
    }

    private void setEffectBorders() {
        //TODO Extract, make a loop ------------------------------------
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
        //TODO Extract, make a loop ------------------------------------
    }

    private void displayActionTiles() {
        for (ActionTile actionTile : gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles()) {
            Image actionTileImage = new Image(getActionTileTypeImage(actionTile));
            ImageView actionTileImageView = new ImageView(actionTileImage);

            //TODO Remove
            actionTileImageView.setFitHeight(140);
            actionTileImageView.setFitWidth(114);

            setActionTileEventHandlers(actionTileImageView);

            //Add to GUI
            actionTileImageView.setX(playersActionTiles.getChildren().size() * 50);
            playersActionTiles.getChildren().add(actionTileImageView);
        }
        playersActionTilesHolder.getChildren().clear();
        playersActionTilesHolder.getChildren().add(playersActionTiles);
    }

    private void displayPlayerQueue() {
        double sizeCoefficient = 1.4;
        double opacityCoefficient = 1.0;
        for (Node playerPiecePreview : playerQueue.getChildren()) {
            ((ImageView) playerPiecePreview).setFitHeight(((ImageView) playerPiecePreview).getImage().getHeight() * sizeCoefficient);
            ((ImageView) playerPiecePreview).setFitWidth(((ImageView) playerPiecePreview).getImage().getWidth() * sizeCoefficient);
            playerPiecePreview.setOpacity(opacityCoefficient);
            sizeCoefficient -= 0.3;
            opacityCoefficient -= 0.3;
        }
    }

    private void createDropDownMenu() {
        dropDownMenu.getItems().addAll(
                "Save",
                "Save and Exit",
                "Exit"
        );

        dropDownMenu.setOnAction(event -> {
            if (dropDownMenu.getValue() == "Save") {
                try {
                    gameService.save(gameBoard.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (dropDownMenu.getValue() == "Save and Exit") {
                try {
                    gameService.save(gameBoard.getName());
                    //TODO Change
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //TODO Change
                System.exit(0);
            }
        });
    }

    private void setEdgeTileEventHandlers(ImageView edgeTileImageView) {
        ColorAdjust highlight = new ColorAdjust();

        edgeTileImageView.setOnMouseEntered(event -> {
            highlight.setBrightness(-0.2);
            edgeTileImageView.setEffect(highlight);
        });

        edgeTileImageView.setOnMouseExited(event -> {
            highlight.setBrightness(0);
            edgeTileImageView.setEffect(highlight);
        });

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
                //Top or Bottom edge
                } else if (targetRow == -1 || targetRow == gameBoardView.getHeight()) {
                    slideColTemp(targetCol, targetRow);
                }
                //Update GameBoard
                gameBoard.insert(targetCol, targetRow, (FloorTile) drawnTile, (int) (drawnFloorTile.getRotate() / 90));
                //Remove drawn FloorTile image
                drawnFloorTile.setImage(null);

                floorTileInserted = true;
            }
        });
    }

    private void setFloorTileEventHandlers(ImageView floorTileImageView) {
        ColorAdjust highlight = new ColorAdjust();

        //TODO Remove
        floorTileImageView.setOnMouseClicked(event -> {
            System.out.println("Col: " + getItemCol(floorTileImageView));
            System.out.println("Row: " + getItemRow(floorTileImageView));
            System.out.println("Paths: " + gameBoard.getTileAt(getItemRow(floorTileImageView), getItemCol(floorTileImageView)).getPathsBits());
        });

        floorTileImageView.setOnMouseEntered(event -> {
            highlight.setBrightness(0.2);
            floorTileImageView.setEffect(highlight);
        });

        floorTileImageView.setOnMouseExited(event -> { ;
            highlight.setBrightness(0);
            floorTileImageView.setEffect(highlight);
        });

        floorTileImageView.setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
        });

        floorTileImageView.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            //PlayerPieceImageView dropped
            if (playerPieceGroup.getChildren().contains(source)) {
                movePlayerPieceImageView(source, floorTileImageView);

            //ActionTileImageView dropped
            } else if (playersActionTiles.getChildren().contains(source)) {
                //TODO String not very sensible
                if (getActionTileEffectType(source).equals("AreaEffect") && effectCanBeUsed(source, getItemRow(floorTileImageView), getItemCol(floorTileImageView))) {
                    applyEffectImageView(source, floorTileImageView);
                    removeAreaEffectActionTile(source, getItemCol(floorTileImageView), getItemRow(floorTileImageView));
                    event.setDropCompleted(true);
                } else {
                    displayError(CANT_SET_LOCATION_ON_FIRE_MSG);
                }
                //Is Player Effect ActionTile
            } else {
                displayError(PLAYEREFFECT_USED_ON_FLOORTILE_MSG);
            }
        });

    }

    private void setPlayerPieceEventHandlers(ImageView playerPieceImageView) {
        //TODO Remove
        playerPieceImageView.setOnMouseClicked(event -> {
            System.out.println("Col: " + getItemCol(playerPieceImageView));
            System.out.println("Row: " + getItemRow(playerPieceImageView));
        });

        playerPieceImageView.setOnDragDetected(event -> {
            if (!floorTileInserted && drawnTile instanceof FloorTile) {
                displayError(FLOORTILE_NOT_INSERTED_MSG);
            } else {
                ImageView source = (ImageView) event.getSource();

                if (numberOfMoves > 0 ) {
                    int currentPlayerNum = gameService.getCurrentPlayerNum();
                    int sourcePlayerPieceCol = getItemCol(source);
                    int sourcePlayerPieceRow = getItemRow(source);
                    Position draggedPlayerPiecePosition = new Position(sourcePlayerPieceRow, sourcePlayerPieceCol);

                    //Check if the Player is dragging theirs PlayerPiece
                    if (draggedPlayerPiecePosition.equals(gameService.getGameBoard().getPlayerPiecePosition(currentPlayerNum))) {
                        Dragboard dragboard = playerPieceImageView.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(playerPieceImageView.getImage());
                        dragboard.setContent(content);
                        event.consume();
                    }
                }
            }
        });

        playerPieceImageView.setOnDragOver(event ->  {
            event.acceptTransferModes(TransferMode.ANY);
        });

        playerPieceImageView.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            if (playersActionTiles.getChildren().contains(source)) {
                //TODO String not very sensible
                if (getActionTileEffectType(source).equals("PlayerEffect")) {
                    removePlayerEffectActionTile(source, playerPieceImageView);
                    event.setDropCompleted(true);
                } else {
                    displayError(AREAEFFECT_USED_ON_PLAYER_MSG);
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

    private void setActionTileEventHandlers(ImageView actionTileImageView) {

        actionTileImageView.setOnMouseEntered(event -> {
            actionTileImageView.setY(actionTileImageView.getY() - TILE_HEIGHT / 2);
            actionTileImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.45), 10, 0.1, 10, 35)");
        });

        actionTileImageView.setOnMouseExited(event -> {
            actionTileImageView.setY(actionTileImageView.getY() + TILE_HEIGHT / 2);
            actionTileImageView.setStyle(null);
        });

        actionTileImageView.setOnDragDetected(event -> {
            if (!floorTileInserted && drawnTile instanceof FloorTile) {
                displayError(FLOORTILE_NOT_INSERTED_MSG);
            } else {
                if (!actionTilePlayed) {
                    Dragboard dragboard = actionTileImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(((ImageView) event.getSource()).getImage());
                    dragboard.setContent(content);
                    event.consume();
                } else {
                    displayError(ACTIONTILE_ALREADY_USED_MSG);
                }
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
        List<Node> effectsToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getItemCol((ImageView) t) == col)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getItemCol((ImageView) t) == col)
                .collect(Collectors.toList());

        effectsToMove = effectGroup.getChildren()
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

        for (Node tileWithEffect : effectsToMove) {
            if (row < col) {
                tileWithEffect.setLayoutY(tileWithEffect.getLayoutY() + TILE_HEIGHT);
            } else { //Bottom row
                tileWithEffect.setLayoutY(tileWithEffect.getLayoutY() - TILE_HEIGHT);
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

    //TODO To be hopefully implemented
    private void slideCol(int col, int row) {
        ImageView floorTileDisplay = getTileImageView(floorTileImage);
        floorTileDisplay.setLayoutX(col * TILE_WIDTH);
        floorTileDisplay.setLayoutY(row * TILE_HEIGHT);
        tileGroup.getChildren().add(floorTileDisplay);

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

        List<Node> lastTile = null;

        tileGroup.getChildren().remove(lastTile.get(0));

        double startPosition;
        double endPosition;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        for (Node floorTile : floorTilesToMove) {
            DoubleProperty property = floorTile.translateYProperty();
            if (row < col) {
                startPosition = row;
                endPosition = startPosition + TILE_HEIGHT;
                lastTile = tileGroup.getChildren()
                        .stream()
                        .filter(t -> getItemCol((ImageView) t) == col &&
                                getItemRow((ImageView) t) == gameBoardView.getHeight())
                        .collect(Collectors.toList());
            } else {
                startPosition = row;
                endPosition = startPosition - TILE_HEIGHT;
                lastTile = tileGroup.getChildren()
                        .stream()
                        .filter(t -> getItemCol((ImageView) t) == col &&
                                getItemRow((ImageView) t) == -1)
                        .collect(Collectors.toList());
            }
            timeline.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(property, startPosition)),
                    new KeyFrame(new Duration(1000), new KeyValue(property, endPosition))
            );
        }
        timeline.play();
        Node pushedOfTile = lastTile.get(0);

        timeline.setOnFinished(event -> {
            tileGroup.getChildren().remove(pushedOfTile);
        });
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
        List<Node> effectsToMove;

        floorTilesToMove = tileGroup.getChildren()
                .stream()
                .filter(t -> getItemRow((ImageView) t) == row)
                .collect(Collectors.toList());

        playerPiecesToMove = playerPieceGroup.getChildren()
                .stream()
                .filter(t -> getItemRow((ImageView) t) == row)
                .collect(Collectors.toList());

        effectsToMove = effectGroup.getChildren()
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
            if (playerPiece.getLayoutX() > gameBoardView.getWidth() * TILE_WIDTH) {
                playerPiece.setLayoutX(6);
            }
        }

        for (Node tileWithEffect : effectsToMove) {
            if (col < row) {
                tileWithEffect.setLayoutX(tileWithEffect.getLayoutX() + TILE_WIDTH);
            } else { //Bottom row
                tileWithEffect.setLayoutX(tileWithEffect.getLayoutX() - TILE_WIDTH);
            }
        }
    }

    @FXML
    private void drawTile() {
        drawnTile = gameService.getSilkBag().take();

        if (drawnTile instanceof FloorTile) {
            Image newFloorTileImage = new Image((getFloorTileTypeImage((FloorTile) drawnTile)));
            drawnFloorTile.setImage(newFloorTileImage);
        } else {
            //Add to Player class
            gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).addDrawnActionTile((ActionTile) drawnTile);
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
        endTurnButton.setOnMousePressed(event -> {
            endTurnButton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
        });

        endTurnButton.setOnMouseReleased(event -> {
           endTurnButton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 5, 0.5, 0, 3)");
        });

        if (drawnTile instanceof FloorTile && !floorTileInserted) {
            displayError(FLOORTILE_NOT_INSERTED_MSG);
        } else if (playerPieceCanMove() && numberOfMoves > 0) {
            displayError(PLAYERPIECE_NOT_MOVED_MSG);
        } else {
            ImageView currentPlayerPiecePreview = (ImageView) playerQueue.getChildren().get(0);
            playerQueue.getChildren().remove(0);
            playerQueue.getChildren().add(currentPlayerPiecePreview);
            gameService.nextTurn();
            actionTilePlayed = false;
            numberOfMoves = 1;
            floorTileInserted = false;
            infoBox.setText(null);
            drawnFloorTile.setRotate(0);
            displayGameView();
        }
    }

    @FXML
    private void onDropDownMenuClicked() {

    }

    private void displayError(String errorText) {
        infoBox.setText(errorText);
        playSound(ERROR_SOUND);
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

    private void movePlayerPieceImageView(ImageView playerPieceImageView, ImageView targetFloorTileImageView) {
        int sourceFloorTileCol = getItemCol(playerPieceImageView);
        int sourceFloorTileRow = getItemRow(playerPieceImageView);

        int targetFloorTileCol = getItemCol(targetFloorTileImageView);
        int targetFloorTileRow = getItemRow(targetFloorTileImageView);

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
            setPlayerPieceImageViewPosition(playerPieceImageView, getItemRow(targetFloorTileImageView), getItemCol(targetFloorTileImageView));

            //Fix if left a FloorTile with multiple PlayerPieces
            setPlayerPieceImageViewPosition(null, sourceFloorTileRow, sourceFloorTileCol);

            gameBoard.movePlayerPiece(targetFloorTileRow, targetFloorTileCol);
            numberOfMoves--;


            //Check for win
            //TODO Implement
            if (gameBoard.getTileAt(targetFloorTileRow, targetFloorTileCol).getType() == TileType.GOAL) {
                System.exit(0);
            }
        }
    }

    private void setPlayerPieceImageViewPosition(ImageView playerPieceImageView, int targetRow, int targetCol) {
        List<ImageView> playerPiecesAtFloorTile = new ArrayList<>();
        if (playerPieceImageView != null) {
            playerPiecesAtFloorTile.add(playerPieceImageView);
        }
        for (int i = 0; i < playerPieceGroup.getChildren().size(); i++) {
            ImageView playerPiece = (ImageView) playerPieceGroup.getChildren().get(i);
            if (getItemRow(playerPiece) == targetRow && getItemCol(playerPiece) == targetCol && !playerPiecesAtFloorTile.contains(playerPiece)) {
                playerPiecesAtFloorTile.add(playerPiece);
            }
        }

        for (int i = 0; i < playerPiecesAtFloorTile.size(); i++) {
            playerPiecesAtFloorTile.get(i).setFitHeight(PLAYER_PIECE_HEIGHT / playerPiecesAtFloorTile.size() + 10);
            playerPiecesAtFloorTile.get(i).setFitWidth(PLAYER_PIECE_WIDTH / playerPiecesAtFloorTile.size() + 10);
//            playerPiecesAtFloorTile.get(i).setLayoutX(targetCol * TILE_WIDTH + 12);
//            playerPiecesAtFloorTile.get(i).setLayoutY(targetRow * TILE_HEIGHT + 12);
        }

        //TODO Make loop, see above
        if (playerPiecesAtFloorTile.size() == 4) {
            playerPiecesAtFloorTile.get(0).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 - 15);
            playerPiecesAtFloorTile.get(0).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2 - 15);

            playerPiecesAtFloorTile.get(1).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 15);
            playerPiecesAtFloorTile.get(1).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2 - 15);

            playerPiecesAtFloorTile.get(2).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 - 15);
            playerPiecesAtFloorTile.get(2).setLayoutY(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 15);

            playerPiecesAtFloorTile.get(3).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 15);
            playerPiecesAtFloorTile.get(3).setLayoutY(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 15);

        } else if (playerPiecesAtFloorTile.size() == 3) {
            playerPiecesAtFloorTile.get(0).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 - 20);
            playerPiecesAtFloorTile.get(0).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2);

            playerPiecesAtFloorTile.get(1).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2);
            playerPiecesAtFloorTile.get(1).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2);

            playerPiecesAtFloorTile.get(2).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 20);
            playerPiecesAtFloorTile.get(2).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2);

        } else if (playerPiecesAtFloorTile.size() == 2) {
            playerPiecesAtFloorTile.get(0).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 - 15);
            playerPiecesAtFloorTile.get(0).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2);

            playerPiecesAtFloorTile.get(1).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(1).getFitWidth()) / 2 + 15);
            playerPiecesAtFloorTile.get(1).setLayoutY(targetRow * TILE_HEIGHT + (TILE_HEIGHT - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2);

        } else if (playerPiecesAtFloorTile.size() == 1) {
            playerPiecesAtFloorTile.get(0).setLayoutX(targetCol * TILE_WIDTH + 12);
            playerPiecesAtFloorTile.get(0).setLayoutY(targetRow * TILE_HEIGHT + 12);
        }
    }

    private void applyEffectImageView(ImageView effectImageView, ImageView floorTileImageView) {
        //TODO Get from corresponding class
        int area = 3;

        double centerY = floorTileImageView.getLayoutY();
        double centerX = floorTileImageView.getLayoutX();

        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(effectImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);
        for (int row = 0; row < area; row++) {
            for (int col = 0; col < area; col++) {

                Image effectOverlayImage = new Image(getEffectTypeImage(usedActionTile.use().getEffectType()));
                ImageView effectOverlayImageView = new ImageView(effectOverlayImage);

                effectOverlayImageView.setFitHeight(TILE_HEIGHT);
                effectOverlayImageView.setFitWidth(TILE_WIDTH);
                effectOverlayImageView.setLayoutY(centerY + TILE_HEIGHT - row * TILE_WIDTH);
                effectOverlayImageView.setLayoutX(centerX - TILE_WIDTH + col * TILE_WIDTH);

                //TODO Delete them all together?
                if (getItemCol(effectOverlayImageView) >= gameBoardView.getWidth() || getItemCol(effectOverlayImageView) < 0 || getItemRow(effectOverlayImageView) >= gameBoardView.getHeight() || getItemRow(effectOverlayImageView) < 0) {
                    effectOverlayImageView.setVisible(false);
                }
                setEffectEventHandlers(effectOverlayImageView);
                effectGroup.getChildren().add(effectOverlayImageView);
            }
        }

        //Sound
        if (usedActionTile.use().getEffectType() == EffectType.ICE) {
            playSound(ICE_ACTION_TILE_SOUND);
            playVideo();
        }
    }

    private void removeAreaEffectActionTile(ImageView actionTileImageView, int col, int row) {
        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(actionTileImageView);

        playersActionTiles.getChildren().remove(actionTileImageView);

        //Remove ActionTile from Player class
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);
        gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().remove(usedActionTileIndex);
        actionTilePlayed = true;

        //Apply effect on the GameBoard
        gameBoard.applyEffect((AreaEffect) usedActionTile.use(), new Position(row, col));
    }

    private void removePlayerEffectActionTile(ImageView actionTileImageView, ImageView targetPlayerPieceImageView) {
        boolean success = false;

        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(actionTileImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);

        int currentPlayerNum = gameService.getCurrentPlayerNum();
        Position playerPieceImageViewPosition = new Position(getItemRow(targetPlayerPieceImageView), getItemCol(targetPlayerPieceImageView));

        boolean targetNotSelf = !playerPieceImageViewPosition.equals(gameBoard.getPlayerPiecePosition(currentPlayerNum));

        //TODO Check if used effect on target contain

        if (usedActionTile.use().getEffectType() == EffectType.BACKTRACK && targetNotSelf) {
            Position previousPosition = gameBoard.backtrack(gameBoard.getPlayerPieceByImage(targetPlayerPieceImageView.getImage()), 2);
            setPlayerPieceImageViewPosition(targetPlayerPieceImageView, previousPosition.getRowNum(), previousPosition.getColNum());
            success = true;
        } else if (usedActionTile.use().getEffectType() == EffectType.DOUBLE_MOVE && !targetNotSelf) {
            useDoubleMoveActionTile();
            success = true;
        } else {
            displayError(INVALID_TARGET_MSG);
        }

        if (success) {
            //Remove from GUI
            playersActionTiles.getChildren().remove(actionTileImageView);

            //Remove from Player class
            gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().remove(usedActionTileIndex);
            actionTilePlayed = true;
        }
    }

    private void useDoubleMoveActionTile() {
        numberOfMoves += 1;
    }

    private boolean effectCanBeUsed(ImageView effectImageView, int centerRow, int centerCol) {
        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(effectImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);

        if (usedActionTile.use().getEffectType().equals(EffectType.FIRE)) {
            //TODO extract
            int area = 3;

            for (int row = 0; row < area; row++) {
                for (int col = 0; col < area; col++) {
                    Position positionToCheck = new Position(centerRow + 1 - row, centerCol - 1 + col);
                    if (gameBoard.getPlayerPieceIndexByPosition(positionToCheck) != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void playSound(String soundPath) {
        Media sound = new Media(new File(soundPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    private void playVideo() {
        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("iceEffect.mp4").toExternalForm()));
        MediaView mediaView = new MediaView(player);
        background.getChildren().add(mediaView);
        mediaView.toFront();

        player.play();
    }

    private String getActionTileEffectType(ImageView actionTileImageView) {
        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(actionTileImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);
        return usedActionTile.use() instanceof  AreaEffect ? "AreaEffect" : "PlayerEffect";
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
                return "view/res/img/action_tiles/FireActionTile.png";
            case ICE:
                return "view/res/img/action_tiles/IceActionTile.png";
            case BACKTRACK:
                return "view/res/img/action_tiles/backTrackActionTile.png";
            case DOUBLE_MOVE:
                return "view/res/img/action_tiles/doubleMoveActionTile.png";
        }
        return null;
    }

    private String getEffectTypeImage(EffectType type) {
        switch (type) {
            case FIRE:
                return "fireOverlay.png";
            case ICE:
                return "iceOverlay.png";
        }
        return null;
    }

    private void animateBackground() {
        Random rand = new Random();

        int chance = rand.nextInt(2);

        if (chance == 1) {
            double sizeCoefficient = rand.nextDouble();

            int positionOffset = rand.nextInt(800 - 10) + 10;

            int wait = rand.nextInt(15000);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            Image cometImage = new Image("view/res/img/comet.png");
            ImageView comet = new ImageView(cometImage);
            comet.setFitHeight(comet.getFitHeight() * sizeCoefficient);
            comet.setFitWidth(comet.getFitWidth() * sizeCoefficient);
            comet.setLayoutX(1500 + positionOffset);
            comet.setLayoutY(-200 - positionOffset);
            comet.setRotate(comet.getRotate() + 15);
            DoubleProperty propertyX = comet.layoutXProperty();
            DoubleProperty propertyY = comet.layoutYProperty();
            background.getChildren().add(comet);
            comet.toBack();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(propertyX, 1800 - positionOffset)),
                    new KeyFrame(new Duration(0), new KeyValue(propertyY, -200 - positionOffset)),
                    new KeyFrame(new Duration(wait), new KeyValue(propertyX, 1800 - positionOffset)),
                    new KeyFrame(new Duration(wait), new KeyValue(propertyY, -200 - positionOffset)),
                    new KeyFrame(new Duration(wait + 1000), new KeyValue(propertyX, - positionOffset)),
                    new KeyFrame(new Duration(wait + 1000), new KeyValue(propertyY, 864 - positionOffset))
            );
            timeline.play();
        }
    }
}
