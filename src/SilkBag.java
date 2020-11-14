import generic_data_structures.SearchQueue;

import java.util.NoSuchElementException;

/**
 * A silk bag holds tiles of arbitrary type. Tiles can be put into the silk bag,
 * a random tile can be taken out of a silk bag, and a random floor tile can be
 * taken out of the silk bag.
 *
 * @author nylecm, paired with ashrw0
 */
public class SilkBag {
    private final SearchQueue<String> tiles = new SearchQueue<>();

    /**
     * Instantiates a new Silk bag, filling it with tiles.
     *
     * @param newTiles the tiles that ought to be added, in random order.
     */
    public SilkBag(Tile[] newTiles) {
        for (Tile tile : newTiles) {
            tiles.enqueue(tile.getTypeName());
        }
    }

    /**
     * Puts a tile into the silk bag.
     *
     * @param t the tile to be put into the silk bag.
     */
    public void put(Tile t) {
        tiles.enqueue(t.getTypeName());
    }

    /**
     * Takes a tile out of the silk bag.
     *
     * @return a random tile from the silk bag.
     * @throws NoSuchElementException when no tiles are in the silk bag.
     */
    public Tile take() throws NoSuchElementException {
        String tileType = tiles.peek();

        if (Tile.FLOOR_TILE_TYPES.contains(tileType)) {
            tiles.dequeue();
            return new FloorTile(tileType, false, 0, false);
        } else if (Tile.ACTION_TILE_TYPES.contains(tileType)) {
            tiles.dequeue();
            return new ActionTile(tileType);
        } else {
            throw new IllegalStateException("Tile of invalid type found!");
        }
    }

    /**
     * Takes a floor tile out of the silk bag.
     *
     * @return a random floor tile from the silk bag.
     * @throws NoSuchElementException when no floor tiles are in the silk bag.
     */
    public FloorTile takeFloor() throws NoSuchElementException {
        if (isLackingFloorTiles()) {
            throw new NoSuchElementException("No floor tiles in silk bag!");
        } else {
            while (!Tile.FLOOR_TILE_TYPES.contains(tiles.peek())) {
                String oldHead = tiles.peek();
                tiles.dequeue();
                tiles.enqueue(oldHead);
            }
            FloorTile returnTile = new FloorTile(tiles.peek(), false, 0, false);
            tiles.dequeue();
            return returnTile;
        }
    }

    /**
     * Checks if there are any tiles in the silk bag.
     *
     * @return true if no tiles are found.
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Checks if there are any tiles in the silk bag.
     *
     * @return true if no tiles are found.
     */
    public boolean isLackingFloorTiles() {
        return isEmpty() || !tiles.isMemberOfPresent(Tile.FLOOR_TILE_TYPES);
    }

    /*@Override
    public String toString() {
        return ""; //todo silk bag to string
    }*/

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Tile[] ts = {new ActionTile("fire")};

        SilkBag s = new SilkBag(ts);

        s.put(new ActionTile("fire"));
        s.put(new ActionTile("ice"));
        s.put(new ActionTile("backtrack"));
        s.put(new ActionTile("double_move"));
        s.put(new FloorTile("goal", false, 1111, false));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));

        System.out.println(s.takeFloor());
        //System.out.println(s.takeFloor());

        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        // s.take();
    }
}