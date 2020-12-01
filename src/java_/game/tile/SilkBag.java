package java_.game.tile;

import java_.util.generic_data_structures.Queue;

import java.util.NoSuchElementException;

/**
 * A silk bag holds tiles of arbitrary type. Tiles can be put into the silk bag,
 * a random tile can be taken out of a silk bag, and a random floor tile can be
 * taken out of the silk bag.
 *
 * @author nylecm, paired with ashrw0
 */
public class SilkBag {
    private final Queue<TileType> tiles = new Queue<>();

    /**
     * Instantiates a new Silk bag, filling it with tiles.
     *
     * @param newTiles the tiles that ought to be added, in random order.
     */
    public SilkBag(Tile[] newTiles) {
        for (Tile tile : newTiles) {
            tiles.enqueue(tile.getType());
        }
    }

    /**
     * Instantiates a new silk bag without filling it with tiles.
     */
    public SilkBag() {
    }

    /**
     * Puts a tile into the silk bag.
     *
     * @param t the tile to be put into the silk bag.
     */
    public void put(TileType t) {
        tiles.enqueue(t);
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
            tiles.dequeue();
            return new FloorTile(tileType, false);
        } else {
            tiles.dequeue();
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

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     *//*
    public static void main(String[] args) {
        Tile[] ts = {new ActionTile(TileType.FIRE)};

        SilkBag s = new SilkBag(ts);

        s.put(TileType.FIRE);
        s.put(TileType.ICE);
        s.put(TileType.BACKTRACK);
        s.put(TileType.DOUBLE_MOVE);
        s.put(TileType.STRAIGHT);
        s.put(TileType.FIRE);
        s.put(TileType.FIRE);
        s.put(TileType.FIRE);
        s.put(TileType.FIRE);

        System.out.println(s);

        s.take();
        s.take();
        s.take();
        s.take();
        s.take();

        System.out.println(s);

        s.take();
        s.take();
        s.take();
        s.take();
        s.take();

        System.out.println(s);
        //s.take();
    }*/
}
