package java_.game.controller;

import java_.controller.GameController;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import java_.game.player.PlayerService;
import java_.game.tile.*;
import java_.util.Position;
import java_.util.generic_data_structures.Link;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GameService {
    private static GameService instance = null;
    private GameController controller;

    private GameBoard gameBoard;
    private final PlayerService playerService;
    private SilkBag silkBag;
    private int turnCount;
    private boolean isWin;
    private boolean actionTilePlayed; //TODO Move somewhere else?
    private boolean playerPieceMoved; //TODO Move somewhere else?

    private static final String DELIMITER = "`";
    private static final String FILE_WORD_SPACER = "_";
    private static final String DATA_FILE_EXTENSION = ".txt";
    private static final String GAME_BOARD_FILE_PATH =
            "data/game_board" + DATA_FILE_EXTENSION;
    private static final String SAVE_GAME_FILE_PATH = "data/saves/";
    private static final String NO_LEVEL_WITH_SUCH_NAME_MSG =
            "No level with such name found!";

    private static final int MAX_NUM_OF_SAVE_FILES_WITH_SAME_NAME = 256;

    private GameService() {
        playerService = PlayerService.getInstance().remake();
    }

    /**
     * Returns the current instance of GameService. If there isn't one, a new one is made and returned.
     *
     * @return The current or new instance of GameService.
     */
    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    /**
     * Sets a new instance of GameService to be a game that had been previously been saved to a file.
     *
     * @param players   All players participating in the game.
     * @param boardName The name of the board the game is being played on.
     * @throws FileNotFoundException If the gameboard file path cannot be found an exception is thrown.
     */
    public void loadNewGame(Player[] players, String boardName) throws FileNotFoundException {
        remake();
        PlayerPiece[] playerPieces = new PlayerPiece[players.length];

        for (int i = 0; i < players.length; i++) {
            playerPieces[i] = players[i].getPlayerPiece();
        }
        Scanner in = new Scanner(new File(GAME_BOARD_FILE_PATH));
        in.useDelimiter(DELIMITER);
        gameBoard = readSelectGameBoard(boardName, players.length, in, playerPieces);
        in.close();

        playerService.setPlayers(players);
        playerService.setGameService(this);

        actionTilePlayed = false;
        playerPieceMoved = false;
    }

    private GameBoard readSelectGameBoard(String boardName, int nPlayers, Scanner in,
                                          PlayerPiece[] playerPieces) throws IllegalArgumentException {
        while (in.hasNextLine()) {
            if (in.next().equals(boardName)) {
                int nRows = in.nextInt();
                int nCols = in.nextInt();

                // Dealing with fixed tiles:
                FloorTilePositionBundle fixedTileAndPositions = readFixedTiles(in);

                FloorTile[] fixedTiles = fixedTileAndPositions.getFloorTiles();
                Position[] fixedTilePositions = fixedTileAndPositions.getPositions();

                // Dealing with non-fixed floor tiles:
                ArrayList<FloorTile> floorTiles = readFloorTiles(in);

                // Taking first floor tiles for the initial set to populate game board.
                FloorTile[] floorTilesForGameBoard = getFloorTilesForGameBoard
                        (nRows, nCols, fixedTiles.length, floorTiles);

                //Action tiles:
                ArrayList<ActionTile> actionTiles = readActionTiles(in);

                // Player Pieces:
                Position[] playerPiecePositions = readPlayerPiecePositions(nPlayers, in);

                // Silk Bag:
                ArrayList<Tile> tilesForSilkBag = new ArrayList<>();
                tilesForSilkBag.addAll(floorTiles);
                tilesForSilkBag.addAll(actionTiles);

                silkBag = new SilkBag(tilesForSilkBag);

                return new GameBoard(playerPieces, playerPiecePositions, fixedTiles, fixedTilePositions,
                        floorTilesForGameBoard, nCols, nRows, boardName);
            }
        }
        throw new IllegalArgumentException(NO_LEVEL_WITH_SUCH_NAME_MSG);
    }

    private FloorTilePositionBundle readFixedTiles(Scanner in) {
        int nFixedTiles = in.nextInt();
        FloorTile[] fixedTiles = new FloorTile[nFixedTiles];
        Position[] fixedTilePositions = new Position[nFixedTiles];

        for (int i = 0; i < nFixedTiles; i++) {
            int rotation = in.nextInt();
            int row = in.nextInt();
            int col = in.nextInt();
            TileType tileType = TileType.valueOf(in.next().toUpperCase());

            Position p = new Position(row, col);
            FloorTile t = new FloorTile(tileType, true, rotation);
            fixedTiles[i] = t;
            fixedTilePositions[i] = p;
        }
        return new FloorTilePositionBundle(fixedTiles, fixedTilePositions);
    }

    private ArrayList<FloorTile> readFloorTiles(Scanner in) {
        ArrayList<FloorTile> floorTiles = new ArrayList<>();

        for (int i = 0; i < FloorTile.FLOOR_TILE_TYPES.size(); i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            int nOfThisType = in.nextInt();

            for (int j = 0; j < nOfThisType; j++) {
                FloorTile ft = new FloorTile(tileType);
                System.out.println("Ft: ");
                floorTiles.add(ft);
            }
        }
        for (FloorTile floorTile : floorTiles) {
            System.out.println(floorTile.getPathsBits());
        }
        return floorTiles;
    }

    private FloorTile[] getFloorTilesForGameBoard
            (int nRows, int nCols, int nFixedTiles, ArrayList<FloorTile> floorTiles) {
        FloorTile[] floorTilesForGameBoard = new FloorTile[(nRows * nCols) - nFixedTiles];

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

    public void loadSavedInstance(File f) throws FileNotFoundException, MalformedURLException {
        //todo chance that it works 1/4
        remake();

        Scanner in = new Scanner(f);
        in.useDelimiter(DELIMITER);

        //Read game details
        int nPlayers = in.nextInt();
        String gameBoardName = in.next();
        int nRows = in.nextInt();
        int nCols = in.nextInt();
        turnCount = in.nextInt();
        in.nextLine();

        Position[] playerPositions = new Position[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int row = in.nextInt();
            int col = in.nextInt();
            playerPositions[i] = new Position(row, col);
        }
        in.nextLine();

        PlayerPiece[] playerPieces = new PlayerPiece[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            URL playerPieceImageURL = new URL(in.next());
            playerPieces[i] = new PlayerPiece(playerPieceImageURL);
        }
        in.nextLine();

        Player[] players = new Player[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            String username = in.next();
            players[i] = new Player(username, playerPieces[i]);

            int numberOfDrawnActionTiles = in.nextInt();
            TileType[] drawnActionTiles = new TileType[numberOfDrawnActionTiles];

            for (int j = 0; j < numberOfDrawnActionTiles; j++) {
                drawnActionTiles[i] = TileType.valueOf(in.next().toUpperCase());
            }
            players[i].addDrawnActionTiles(drawnActionTiles);

            int numberOfPreviouslyAppliedEffects = in.nextInt();
            EffectType[] previouslyAppliedEffects = new EffectType[numberOfPreviouslyAppliedEffects];

            for (int j = 0; j < numberOfDrawnActionTiles; j++) {
                previouslyAppliedEffects[i] = EffectType.valueOf(in.next().toUpperCase());
            }
            players[i].addPreviouslyAppliedEffects(previouslyAppliedEffects);
            in.nextLine();
        }

        playerService.setPlayers(players);
        playerService.setGameService(this);

        FloorTile[] floorTilesForGameBoard = new FloorTile[nRows * nCols];
        for (int i = 0; i < nRows * nCols; i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            boolean isFixed = in.nextBoolean();
            String rotationStr = in.next();
            int rotation = Integer.parseInt(rotationStr);
            floorTilesForGameBoard[i] = new FloorTile(tileType, isFixed, rotation);
        }
        in.nextLine();

        gameBoard = new GameBoard(playerPieces, playerPositions, floorTilesForGameBoard, nCols, nRows, gameBoardName);
        PlayerService.getInstance().setPlayers(players); //todo player effects...

        silkBag = new SilkBag();

        int c = 0;
        while (in.hasNextLine() && c < 1) {
            silkBag.put(TileType.valueOf(in.next().toUpperCase()));
            c++;
        }
        in.nextLine();

        while (in.hasNext()) {
            int effectRow = in.nextInt();
            int effectCol = in.nextInt();
            Position effectPos = new Position(effectRow, effectCol);
            EffectType effectType = EffectType.valueOf(in.next().toUpperCase());
            int radius = in.nextInt(); //todo reconsider this...
            String durationRemainingStr = in.next();
            int durationRemaining = Integer.parseInt(durationRemainingStr);
            AreaEffect areaEffect = new AreaEffect(effectType, 0, durationRemaining);
            gameBoard.applyEffect(areaEffect, effectPos);
        }
        in.close();

        actionTilePlayed = true; //todo check with mateo
        playerPieceMoved = true; //
    }

    public void nextTurn() { // todo gameplay loop...
        actionTilePlayed = false;
        playerPieceMoved = false;
        gameBoard.refreshEffects();
        turnCount++;
    }

    /**
     * Saves the current instance of the game to a file.
     *
     * @param saveFileName The name of the file in which the game data is to be stored.
     * @throws IOException If a file cannot be created due to an invalid file path.
     */
    public void save(String saveFileName) throws IOException { //todo
        File gameSaveFile = createFile(saveFileName);

        PrintWriter out = null;
        out = new PrintWriter(gameSaveFile);

        writeGameInstanceDetails(out);
        writePlayerPiecePositionDetails(out);
        writePlayerPieceDetails(out);
        writePlayerInstanceDetailsForAllPlayers(out);
        writeGameBoardInstanceTileDetails(out, playerService.getPlayers().length);
        writeSilkBagInstanceDetails(out);
        writeAreaEffectDetails(out);

        out.flush();
        out.close();
    }

    private void writePlayerPiecePositionDetails(PrintWriter out) {
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            out.print(gameBoard.getPlayerPiecePosition(i).getRowNum());
            out.print(DELIMITER);
            out.print(gameBoard.getPlayerPiecePosition(i).getColNum());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    private void writePlayerPieceDetails(PrintWriter out) {
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            out.print(gameBoard.getPlayerPiece(i).getImageURL().toString());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    private File createFile(String fileName) throws IOException {
        File gameSaveFile = new File
                (SAVE_GAME_FILE_PATH + fileName + DATA_FILE_EXTENSION);

        boolean isFileCreated = false;
        int filesWithSameName = 0;

        while (!isFileCreated && filesWithSameName < MAX_NUM_OF_SAVE_FILES_WITH_SAME_NAME) {
            if (gameSaveFile.createNewFile()) {
                isFileCreated = true;
            } else {
                filesWithSameName++;
                gameSaveFile = new File(SAVE_GAME_FILE_PATH + fileName +
                        FILE_WORD_SPACER + filesWithSameName + DATA_FILE_EXTENSION);
            }
        }

        if (!isFileCreated) {
            throw new IllegalArgumentException("Too many files with same name!");
        }
        return gameSaveFile;
    }

    private void writeGameInstanceDetails(PrintWriter out) {
        out.print(playerService.getPlayers().length); // Number of players
        out.print(DELIMITER);
        out.print(gameBoard.getName());
        out.print(DELIMITER);
        out.print(gameBoard.getnRows());
        out.print(DELIMITER);
        out.print(gameBoard.getnCols());
        out.print(DELIMITER);
        out.print(turnCount);
        out.print(DELIMITER);
        out.print('\n');
    }

    private void writeGameBoardInstanceTileDetails(PrintWriter out, int nPlayers) {
        /*for (int i = 0; i < playerService.getPlayers().length; i++) {
            Position playerPiecePosition = gameBoard.getPlayerPiecePosition(i);
            out.print(playerPiecePosition.getRowNum());
            out.print(DELIMITER);
            out.print(playerPiecePosition.getColNum());
            out.print(DELIMITER);
        }*/

        for (int i = 0; i < gameBoard.getnRows(); i++) {
            for (int j = 0; j < gameBoard.getnCols(); j++) {
                out.print(gameBoard.getTileAt(i, j).getType());
                out.print(DELIMITER);
                out.print(gameBoard.getTileAt(i, j).isFixed());
                out.print(DELIMITER);
                out.print(gameBoard.getTileAt(i, j).getRotation()); //todo check Matej's method
                out.print(DELIMITER);

                /*if (gameBoard.getEffectAt(new Position(i, j)) != null) {
                    out.print(true);
                    out.print(DELIMITER);
                    out.print(gameBoard.getEffectAt(new Position(i, j)).getEffectType());
                    out.print(DELIMITER);
                    out.print(gameBoard.getEffectAt(new Position(i, j)).getRemainingDuration());
                    out.print(DELIMITER);
                    out.print(gameBoard.getEffectAt(new Position(i, j)).getRadius());
                    out.print(DELIMITER);
                } else {
                    out.print(false);
                    out.print(DELIMITER);
                }*/
            }
        }
        out.print('\n');
    }

    private void writeAreaEffectDetails(PrintWriter out) {
        Set<Position> activeEffectPositions = gameBoard.getActiveEffectPositions();

        for (Position effectPosition : activeEffectPositions) {
            out.print(effectPosition.getRowNum());
            out.print(DELIMITER);
            out.print(effectPosition.getColNum());
            out.print(DELIMITER);
            out.print(gameBoard.getEffectAt(effectPosition).getEffectType());
            out.print(DELIMITER);
            out.print(gameBoard.getEffectAt(effectPosition).getRemainingDuration());
            out.print(DELIMITER);
            out.print(gameBoard.getEffectAt(effectPosition).getRadius());
            out.print(DELIMITER);
        }
    }

    private void writeSilkBagInstanceDetails(PrintWriter out) {
        while (!silkBag.isEmpty()) {
            out.print(silkBag.take().getType());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    private void writePlayerInstanceDetailsForAllPlayers(PrintWriter out) {
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            writePlayerInstanceDetails(out, i);
        }
    }

    private void writePlayerInstanceDetails(PrintWriter out, int i) {
        out.print(playerService.getPlayer(i).getUsername());
        out.print(DELIMITER);
        out.print(playerService.getPlayer(i).getDrawnActionTiles().size()); // N of drawn action tiles.
        out.print(DELIMITER);

        for (ActionTile actionTile : playerService.getPlayer(i).getDrawnActionTiles()) {
            out.print(actionTile.getType().toString());
            out.print(DELIMITER);
        }

        out.print(playerService.getPlayer(i).getPreviousAppliedEffect().size()); // N of drawn action tiles.
        out.print(DELIMITER);

        for (EffectType effect : playerService.getPlayer(i).getPreviousAppliedEffect()) {
            out.print(effect);
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    public void gameplayLoop() { // todo gameplay loop...
        while (!isWin) {
            playerService.playerTurn(playerService.getPlayer(turnCount
                    % playerService.getPlayers().length)); // todo improve player service
            System.out.println("Have fun!");
            gameBoard.refreshEffects();
            turnCount++;
        }
    }

    /**
     * Returns the PlayerService belonging to the GameService.
     *
     * @return The GameService's PlayerService.
     */
    public PlayerService getPlayerService() {
        return playerService;
    }

    /**
     * Returns the current turn number for the game.
     *
     * @return The number of turns made.
     */
    public int getTurnCount() {
        return turnCount;
    }

    public int getCurrentPlayerNum() {
        return getTurnCount() % playerService.getPlayers().length;
    }

    /**
     * Returns true if a Player has won the game.
     *
     * @return True if a player has won the game, otherwise false.
     */
    public boolean isWin() {
        return isWin;
    }

    /**
     * Clears the current instance of GameService.
     */
    public void destroy() {
        instance = null;
    }

    /**
     * Returns a new instance of GameService.
     *
     * @return The new instance of GameService.
     */
    public GameService remake() {
        return new GameService();
    }

    /**
     * Returns the gameboard for the GameService
     *
     * @return The gameboard used for the game.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Retunrs the SilkBag being used by the GameService.
     *
     * @return The GameService's SilkBag.
     */
    public SilkBag getSilkBag() {
        return silkBag;
    }

    public boolean getActionTilePlayed() {
        return actionTilePlayed;
    }

    public void setActionTilePlayed(boolean actionTilePlayed) {
        this.actionTilePlayed = actionTilePlayed;
    }

    public boolean getPlayerPieceMoved() {
        return playerPieceMoved;
    }

    public void setPlayerPieceMoved(boolean playerPieceMoved) {
        this.playerPieceMoved = playerPieceMoved;
    }

    /**
     * The entry point of application, for testing only. todo remove this.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
        JFXPanel jfxPanel = new JFXPanel(); //suppresses strange error...


        GameService gs = GameService.getInstance();
        gs.loadNewGame(
                new Player[]{new Player("nylecm", new PlayerPiece
                        (new File("view/res/img/player_piece/alien_ufo_1.png").toURL())),
                        new Player("nylecm1", new PlayerPiece
                                (new File("view/res/img/player_piece/alien_ufo_2.png").toURL()))},
                "oberon_1");
        System.out.println(gs.gameBoard);
        //gs.gameBoard.insert(-1, 0, new FloorTile(TileType.STRAIGHT, false));
        System.out.println(gs.gameBoard);

        AreaEffect effect = new AreaEffect(EffectType.FIRE, 1, 3);

        gs.gameBoard.applyEffect(effect, new Position(4, 0));
        for (Position pos : gs.gameBoard.getPositionsWithActiveEffects()) {
            System.out.println(pos.getRowNum() + " " + pos.getColNum());
        }

        AreaEffect test = gs.gameBoard.getActiveEffects().get(new Position(0, 0));
        System.out.println(test);

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.STRAIGHT), 0);
        System.out.println();

        for (Position pos : gs.gameBoard.getPositionsWithActiveEffects()) {
            System.out.println(pos.getRowNum() + " " + pos.getColNum());
        }

        System.out.println(test);
        System.out.println(gs.gameBoard.getPlayerPiecePosition(0));

        try {
            gs.save("faron");
        } catch (IOException e) {
            e.printStackTrace();
        }

        gs.gameBoard.refreshEffects();
        gs.gameBoard.refreshEffects();
        gs.gameBoard.refreshEffects();
        gs.gameBoard.refreshEffects();
        gs.gameBoard.movePlayerPieceDown(0);
        gs.gameBoard.movePlayerPieceDown(0);
        gs.gameBoard.movePlayerPieceDown(0);
        gs.gameBoard.movePlayerPieceDown(0);

        gs.playerService.applyBackTrackEffect(0);
        System.out.println(gs.gameBoard.getPlayerPiecePosition(0));

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.T_SHAPED), 0);

        System.out.println("r1");
        System.out.println(gs.gameBoard.getTileAt(0, 0));
        System.out.println(gs.gameBoard.getTileAt(0, 1));
        System.out.println(gs.gameBoard.getTileAt(0, 2));
        System.out.println(gs.gameBoard.getTileAt(0, 3));

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.T_SHAPED), 1);

        System.out.println("r2");
        System.out.println(gs.gameBoard.getTileAt(0, 0));
        System.out.println(gs.gameBoard.getTileAt(0, 1));
        System.out.println(gs.gameBoard.getTileAt(0, 2));
        System.out.println(gs.gameBoard.getTileAt(0, 3));

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.T_SHAPED), 2);

        System.out.println("r3");
        System.out.println(gs.gameBoard.getTileAt(0, 0));
        System.out.println(gs.gameBoard.getTileAt(0, 1));
        System.out.println(gs.gameBoard.getTileAt(0, 2));
        System.out.println(gs.gameBoard.getTileAt(0, 3));

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.T_SHAPED), 3);

        System.out.println("r4");
        System.out.println(gs.gameBoard.getTileAt(0, 0));
        System.out.println(gs.gameBoard.getTileAt(0, 1));
        System.out.println(gs.gameBoard.getTileAt(0, 2));
        System.out.println(gs.gameBoard.getTileAt(0, 3));

        gs.gameBoard.insert(-1, 0, new FloorTile(TileType.T_SHAPED), 4);


        System.out.println("r5");
        System.out.println(gs.gameBoard.getTileAt(0, 0));
        System.out.println(gs.gameBoard.getTileAt(0, 1));
        System.out.println(gs.gameBoard.getTileAt(0, 2));
        System.out.println(gs.gameBoard.getTileAt(0, 3));

        System.out.println();


        System.out.println(gs.gameBoard);
        System.out.println(gs.gameBoard.getTileAt(1, 0).getPathsBits());
        System.out.println(gs.gameBoard.getTileAt(0, 1));

        gs.destroy();

        try {
            gs.loadSavedInstance(new File("C:\\Users\\micha\\IdeaProjects\\CS-230-Group-4\\data\\saves\\faron_2.txt"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private static class FloorTilePositionBundle {
        private final FloorTile[] floorTiles;
        private final Position[] positions;

        public FloorTilePositionBundle(FloorTile[] floorTiles, Position[] positions) {
            this.floorTiles = floorTiles;
            this.positions = positions;
        }

        public FloorTile[] getFloorTiles() {
            return floorTiles;
        }

        public Position[] getPositions() {
            return positions;
        }
    }
}
