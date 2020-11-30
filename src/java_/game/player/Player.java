package java_.game.player;

import java_.game.tile.ActionTile;

import java_.game.tile.EffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The player which is one of up to four players that participates in the game. A player has a PlayerPiece which represents
 * them on the gameboard. The player can draw action tiles which can be used to apply effects. A player can also have effects applied to them.
 * A player is identified by a username.
 *
 * @author nylecm, coxy7, bishwo
 */
public class Player {
    private final String username;
    private final Set<EffectType> previousAppliedEffect = new HashSet<>();
    private final List<ActionTile> drawnActionTiles = new ArrayList<>();
    private final PlayerPiece playerPiece;

    /**
     * Instantiates the Player, giving them a username and playerPiece.
     * @param username The username to be used to identify the Player.
     * @param playerPiece The PlayerPiece to be used to represent the Player in the game.
     */
    public Player(String username, PlayerPiece playerPiece) {
        this.username = username;
        this.playerPiece = playerPiece;
    }

    /**
     * Adds a new effect type to already known effect types of effects
     * that have been previously applied to the Player.
     * @param e The effect type of the new effect that has been applied to the Player.
     */
    public void addPreviouslyAppliedEffect(EffectType e) {
        previousAppliedEffect.add(e);
    }

    /**
     * Adds a new action tile to already drawn action tiles.
     * @param actionTile The action tile which has been drawn.
     */
    public void addDrawnActionTile(ActionTile actionTile) {
        drawnActionTiles.add(actionTile);
    }

    /**
     * Returns the username which identifies the Player.
     * @return The username of the Player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the PlayerPiece which represents the Player in the game.
     * @return The PlayerPiece belonging to the Player.
     */
    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    /**
     * Returns all effect types of effects that have been previously applied to the Player.
     * @return All previously applied effect types.
     */
    public Set<EffectType> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    /**
     * Returns all action tiles which have been drawn by the Player.
     * @return All previously drawn action tiles.
     */
    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }

    /**
     * Returns Player data such as username, playerPiece, previouslyAppliedEffects and drawnActionTiles.
     * @return Player data as a string.
     */
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
