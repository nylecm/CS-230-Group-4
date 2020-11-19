package java_.game.player;

import java_.game.tile.ActionTile;
import java_.game.tile.Effect;
import java_.game.tile.SilkBag;
import java_.game.tile.Tile;

public class PlayerService {

    private Player[] players;

    public void playerTurn(Player p, SilkBag silkBag) {
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
}
