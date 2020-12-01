package java_.game.tile;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The type Floor tile.
 *
 * @author mnabina, nylecm
 */
public class FloorTile extends Tile {
    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(TileType.STRAIGHT, TileType.CORNER, TileType.T_SHAPED, TileType.GOAL);

    private final static int[] GOAL_TILE_ROTATIONS = {15}; // NESW
    private final static int[] STRAIGHT_TILE_PATHS = {10, 5}; // NS, WE
    // rotation = 0  1  2  3
    private final static int[] CORNER_TILE_PATHS = {12, 6, 3, 9}; // NE, SE, SW, NW
    private final static int[] T_SHAPED_TILE_PATHS = {14, 7, 11, 13}; // NES, ESW, SWN, WNE

    private final static String TILE_TYPE_INVALID_MSG = "Invalid tile type entered.";

    private final boolean isFixed;
    private int paths;
    private int[] availablePaths;
    private final boolean isGoalTile;

    /**
     * Instantiates a new Floor tile.
     *
     * @param type    the type name
     * @param isFixed the is fixed
     */
    public FloorTile(TileType type, boolean isFixed) throws IllegalArgumentException {
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;

        switch (type) {
            case STRAIGHT:
                availablePaths = STRAIGHT_TILE_PATHS;
                paths = availablePaths[0];
                isGoalTile = false;
                break;
            case CORNER:
                availablePaths = CORNER_TILE_PATHS;
                paths = availablePaths[0];
                isGoalTile = false;
                break;
            case T_SHAPED:
                availablePaths = T_SHAPED_TILE_PATHS;
                paths = availablePaths[0];
                isGoalTile = false;
                break;
            case GOAL:
                isGoalTile = true;
                availablePaths = GOAL_TILE_ROTATIONS;
                paths = availablePaths[0];
                break;
            default:
                throw new IllegalArgumentException(TILE_TYPE_INVALID_MSG);
        }
    }

    // Creates a pre-rotated tile (for fixed tiles)
    public FloorTile(TileType type, boolean isFixed, boolean isGoalTile, int rotationAmount) { //todo consider restricting this method for fixed tiles only...
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;
        this.isGoalTile = isGoalTile;

        switch (type) {
            case STRAIGHT:
                availablePaths = STRAIGHT_TILE_PATHS;
                paths = availablePaths[rotationAmount % availablePaths.length];
                break;
            case CORNER:
                availablePaths = CORNER_TILE_PATHS;
                paths = availablePaths[rotationAmount % availablePaths.length];
                break;
            case T_SHAPED:
                availablePaths = T_SHAPED_TILE_PATHS;
                paths = availablePaths[rotationAmount % availablePaths.length];
                break;
            case GOAL:
                availablePaths = GOAL_TILE_ROTATIONS;
                paths = availablePaths[rotationAmount % availablePaths.length];
                break;
        }
    }

    public void rotateClockwise() {
        int currentPathIndex = -1;

        for (int i = 0; i < availablePaths.length; i++) {
            if (availablePaths[i] == paths) {
                currentPathIndex = i;
            }
        }

        if (currentPathIndex == availablePaths.length - 1) {
            paths = availablePaths[0];
        } else {
            paths = availablePaths[currentPathIndex + 1];
        }
    }

    public void rotateClockwise(int rotationAmount) {
        for (int i = 0; i < rotationAmount; i++) {
            rotateClockwise();
        }
    }

    public void rotateAntiClockwise() {
        int currentPathIndex = -1;

        for (int i = 0; i < availablePaths.length; i++) {
            if (availablePaths[i] == paths) {
                currentPathIndex = i;
            }
        }

        if (currentPathIndex == 0) {
            paths = availablePaths[availablePaths.length - 1];
        } else {
            paths = availablePaths[currentPathIndex - 1];
        }
    }

    /**
     * Gets paths.
     *
     * @return the paths
     */
    public int getPaths() {
        return paths;
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

    @Override
    public String toString() {
        return "game.tile.FloorTile{" +
                "isFixed=" + isFixed +
                ", paths=" + paths +
                ", availablePaths=" + Arrays.toString(availablePaths) +
                ", isGoalTile=" + isGoalTile +
                '}';
    }

    public static void main(String[] args) {
        FloorTile t1 = new FloorTile(TileType.STRAIGHT, false);
        System.out.println(t1);
        t1.rotateClockwise();
        System.out.println(t1);
        t1.rotateAntiClockwise();
        System.out.println(t1);

        System.out.println();

        FloorTile t2 = new FloorTile(TileType.T_SHAPED, false);
        System.out.println(t2);
        t2.rotateClockwise();
        System.out.println(t2);
        t2.rotateClockwise();
        System.out.println(t2);
        t2.rotateClockwise();
        System.out.println(t2);
        t2.rotateClockwise();
        System.out.println(t2);

        System.out.println();

        System.out.println(t2);
        t2.rotateAntiClockwise();
        System.out.println(t2);
        t2.rotateAntiClockwise();
        System.out.println(t2);
        t2.rotateAntiClockwise();
        System.out.println(t2);
        t2.rotateAntiClockwise();
        System.out.println(t2);

        System.out.println();

        FloorTile t3 = new FloorTile(TileType.GOAL, false);
        System.out.println(t3);
        t3.rotateClockwise();
        System.out.println(t3);
        t3.rotateAntiClockwise();
        System.out.println(t3);
    }
}
