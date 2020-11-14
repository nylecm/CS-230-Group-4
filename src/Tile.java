import java.util.Arrays;
import java.util.TreeSet;

/**
 * The type Tile.
 * @author mnabina
 */
public abstract class Tile {

    public final static TreeSet<String> FLOOR_TILE_TYPES = new TreeSet<>(Arrays.asList
            ("corner", "straight", "t_shaped", "goal"));
    public final static TreeSet<String> ACTION_TILE_TYPES = new TreeSet<>(Arrays.asList
            ("ice", "fire", "double_move", "backtrack"));


    private final String typeName;

    /**
     * Instantiates a new Tile.
     *
     * @param typeName the type name
     */
    public Tile(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets type name.
     *
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}

