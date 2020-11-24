package java_.controller;

import java_.game.tile.*;
import java_.util.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private static final int TILE_WIDTH = 64;

    private static final int TILE_HEIGHT = 32;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Label positionLabel;

    @FXML
    ImageView floorTile;

    @FXML
    Label validMoveLabel;

    @FXML
    Button slideButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FloorTile[][] gameBoard = loadGameboard().board;
        displayGameBoard(gameBoard);
    }

    @FXML
    public void dragDetected() {
        System.out.println("TRUE");
        Dragboard dragboard = floorTile.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("TEST");
        dragboard.setContent(content);
    }

    @FXML
    private GameBoard loadGameboard() {
        Position[] playerPiecesPosition = {
                                        new Position(1, 5),
                                        new Position(6, 2)
                                        };

        Tile[] newTiles = new Tile[0];

        FloorTile A = new FloorTile(TileType.CORNER, true, false);
        FloorTile B = new FloorTile(TileType.STRAIGHT, true, false);
        FloorTile C = new FloorTile(TileType.T_SHAPED, true, false);

        FloorTile[] fixedTiles = new FloorTile[3];
        fixedTiles[0] = A;
        fixedTiles[1] = B;
        fixedTiles[2] = C;

        Position[] fixedTilePositions = new Position[3];
        fixedTilePositions[0] = new Position(0, 0);
        fixedTilePositions[1] = new Position(1, 1);
        fixedTilePositions[2] = new Position(2, 2);

        FloorTile D = new FloorTile(TileType.CORNER, false, false);
        FloorTile E = new FloorTile(TileType.CORNER, false, false);
        FloorTile F = new FloorTile(TileType.T_SHAPED, false, false);
        FloorTile G = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile H = new FloorTile(TileType.STRAIGHT, false, false);
        FloorTile I = new FloorTile(TileType.CORNER, false, false);

        FloorTile[] tiles = new FloorTile[6];
        tiles[0] = D;
        tiles[1] = E;
        tiles[2] = F;
        tiles[3] = G;
        tiles[4] = H;
        tiles[5] = I;


       return new GameBoard(playerPiecesPosition, fixedTiles, fixedTilePositions, tiles, 3, 3, "GameBoard1");
    }

    private void displayGameBoard(FloorTile[][] gameBoard) {
        Group gameBoardView = new Group();
        Dimension2D dimension = new Dimension2D(gameBoard.length, gameBoard.length); //Needs to be changed if a gameboard can be a rectangle
        setBoarders(gameBoardView, dimension);
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

                tileDisplay.setOnMouseEntered(event -> {
                    positionLabel.setText(tilePosition);
                });
            }
        }
        scrollPane.setContent(gameBoardView);
    }

    private void setBoarders(Group gameBoardView, Dimension2D dimension) {
        for (int row = 0; row < dimension.getWidth(); row++) {
            for (int col = 0; col < dimension.getHeight(); col++) {
                Image tileImage = new Image("empty.png");
                ImageView tileDisplay = new ImageView(tileImage);
                tileDisplay.setFitWidth(TILE_WIDTH);
                tileDisplay.setFitHeight(TILE_HEIGHT);
                tileDisplay.setX((col - row) * (TILE_WIDTH / 2));
                tileDisplay.setY((col + row) * (TILE_HEIGHT / 2));
            }
        }
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
    private void onSlideButtonClicked(ActionEvent e) {

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
}
