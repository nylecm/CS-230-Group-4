import game.tile.TileType;

import java.util.EnumSet;

import static game.tile.TileType.*;

/**
 * The type Tile.
 *
 * @author mnabina & nylecm
 */
public abstract class Tile {

    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(STRAIGHT, CORNER, T_SHAPED, GOAL);
    public final static EnumSet<TileType> ACTION_TILE_TYPES =
            EnumSet.of(ICE, FIRE, BACKTRACK, DOUBLE_MOVE);
    //public final static EnumMap<TileType, EffectType> = ; //todo

    private final TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }
}

