import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Silk bag.
 */
public class SilkBag {
    //private Set<Integer> curPosOccupied;
    private Tile[] tiles;

    /**
     * Instantiates a new Silk bag.
     *
     * @param cap The capacity of the silk bag.
     */
    public SilkBag(int cap) {
        tiles = new Tile[cap];
        //curPosOccupied = new int[];
    }

    /**
     * Add.
     *
     * @param t the tile
     */
    public void add(Tile t) {
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
     * Take tile.
     *
     * @return the tile
     * @throws IllegalStateException the illegal state exception
     */
    public Tile take() throws IllegalStateException {
        if (isEmpty()) {
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
     * Take floor floor tile.
     *
     * @return the floor tile
     */
    public FloorTile takeFloor() {
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

    private boolean isFloorTileLess() {
        for (Tile t: tiles) {
            if (t instanceof FloorTile) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmpty() {
        for (Tile t: tiles) {
            if (t != null) {
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
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SilkBag s = new SilkBag(1000);

        s.add(new ActionTile("fire"));
        s.add(new ActionTile("ice"));
        s.add(new ActionTile("back_track"));
        s.add(new ActionTile("double_move"));
        s.add(new FloorTile("cross", false, 100, false));
        s.add(new ActionTile("Fire"));
        s.add(new ActionTile("Fire"));
        s.add(new ActionTile("Fire"));
        s.add(new ActionTile("Fire"));
        s.add(new ActionTile("Fire"));

        System.out.println(Arrays.toString(s.tiles));

        Tile t1 = s.take();


        System.out.println(Arrays.toString(s.tiles));

        s.take();
        s.take();
        s.take();
        s.take();
        s.take();

        System.out.println(s.takeFloor());

        System.out.println(Arrays.toString(s.tiles));


        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();
        s.take();

        System.out.println(Arrays.toString(s.tiles));
    }
}
