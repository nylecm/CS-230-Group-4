public class EffectFactory {
    public Effect getEffect(String effectName) {
        if (effectName == null) {
            throw new IllegalArgumentException("");
        }
        if (ActionTile.ACTION_TILE_TYPES.contains(effectName)) {
            if (effectName.equals("ice")) {
                return new AreaEffect(EffectType.ICE, 2, 4); //todo update duration, and get rid of magic numbers.
            } else if (effectName.equals("fire")) {
                return new AreaEffect(EffectType.FIRE, 2, 4); //todo update duration, and get rid of magic numbers.
            } else if (effectName.equals("backtrack")) {
                return new PlayerEffect(EffectType.BACKTRACK, null); //todo get user input for target...
            } else if (effectName.equals("double_move")) {
                return new PlayerEffect(EffectType.DOUBLE_MOVE, null); //todo -"-
            }
        }
        throw new IllegalArgumentException("");
    }

    public static void main(String[] args) {
        EffectFactory f = new EffectFactory();
        AreaEffect ae = (AreaEffect) f.getEffect("fire");
        System.out.println(ae);
        PlayerEffect pe = (PlayerEffect) f.getEffect("backtrack");
        System.out.println(pe);
    }
}
