package java_.game.tile;

import java.util.EnumSet;

/**
 * Represents a floor tile, which is a type of Tile that makes up the game board.
 * A floor tile has paths in upto 4 directions, can be fixed, a winning tile, and
 * it keeps track of how many times it has been rotated clockwise from its default
 * position.
 *
 * @author nylecm and bitmask improved by Matej Hlaky
 */
public class FloorTile extends Tile {
    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(TileType.STRAIGHT, TileType.CORNER, TileType.T_SHAPED, TileType.GOAL);

    public static final int NORTH_PATH_MASK = 0x8; //1000
    public static final int EAST_PATH_MASK = 0x4; //0100
    public static final int SOUTH_PATH_MASK = 0x2; //0010
    public static final int WEST_PATH_MASK = 0x1; //0001

    //private static final String TILE_TYPE_INVALID_MSG = "Invalid tile type entered.";

    private final boolean isFixed;
    private byte pathsBits;
    private boolean isGoalTile;
    private int rotation;

    /**
     * Instantiates a new Floor tile, of a set type.
     *
     * @param type the TileType of the new floor tile (eg CORNER).
     */
    public FloorTile(TileType type) throws IllegalArgumentException {
        super(type, FLOOR_TILE_TYPES);
        isFixed = false;

        switch (type) {
            case STRAIGHT:
                isGoalTile = false;
                pathsBits = WEST_PATH_MASK + EAST_PATH_MASK;
                break;
            case CORNER:
                isGoalTile = false;
                pathsBits = NORTH_PATH_MASK + WEST_PATH_MASK;
                break;
            case T_SHAPED:
                isGoalTile = false;
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + EAST_PATH_MASK;
                break;
            case GOAL:
                isGoalTile = true;
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + NORTH_PATH_MASK + EAST_PATH_MASK;
                break;
        }
    }

    /**
     * Instantiates a pre-rotated floor tile that can be fixed or not fixed.
     *
     * @param type           the TileType of the new floor tile (eg CORNER).
     * @param isFixed        true if the tile the new tile ought to be fixed.
     * @param rotationAmount number of clockwise rotations from default value
     *                       that ought to be performed on the tile.
     */
    public FloorTile(TileType type, boolean isFixed, int rotationAmount) {
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;

        switch (type) {
            case STRAIGHT:
                isGoalTile = false;
                pathsBits = WEST_PATH_MASK + EAST_PATH_MASK;
                break;
            case CORNER:
                isGoalTile = false;
                pathsBits = NORTH_PATH_MASK + WEST_PATH_MASK;
                break;
            case T_SHAPED:
                isGoalTile = false;
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + EAST_PATH_MASK;
                break;
            case GOAL:
                isGoalTile = true;
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + NORTH_PATH_MASK + EAST_PATH_MASK;
                break;
        }
        rotate(rotationAmount);
    }

    /**
     * Rotates a tile's paths clockwise.
     *
     * @param rotationAmount the amount of times the tile ought to be rotated clockwise.
     */
    public void rotate(int rotationAmount) {
        for (int i = 0; i < rotationAmount; i++) {
            pathsBits = (byte) (pathsBits << 3);
            pathsBits = (byte) (((pathsBits & 0xf0) >> 4) + (pathsBits & 0xf));
        }
        rotation = rotationAmount % 4;
    }

    /**
     * Gets the tile's rotation.
     *
     * @return number of times the tile has been rotated clockwise from the
     * default position.
     */
    public int getRotation() {
        return rotation;
    }

    public byte getPathsBits() {
        return pathsBits;
    }

    /**
     * Is fixed boolean.
     *
     * @return true if the floor tile is fixed.
     */
    public boolean isFixed() {
        return isFixed;
    }

    /**
     * Is goal tile boolean.
     *
     * @return true if the tile is a goal tile.
     */
    public boolean isGoalTile() {
        return isGoalTile;
    }

    /**
     * Prints a string describing the tile.
     *
     * @return formatted string describing the tile.
     */
    @Override
    public String toString() {
        return "FloorTile{" +
                "isFixed=" + isFixed +
                ", pathsBits=" + pathsBits +
                ", isGoalTile=" + isGoalTile +
                ", rotation=" + rotation +
                ", type=" + type +
                '}';
    }
}
