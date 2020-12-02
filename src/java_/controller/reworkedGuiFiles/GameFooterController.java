package java_.controller.reworkedGuiFiles;

import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.tile.ActionTile;
import java_.game.tile.TileType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

public class GameFooterController {

    @FXML
    private GameMainController gameMainController;

    @FXML
    private HBox drawnActionTiles;

    public void injectMainController(GameMainController gameMainController) {
        this.gameMainController = gameMainController;
    }

    public void displayActionTiles(Player player) {
        List<ActionTile> playersActionTiles = GameService.getInstance().getPlayerService().getDrawnActionTiles(player);
        if (!playersActionTiles.isEmpty()) {
            for (ActionTile actionTile : playersActionTiles) {
                Image actionTileImage = new Image(getActionTileTypeImage(actionTile));
                ImageView actionTileDisplay = new ImageView(actionTileImage);
                drawnActionTiles.getChildren().add(actionTileDisplay);
            }
        }
    }

    static String getActionTileTypeImage(ActionTile actionTile) {
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
