package java_.game.player;

import java_.game.tile.Effect;

public class PlayerService {

    private Player[] players;

    public void playerTurn(Player p) {
        //Get random tile from SilkBag
        //Use tile
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
