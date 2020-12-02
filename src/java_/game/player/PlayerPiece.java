package java_.game.player;

import java_.util.Position;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.Stack;

/**
 * A PlayerPiece represents the player playing the game and their position on the gameboard.
 * A PlayerPiece is represented graphically with an image.
 * It also keeps track of every position of a gameboard it has previously been on.
 *
 * @author nylecm, java doc by SamCox7500
 */
public class PlayerPiece {
    private final Stack<Position> previousPlayerPiecePositions;

    private Image image;
    private URL imageURL;

    /**
     * Instantiates a PlayerPiece giving it an image.
     * @param imageFilePath The file path for the image which is to be set for the PlayerPiece.
     */
    public PlayerPiece(URL imageFilePath) {
        previousPlayerPiecePositions = new Stack<>();
        imageURL = imageFilePath;
        image = new Image(String.valueOf(imageFilePath));
    }

    @Deprecated //fixme test only
    public PlayerPiece() {
        previousPlayerPiecePositions = new Stack<>();
    }

    /**
     * Returns the image representing PlayerPiece.
     * @return The image which represents the PlayerPiece graphically.
     */
    public Image getImage() {
        return image;
    }

    //todo javadoc this + update
    public URL getImageURL() {
        return imageURL;
    }

    /**
     * Sets an image to represent the PlayerPiece.
     * @param imageFilePath The file path for the image which is to be set for the PlayerPiece.
     */
    public void setImage(URL imageFilePath) {
        imageURL = imageFilePath;
        image = new Image(String.valueOf(imageFilePath));
    }

    /**
     * Returns the gameboard position history of the PlayerPiece.
     * @return All positions on the gameboard which have been previously visited by the PlayerPiece.
     */
    public Stack<Position> getPreviousPlayerPiecePositions() {
        return previousPlayerPiecePositions;
    }

    /**
     * Adds a new position to the PlayerPiece position history.
     * @param newPreviousPosition The position to be added to the PlayerPiece position history.
     */
    public void addPreviousPlayerPosition(Position newPreviousPosition) {
        previousPlayerPiecePositions.push(newPreviousPosition);
    }

    /**
     * Returns the position of the gameboard visited by the PlayerPiece prior to the one it is currently in.
     * @return The previous position of the PlayerPiece.
     */
    public Position getPreviousPlayerPosition() {
        return previousPlayerPiecePositions.peek();
    }
}
