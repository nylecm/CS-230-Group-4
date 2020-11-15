public abstract class Effect {
    protected EffectType effectType;

    public Effect(EffectType effectType) {
        this.effectType = effectType;
    }

    @Override
    public String toString() {
        return "Effect{" +
                "effectType=" + effectType +
                '}';
    }

    public EffectType getEffectType() {
        return effectType;
    }
}
