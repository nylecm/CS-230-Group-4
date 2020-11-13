import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A silk bag holds tiles of arbitrary type. Tiles can be put into the silk bag,
 * a random tile can be taken out of a silk bag, and a random floor tile can be
 * taken out of the silk bag.
 *
 * @author nylecm, paired with ashrw0
 */
public class SilkBag {
    private final HashMap<String, Integer> tiles = new HashMap<>();
    //todo consider storing in tile class. *************************************
    public final static TreeSet<String> FLOOR_TILE_TYPES = new TreeSet<>(Arrays.asList
            ("corner", "straight", "t_shaped", "goal"));
    public final static TreeSet<String> ACTION_TILE_TYPES = new TreeSet<>(Arrays.asList
            ("ice", "fire", "double_move", "backtrack"));
    public final static TreeSet<String> TILE_TYPES =
            mergeSets(FLOOR_TILE_TYPES, ACTION_TILE_TYPES);

    private static TreeSet<String> mergeSets(TreeSet<String> s, TreeSet<String> s2) {
        s.addAll(s2);
        return s;
    }

    /**
     * Instantiates a new Silk bag
     */
    public SilkBag() {
        for (String tile_type : TILE_TYPES) {
            tiles.put(tile_type, 0);
        }
    }

    /**
     * Puts a tile into the silk bag.
     *
     * @param t the tile to be put into the silk bag.
     */
    public void put(Tile t) {
       // boolean isTileAdded = false;

        if (TILE_TYPES.contains(t.getTypeName())) {
            Integer numType = tiles.get(t.getTypeName()); //todo null check?? TILES MUST BE NAMED CORRECTLY!!!!
            numType++;
            tiles.put(t.getTypeName(), numType);
        } else {
            throw new IllegalArgumentException("This type of tile is not " +
                    "supported by the silk bag.");
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
        } else {
            int[] tally = new int[TILE_TYPES.size()];
            String[] types = new String[TILE_TYPES.size()];
            int tileCount = 0;
            int i = 0;

            for (String tileType : TILE_TYPES) {
                types[i] = tileType;

                tally[i] = tiles.get(tileType);
                tileCount += tally[i];
                i++;
            }
            int random = ThreadLocalRandom.current().nextInt(0, tileCount);

            i = 0;

            while (random > 0) { //todo check
                random -= tally[i];
                i++;
            }

            if (FLOOR_TILE_TYPES.contains(types[i])) {
                tiles.put(types[i], tally[i] - 1);
                return new FloorTile(types[i], false, 999, false); //todo update floor tile.
            } else if (ACTION_TILE_TYPES.contains(types[i])) {
                tiles.put(types[i], tally[i] - 1);
                return new ActionTile(types[i]);
            } else {
                throw new IllegalStateException(""); //todo write
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
        } else {
            int[] tally = new int[FLOOR_TILE_TYPES.size()];
            String[] types = new String[FLOOR_TILE_TYPES.size()];
            int tileCount = 0;
            int i = 0;

            for (String tileType : FLOOR_TILE_TYPES) {
                types[i] = tileType;

                tally[i] = tiles.get(tileType);
                tileCount += tally[i];
                i++;
            }
            int random = ThreadLocalRandom.current().nextInt(0, tileCount);

            i = 0;

            while (random > 0) { //todo check
                random -= tally[i];
                i++;
            }

            if (FLOOR_TILE_TYPES.contains(types[i])) {
                tiles.put(types[i], tally[i] - 1);
                return new FloorTile(types[i], false, 999, false); //todo update floor tile.
            } else
                throw new IllegalStateException(""); //todo write
        }
    }

    /**
     * Checks if there are any tiles in the silk bag.
     *
     * @return true if no tiles are found.
     */
    private boolean isTileLess() {
        int tileCount = 0;
        for (String tileType : TILE_TYPES) {
            tileCount += tiles.get(tileType);
        }
        return tileCount == 0;
    }

    /**
     * Checks if there are any floor tiles in the silk bag.
     *
     * @return true if no floor tiles are found.
     */
    private boolean isFloorTileLess() {
        int tileCount = 0;
        for (String tileType : FLOOR_TILE_TYPES) {
            tileCount += tiles.get(tileType);
        }
        return tileCount == 0;
    }

    @Override
    public String toString() {
        String s = "{";
        for (String tileType : TILE_TYPES) {
            s += "{ " + tileType + ": " + tiles.get(tileType) + "}";
        }
        return s += "}";
    }

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SilkBag s = new SilkBag(); //todo rewrite

        s.put(new ActionTile("fire"));
        s.put(new ActionTile("ice"));
        s.put(new ActionTile("backtrack"));
        s.put(new ActionTile("double_move"));
        s.put(new FloorTile("goal", false, 1111, false));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));
        s.put(new ActionTile("fire"));

        System.out.println(s.toString());
    }
}

