package java_.game.tile;

import java.util.EnumSet;

/**
 * The type java.game.tile.Tile.
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
        return "java.game.tile.Tile{" +
                "typeName='" + type.toString() + '\'' +
                '}';
    }
}

