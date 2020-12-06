package java_.controller.reworkedGuiFiles;

import java_.controller.reworkedGuiFiles.GameBoardController;
import java_.controller.reworkedGuiFiles.GameFooterController;
import java_.controller.reworkedGuiFiles.GameMainController;
import java_.game.controller.GameService;
import java_.game.tile.ActionTile;
import java_.game.tile.FloorTile;
import java_.game.tile.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSidebarController implements Initializable {

    @FXML
    private GameMainController gameMainController;

    @FXML
    private GameBoardController gameBoardController;

    @FXML
    private ImageView drawnFloorTile = new ImageView(new Image("fullFlat.png"));

    @FXML
    private static ImageView drawnActionTile = new ImageView(new Image("fullFlat.png"));

    @FXML
    private Button drawTileButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameBoardController.injectSidebarController(this);
    }


    public void injectMainController(GameMainController gameMainController) {
        this.gameMainController = gameMainController;
    }

    public void injectBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    @FXML
    public void onDrawTileButtonClicked() {
        drawnFloorTile.setFitWidth(60);
        drawnFloorTile.setFitHeight(60);
//        Tile drawnTile = GameService.getInstance().getPlayerService().playerTurn(null); //Get current player

        Tile drawnTile = GameService.getInstance().getSilkBag().take();

        if (drawnTile instanceof FloorTile) {
            Image newFloorTileImage = new Image((gameBoardController.getFloorTileTypeImage((FloorTile) drawnTile)));
            drawnFloorTile.setImage(newFloorTileImage);
        } else { //Action Tile drawn
            Image newActionTileImage = new Image((GameFooterController.getActionTileTypeImage((ActionTile) drawnTile)));
            drawnActionTile.setImage(newActionTileImage);
        }

        drawnFloorTile.setOnDragDetected(event -> {
            Dragboard dragboard = drawnFloorTile.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(drawnFloorTile.getImage());
            dragboard.setContent(content);
            event.consume();
        });
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
    public static ImageView getDrawnActionTile() {
        return drawnActionTile;
    }

    @FXML
    public ImageView getDrawnFloorTile() {
        return drawnFloorTile;
    }
}
