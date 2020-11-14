import game.tile.TileType;

/**
 * The type Floor tile.
 * @author mnabina
 */
public class FloorTile extends Tile {
    private final boolean isFixed;
    private int paths;
    private final boolean isGoalTile;

    /**
     * Instantiates a new Floor tile.
     *
     * @param typeName   the type name
     * @param isFixed    the is fixed
     * @param paths      the paths
     * @param isGoalTile the is goal tile
     */
    public FloorTile(TileType typeName, boolean isFixed, boolean isGoalTile) {
        super(typeName);
        this.isFixed = isFixed;
        this.paths = paths;
        this.isGoalTile = isGoalTile;
        //todo calculate paths
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
}
