package java_;

/**
 * Represents an effect that is applied to an area (can be multiple tiles) on a java.GameBoard.
 * The radius dictates the area that the effect covers.
 * It also has a duration that decrements every turn.
 */
public class AreaEffect extends Effect {
    private int radius;
    private int remainingDuration;

    /**
     * Initialises the java.AreaEffect giving it an effect type, a radius and a duration.
     *
     * @param effectType The type of the area effect.
     * @param radius     The radius of the area effect. 0 indicates it affects only 1 tile.
     * @param duration   The duration of the area effect.
     */
    public AreaEffect(EffectType effectType, int radius, int duration) {
        super(effectType);
        this.radius = radius;
        this.remainingDuration = duration;
    }

    /**
     * Returns the radius of the area effect.
     *
     * @return The radius of the area effect.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the area effect.
     *
     * @param radius The radius to be set for the area effect.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Returns the remaining duration of the area effect.
     *
     * @return The remaining duration of the area effect.
     */
    public int getRemainingDuration() {
        return remainingDuration;
    }

    /**
     * Sets the remaining duration of the area effect.
     *
     * @param remainingDuration The remaining duration of the area effect.
     */
    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    /**
     * Decreases the remaining duration of the area effect by 1.
     */
    public void decrementRemainingDuration() {
        remainingDuration--;
    }

    /**
     * Returns the area effect as a string including the radius, remaining duration and effect type.
     *
     * @return The string containing area effect data.
     */
    @Override
    public String toString() {
        return "java.AreaEffect{" +
                "radius=" + radius +
                ", remainingDuration=" + remainingDuration +
                ", effectType=" + effectType +
                '}';
    }
}
