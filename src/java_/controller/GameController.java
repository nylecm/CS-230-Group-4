package java_.controller;

import java_.game.player.PlayerPiece;
import java_.game.tile.*;
import java_.util.Position;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.persistence.Column;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    ScrollPane scrollPane;

    @FXML
    Label positionLabel;

    @FXML
    Rectangle tileToBeInserted;

    @FXML
    Label validMoveLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGameboard();
    }

    @FXML
    public void dragDetected() {
        System.out.println("TRUE");
        Dragboard dragboard = tileToBeInserted.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("TEST");
        dragboard.setContent(content);
    }

    private void setupGameboard() {
        Group gameBoardView = new Group();
        int tileWidth = 64;
        int tileHeight = 32;
        Dimension2D dimension = new Dimension2D(8, 8);
        Image tileImage = new Image("tileCalculated.png");

        for (int row = 0; row < dimension.getWidth(); row++) {
            for (int col = 0; col < dimension.getHeight(); col++) {
                ImageView tile = new ImageView(tileImage);
                tile.setFitWidth(tileWidth);
                tile.setFitHeight(tileHeight);
                tile.setX((col - row) * (tileWidth / 2));
                tile.setY((col + row) * (tileHeight / 2));

                String tilePosition = "Tile position: X = " + col + ", Y = " + row;

                tile.setOnMouseEntered(event -> {
                    positionLabel.setText(tilePosition);
                });

                tile.setOnDragDropped(event -> {
                    if (tile.getX() == 0 || tile.getY() == 0) {
                        validMoveLabel.setText("Valid move: true");
                    } else {
                        validMoveLabel.setText("Valid move: false");
                    }
                });
                gameBoardView.getChildren().add(tile);
            }
        }
        scrollPane.setContent(gameBoardView);

    }

    private void gameBoardTest() {

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
