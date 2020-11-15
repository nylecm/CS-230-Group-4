import game.tile.TileType;

import java.util.Arrays;
import java.util.EnumSet;

import static game.tile.TileType.*;

/**
 * The type Floor tile.
 *
 * @author mnabina, nylecm
 */
public class FloorTile extends Tile {
    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(STRAIGHT, CORNER, T_SHAPED, GOAL);

    private final static int[] GOAL_TILE_ROTATIONS = {15}; // NESW
    private final static int[] STRAIGHT_TILE_PATHS = {9, 5}; // NS, WE
    private final static int[] CORNER_TILE_PATHS = {12, 6, 3, 9}; // NE, SE, SW, NW
    private final static int[] T_SHAPED_TILE_PATHS = {14, 7, 11, 13}; // NES, ESW, SWN, WNE

    private final boolean isFixed;
    private int paths;
    private int[] availablePaths;
    private final boolean isGoalTile;

    /**
     * Instantiates a new Floor tile.
     *
     * @param type       the type name
     * @param isFixed    the is fixed
     * @param isGoalTile the is goal tile
     */
    public FloorTile(TileType type, boolean isFixed, boolean isGoalTile) {
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;
        this.isGoalTile = isGoalTile;

        switch (type) {
            case STRAIGHT:
                availablePaths = STRAIGHT_TILE_PATHS;
                paths = availablePaths[0];
                break;
            case CORNER:
                availablePaths = CORNER_TILE_PATHS;
                paths = availablePaths[0];
                break;
            case T_SHAPED:
                availablePaths = T_SHAPED_TILE_PATHS;
                paths = availablePaths[0];
                break;
            case GOAL:
                availablePaths = GOAL_TILE_ROTATIONS;
                paths = availablePaths[0];
                break;
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
        return "FloorTile{" +
                "isFixed=" + isFixed +
                ", paths=" + paths +
                ", availablePaths=" + Arrays.toString(availablePaths) +
                ", isGoalTile=" + isGoalTile +
                '}';
    }

    public static void main(String[] args) {
        FloorTile t1 = new FloorTile(STRAIGHT, false, false);
        System.out.println(t1);
        t1.rotateClockwise();
        System.out.println(t1);
        t1.rotateAntiClockwise();
        System.out.println(t1);

        System.out.println();

        FloorTile t2 = new FloorTile(CORNER, false, false);
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
        t1.rotateAntiClockwise();
        System.out.println(t2);
    }
}
