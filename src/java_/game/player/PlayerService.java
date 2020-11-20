package java_.game.player;

import java_.game.tile.ActionTile;
import java_.game.tile.Effect;
import java_.game.tile.SilkBag;
import java_.game.tile.Tile;

public class PlayerService {

    private static PlayerService playerService = null;

    private Player[] players;
    private SilkBag silkBag;

    public PlayerService() {
    }

    public static PlayerService getInstance() {
        if (playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    public void playerTurn(Player p) {
        Tile drawnTile = silkBag.take();
        if (drawnTile instanceof ActionTile) {
            p.addDrawnActionTile((ActionTile) drawnTile);
        } else {
            System.out.println("Use FloorTile");
        }
        //(Apply effects)
        //Move PlayerPiece
        //After turn, game checks effects
    }

    public void addPreviouslyAppliedEffect(Player p, Effect e) {
        p.addPreviouslyAppliedEffect(e);
    }

    public boolean containsEffect(Player p, Effect e) {
        return p.getPreviousAppliedEffect().contains(e);
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
