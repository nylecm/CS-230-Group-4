package java_.game.player;

import java_.game.tile.ActionTile;

import java_.game.tile.EffectType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A player is one of up to four participants in the game. They have a PlayerPiece which is
 * used to represent their position on the board. They are identified by a username.
 * A player can draw action tiles which are used to apply effects and a player can have effects applied to them.
 *
 * @author nylecm, coxy7, bishwo
 */
public class Player {

    private String username;
    //private int numberOfWins;
    //private int numberOfLoses;
    private final Set<EffectType> previousAppliedEffect = new HashSet<>();
    private final List<ActionTile> drawnActionTiles = new ArrayList<>();
    private PlayerPiece playerPiece;

    /**
     * Instantiates a player, giving them a username and a player piece.
     *
     * @param username the username used to identify the player.
     * @param playerPiece the player piece which represents the player on the gameboard.
     */
    public Player(String username, PlayerPiece playerPiece) {
        this.username = username;
        //this.numberOfWins = numberOfWins;
        //this.numberOfLoses = numberOfLoses;
        this.playerPiece = playerPiece;
    }

    /**
     * Adds a new effect type to known effect types previously applied to the player.
     * @param e The type of the effect that has been previously applied to the player.
     */
    public void addPreviouslyAppliedEffect(EffectType e) {
        previousAppliedEffect.add(e);
    }

    /**
     * Adds a new action tile to known action tiles drawn by the player.
     * @param actionTile The action tile to be added to the already drawn action tiles.
     */
    public void addDrawnActionTile(ActionTile actionTile) {
        drawnActionTiles.add(actionTile);
    }

    /**
     * The username which identifies the player.
     * @return The player's username.
     */
    public String getUsername() {
        return username;
    }

    /*
    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getNumberOfLoses() {
        return numberOfLoses;
    }
    */

    /**
     * Returns the player piece which represents the player.
     * @return The player's player piece
     */
    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    /**
     * Returns the set of effect types of effects that have been previously applied to the player.
     * @return
     */
    public Set<EffectType> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    /**
     * Returns all action tiles drawn by the player.
     * @return The list of action tiles drawn by the player.
     */
    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) { }


}
