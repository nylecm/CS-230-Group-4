public class AreaEffect extends Effect {
    private int radius;
    private int remainingDuration;

    public AreaEffect(EffectType effectType, int radius, int duration) {
        super(effectType);
        this.radius = radius;
        this.remainingDuration = duration;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRemainingDuration() {
        return remainingDuration;
    }

    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public void decrementRemainingDuration() {
        remainingDuration--;
    }

    @Override
    public String toString() {
        return "AreaEffect{" +
                "radius=" + radius +
                ", remainingDuration=" + remainingDuration +
                '}';
    }
}
