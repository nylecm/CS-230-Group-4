package java_.game.player;

import java_.util.Position;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.Stack;

public class PlayerPiece {
    private final Stack<Position> previousPlayerPiecePositions;

    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(URL imageFilePath) {
        image = new Image(String.valueOf(imageFilePath));
    }

    public PlayerPiece(URL imageFilePath) {
        previousPlayerPiecePositions = new Stack<>();
        image = new Image(String.valueOf(imageFilePath));
    }

    @Deprecated //fixme test only
    public PlayerPiece() {
        previousPlayerPiecePositions = new Stack<>();
    }

    public Stack<Position> getPreviousPlayerPiecePositions() {
        return previousPlayerPiecePositions;
    }

    public void addPreviousPlayerPosition(Position newPreviousPosition) {
        previousPlayerPiecePositions.push(newPreviousPosition);
    }

    public Position getPreviousPlayerPosition() {
        return previousPlayerPiecePositions.peek();
    }
}
