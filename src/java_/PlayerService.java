package java_;

import java.util.List;

/**
 * java.PlayerService is a service for storing and maintaining PlayerData for a game.
 * This includes the Players in a game and their related data.
 */
public class PlayerService {
    public static final String BACKTRACK_EFFECT_PREVIOUSLY_APPLIED_TO_PLAYER = "Backtrack effect has been previously applied to that player.";
    public static final int TARGET_NUMBER_OF_BACKTRACKS = 2;
    public static final String BACK_TRACK_EFFECT_IS_NOT_POSSIBLE = "Back track effect is not possible.";
    private static PlayerService playerService = null;
    private GameService gameService;

    private Player[] players;

    /**
     * Initialises java.PlayerService
     */
    public PlayerService() {
    }

    /**
     * Sets the java.GameService for the java.PlayerService
     * @param gameService The java.GameService to be set for the java.PlayerService.
     */
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Returns the current instance of the java.PlayerService. If there isn't one, a new one is created and returned.
     * @return The current instance or new instance of java.PlayerService.
     */
    public static PlayerService getInstance() {
        if (playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    /**
     * Applies the back track effect to a java.Player with the specified index.
     * @param playerNum the index identifying the java.Player that the backtrack effect is to be applied to.
     * @throws IllegalStateException If there has been an attempt to apply a backtrack effect to a java.Player that has previously had the effect applied to them.
     */
    public void applyBackTrackEffect(int playerNum) throws IllegalStateException {
        if (players[playerNum].getPreviousAppliedEffect().contains(EffectType.BACKTRACK)) {
            throw new IllegalStateException(BACKTRACK_EFFECT_PREVIOUSLY_APPLIED_TO_PLAYER);
        } else {
            if (gameService.getGameBoard().isBacktrackPossible(playerNum)) {
                gameService.getGameBoard().backtrack(playerNum, TARGET_NUMBER_OF_BACKTRACKS);
            } else {
                throw new IllegalStateException(BACK_TRACK_EFFECT_IS_NOT_POSSIBLE);
            }
        }
    }

    /**
     * Adds a new previously applied effect to a specified player.
     * @param p The player that is to have the new previously applied effect.
     * @param e Teh type of the previously applied effect.
     */
    public void addPreviouslyAppliedEffect(Player p, EffectType e) {
        p.addPreviouslyAppliedEffect(e);
    }

    /**
     * Returns if a player has had a specific effect applied to them.
     * @param p The player that is to be checked.
     * @param e The effect that is to be checked if it has been applied to the player.
     * @return True if the effect has been applied to the player, false otherwise.
     */
    public boolean containsEffect(Player p, EffectType e) {
        return p.getPreviousAppliedEffect().contains(e);
    }

    /**
     * Adds a new drawn action tile to the java.Player's drawn action tiles.
     * @param p The player that has drawn the new action tile.
     * @param actionTile The action tile that has been drawn.
     */
    public void addDrawnActionTile(Player p, ActionTile actionTile) {
        p.addDrawnActionTile(actionTile);
    }

    /**
     * Returns all drawn action tiles for a specific java.Player.
     * @param p The java.Player whose drawn action tiles are to be returned.
     * @return The list of all drawn action tiles.
     */
    public List<ActionTile> getDrawnActionTiles(Player p) {
        return p.getDrawnActionTiles();
    }

    /**
     * Gets the drawn action tile for a player at a specified index.
     * @param p The player who had the drawn action tiles.
     * @param index The index of the action tile to be returned.
     * @return The action tile at the specific index.
     */
    public ActionTile getDrawnActionTile(Player p, int index) {
        return getDrawnActionTiles(p).get(index);
    }

    /**
     * Returns all players in the java.PlayerService.
     * @return An array containing all java.PlayerService players.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Returns a player at a specified index.
     * @param i The index of the player to be returned
     * @return The player at the specified index.
     */
    public Player getPlayer(int i) {
        return players[i]; // Potentially i - 1
    }

    /**
     * Returns a player index by given player reference
     * @param player The player to search for
     * @return index if found, -1 if not found
     */
    public int getPlayerIndex(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Creates and returns a new java.PlayerService.
     * @return The new java.PlayerService.
     */
    public PlayerService remake() {
        return new PlayerService();
    }

    /**
     * Sets the players for the java.PlayerService.
     * @param players The players to be set for the java.PlayerService.
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
