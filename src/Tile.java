/**
 * The type Tile.
 * @author mnabina
 */
public abstract class Tile {

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
}

