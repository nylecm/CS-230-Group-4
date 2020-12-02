package java_.game.player;

import java_.game.controller.GameService;
import java_.game.tile.*;

import java.util.List;

public class PlayerService {
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
        } else {
            //TODO: Add FloorTile to the viewport
        }
        return drawnTile;
        //(Apply effects)
        //Move PlayerPiece
        //After turn, game checks effects
    }

    public void applyBackTrackEffect(int playerNum) {
        if (players[playerNum].getPreviousAppliedEffect().contains(EffectType.BACKTRACK)) {
            throw new IllegalStateException("Backtrack effect has been previously applied to that player.");
        } else {
            if (gameService.getGameBoard().isBacktrackPossible(playerNum)) {
                gameService.getGameBoard().backtrack(playerNum, 2); //todo magic number
            } else {
                System.out.println("fail");
            }
        } //todo finish off
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
