import game.tile.TileType;

import java.util.EnumSet;

import static game.tile.TileType.*;
import static game.tile.TileType.DOUBLE_MOVE;

/**
 * The type Action tile.
 *
 * @author mnabina, nylecm
 */
public class ActionTile extends Tile {
    public final static EnumSet<TileType> ACTION_TILE_TYPES =
            EnumSet.of(ICE, FIRE, BACKTRACK, DOUBLE_MOVE);

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
    public void use() {

    }
}
