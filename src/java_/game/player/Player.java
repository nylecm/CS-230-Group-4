package java_.game.player;

import java_.game.tile.ActionTile;

import java_.game.tile.EffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author nylecm, coxy7, bishwo
 */

public class Player {
    private final String username;
    private final Set<EffectType> previousAppliedEffect = new HashSet<>();
    private final List<ActionTile> drawnActionTiles = new ArrayList<>();
    private final PlayerPiece playerPiece;

    /**
     * @param username Unique game.player.Player I
     */
    public Player(String username, PlayerPiece playerPiece) {
        this.username = username;
        this.playerPiece = playerPiece;
    }

    public void addPreviouslyAppliedEffect(EffectType e) {
        previousAppliedEffect.add(e);
    }

    public void addDrawnActionTile(ActionTile actionTile) {
        drawnActionTiles.add(actionTile);
    }

    public String getUsername() {
        return username;
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    public Set<EffectType> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", previousAppliedEffect=" + previousAppliedEffect +
                ", drawnActionTiles=" + drawnActionTiles +
                ", playerPiece=" + playerPiece +
                '}';
    }
}
