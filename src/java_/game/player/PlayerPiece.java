package java_.game.player;

import java_.util.Position;

import java.io.File;
import java.util.Stack;

public class PlayerPiece {
    private final Stack<Position> previousPlayerPiecePositions;

    private File image;

    public File getImage() {
        return image;
    }

    public void setImage(String imageFilePath) {
        File newImage = new File(imageFilePath);
        image = newImage;
    }

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
