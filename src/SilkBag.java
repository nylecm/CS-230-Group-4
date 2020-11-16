import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A silk bag holds tiles of arbitrary type. Tiles can be put into the silk bag,
 * a random tile can be taken out of a silk bag, and a random floor tile can be
 * taken out of the silk bag.
 *
 * @author nylecm, paired with ashrw0
 */
public class SilkBag {
    private final Tile[] tiles;

    /**
     * Instantiates a new Silk bag, of a set capacity.
     *
     * @param cap The capacity of the silk bag.
     */
    public SilkBag(int cap) {
        tiles = new Tile[cap];
    }

    /**
     * Puts a tile into the silk bag.
     *
     * @param t the tile to be put into the silk bag.
     */
    public void put(Tile t) {
        boolean isTileAdded = false;
        int i = 0;

        while (!isTileAdded && i < tiles.length) {
            if (tiles[i] == null) {
                tiles[i] = t;
                isTileAdded = true;
            }
            i++;
        }
    }

    /**
     * Takes a random tile out of the silk bag.
     *
     * @return a random tile from the silk bag.
     * @throws IllegalStateException when no tiles are in the silk bag.
     */
    public Tile take() throws IllegalStateException {
        if (isTileLess()) {
            throw new IllegalStateException("No tiles in silk bag!");
        }

        while (true) {
            int random = ThreadLocalRandom.current().nextInt(0, tiles.length);

            if (tiles[random] != null) {
                Tile returnTile = tiles[random];
                tiles[random] = null;
                return returnTile;
            }
        }
    }

    /**
     * Takes a random floor tile out of the silk bag.
     *
     * @return a random floor tile from the silk bag.
     * @throws IllegalStateException when no floor tiles are in the silk bag.
     */
    public FloorTile takeFloor() throws IllegalStateException {
        if (isFloorTileLess()) {
            throw new IllegalStateException("No floor tiles in silk bag!");
        }

        while (true) {
            int random = ThreadLocalRandom.current().nextInt(0, tiles.length);

            if (tiles[random] != null && tiles[random] instanceof FloorTile) {
                FloorTile returnTile = (FloorTile) tiles[random];
                tiles[random] = null;
                return returnTile;
            }
        }
    }

    /**
     * Checks if there are any tiles in the silk bag.
     *
     * @return true if no tiles are found.
     */
    private boolean isTileLess() {
        for (Tile t : tiles) {
            if (t != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there are any floor tiles in the silk bag.
     *
     * @return true if no floor tiles are found.
     */
    private boolean isFloorTileLess() {
        for (Tile t : tiles) {
            if (t instanceof FloorTile) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "SilkBag{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SilkBag s = new SilkBag(1000);

        s.put(new ActionTile("fire"));
        s.put(new ActionTile("ice"));
        s.put(new ActionTile("back_track"));
        s.put(new ActionTile("double_move"));
        s.put(new FloorTile("cross", false, 1111, false));
        s.put(new ActionTile("Fire"));
        s.put(new ActionTile("Fire"));
        s.put(new ActionTile("Fire"));
        s.put(new ActionTile("Fire"));
        s.put(new ActionTile("Fire"));

        System.out.println(Arrays.toString(s.tiles));

        Tile t1 = s.take();

        System.out.println(Arrays.toString(s.tiles));


        System.out.println(s.takeFloor());

        s.take();
        s.take();
        s.take();
        s.take();
        s.take();


        System.out.println(Arrays.toString(s.tiles));


        /*s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();*/

        System.out.println(Arrays.toString(s.tiles));
    }
}
