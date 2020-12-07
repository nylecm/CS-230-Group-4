package java_.game.tile;

import java_.game.controller.GameService;

/**
 * Creates effects from tile types.
 *
 * @author nylecm
 */
public class EffectFactory {
    private final static String TILE_NO_EFFECT_MESSAGE =
            "Tile has an effect that is not supported by the factory!";
    private final static int THREE_BY_THREE_EFFECT_RADIUS = 1;

    /**
     * Returns an effect from a tile type.
     *
     * @param type The type of the floor tile used to create the effect.
     * @return The effect created from the tile type.
     * @throws IllegalArgumentException If the tile type is one that does create effects.
     */
    public static Effect getEffect(TileType type) throws IllegalArgumentException {

        if (ActionTile.ACTION_TILE_TYPES.contains(type)) {
            if (type == TileType.ICE) {
                return new AreaEffect(EffectType.ICE, THREE_BY_THREE_EFFECT_RADIUS, GameService.getInstance().getPlayerService().getPlayers().length);
            } else if (type == TileType.FIRE) {
                return new AreaEffect(EffectType.FIRE, THREE_BY_THREE_EFFECT_RADIUS, GameService.getInstance().getPlayerService().getPlayers().length);
            } else if (type == TileType.BACKTRACK) {
                return new PlayerEffect(EffectType.BACKTRACK);
            } else if (type == TileType.DOUBLE_MOVE) {
                return new PlayerEffect(EffectType.DOUBLE_MOVE);
            }
        }
        throw new IllegalArgumentException(TILE_NO_EFFECT_MESSAGE);
    }
}
