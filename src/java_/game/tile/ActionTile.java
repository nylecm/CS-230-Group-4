package java_.game.tile;

import java.util.EnumSet;

/**
 * The type Action tile.
 *
 * @author mnabina, nylecm
 */
public class ActionTile extends Tile {
    public final static EnumSet<TileType> ACTION_TILE_TYPES =
            EnumSet.of(TileType.ICE, TileType.FIRE, TileType.BACKTRACK, TileType.DOUBLE_MOVE);

    /**
     * Instantiates a new Action tile.
     *
     * @param type the type name
     */
    public ActionTile(TileType type) {
        super(type, ACTION_TILE_TYPES);
    }

    /**
     * Use.
     */
    public Effect use() {
        return EffectFactory.getEffect(type);
    }
}