package java_;

import java.util.*;

/**
 * A silk bag holds tiles of arbitrary type. Tiles can be put into the silk bag,
 * a random tile can be taken out of a silk bag, and a random floor tile can be
 * taken out of the silk bag.
 *
 * @author nylecm, paired with ashrw0, help from Matej Hladky
 */
public class SilkBag {
    private final Queue<TileType> tiles = new LinkedList<>();

    /**
     * Instantiates a new Silk bag, filling it with tiles.
     *
     * @param newTiles the tiles that ought to be added, they do not need to be
     *                 in random order.
     */
    public SilkBag(List<Tile> newTiles) {
        Collections.shuffle(newTiles);
        for (Tile tile : newTiles) {
            tiles.add(tile.getType());
        }
    }

    /**
     * Instantiates a new empty silk bag.
     */
    public SilkBag() {
    }

    /**
     * Puts a tile into the silk bag.
     *
     * @param t the tile type of the tile to be put into the silk bag.
     */
    public void put(TileType t) {
        tiles.add(t);
    }

    /**
     * Takes a tile out of the silk bag.
     *
     * @return a random tile from the silk bag.
     * @throws NoSuchElementException when no tiles are in the silk bag.
     */
    public Tile take() throws NoSuchElementException {
        TileType tileType = tiles.peek();

        if (FloorTile.FLOOR_TILE_TYPES.contains(tileType)) {
            tiles.remove();
            return new FloorTile(tileType);
        } else {
            tiles.remove();
            return new ActionTile(tileType);
        }
    }

    /**
     * Checks if silk bag is empty.
     *
     * @return true if the silk bag is empty.
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Gets the size of the silk bag,
     *
     * @return the number of tiles in the silk bag.
     */
    public int size() {
        return tiles.size();
    }

    /**
     * Prints the contents of the silk bag.
     *
     * @return a string that lists the tiles in the silk bag ordered from the
     * the next item that will be taken out to the last item that will be taken
     * out.
     */
    @Override
    public String toString() {
        return tiles.toString();
    }
}
