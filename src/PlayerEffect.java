public class PlayerEffect extends Effect {
    private Player target;

    public PlayerEffect(EffectType effectType, Player target) {
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
        return "PlayerEffect{" +
                "target=" + target +
                '}';
    }
}
