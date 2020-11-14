import game.tile.TileType;

import java.util.EnumSet;

import static game.tile.TileType.*;
import static game.tile.TileType.GOAL;

/**
 * The type Floor tile.
 *
 * @author mnabina, nylecm
 */
public class FloorTile extends Tile {
    public final static EnumSet<TileType> FLOOR_TILE_TYPES =
            EnumSet.of(STRAIGHT, CORNER, T_SHAPED, GOAL);

    private final boolean isFixed;
    private int paths;
    private final boolean isGoalTile;

    /**
     * Instantiates a new Floor tile.
     *
     * @param type   the type name
     * @param isFixed    the is fixed
     * @param isGoalTile the is goal tile
     */
    public FloorTile(TileType type, boolean isFixed, boolean isGoalTile) {
        super(type, FLOOR_TILE_TYPES);

        this.isFixed = isFixed;
        this.isGoalTile = isGoalTile;

        switch (type) {
            case STRAIGHT:
                paths = 9; // or 5
                break;
            case CORNER:
                paths = 12; // or 9, 6, 3
                break;
            case T_SHAPED:
                paths = 14; // or 13, 11, 7
            case GOAL:
                paths = 15;
        }
    }

    //todo creation of pre-rotated fixed tiles.

    public void rotateClockwise() {
        paths = Integer.rotateRight(paths, 1);
    }

    public void rotateAntiClockwise() {
        paths = Integer.rotateLeft(paths, 1);
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
     * Gets paths.
     *
     * @return the paths
     */
    public int getPaths() {
        return paths;
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
                ", isGoalTile=" + isGoalTile +
                '}';
    }

    public static void main(String[] args) {
        FloorTile t1 = new FloorTile(STRAIGHT, false, false);
    }
}
