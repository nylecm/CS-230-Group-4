package java_;

import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Stack;

/**
 * A java.PlayerPiece represents the player playing the game and their position on the gameboard.
 * A java.PlayerPiece is represented graphically with an image.
 * It also keeps track of every position of a gameboard it has previously been on.
 *
 * @author nylecm, java doc by SamCox7500
 */
public class PlayerPiece {
    private final Stack<Position> previousPlayerPiecePositions;

    private Image image;
    private File imageFile;

    /**
     * Instantiates a java.PlayerPiece giving it an image.
     *
     * @param imageFile The file for the image which is to be set for the java.PlayerPiece.
     */
    public PlayerPiece(File imageFile) throws MalformedURLException {
        previousPlayerPiecePositions = new Stack<>();
        this.imageFile = imageFile;
        image = new Image(String.valueOf(imageFile.toURI().toURL()));
    }

    /**
     * Returns the image representing java.PlayerPiece.
     *
     * @return The image which represents the java.PlayerPiece graphically.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets image file.
     *
     * @return this player's image file.
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * Sets an image to represent the java.PlayerPiece.
     *
     * @param imageFile The file for the image which is to be set for the java.PlayerPiece.
     */
    public void setImage(File imageFile) throws MalformedURLException {
        this.imageFile = imageFile;
        image = new Image(String.valueOf(imageFile.toURI().toURL()));
    }

    /**
     * Returns the gameboard position history of the java.PlayerPiece.
     *
     * @return All positions on the gameboard which have been previously visited by the java.PlayerPiece.
     */
    public Stack<Position> getPreviousPlayerPiecePositions() {
        return previousPlayerPiecePositions;
    }

    /**
     * Adds a new position to the java.PlayerPiece position history.
     *
     * @param newPreviousPosition The position to be added to the java.PlayerPiece position history.
     */
    public void addPreviousPlayerPosition(Position newPreviousPosition) {
        previousPlayerPiecePositions.push(newPreviousPosition);
    }

    /**
     * Returns the position of the gameboard visited by the java.PlayerPiece prior to the one it is currently in.
     *
     * @return The previous position of the java.PlayerPiece.
     */
    public Position getPreviousPlayerPosition() {
        return previousPlayerPiecePositions.peek();
    }
}
