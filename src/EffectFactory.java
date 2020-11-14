public class EffectFactory {
    public Effect getEffect(String effectName) {
        if (effectName == null) {
            throw new IllegalArgumentException("");
        }
        if (effectName.equals(Tile.ACTION_TILE_TYPES)) {
            if (effectName.equals("ice")) {
                return new AreaEffect(EffectTypes.ICE, 2, 4); //todo update duration, and get rid of magic numbers.
            }
            if (effectName.equals("fire")) {
                return new AreaEffect(EffectTypes.FIRE, 2, 4); //todo update duration, and get rid of magic numbers.
            }
            if (effectName.equals("backtrack")) {
                return new PlayerEffect(EffectTypes.BACKTRACK, null); //todo get user input for target...
            }
            if (effectName.equals("double_move")) {
                return new PlayerEffect(EffectTypes.DOUBLE_MOVE, null); //todo -"-
            }
        }
        throw new IllegalArgumentException("");
    }

    public static void main(String[] args) {

    }
}
