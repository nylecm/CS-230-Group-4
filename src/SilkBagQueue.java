import java.util.*;

public class SilkBagQueue {

    private Queue<Tile> bag = new ArrayDeque<>();

    public SilkBagQueue(List<Tile> tiles) {
        Collections.shuffle(tiles);
        this.bag.addAll(tiles);
    }

    public Tile take() {
        return bag.poll();
    }
}
