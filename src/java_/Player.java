package java_;

import java.util.*;

/**
 * The player which is one of up to four players that participates in the game. A player has a java.PlayerPiece which represents
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
     * Instantiates the java.Player, giving them a username and playerPiece.
     *
     * @param username    The username to be used to identify the java.Player.
     * @param playerPiece The java.PlayerPiece to be used to represent the java.Player in the game.
     */
    public Player(String username, PlayerPiece playerPiece) {
        this.username = username;
        this.playerPiece = playerPiece;
    }

    /**
     * Adds a new effect type to already known effect types of effects
     * that have been previously applied to the java.Player.
     *
     * @param e The effect type of the new effect that has been applied to the java.Player.
     */
    public void addPreviouslyAppliedEffect(EffectType e) {
        previousAppliedEffect.add(e);
    }

    /**
     * Adds a new action tile to already drawn action tiles.
     *
     * @param actionTile The action tile which has been drawn.
     */
    public void addDrawnActionTile(ActionTile actionTile) {
        drawnActionTiles.add(actionTile);
    }

    /**
     * Returns the username which identifies the java.Player.
     *
     * @return The username of the java.Player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the java.PlayerPiece which represents the java.Player in the game.
     *
     * @return The java.PlayerPiece belonging to the java.Player.
     */
    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    /**
     * Returns all effect types of effects that have been previously applied to the java.Player.
     *
     * @return All previously applied effect types.
     */
    public Set<EffectType> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    /**
     * Returns all action tiles which have been drawn by the java.Player.
     *
     * @return All previously drawn action tiles.
     */
    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }

    /**
     * Returns java.Player data such as username, playerPiece, previouslyAppliedEffects and drawnActionTiles.
     *
     * @return java.Player data as a string.
     */
    @Override
    public String toString() {
        return "java.Player{" +
                "username='" + username + '\'' +
                ", previousAppliedEffect=" + previousAppliedEffect +
                ", drawnActionTiles=" + drawnActionTiles +
                ", playerPiece=" + playerPiece +
                '}';
    }

    /**
     * Adds additional drawn action tiles to known drawn action tiles.
     *
     * @param drawnActionTilesToBeAdded The new drawn action tiles to be added.
     */
    public void addDrawnActionTiles(TileType[] drawnActionTilesToBeAdded) {
        for (TileType tile : drawnActionTilesToBeAdded) {
            System.out.println(tile);
            drawnActionTiles.add(new ActionTile(tile));
        }
    }

    /**
     * Adds multiple effects to known previously applied effects.
     *
     * @param previouslyAppliedActionTiles The effects to be added to the previously applied effects.
     */
    public void addPreviouslyAppliedEffects(EffectType[] previouslyAppliedActionTiles) {
        previousAppliedEffect.addAll(Arrays.asList(previouslyAppliedActionTiles));
    }
}
