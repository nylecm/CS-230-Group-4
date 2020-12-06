package java_.game.tile;

import java_.game.controller.GameService;

public class EffectFactory {
    private final static String TILE_NO_EFFECT_MESSAGE =
            "Tile has an effect that is not supported by the factory!";
    private final static int THREE_BY_THREE_EFFECT_RADIUS = 1;

    public static Effect getEffect(TileType type) throws IllegalArgumentException {

        if (ActionTile.ACTION_TILE_TYPES.contains(type)) {
            if (type == TileType.ICE) {
                return new AreaEffect(EffectType.ICE, THREE_BY_THREE_EFFECT_RADIUS, GameService.getInstance().getCurrentPlayerNum());
            } else if (type == TileType.FIRE) {
                return new AreaEffect(EffectType.FIRE, THREE_BY_THREE_EFFECT_RADIUS, GameService.getInstance().getCurrentPlayerNum());
            } else if (type == TileType.BACKTRACK) {
                return new PlayerEffect(EffectType.BACKTRACK); //todo get user input for target...
            } else if (type == TileType.DOUBLE_MOVE) {
                return new PlayerEffect(EffectType.DOUBLE_MOVE); //todo -"-
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
