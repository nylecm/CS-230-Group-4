package java_.controller;


import java_.game.controller.CoinHandler;
import java_.game.controller.GameService;
import java_.game.controller.LeaderboardHandler;
import java_.game.player.Player;;
import java_.game.tile.*;
import java_.util.Position;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Works as a controller and view entity between the main game window
 * and related backend classes.
 * Takes care of the moves, animation and effects, and updating the data
 * in those backend classes.
 *
 * @author Matej Hladky
 */
public class GameController implements Initializable {

    /**
     * Reference to the GameService singleton.
     */
    private GameService gameService;

    /**
     * Reference to the current Game Board.
     */
    private GameBoard gameBoard;

    /**
     * The main pane holding all other Nodes. Holds the
     * background image and displays the background
     * animation.
     */
    @FXML
    private AnchorPane background;

    /**
     * Displays the whole Game Board view.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Represents the content of the Game Board in layers of
     * Edge Tiles, Floor Tiles, Effect Overlays and Player Pieces.
     */
    @FXML
    private StackPane content;

    /**
     * Represents the size of the Game Board.
     */
    @FXML
    private Dimension2D gameBoardView;

    /**
     * Represents the empty border tiles for FloorTile insertion.
     */
    @FXML
    private Group edgeTileGroup;

    /**
     * Holds all of the FloorTiles displayed on the Game Board.
     */
    @FXML
    private Group tileGroup;

    /**
     * Holds all of the Player Pieces displayed on
     * the Game Board.
      */
    @FXML
    private Group playerPieceGroup;

    /**
     * Holds the effect textures/overlays applied to
     * individual Floor Tiles.
     */
    @FXML
    private Group effectGroup;

    /**
     * Holds the image of drawn FloorTile
     * that the player inserts.
     */
    @FXML
    private ImageView drawnFloorTile;

    /**
     * Holds Player's drawn Action Tiles.
     */
    @FXML
    private AnchorPane playersActionTilesHolder;

    /**
     * Represents the action tiles player has drawn.
     */
    @FXML
    private Group playersActionTiles;

    /**
     * Shows the order of the players to
     * take turns.
     */
    @FXML
    private VBox playerQueue;

    /** A button to end the current player's
     * turn.
     */
    @FXML
    private Button endTurnButton;

    /**
     * Represents a menu for saving
     * and exiting the current game.
     */
    @FXML
    private ComboBox dropDownMenu;

    /**
     * Displays any error messages related
     * to thge gameplay.
     */
    @FXML
    private Label infoBox;

    /**
     * Represents the actual Tile object player has drawn.
     */
    private Tile drawnTile;

    /**
     * Message displayed when Player has drawn a Floor Tile and hasn't inserted it.
     */
    private final String FLOORTILE_NOT_INSERTED_MSG = "ERROR!\nYOU HAVE TO INSERT FLOOR TILE FIRST!";

    /**
     * Message displayed when Player tries to use Fire Action Tile on a location with players.
     */
    private final String CANT_SET_LOCATION_ON_FIRE_MSG = "ERROR!\nYOU CAN SET FIRE ON LOCATION WITH PLAYERS!";

    /**
     * Message displayed when Player already used an Action Tile during current turn.
     */
    private final String ACTIONTILE_ALREADY_USED_MSG = "ERROR!\nYOU CAN USE ONLY ONE ACTION TILE PER ROUND!";

    /**
     * Message displayed when Player uses a Player Effect Action Tile on a Floor Tile
     */
    private final String PLAYEREFFECT_USED_ON_FLOORTILE_MSG = "ERROR!\nYOU CAN'T USE PLAYER EFFECT ON A FLOOR TILE!";

    /**
     * Message displayed when Player uses an Area Effect
     */
    private final String AREAEFFECT_USED_ON_PLAYER_MSG = "ERROR!\nYOU CAN'T USE AREA EFFECT ACTION TILE ON A PLAYER!";

    /**
     * Message displayed when Player hasn't moved their Player Piece and a move is possible.
     */
    private final String PLAYERPIECE_NOT_MOVED_MSG = "ERROR!\nYOU HAVE TO MOVE YOUR PLAYER PIECE!";

    /**
     * Message displayed when an Action Tile can't be used on a target.
     */
    private final String INVALID_TARGET_MSG = "ERROR!\nTHAT IS NOT A VALID TARGET!";

    /**
     * Sound played together with any error message
     * related to the gameplay.
     */
    private final String ERROR_SOUND = "src/view/res/sfx/error.mp3";

    /**
     * Is set to the corners of the Game Board View to solve
     * any centering issues.
     */
    private final Image CORNER_PLACEHOLDER = new Image("/view/res/img/gui/cornerPlaceholder.png");

    /**
     * The image for edge tiles for insertion.
     */
    private final Image EDGE_TILE = new Image("/view/res/img/gui/edgeTile.png");

    /**
     * The width of the FloorTile displayed on the
     * screen.
     */
    private static final int TILE_WIDTH = 70;

    /**
     * The height of the FloorTile displayed on the
     * screen.
     */
    private  static final int TILE_HEIGHT = 70;

    /**
     * The width of the Player Piece displayed on the screen.
     */
    private static final int PLAYER_PIECE_WIDTH = 28;

    /**
     * The height of the Player Piece displayed on the screen.
     */
    private static final int PLAYER_PIECE_HEIGHT = 28;

    /**
     * The width any drawn Action Tile.
     */
    private static final int ACTION_TILE_WIDTH = 114;

    /**
     * The height any drawn Action Tile.
     */
    private static final int ACTION_TILE_HEIGHT = 145;

    /**
     * The X-axis spacing between action tiles
     */
    private static final int ACTION_TILE_OFFSET = 50;

    /**
     * Represents the default number of available moves
     * for each turn.
     */
    private static final int DEFAULT_NUMBER_OF_MOVES = 1;

    /**
     * The starting value for the size of the current player
     * in the player queue.
     */
    private static final double PLAYER_QUEUE_SIZE_DEFAULT_VALUE = 1.4;

    /**
     * The starting value for the opacity of the current player
     * in the player queue.
     */
    private static final double PLAYER_QUEUE_OPACITY_DEFAULT_VALUE = 1.0;

    /**
     * How much the size between each player piece differs in the player queue.
     */
    private static final double PLAYER_QUEUE_SIZE_STEP = 0.3;

    /**
     * How much the opacity between each player piece differs in the player queue.
     */
    private static final double PLAYER_QUEUE_OPACITY_STEP = 0.3;

    /**
     * The brightness adjustment when hovered over edge tiles.
     */
    private static final double EDGE_TILE_HIGHLIGHT = -0.2;

    /**
     * The brightness adjustment when hovered over floor tiles.
     */
    private static final double FLOOR_TILE_HIGHLIGHT = 0.2;

    /**
     * True if Action Tile was played during the current turn.
     */
    private boolean actionTilePlayed;

    /**
     * Represents the number of available moves for the current turns
     * (used for Double Move as well).
     */
    private int numberOfMoves;

    /**
     * Checks whether player has inserted a Floor Tile.
     */
    private boolean floorTileInserted;

    /**
     * Creates the game window and sets all things needed using other methods.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameService = GameService.getInstance();

        gameBoard = gameService.getGameBoard();

        gameBoardView = new Dimension2D(gameBoard.getnCols(), gameBoard.getnRows());

        //Sets the default values
        actionTilePlayed = false;
        numberOfMoves = DEFAULT_NUMBER_OF_MOVES;
        floorTileInserted = false;

        //TODO Move somewhere else
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

        //Handler for dragging the drawn FloorTile and inserting it
        drawnFloorTile.setOnDragDetected(event -> {
            if ( drawnFloorTile.getImage() != null) {
                Dragboard dragboard = drawnFloorTile.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(drawnFloorTile.getImage());
                dragboard.setContent(content);
            }
        });
    }

    /**
     * Displays the main Game Board View
     */
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

    /**
     * Creates the isometric view of the Game Board.
     * Not functional.
     * @param gameBoard
     */
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

    /**
     * Displays the edges for insertion.
     */
    //TODO Remake, loop
    private void displayEdges() {
        ImageView edgeTileDisplayTop;
        ImageView edgeTileDisplayBottom;
        ImageView edgeTileDisplayLeft;
        ImageView edgeTileDisplayRight;

        //Sets the top and bottom edges
        for (int i = 0; i < gameBoardView.getWidth(); i++) {
            edgeTileDisplayTop = getTileImageView(EDGE_TILE);
            edgeTileDisplayBottom = getTileImageView(EDGE_TILE);

            edgeTileDisplayTop.setLayoutY(- TILE_HEIGHT);
            edgeTileDisplayTop.setLayoutX(i * TILE_WIDTH);


            edgeTileDisplayBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
            edgeTileDisplayBottom.setLayoutX(i * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayTop);
            edgeTileGroup.getChildren().add(edgeTileDisplayBottom);

            setEdgeTileEventHandlers(edgeTileDisplayTop);
            setEdgeTileEventHandlers(edgeTileDisplayBottom);

            if (gameBoard.isColumnFixed(getItemCol(edgeTileDisplayTop)) || gameBoard.isColumnFixed(getItemCol(edgeTileDisplayBottom))) {
                edgeTileDisplayBottom.setOpacity(0.001);
                edgeTileDisplayTop.setOpacity(0.001);
            }
        }

        for (int i = 0; i < gameBoardView.getHeight(); i++) {
            edgeTileDisplayLeft = getTileImageView(EDGE_TILE);
            edgeTileDisplayRight = getTileImageView(EDGE_TILE);

            edgeTileDisplayLeft.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayLeft.setLayoutX(- TILE_WIDTH);

            edgeTileDisplayRight.setLayoutY(i * TILE_HEIGHT);
            edgeTileDisplayRight.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);

            edgeTileGroup.getChildren().add(edgeTileDisplayLeft);
            edgeTileGroup.getChildren().add(edgeTileDisplayRight);

            setEdgeTileEventHandlers(edgeTileDisplayLeft);
            setEdgeTileEventHandlers(edgeTileDisplayRight);

            if (gameBoard.isRowFixed(getItemRow(edgeTileDisplayLeft)) || gameBoard.isRowFixed(getItemRow(edgeTileDisplayRight))) {
                edgeTileDisplayLeft.setOpacity(0.001);
                edgeTileDisplayRight.setOpacity(0.001);
            }
        }
    }

    /**
     * Creates the actual GameBoard by reading the
     * FloorTiles from the Gameboard class
     */
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

    /**
     * Displays any effect overlays above the Floor Tiles by
     * reading the Active Effects in the Gameboard class
     */
    private void displayEffects() {
        for (Position position : gameBoard.getPositionsWithActiveEffects()) {
            AreaEffect effect = gameBoard.getEffectAt(position);
            Image effectImage = new Image(getEffectTypeImage(effect.getEffectType()));
            ImageView effectImageView = getTileImageView(effectImage);
            effectImageView.setLayoutX(position.getColNum() * TILE_WIDTH);
            effectImageView.setLayoutY(position.getRowNum() * TILE_HEIGHT);
            setEffectEventHandlers(effectImageView);
            effectGroup.getChildren().add(effectImageView);
        }
    }

    /**
     * Display the Player Pieces on their correct position
     * by reading the positions in the Gameboard class.
     */
    private void displayPlayerPieces() {
        //Sets the corner images to solve any centering issues
        //TODO Extract, make a loop ------------------------------
        ImageView leftTop = new ImageView(CORNER_PLACEHOLDER);
        leftTop.setFitWidth(TILE_WIDTH);
        leftTop.setFitHeight(TILE_HEIGHT);
        leftTop.setLayoutX(- TILE_WIDTH);
        leftTop.setLayoutY(- TILE_HEIGHT);
        playerPieceGroup.getChildren().add(leftTop);

        ImageView rightTop = new ImageView(CORNER_PLACEHOLDER);
        rightTop.setFitWidth(TILE_WIDTH);
        rightTop.setFitHeight(TILE_HEIGHT);
        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightTop.setLayoutY(- TILE_HEIGHT);
        playerPieceGroup.getChildren().add(rightTop);

        ImageView leftBottom = new ImageView(CORNER_PLACEHOLDER);
        leftBottom.setFitWidth(TILE_WIDTH);
        leftBottom.setFitHeight(TILE_HEIGHT);
        leftBottom.setLayoutX(- TILE_WIDTH);
        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        playerPieceGroup.getChildren().add(leftBottom);

        ImageView rightBottom = new ImageView(CORNER_PLACEHOLDER);
        rightBottom.setFitWidth(TILE_WIDTH);
        rightBottom.setFitHeight(TILE_HEIGHT);
        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        playerPieceGroup.getChildren().add(rightBottom);
        //TODO Extract, make a loop ------------------------------------

        //Reads the game board and puts the Player Pieces on the Game Board
        for (int i = 0; i < gameBoard.getNumOfPlayerPieces(); i++) {
            int row = gameBoard.getPlayerPiecePosition(i).getRowNum();
            int col = gameBoard.getPlayerPiecePosition(i).getColNum();
            ImageView playerPieceImageView = getPlayerPieceImageView(gameBoard.getPlayerPiece(i).getImage());
            setPlayerPieceImageViewPosition(playerPieceImageView, row, col);
            playerPieceGroup.getChildren().add(playerPieceImageView);
            setPlayerPieceEventHandlers(playerPieceImageView);
        }
    }

    /**
     * Sets the invisible corners of the Game Board
     * to solve any centering issues for effect overlays.
     */
    private void setEffectBorders() {
        //TODO Extract, make a loop ------------------------------------
        ImageView leftTop = new ImageView(CORNER_PLACEHOLDER);
        leftTop.setFitWidth(TILE_WIDTH);
        leftTop.setFitHeight(TILE_HEIGHT);
        leftTop.setLayoutX(- TILE_WIDTH);
        leftTop.setLayoutY(- TILE_HEIGHT);
        effectGroup.getChildren().add(leftTop);

        ImageView rightTop = new ImageView(CORNER_PLACEHOLDER);
        rightTop.setFitWidth(TILE_WIDTH);
        rightTop.setFitHeight(TILE_HEIGHT);
        rightTop.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightTop.setLayoutY(- TILE_HEIGHT);
        effectGroup.getChildren().add(rightTop);

        ImageView leftBottom = new ImageView(CORNER_PLACEHOLDER);
        leftBottom.setFitWidth(TILE_WIDTH);
        leftBottom.setFitHeight(TILE_HEIGHT);
        leftBottom.setLayoutX(- TILE_WIDTH);
        leftBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        effectGroup.getChildren().add(leftBottom);

        ImageView rightBottom = new ImageView(CORNER_PLACEHOLDER);
        rightBottom.setFitWidth(TILE_WIDTH);
        rightBottom.setFitHeight(TILE_HEIGHT);
        rightBottom.setLayoutX(gameBoardView.getWidth() * TILE_WIDTH);
        rightBottom.setLayoutY(gameBoardView.getHeight() * TILE_HEIGHT);
        effectGroup.getChildren().add(rightBottom);
        //TODO Extract, make a loop ------------------------------------
    }

    /**
     * Loads the current player's drawn Action Tiles
     */
    private void displayActionTiles() {
        for (ActionTile actionTile : gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles()) {
            Image actionTileImage = new Image(getActionTileTypeImage(actionTile));
            ImageView actionTileImageView = new ImageView(actionTileImage);
            actionTileImageView.setFitHeight(ACTION_TILE_HEIGHT);
            actionTileImageView.setFitWidth(ACTION_TILE_WIDTH);
            setActionTileEventHandlers(actionTileImageView);

            //Add to GUI
            actionTileImageView.setX(playersActionTiles.getChildren().size() * ACTION_TILE_OFFSET);
            playersActionTiles.getChildren().add(actionTileImageView);
        }
        //Delete current Action Tiles
        playersActionTilesHolder.getChildren().clear();
        playersActionTilesHolder.getChildren().add(playersActionTiles);
    }

    /**
     * Display the Player Queue
     * (turn order).
     */
    private void displayPlayerQueue() {
        double sizeCoefficient = PLAYER_QUEUE_SIZE_DEFAULT_VALUE;
        double opacityCoefficient = PLAYER_QUEUE_OPACITY_DEFAULT_VALUE;
        for (Node playerPiecePreview : playerQueue.getChildren()) {
            ((ImageView) playerPiecePreview).setFitHeight(((ImageView) playerPiecePreview).getImage().getHeight() * sizeCoefficient);
            ((ImageView) playerPiecePreview).setFitWidth(((ImageView) playerPiecePreview).getImage().getWidth() * sizeCoefficient);
            playerPiecePreview.setOpacity(opacityCoefficient);
            sizeCoefficient -= PLAYER_QUEUE_SIZE_STEP;
            opacityCoefficient -= PLAYER_QUEUE_OPACITY_STEP;
        }
    }

    /**
     * Creates a drop down menu for Saving and
     * Exiting the game.
     */
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

    /**
     * Sets all of the event handlers for every edge tile.
     * @param edgeTileImageView - the edge tile
     */
    private void setEdgeTileEventHandlers(ImageView edgeTileImageView) {
        ColorAdjust highlight = new ColorAdjust();

        edgeTileImageView.setOnMouseEntered(event -> {
            highlight.setBrightness(EDGE_TILE_HIGHLIGHT);
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
                drawnFloorTile.setRotate(0);
                drawnFloorTile.setImage(null);

                floorTileInserted = true;
            }
        });
    }

    /**
     * Sets all of the event handlers for every floor tile.
     * @param floorTileImageView - the floor tile
     */
    private void setFloorTileEventHandlers(ImageView floorTileImageView) {
        ColorAdjust highlight = new ColorAdjust();

        floorTileImageView.setOnMouseEntered(event -> {
            highlight.setBrightness(FLOOR_TILE_HIGHLIGHT);
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
                movePlayerPieceImageView(source, floorTileImageView, event);

                //ActionTileImageView dropped
            } else if (playersActionTiles.getChildren().contains(source)) {
                //TODO String not very sensible
                if (getActionTileEffectType(source).equals("AreaEffect")) {
                    if (effectCanBeUsed(source, getItemRow(floorTileImageView), getItemCol(floorTileImageView))) {
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
            }
        });

    }

    /**
     * Sets all of the event handlers for every player piece.
     * @param playerPieceImageView - the player piece
     */
    private void setPlayerPieceEventHandlers(ImageView playerPieceImageView) {
        playerPieceImageView.setOnDragDetected(event -> {
            if (!floorTileInserted && drawnTile instanceof FloorTile) {
                displayError(FLOORTILE_NOT_INSERTED_MSG);
            } else {
                if (numberOfMoves > 0 ) {
                    int draggedPlayerPieceIndex = gameBoard.getPlayerByPlayerPieceImage(playerPieceImageView.getImage());

                    System.out.println(draggedPlayerPieceIndex);
                    System.out.println(gameService.getCurrentPlayerNum());
                    //Check if the Player is dragging theirs PlayerPiece
                    if (draggedPlayerPieceIndex == gameService.getCurrentPlayerNum()) {
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
                } else {
                    displayError(AREAEFFECT_USED_ON_PLAYER_MSG);
                }
            }
        });
    }

    /**
     * Sets all of the event handlers for every effect overlay.
     * @param effectImageView - the effect overlay.
     */
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

                movePlayerPieceImageView(source, (ImageView) targetFloorTile, event);
            }
        });
    }

    /**
     * Sets all of the event handlers for every drawn action tile.
     * @param actionTileImageView - the action tile.
     */
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

    /**
     * Slides all elements in the given column after insertion
     * @param col - the column
     * @param row - row the floor tile was inserted in (bottom/top)
     */
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

        List<Node> playerPiecesOutOfBounds = new ArrayList<>();
        for (Node playerPiece : playerPiecesToMove) {
            if (row < col) {
                playerPiece.setLayoutY(playerPiece.getLayoutY() + TILE_HEIGHT);
            } else { //Bottom row
                playerPiece.setLayoutY(playerPiece.getLayoutY() - TILE_HEIGHT);
            }

            //Return back if yeeted out
            if (playerPiece.getLayoutY() < 0) {
                playerPiece.setLayoutY((gameBoardView.getHeight() - 1) * TILE_HEIGHT );
                playerPiecesOutOfBounds.add(playerPiece);
            }
            if (playerPiece.getLayoutY() > gameBoardView.getHeight() * TILE_HEIGHT) {
                playerPiece.setLayoutY(0);
                playerPiecesOutOfBounds.add(playerPiece);
            }
        }
        if (!playerPiecesOutOfBounds.isEmpty()){
            setPlayerPieceImageViewPosition((ImageView) playerPiecesOutOfBounds.get(0), getItemRow((ImageView) playerPiecesOutOfBounds.get(0)), getItemCol((ImageView) playerPiecesOutOfBounds.get(0)));
        }

        for (Node tileWithEffect : effectsToMove) {
            if (row < col) {
                tileWithEffect.setLayoutY(tileWithEffect.getLayoutY() + TILE_HEIGHT);
            } else { //Bottom row
                tileWithEffect.setLayoutY(tileWithEffect.getLayoutY() - TILE_HEIGHT);
            }
        }

        //TODO Don't iterate using X streams
        List<Node> lastTile;
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

    /**
     * Slides all elements in the given row after insertion
     * @param row - the row
     * @param col - row the floor tile was inserted in (left/right)
     */
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

        //TODO Don't iterate using X streams
        List<Node> lastTile;
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

        List<Node> playerPiecesOutOfBounds = new ArrayList<>();
        for (Node playerPiece : playerPiecesToMove) {
            if (col < row) {
                playerPiece.setLayoutX(playerPiece.getLayoutX() + TILE_WIDTH);
            } else { //Bottom row
                playerPiece.setLayoutX(playerPiece.getLayoutX() - TILE_WIDTH);
            }
            //Return back if yeeted out
            if (playerPiece.getLayoutX() < 0) {
                playerPiece.setLayoutX((gameBoardView.getWidth() - 1) * TILE_WIDTH);
                playerPiecesOutOfBounds.add(playerPiece);
            }
            if (playerPiece.getLayoutX() > gameBoardView.getWidth() * TILE_WIDTH) {
                playerPiece.setLayoutX(0);
                playerPiecesOutOfBounds.add(playerPiece);
            }
        }
        if (!playerPiecesOutOfBounds.isEmpty()){
            setPlayerPieceImageViewPosition((ImageView) playerPiecesOutOfBounds.get(0), getItemRow((ImageView) playerPiecesOutOfBounds.get(0)), getItemCol((ImageView) playerPiecesOutOfBounds.get(0)));
        }

        for (Node tileWithEffect : effectsToMove) {
            if (col < row) {
                tileWithEffect.setLayoutX(tileWithEffect.getLayoutX() + TILE_WIDTH);
            } else { //Bottom row
                tileWithEffect.setLayoutX(tileWithEffect.getLayoutX() - TILE_WIDTH);
            }
        }
    }

    /**
     * Draws a tile from the silk bag.
     */
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

    /**
     * Event handler for rotating the drawn floor tile
     * in a clockwise direction.
     */
    @FXML
    public void onRotateClockwiseButtonClicked() {
        drawnFloorTile.setRotate(drawnFloorTile.getRotate() + 90);
    }

    /**
     * Event handler for rotating the drawn floor tile
     * in a anticlockwise direction.
     */
    @FXML
    public void onRotateAntiClockwiseButtonClicked() {
        drawnFloorTile.setRotate(drawnFloorTile.getRotate() - 90);
    }

    /**
     * Event handler for ending the turn (if possible).
     */
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
            numberOfMoves = DEFAULT_NUMBER_OF_MOVES;
            floorTileInserted = false;
            infoBox.setText(null);
            displayGameView();
        }
    }

    private void displayError(String errorText) {
        infoBox.setText(errorText);
        playSound(ERROR_SOUND);
    }

    /**
     * Checks whether a player piece can move, in other words,
     * if there is a possible move the player can make.
     * @return true if there is a possible move,
     * false otherwise.
     */
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
        if (currentPlayerPieceRow != gameBoardView.getHeight() - 1) {
            if (gameBoard.validateMove(currentPlayerPieceCol, currentPlayerPieceRow, currentPlayerPieceCol, currentPlayerPieceRow + 1, FloorTile.SOUTH_PATH_MASK, FloorTile.NORTH_PATH_MASK)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Moves the player piece in the GUI and checks for win.
     * @param playerPieceImageView - the player piece to move
     * @param targetFloorTileImageView - the target floor tile
     * @param event - the source event handler
     */
    private void movePlayerPieceImageView(ImageView playerPieceImageView, ImageView targetFloorTileImageView, Event event) {
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
            if (gameBoard.getTileAt(targetFloorTileRow, targetFloorTileCol).getType() == TileType.GOAL) {
                try {
                    LeaderboardHandler.updateLeaderboard(gameService.getPlayerService().getPlayers(), gameBoard.getPlayerByPlayerPieceImage(playerPieceImageView.getImage()));
                    CoinHandler.updateCoins(gameService.getPlayerService().getPlayers(), gameBoard.getPlayerByPlayerPieceImage(playerPieceImageView.getImage()));
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Pane game = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/GameWon.fxml"));
                    currentStage.setScene(new Scene(game));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets player piece position within a floor tile and
     * fixes the positioning for more player pieces on the
     * same floor tile.
     * @param playerPieceImageView - the checked player piece
     * @param targetRow - the row of the target floor tile
     * @param targetCol - the column of the target floor tile
     */
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
            playerPiecesAtFloorTile.get(2).setLayoutY(targetRow * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2 + 15);

            playerPiecesAtFloorTile.get(3).setLayoutX(targetCol * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitWidth()) / 2 + 15);
            playerPiecesAtFloorTile.get(3).setLayoutY(targetRow * TILE_WIDTH + (TILE_WIDTH - playerPiecesAtFloorTile.get(0).getFitHeight()) / 2 + 15);

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

    /**
     * Displays an effect overlay.
     * @param effectImageView - the effect overlay to display
     * @param floorTileImageView - the target floor tile
     */
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
    }

    /**
     * Removes an area effect overlay from the GUI and the GameBoard class.
     * @param actionTileImageView - the action tile used
     * @param col - the target's column
     * @param row - the target's row
     */
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

    /**
     * Removes a player effect overlay from the GUI and the GameBoard class.
     * @param actionTileImageView - the action tile used
     * @param targetPlayerPieceImageView - the target player piece
     */
    private void removePlayerEffectActionTile(ImageView actionTileImageView, ImageView targetPlayerPieceImageView) {
        boolean success = false;

        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(actionTileImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);

        int currentPlayerNum = gameService.getCurrentPlayerNum();
        Position playerPieceImageViewPosition = new Position(getItemRow(targetPlayerPieceImageView), getItemCol(targetPlayerPieceImageView));

        boolean targetNotSelf = !playerPieceImageViewPosition.equals(gameBoard.getPlayerPiecePosition(currentPlayerNum));

        Player targetPlayerPieceOwner = gameService.getPlayerService().getPlayer(gameBoard.getPlayerByPlayerPieceImage(targetPlayerPieceImageView.getImage()));
        int playerPieceOwnerIndex = gameService.getPlayerService().getPlayerIndex(targetPlayerPieceOwner);

        if (usedActionTile.use().getEffectType() == EffectType.BACKTRACK && targetNotSelf && gameBoard.isBacktrackPossible(playerPieceOwnerIndex)) {
            Position previousPosition = gameBoard.backtrack(playerPieceOwnerIndex, 2);
            setPlayerPieceImageViewPosition(targetPlayerPieceImageView, previousPosition.getRowNum(), previousPosition.getColNum());
            success = true;
        } else if (usedActionTile.use().getEffectType() == EffectType.DOUBLE_MOVE && !targetNotSelf) {
            useDoubleMoveActionTile();
            success = true;
        }

        if (success) {
            //Remove from GUI
            playersActionTiles.getChildren().remove(actionTileImageView);

            //Update Player class
            gameService.getPlayerService().addPreviouslyAppliedEffect(targetPlayerPieceOwner, usedActionTile.use().getEffectType());
            //Remove from Player class
            gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().remove(usedActionTileIndex);
            actionTilePlayed = true;
        } else {
            displayError(INVALID_TARGET_MSG);
        }
    }

    /**
     * Increases the number of available moves after using
     * the Double Move Action Tile.
     */
    private void useDoubleMoveActionTile() {
        numberOfMoves += 1;
    }

    /**
     * Checkes whether player can use an Area Effect Action Tile
     * @param effectImageView - the used effect
     * @param centerRow - the center floor tile row
     * @param centerCol - the center floor tile column
     * @return true if the effect can be used,
     * false otherwise
     */
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

    /**
     * Plays a given sound.
     * @param soundPath - the URL to the sound file
     */
    private void playSound(String soundPath) {
        Media sound = new Media(new File(soundPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    /**
     * Returns the type of an Action Tile.
     * @param actionTileImageView - the action tile
     * @return "AreaEffect" for an Area Effect,
     * "PlayerEffect for a Player Effect.
     */
    private String getActionTileEffectType(ImageView actionTileImageView) {
        int usedActionTileIndex = playersActionTiles.getChildren().indexOf(actionTileImageView);
        ActionTile usedActionTile = gameService.getPlayerService().getPlayer(gameService.getCurrentPlayerNum()).getDrawnActionTiles().get(usedActionTileIndex);
        return usedActionTile.use() instanceof  AreaEffect ? "AreaEffect" : "PlayerEffect";
    }

    /**
     * Returns a constructed ImageView from a
     * tile image.
     * @param tileImage - the image
     * @return the constructed ImageView
     */
    private ImageView getTileImageView(Image tileImage) {
        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setFitWidth(TILE_WIDTH);
        tileImageView.setFitHeight(TILE_HEIGHT);
        return tileImageView;
    }

    /**
     * Returns a constructed ImageView from a
     * player piece image.
     * @param playerPieceImage - the image
     * @return the constructed ImageView
     */
    private ImageView getPlayerPieceImageView(Image playerPieceImage) {
        //TODO Remove magical numbers
        ImageView playerPieceImageView = new ImageView(playerPieceImage);
        playerPieceImageView.setFitWidth(28);
        playerPieceImageView.setFitHeight(28);
        return playerPieceImageView;
    }

    /**
     * Returns the column of an element on the Game Board.
     * @param tileImageView - the element
     * @return the column of the element
     */
    private int getItemCol(ImageView tileImageView) {
        return (int) (tileImageView.getLayoutX() / TILE_WIDTH);
    }

    /**
     * Returns the row of an element on the Game Board.
     * @param tileImageView - the element
     * @return the row of the element
     */
    private int getItemRow(ImageView tileImageView) {
        return (int) (tileImageView.getLayoutY() / TILE_HEIGHT);
    }

    /**
     * Returns the image path for a given FloorTile
     * @param floorTile - the floor tile
     * @return the image path, null if not found
     */
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

    /**
     * Returns the image path for a given ActionTile
     * @param actionTile - the action tile
     * @return the image path, null if not found
     */
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

    /**
     * Returns the image path for a given EffectType
     * @param type - the effect type
     * @return the image path, null if not found
     */
    private String getEffectTypeImage(EffectType type) {
        switch (type) {
            case FIRE:
                return "fireOverlay.png";
            case ICE:
                return "iceOverlay.png";
        }
        return null;
    }


    /**
     * Creates a random animation in the background.
     */
    //TODO Turn into constants
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
