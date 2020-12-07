package java_;

/**
 * An effect that can be applied to a player e.g. backtrack or double move.
 */
public class PlayerEffect extends Effect {
    private Player target;

    /**
     * Initialises the player effect, giving it an effect type.
     * and a target player.
     *
     * @param effectType
     */
    public PlayerEffect(EffectType effectType) {
        super(effectType);
        this.target = target;
    }

    /**
     * Returns the player that the effect is to be applied to.
     *
     * @return The target player of the effect.
     */
    public Player getTarget() {
        return target;
    }

    /**
     * Sets the target player of the effect.
     *
     * @param target
     */
    public void setTarget(Player target) {
        this.target = target;
    }

    /**
     * Returns the player effect as a string storing the target player.
     *
     * @return The string containing the player effect data.
     */
    @Override
    public String toString() {
        return "game.tile.java.PlayerEffect{" +
                "target=" + target +
                '}';
    }
}
