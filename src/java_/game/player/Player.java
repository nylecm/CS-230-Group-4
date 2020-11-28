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

    private String username;
    private int numberOfWins;
    private int numberOfLoses;
    private final Set<EffectType> previousAppliedEffect = new HashSet<>();
    private final List<ActionTile> drawnActionTiles = new ArrayList<>();
    private PlayerPiece playerPiece;

    /**
     * @param username            Unique game.player.Player ID
     * @param numberOfWins        Number of Wins
     * @param numberOfLoses       Number of Loss
     */
    public Player(String username, int numberOfWins, int numberOfLoses, PlayerPiece playerPiece) {
        this.username = username;
        this.numberOfWins = numberOfWins;
        this.numberOfLoses = numberOfLoses;
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


    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getNumberOfLoses() {
        return numberOfLoses;
    }


    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    public Set<EffectType> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    public static void main(String[] args) { }

    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }
}
