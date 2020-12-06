package java_.game.tile;

import java_.game.player.Player;

public class PlayerEffect extends Effect {
    private Player target;

    public PlayerEffect(EffectType effectType) {
        super(effectType);
        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "game.tile.PlayerEffect{" +
                "target=" + target +
                '}';
    }
}
