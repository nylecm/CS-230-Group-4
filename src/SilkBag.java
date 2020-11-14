import game.tile.TileType;
import generic_data_structures.Queue;

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
     * Puts a tile into the silk bag.
     *
     * @param t the tile to be put into the silk bag.
     */
    public void put(Tile t) {
        tiles.enqueue(t.getType());
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
            return new FloorTile(tileType, false, false);
        } else {
            tiles.dequeue();
            return new ActionTile(tileType);
        }
    }

    @Override
    public String toString() {
        return tiles.toString();
    }

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Tile[] ts = {new ActionTile(TileType.FIRE)};

        SilkBag s = new SilkBag(ts);

        s.put(new ActionTile(TileType.FIRE));
        s.put(new ActionTile(TileType.ICE));
        s.put(new ActionTile(TileType.BACKTRACK));
        s.put(new ActionTile(TileType.DOUBLE_MOVE));
        s.put(new FloorTile(TileType.STRAIGHT, false, false));
        s.put(new ActionTile(TileType.FIRE));
        s.put(new ActionTile(TileType.FIRE));
        s.put(new ActionTile(TileType.FIRE));
        s.put(new ActionTile(TileType.FIRE));

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
    }
}