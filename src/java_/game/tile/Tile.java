package java_.game.tile;

import java.util.EnumSet;

/**
 * Represents a tile which are the building blocks of the game. It cannot be
 * instantiated. A tile is of a certain type, so it can be for example a floor
 * tile or a player tile.
 *
 * @author nylecm, original version mnabina.
 */
public abstract class Tile {
    private static final String FLOOR_TILE_NOT_OF_ALLOWED_TYPE_MSG =
            "Tile must have an appropriate typeName, for it's type.";
    protected final TileType type;

    /**
     * Requirement to instantiate a new tile that is a subclass of Tile. Checks
     * to ensure that the given tile is of a valid type for its classes' allowed
     * types.
     *
     * @param type         the TileType of the new instance.
     * @param typesAllowed a set of allowed TileType enums, for that subclass.
     */
    public Tile(TileType type, EnumSet<TileType> typesAllowed) {
        if (typesAllowed.contains(type)) {
            this.type = type;
        } else {
            throw new IllegalArgumentException(FLOOR_TILE_NOT_OF_ALLOWED_TYPE_MSG);
        }
    }

    /**
     * Gets the type of tile.
     *
     * @return TileType type of tile.
     */
    public TileType getType() {
        return type;
    }

    /**
     * Prints a string describing the tile.
     *
     * @return formatted string describing the tile.
     */
    @Override
    public String toString() {
        return "java.game.tile.Tile{" +
                "typeName='" + type.toString() + '}';
    }
}
