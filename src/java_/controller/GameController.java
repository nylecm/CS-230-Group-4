package java_.controller;

import java_.game.controller.GameService;
import java_.game.player.PlayerPiece;
import java_.game.tile.*;
import java_.util.Position;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private static final int TILE_WIDTH = 40;

    private static final int TILE_HEIGHT = 40;

    private static final int BORDER_OFFSET = 2;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label positionLabel;

    @FXML
    private ImageView floorTile;

    @FXML
    private Button drawPlayerPieceButton;

    private Dimension2D gameBoardView;

    private Group tiles = new Group();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameService gameService = GameService.getInstance();
        gameBoardView = new Dimension2D(8, 8); //TODO: Change for rectangle

        displayGameBoardFlat();
        floorTile.setOnDragDetected(event -> {
            Dragboard dragboard = floorTile.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(floorTile.getImage());
            dragboard.setContent(content);
            event.consume();
        });
    }

    //TEST ONLY
    private void displayGameBoardFlat() {
        for (int row = 0; row < gameBoardView.getWidth(); row++) {
            for (int col = 0; col < gameBoardView.getHeight(); col++) {

                Image tileImage = new Image("fullFlat.png");
                if (row == 0 || col == 0 || row == gameBoardView.getWidth() - 1 || col == gameBoardView.getHeight() - 1) {
                    if ((row == 0 && col == 0) || (row == 0 && col == gameBoardView.getHeight() - 1) || (row == gameBoardView.getWidth() - 1 && col == 0) || (row == gameBoardView.getWidth() - 1 && col == gameBoardView.getHeight() - 1) ) {
                        tileImage = null;
                    } else {
                        tileImage = new Image("emptyFlat.png");
                    }
                }

                ImageView tileDisplay = new ImageView(tileImage);
                tileDisplay.setFitWidth(TILE_WIDTH);
                tileDisplay.setFitHeight(TILE_HEIGHT);
                tileDisplay.setX((col) * TILE_WIDTH);
                tileDisplay.setY((row) * TILE_HEIGHT);

                tiles.getChildren().add(tileDisplay);

                String tilePosition = "Tile position: Col = " + col + ", Row = " + row;
                ColorAdjust highlight = new ColorAdjust();

                tileDisplay.setOnMouseEntered(event -> {
                    positionLabel.setText(tilePosition);
                    highlight.setBrightness(0.2);
                    tileDisplay.setEffect(highlight);
                    event.consume();
                });

                tileDisplay.setOnMouseExited(event -> {
                    highlight.setBrightness(0);
                    tileDisplay.setEffect(highlight);
                    event.consume();
                });

                tileDisplay.setOnDragEntered(event -> {
                    highlight.setBrightness(0.7);
                    tileDisplay.setEffect(highlight);
                    event.consume();
                    double x = tileDisplay.getX();
                    double y = tileDisplay.getY();
                    double tileCol = x / TILE_WIDTH;
                    double tileRow = y / TILE_HEIGHT;
                    if (tileCol == 0 || tileCol == gameBoardView.getWidth() - 1 || tileRow == 0 || tileRow == gameBoardView.getHeight() - 1) {
                        tileDisplay.setImage(new Image("activeFlat.png"));
                    }
                });

                tileDisplay.setOnDragExited(event -> {
                    highlight.setBrightness(0);
                    tileDisplay.setEffect(highlight);
                    event.consume();
                    double x = tileDisplay.getX();
                    double y = tileDisplay.getY();
                    double tileCol = x / TILE_WIDTH;
                    double tileRow = y / TILE_HEIGHT;
                    if (tileCol == 0 || tileCol == gameBoardView.getWidth() - 1 || tileRow == 0 || tileRow == gameBoardView.getHeight() - 1) {
                        tileDisplay.setImage(new Image("emptyFlat.png"));
                    }
                });

                tileDisplay.setOnDragOver(event -> {
                    event.acceptTransferModes(TransferMode.ANY);
                });

                //TODO: Duplicity, methods etc
                tileDisplay.setOnDragDropped(event -> {
                    ImageView source = (ImageView) event.getGestureSource();
                    if (source.getId().equals(floorTile.getId())) {
                        tileDisplay.setImage(event.getDragboard().getImage());
                        int tileCol = (int) (tileDisplay.getX() / TILE_WIDTH);
                        int tileRow = (int) (tileDisplay.getY() / TILE_HEIGHT);
                        ImageView emptyTile = new ImageView(new Image("fullFlat.png"));
                        emptyTile.setFitWidth(TILE_WIDTH);
                        emptyTile.setFitHeight(TILE_HEIGHT);
                        emptyTile.setX((tileCol) * TILE_WIDTH);
                        emptyTile.setY((tileRow) * TILE_HEIGHT);
                        tiles.getChildren().add(emptyTile);
                        if (tileRow == 0 || tileRow == gameBoardView.getHeight() - 1) {
                            slideCol(tileCol, tileRow);
                        } else if (tileCol == 0 || tileCol == gameBoardView.getWidth() - 1) {
                            slideRow(tileRow, tileCol);
                        }
                    } else { //PlayerPiece
                        int offsetX = (int) ((TILE_WIDTH - source.getFitWidth()) / 2);
                        int offsetY = (int) ((TILE_HEIGHT - source.getFitHeight()) / 2);
                        source.setX(tileDisplay.getX() + offsetX);
                        source.setY(tileDisplay.getY() + offsetY);
                    }
                });
            }
        }
        StackPane gameBoardViewHolder = new StackPane(tiles);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(gameBoardViewHolder);
    }

    //TODO: Sliding using just one function
    private void slide(int col, int row) {
        List<Node> tiles = new ArrayList<>();
        double startValue = 0;
        double endValue = 0;
        DoubleProperty property = null;
        for (Node tile : this.tiles.getChildren()) {
            double x = ((ImageView) tile).getX();
            double y = ((ImageView) tile).getY();
            double tileCol = x / TILE_WIDTH;
            double tileRow = y / TILE_HEIGHT;
            if (col == 0) { //Left to right
                if (tileRow == row) {
                    tiles.add(tile);
                }
            } else if (col == gameBoardView.getWidth() - 1) { //Right to left
                if (tileRow == row) {
                    tiles.add(tile);
                }
            } else if (row == 0) { //Down
                if (tileCol == col) {
                    tiles.add(tile);
                }
            } else if (row == gameBoardView.getHeight() -1) { //Up
                if (tileCol == col) {
                    tiles.add(tile);
                }
            }
        }
        Timeline t = new Timeline();
        t.setCycleCount(1);
        for (Node tile : tiles) {
            t.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(property, startValue)),
                    new KeyFrame(new Duration(1000), new KeyValue(property, endValue))
            );
        }
        t.play();
    }

    private void slideCol(int col, int row) {
        List<Node> tiles = new ArrayList<>();
        double startValue;
        double endValue;
        boolean down = false;
        for (Node tile : this.tiles.getChildren()) {
            double x = ((ImageView) tile).getX();
            double y = ((ImageView) tile).getY();
            double tileCol = x / TILE_WIDTH;
            double tileRow = y / TILE_HEIGHT;
            if (tileCol == col && tileRow != 0 && tileRow != gameBoardView.getHeight() - 1) {
                tiles.add(tile);
            }
        }
        Timeline t = new Timeline();
        t.setCycleCount(1);
        Node lastTile = null;
        for (Node tile : tiles) {
            DoubleProperty property = tile.layoutYProperty();
            if (row < col) {
                startValue = row * TILE_HEIGHT;
                endValue = startValue + TILE_HEIGHT;
                lastTile = tiles.get(tiles.size() - 1);
            } else {
                startValue = row / TILE_HEIGHT;
                endValue = startValue - TILE_HEIGHT;
                lastTile = tiles.get(0);
            }
            t.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(property, startValue)),
                    new KeyFrame(new Duration(1000), new KeyValue(property, endValue))
            );
        }
        t.play();
        Node yeetedOutTile = lastTile;
        t.setOnFinished(event -> {
            this.tiles.getChildren().remove(yeetedOutTile);
        });
    }

    private void slideRow(int row, int col) {
        List<Node> tiles = new ArrayList<>();
        double startValue;
        double endValue;
        for (Node tile : this.tiles.getChildren()) {
            double x = ((ImageView) tile).getX();
            double y = ((ImageView) tile).getY();
            double tileCol = x / TILE_WIDTH;
            double tileRow = y / TILE_HEIGHT;
            if (tileRow == row && tileCol != 0 && tileCol != gameBoardView.getWidth() - 1) {
                tiles.add(tile);
            }

        }
        Timeline t = new Timeline();
        t.setCycleCount(1);
        Node lastTile = null;
        for (Node tile : tiles) {
            DoubleProperty property = tile.layoutXProperty();
            if (col < row) {
                startValue = col * TILE_WIDTH;
                endValue = startValue + TILE_WIDTH;
                lastTile = tiles.get(tiles.size() - 1);
            } else {
                startValue = col / TILE_WIDTH;
                endValue = startValue - TILE_WIDTH;
                lastTile = tiles.get(0);
            }
            t.getKeyFrames().addAll(
                    new KeyFrame(new Duration(0), new KeyValue(property, startValue)),
                    new KeyFrame(new Duration(1000), new KeyValue(property, endValue))
            );
        }
        t.play();
        Node yeetedOutTile = lastTile;
        t.setOnFinished(event -> {
            this.tiles.getChildren().remove(yeetedOutTile);
        });
    }

    private void displayGameBoardIsometric(FloorTile[][] gameBoard) {
        Group gameBoardView = new Group();
        Dimension2D dimension = new Dimension2D(gameBoard.length, gameBoard.length); //Needs to be changed if a gameboard can be a rectangle
        for (int row = 0; row < dimension.getWidth(); row++) {
            for (int col = 0; col < dimension.getHeight(); col++) {
                String tileType = getFloorTileType(gameBoard[row][col]);
                Image tileImage = new Image(tileType);

                ImageView tileDisplay = new ImageView(tileImage);

                tileDisplay.setFitWidth(TILE_WIDTH);
                tileDisplay.setFitHeight(TILE_HEIGHT);
                tileDisplay.setX((col - row) * (TILE_WIDTH / 2));
                tileDisplay.setY((col + row) * (TILE_HEIGHT / 2));

                gameBoardView.getChildren().add(tileDisplay);

                String tilePosition = "Tile position: Col = " + col + ", Row = " + row;
                ColorAdjust highlight = new ColorAdjust();

                tileDisplay.setOnMouseEntered(event -> {
                    positionLabel.setText(tilePosition);
                    highlight.setBrightness(0.2);
                    tileDisplay.setEffect(highlight);
                });

                tileDisplay.setOnMouseExited(event -> {
                    highlight.setBrightness(0);
                    tileDisplay.setEffect(highlight);
                });
            }
        }
        scrollPane.setContent(gameBoardView);
    }

    //For image
    private String getFloorTileType(FloorTile floorTile) {
        TileType type = floorTile.getType();
        switch (type) {
            case STRAIGHT:
                return "straight.png";
            case CORNER:
                return "corner.png";
            case T_SHAPED:
                return "t_shaped.png";
        }
        return null;
    }

    @FXML
    private void onDrawPlayerPieceButtonClicked() {
        Image playerPieceImage = new Image("playerPiece.png");
        ImageView playerPiece = new ImageView(playerPieceImage);
        playerPiece.setFitWidth(28);
        playerPiece.setFitHeight(28);
        playerPiece.setX(1 + 6);
        playerPiece.setY(1 + 6);
        tiles.getChildren().add(playerPiece);
        playerPiece.setId("playerPiece");

        playerPiece.setOnDragDetected(event -> {
            Dragboard dragboard = playerPiece.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(playerPiece.getImage());
            dragboard.setContent(content);
            event.consume();
        });
    }

    @FXML
    private void onSaveButtonClicked(ActionEvent e) {
        //todo Save Game
    }

    @FXML
    private void onSaveExitButtonClicked(ActionEvent e) throws IOException {
        //todo Save Game
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    @FXML
    private void onExitButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    @FXML
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
