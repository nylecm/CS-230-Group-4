package java_.game.player;

import java_.util.Position;

import java.util.Stack;

public class PlayerPiece {
    private final Stack<Position> previousPlayerPiecePositions;


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
