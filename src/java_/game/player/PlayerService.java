package java_.game.player;

import java_.game.controller.GameService;
import java_.game.tile.*;

import java.util.List;

public class PlayerService {
    public static final String BACKTRACK_EFFECT_PREVIOUSLY_APPLIED_TO_PLAYER = "Backtrack effect has been previously applied to that player.";
    public static final int TARGET_NUMBER_OF_BACKTRACKS = 2;
    public static final String BACK_TRACK_EFFECT_IS_NOT_POSSIBLE = "Back track effect is not possible.";
    private static PlayerService playerService = null;
    private GameService gameService;

    private Player[] players;

    public PlayerService() {
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public static PlayerService getInstance() {
        if (playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    public Tile playerTurn(Player p) {
        Tile drawnTile = GameService.getInstance().getSilkBag().take();
        if (drawnTile instanceof ActionTile) {
            p.addDrawnActionTile((ActionTile) drawnTile);
        }
        return drawnTile;
    }

    public void applyBackTrackEffect(int playerNum) {
        if (players[playerNum].getPreviousAppliedEffect().contains(EffectType.BACKTRACK)) {
            throw new IllegalStateException(BACKTRACK_EFFECT_PREVIOUSLY_APPLIED_TO_PLAYER);
        } else {
            if (gameService.getGameBoard().isBacktrackPossible(playerNum)) {
                gameService.getGameBoard().backtrack(playerNum, TARGET_NUMBER_OF_BACKTRACKS); //todo magic number
            } else {
                throw new IllegalStateException(BACK_TRACK_EFFECT_IS_NOT_POSSIBLE);
            }
        }
    }

    public void addPreviouslyAppliedEffect(Player p, EffectType e) {
        p.addPreviouslyAppliedEffect(e);
    }

    public boolean containsEffect(Player p, Effect e) {
        return p.getPreviousAppliedEffect().contains(e);
    }

    public void addDrawnActionTile(Player p, ActionTile actionTile) {
        p.addDrawnActionTile(actionTile);
    }

    public List<ActionTile> getDrawnActionTiles(Player p) {
        return p.getDrawnActionTiles();
    }

    public ActionTile getDrawnActionTile(Player p, int index) {
        return getDrawnActionTiles(p).get(index);
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int i) {
        return players[i]; // Potentially i - 1
    }

    public PlayerService remake() {
        return new PlayerService();
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
