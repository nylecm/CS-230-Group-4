public class SilkBag {
    private Tile[] tiles;

    public SilkBag(int cap) {
        tiles = new Tile[cap];
    }

    public void add(Tile t) {
        System.out.println("You've been had to believe this works!");
    }

    public Tile take() {
        return null;
    }

    public Tile takeFloor() {
        return null;
    }
}
