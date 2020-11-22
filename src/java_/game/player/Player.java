package java_.game.player;

import java_.game.tile.ActionTile;
import java_.game.tile.Effect;
import java_.game.tile.PlayerEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bishwo
 */

public class Player {

    private String username;
    private String name;
    private int numberOfWins;
    private int numberOfLoses;
    private boolean isBot;
    private List<Effect> previousAppliedEffect = new ArrayList<>();
    private List<ActionTile> drawnActionTiles = new ArrayList<>();;
    private PlayerPiece playerPiece;


    /**
     * @param username            Unique game.player.Player ID
     * @param name                game.player.Player Name
     * @param numberOfWins        Number of Wins
     * @param numberOfLoses       Number of Loss
     * @param isBot               Type of game.player.Player: Human or AI
     */
    public Player(String username, String name, int numberOfWins, int numberOfLoses, boolean isBot, PlayerPiece playerPiece) {
        this.username = username;
        this.name = name;
        this.numberOfWins = numberOfWins;
        this.numberOfLoses = numberOfLoses;
        this.isBot = isBot;
        this.playerPiece = new PlayerPiece();
    }

    public void addPreviouslyAppliedEffect(Effect e) {
        previousAppliedEffect.add(e);
    }

    public void addDrawnActionTile(ActionTile actionTile) {
        drawnActionTiles.add(actionTile);
    }

    public int getUsername() {
        return getUsername();
    }

    public String getName() {
        return getName();
    }

    public int getNumberOfWins() {
        return getNumberOfWins();
    }

    public int getNumberOfLoses() {
        return getNumberOfLoses();
    }
    public boolean isBot() {
        return isBot;
    }

    public List<Effect> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    public static void main(String[] args) { }

    public List<ActionTile> getDrawnActionTiles() {
        return drawnActionTiles;
    }
}