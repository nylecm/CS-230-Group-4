package java_.game.tile;

import java.util.EnumSet;

/**
 * Represents a tile which is the main 'currency' of the game. A tile is of
 * a certain type todo
 *
 * @author mnabina, nylecm
 */
public abstract class Tile {
    private static final String FLOOR_TILE_NOT_OF_ALLOWED_TYPE_MSG =
            "Tile must have an appropriate typeName, for it's type.";
    protected final TileType type;

    public Tile(TileType type, EnumSet<TileType> typesAllowed) {
        if (typesAllowed.contains(type)) {
            this.type = type;
        } else {
            throw new IllegalArgumentException(FLOOR_TILE_NOT_OF_ALLOWED_TYPE_MSG);
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

