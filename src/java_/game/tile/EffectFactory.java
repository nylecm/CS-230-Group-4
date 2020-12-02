package java_.game.tile;

public class EffectFactory {
    private final static String TILE_NO_EFFECT_MESSAGE =
            "Tile has an effect that is not supported by the factory!";

    public static Effect getEffect(TileType type) {

        if (ActionTile.ACTION_TILE_TYPES.contains(type)) {
            if (type == TileType.ICE) {
                return new AreaEffect(EffectType.ICE, 1, 4); //todo update duration, and get rid of magic numbers.
            } else if (type == TileType.FIRE) {
                return new AreaEffect(EffectType.FIRE, 1, 4); //todo update duration, and get rid of magic numbers.
            } else if (type == TileType.BACKTRACK) {
                return new PlayerEffect(EffectType.BACKTRACK, null); //todo get user input for target...
            } else if (type == TileType.DOUBLE_MOVE) {
                return new PlayerEffect(EffectType.DOUBLE_MOVE, null); //todo -"-
            }
        }
        throw new IllegalArgumentException(TILE_NO_EFFECT_MESSAGE);
    }

    public static void main(String[] args) {
        AreaEffect ae = (AreaEffect) getEffect(TileType.FIRE);
        System.out.println(ae);
        PlayerEffect pe = (PlayerEffect) getEffect(TileType.BACKTRACK);
        System.out.println(pe);
    }
}
