package java_.game.controller;

import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.game.player.PlayerService;
import java_.game.tile.*;
import java_.util.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GameService {
    private static GameService instance = null;

    private GameBoard gb;
    private final PlayerService ps;
    private SilkBag silkBag;
    private int turnCount;
    private boolean isWin;

    private static final String DELIMITER = "` ";
    private static final String GAME_BOARD_FILE_PATH = "data/game_board.txt";

    private GameService() {
        ps = PlayerService.getInstance().remake();
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void loadNewGame(Player[] players, String boardName) throws FileNotFoundException {
        remake();

        Scanner in = new Scanner(new File(GAME_BOARD_FILE_PATH));
        in.useDelimiter(DELIMITER);
        gb = readSelectGameBoard(boardName, players.length, in);
        in.close();

        //read player file for
        ps.setPlayers(players);
    }

    private GameBoard readSelectGameBoard(String boardName, int nPlayers, Scanner in) throws IllegalArgumentException {
        while (in.hasNextLine() && in.next().equals(boardName)) {//todo to be completed fully when other classes complete...
            int nRows = in.nextInt();
            int nCols = in.nextInt();

            // Dealing with fixed tiles:
            int nFixedTiles = in.nextInt();
            FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
            Position[] fixedTilePositions = new Position[nFixedTiles];

            for (int i = 0; i < nFixedTiles; i++) {
                int rotation = in.nextInt();
                int row = in.nextInt();
                int col = in.nextInt();
                TileType tileType = TileType.valueOf(in.next().toUpperCase());

                Position p = new Position(row, col);
                FloorTile t = new FloorTile(tileType, true, false, rotation);
                fixedTiles[i] = t;
                fixedTilePositions[i] = p;
            }

            // Dealing with non-fixed floor tiles:
            ArrayList<FloorTile> floorTiles = readFloorTiles(in);
            Collections.shuffle(floorTiles);

            // Taking first floor tiles for the initial set to populate game board.
            FloorTile[] floorTilesForGameBoard = getFloorTilesForGameBoard
                    (nRows, nCols, nFixedTiles, floorTiles);

            //Action tiles:
            ArrayList<ActionTile> actionTiles = readActionTiles(in);
            Collections.shuffle(actionTiles);

            // Silk B
            // Player Pieces:
            Position[] playerPiecePositions = readPlayerPiecePositions(nPlayers, in);
            // todo player service...
            ArrayList<Tile> tilesForSilkBag = new ArrayList<>();
            tilesForSilkBag.addAll(floorTiles);
            tilesForSilkBag.addAll(actionTiles);

            silkBag = new SilkBag(tilesForSilkBag.toArray(new Tile[0]));

            return new GameBoard(playerPiecePositions, fixedTiles, fixedTilePositions,
                    floorTilesForGameBoard, nCols, nRows, boardName); // todo consider keeping silk bag in game service...
        }
        throw new IllegalArgumentException("No level with such name found!");
    }

    private ArrayList<FloorTile> readFloorTiles(Scanner in) {
        ArrayList<FloorTile> floorTiles = new ArrayList<>();

        for (int i = 0; i < FloorTile.FLOOR_TILE_TYPES.size(); i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            int nOfThisType = in.nextInt();
            FloorTile t = new FloorTile(tileType, false);

            for (int j = 0; j < nOfThisType; j++) {
                floorTiles.add(t);
            }
        }
        return floorTiles;
    }

    private FloorTile[] getFloorTilesForGameBoard
            (int nRows, int nCols, int nFixedTiles, ArrayList<FloorTile> floorTiles) {
        FloorTile[] floorTilesForGameBoard = new FloorTile[(nRows * nCols) - nFixedTiles]; // todo check if nFixed Tiles is greater than nCol * nRow

        for (int i = 0; i < (nRows * nCols) - nFixedTiles; i++) { //todo check if there are enough tiles for the game board...
            floorTilesForGameBoard[i] = floorTiles.get(0);
            floorTiles.remove(0);
        }
        return floorTilesForGameBoard;
    }

    private ArrayList<ActionTile> readActionTiles(Scanner in) {
        ArrayList<ActionTile> actionTiles = new ArrayList<>();

        for (int i = 0; i < ActionTile.ACTION_TILE_TYPES.size(); i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            int n = in.nextInt();
            ActionTile t = new ActionTile(tileType);

            for (int j = 0; j < n; j++) {
                actionTiles.add(t);
            }
        }
        return actionTiles;
    }

    private Position[] readPlayerPiecePositions(int nPlayers, Scanner in) {
        Position[] positions = new Position[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int startRow = in.nextInt(); //todo load player piece positions...
            int startCol = in.nextInt();
            positions[i] = new Position(startRow, startCol);
        }
        return positions;
    }


    public void loadSavedInstance(File f) throws FileNotFoundException {
        remake(); //todo future homer's problem

        Scanner in = new Scanner(f);
        in.useDelimiter(DELIMITER);

        //int nPlayers = in.nextInt();
        String name = in.next();
        int nRows = in.nextInt();
        int nCols = in.nextInt();
        int turnCount = in.nextInt();
        in.nextLine();

        FloorTile[] floorTilesForGameBoard = new FloorTile[nRows * nCols];
        //effect map...

        for (int i = 0; i < nRows * nCols; i++) {
            int paths = in.nextInt();
            boolean isFixed = in.nextBoolean();
            boolean hasEffect = in.nextBoolean();
            EffectType effectType = EffectType.valueOf(in.next());
            int remainingDur = in.nextInt();
            String radiusStr = in.next();
            int radius = Integer.parseInt(radiusStr);
        }
        in.nextLine();

        silkBag = new SilkBag();

        while (in.hasNext()) {
            silkBag.put(TileType.valueOf(in.next()));
        }

        int nPlayers = 0;
        List<Player> players = new ArrayList<>();

        while (in.hasNextLine()) {

            nPlayers++;
            String username = in.next();
            int rowNum = in.nextInt();
            int colNum = in.nextInt();
            int numOfDrawnActionTiles = in.nextInt();
          //  Player player = new Player(username, );
        }

        //GameBoard = new GameBoard();
        //PlayerService = new PlayerService();
    }

    public void gameplayLoop() { // todo gameplay loop...
        while (!isWin) {
            ps.playerTurn(ps.getPlayer(turnCount % ps.getPlayers().length)); // todo improve player service
            System.out.println("Have fun!");
            gb.refreshEffects(); // todo check
            turnCount++;
        }
    }

    public void save(String saveFileName) throws IOException { //todo
        File gameSaveFile = createFile(saveFileName);

        PrintWriter out = null;
        out = new PrintWriter(gameSaveFile);

        writeGameInstanceDetails(out);
        writeGameBoardInstanceTileDetails(out);
        writeSilkBagInstanceDetails(out);
        writePlayerInstanceDetailsForAllPlayers(out);

        out.flush();
        out.close();
    }

    private File createFile(String fileName) throws IOException {
        File gameSaveFile = new File("data/saves/" + fileName + ".txt");

        boolean isFileCreated = false;
        final int limitOfFilesWithSameName = 256;
        int filesWithSameName = 0;

        while (!isFileCreated && filesWithSameName < limitOfFilesWithSameName) {
            if (gameSaveFile.createNewFile()) {
                isFileCreated = true;
                System.out.println("File Created!");
            } else {
                filesWithSameName++;
                gameSaveFile = new File("data/" + fileName + "_" + filesWithSameName + ".txt");
                System.out.println("File not created yet!");
            }
        }

        if (!isFileCreated) {
            throw new IllegalArgumentException("Too many files with same name!");
        }
        return gameSaveFile;
    }

    private void writeGameInstanceDetails(PrintWriter out) {
        out.print(ps.getPlayers().length); // Number of players
        out.print(DELIMITER);
        out.print(gb.getName());
        out.print(DELIMITER);
        out.print(gb.getnRows());
        out.print(DELIMITER);
        out.print(gb.getnCols());
        out.print(DELIMITER);
        out.print(turnCount);
        out.print(DELIMITER);
        out.print('\n');
    }

    private void writeGameBoardInstanceTileDetails(PrintWriter out) {
        for (int i = 0; i < gb.getnRows(); i++) {
            for (int j = 0; j < gb.getnCols(); j++) {
                out.print(gb.getTileAt(i, j).getPaths());
                out.print(DELIMITER);
                out.print(gb.getTileAt(i, j).isFixed());
                out.print(DELIMITER);

                if (gb.getEffectAt(new Position(i, j)) != null) {
                    out.print(true);
                    out.print(DELIMITER);
                    out.print(gb.getEffectAt(new Position(i, j)).getEffectType());
                    out.print(DELIMITER);
                    out.print(gb.getEffectAt(new Position(i, j)).getRemainingDuration());
                    out.print(DELIMITER);
                    out.print(gb.getEffectAt(new Position(i, j)).getRadius());
                    out.print(DELIMITER);
                } else {
                    out.print(false);
                    out.print(DELIMITER);
                }
            }
        }
        out.print('\n');
    }

    private void writeSilkBagInstanceDetails(PrintWriter out) {
        while (!silkBag.isEmpty()) {
            out.print(silkBag.take().getType());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    private void writePlayerInstanceDetailsForAllPlayers(PrintWriter out) {
        for (int i = 0; i < ps.getPlayers().length; i++) {
            writePlayerInstanceDetails(out, i);
        }
    }

    private void writePlayerInstanceDetails(PrintWriter out, int i) {
        out.print(ps.getPlayer(i).getUsername());
        out.print(DELIMITER);
        out.print(gb.getPlayerPiecePosition(i).getRowNum());
        out.print(DELIMITER);
        out.print(gb.getPlayerPiecePosition(i).getColNum());
        out.print(DELIMITER);

        out.print(ps.getPlayer(i).getDrawnActionTiles().size()); // N of drawn action tiles.
        out.print(DELIMITER);

        for (ActionTile actionTile : ps.getPlayer(i).getDrawnActionTiles()) {
            out.print(actionTile.getType().toString());
            out.print(DELIMITER);
        }

        for (Effect effect : ps.getPlayer(i).getPreviousAppliedEffect()) {
            out.print(effect.getEffectType().toString());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    public void destroy() {
        instance = null;
    }

    public GameService remake() {
        return new GameService();
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameService gs = GameService.getInstance();
        gs.loadNewGame(
                new Player[]{new Player("dd", "bob", 0, 1111, false, new PlayerPiece())}, "oberon_1");
        System.out.println(gs.gb);
        //gs.gb.insert(-1, 0, new FloorTile(TileType.STRAIGHT, false), 0);
        System.out.println(gs.gb);

        AreaEffect effect = new AreaEffect(EffectType.FIRE, 1, 3);

        gs.gb.applyEffect(effect, new Position(1, 0));
        for (Position pos : gs.gb.getPositionsWithActiveEffects()) {
            System.out.println(pos.getRowNum() + " " + pos.getColNum());
        }

        AreaEffect test = gs.gb.getActiveEffects().get(new Position(0, 0));
        System.out.println(test);

        gs.gb.insert(0, 5, new FloorTile(TileType.STRAIGHT, false), 0);
        System.out.println();

        for (Position pos : gs.gb.getPositionsWithActiveEffects()) {
            System.out.println(pos.getRowNum() + " " + pos.getColNum());
        }

        System.out.println(test);
        System.out.println(gs.gb.getPlayerPiecePosition(0));

        try {
            gs.save("faron");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private class FloorTilePositionBundle {
        private FloorTile floorTile;
        private Position position;

        public FloorTilePositionBundle(FloorTile floorTile, Position position) {
            this.floorTile = floorTile;
            this.position = position;
        }

        public FloorTile getFloorTile() {
            return floorTile;
        }

        public void setFloorTile(FloorTile floorTile) {
            this.floorTile = floorTile;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }
    }*/

    public SilkBag getSilkBag() {
        return silkBag;
    }
}
