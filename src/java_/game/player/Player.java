package java_.game.player;

import java_.game.tile.Effect;

import java.util.ArrayList;

/**
 * @author Bishwo
 */

public class Player {
    private String username;
    private String name;
    private int numberOfWins;
    private int numberOfLoses;
    private boolean isBot;
    private ArrayList<Effect> previousAppliedEffect ;

    /**
     * @param username            Unique game.player.Player ID
     * @param name                game.player.Player Name
     * @param numberOfWins        Number of Wins
     * @param numberOfLoses       Number of Loss
     * @param isBot               Type of game.player.Player: Human or AI
     */

    public Player(String username, String name, int numberOfWins, int numberOfLoses, boolean isBot ){
        this.username=username;
        this.name=name;
        this.numberOfWins=numberOfWins;
        this.numberOfLoses=numberOfLoses;
        this.isBot=isBot;
        this.previousAppliedEffect = new ArrayList<>();
    }

    public void addPreviouslyAppliedEffect(Effect e) {
        previousAppliedEffect.add(e);
    }

    public int getUsername(){
        return getUsername();
    }

    public String getName(){
        return getName();
    }
    public int getNumberOfWins(){
        return getNumberOfWins();
    }
    public int getNumberOfLoses(){
        return getNumberOfLoses();
    }
    public boolean isBot() {
        return isBot;
    }

    public ArrayList<Effect> getPreviousAppliedEffect() {
        return previousAppliedEffect;
    }

    public static void main(String[] args) {
        Player testPlayer = new Player("H4jn", "M", 8, 10, false);
        System.out.println(testPlayer.username + testPlayer.name + testPlayer.numberOfWins + testPlayer.numberOfLoses + testPlayer.isBot);
    }
}