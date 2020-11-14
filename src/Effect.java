public abstract class Effect {
    private EffectTypes effectType;

    public Effect(EffectTypes effectType) {
        this.effectType = effectType;
    }

    @Override
    public String toString() {
        return "Effect{" +
                "effectType=" + effectType +
                '}';
    }

    public EffectTypes getEffectType() {
        return effectType;
    }
}
