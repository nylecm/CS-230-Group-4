package java_.game.tile;

import java.util.EnumSet;

/**
 * The type Floor tile.
 *
 * @author mnabina, nylecm
 */
public class FloorTile extends Tile {
    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(TileType.STRAIGHT, TileType.CORNER, TileType.T_SHAPED, TileType.GOAL);

    public static final int NORTH_PATH_MASK = 0x8; //1000
    public static final int EAST_PATH_MASK = 0x4; //0100
    public static final int SOUTH_PATH_MASK = 0x2; //0010
    public static final int WEST_PATH_MASK = 0x1; //0001

    private static final String šTILE_TYPE_INVALID_MSG = "Invalid tile type entered.";

    private boolean isFixed;
    private byte pathsBits;
    private boolean isGoalTile;
    private int rotation;

    /**
     * Instantiates a new Floor tile.
     *
     * @param type    the type name
     * @param isFixed the is fixed
     */
    public FloorTile(TileType type, boolean isFixed) throws IllegalArgumentException {
        super(type, FLOOR_TILE_TYPES);

        switch (type) {
            case STRAIGHT:
                pathsBits = WEST_PATH_MASK + EAST_PATH_MASK;
                break;
            case CORNER:
                pathsBits = NORTH_PATH_MASK + WEST_PATH_MASK;
                break;
            case T_SHAPED:
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + EAST_PATH_MASK;
                break;
            case GOAL:
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + NORTH_PATH_MASK + EAST_PATH_MASK;
                break;
        }

        System.out.println("EYY " + pathsBits);
    }

    // Creates a pre-rotated tile (for fixed tiles)
    public FloorTile(TileType type, boolean isFixed, boolean isGoalTile, int rotationAmount) {
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;
        this.isGoalTile = isGoalTile;

        switch (type) {
            case STRAIGHT:
                pathsBits = WEST_PATH_MASK + EAST_PATH_MASK;
                break;
            case CORNER:
                pathsBits = NORTH_PATH_MASK + WEST_PATH_MASK;
                break;
            case T_SHAPED:
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + EAST_PATH_MASK;
                break;
            case GOAL:
                pathsBits = WEST_PATH_MASK + SOUTH_PATH_MASK + NORTH_PATH_MASK + EAST_PATH_MASK;
                break;
        }
        rotate(rotationAmount);
    }

    public void rotate(int rotationAmount) {
        for (int i = 0; i < rotationAmount; i++) {
            pathsBits = (byte) (pathsBits << 3);
            pathsBits = (byte) (((pathsBits & 0xf0) >> 4) + (pathsBits & 0xf));
        }
        rotation = rotationAmount % 4;
    }

    public int getPathsBits() {
        return pathsBits;
    }

    /**
     * Is fixed boolean.
     *
     * @return the boolean
     */
    public boolean isFixed() {
        return isFixed;
    }

    /**
     * Is goal tile boolean.
     *
     * @return the boolean
     */
    public boolean isGoalTile() {
        return isGoalTile;
    }

    public int getRotation() {
        return rotation;
    }

    public static void main(String[] args) {

    }
}
