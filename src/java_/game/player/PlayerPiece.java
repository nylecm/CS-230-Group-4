package java_.game.player;

import java_.util.Position;

public class PlayerPiece {

    private Position position;

    public PlayerPiece(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
