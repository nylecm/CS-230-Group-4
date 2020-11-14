public abstract class Effect {
    private EffectType effectType;

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
