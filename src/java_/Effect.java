package java_;

/**
 * An effect that belongs to an action tile. When the action tiles are used, the effects are applied.
 */
public abstract class Effect {
    protected EffectType effectType;

    /**
     * Initialises the java.Effect, giving it an effect type.
     *
     * @param effectType The type of the effect.
     */
    public Effect(EffectType effectType) {
        this.effectType = effectType;
    }

    /**
     * Returns the effect as a string containing the effect type.
     *
     * @return The string containing effect data.
     */
    @Override
    public String toString() {
        return "game.tile.java.Effect{" +
                "effectType=" + effectType +
                '}';
    }

    /**
     * Returns the type of the effect.
     *
     * @return The effect type of the effect.
     */
    public EffectType getEffectType() {
        return effectType;
    }
}
