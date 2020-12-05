package java_.game.tile;

import java.util.EnumSet;

/**
 * A tile that is used by the players of the game as the unit of currency which
 * can be traded for the application of its corresponding effect.
 *
 * @author mnabina, nylecm
 */
public class ActionTile extends Tile {
    public final static EnumSet<TileType> ACTION_TILE_TYPES =
            EnumSet.of(TileType.ICE, TileType.FIRE, TileType.BACKTRACK, TileType.DOUBLE_MOVE);

    /**
     * Instantiates a new Action tile, of a specified type.
     *
     * @param type the TileType of the new action tile (eg FIRE).
     */
    public ActionTile(TileType type) {
        super(type, ACTION_TILE_TYPES);
    }

    /**
     * Creates the effect that a tile of this type can produce.
     */
    public Effect use() {
        return EffectFactory.getEffect(type);
    }
}
