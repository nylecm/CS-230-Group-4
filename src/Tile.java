import game.tile.TileType;

import java.util.EnumSet;

/**
 * The type Tile.
 *
 * @author mnabina, nylecm
 */
public abstract class Tile {
    protected final TileType type;

    public Tile(TileType type, EnumSet<TileType> typesAllowed) {
        if (typesAllowed.contains(type)) {
            this.type = type ;
        } else {
            throw new IllegalArgumentException("Floor tile must have a floor tile typeName.");
        }
    }

    public TileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}

