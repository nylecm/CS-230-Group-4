package java_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Game service is responsible for loading new & saved games from file, saving
 * games, and handling turns. It also has references to the player service,
 * the game board, and silk bag, and storing the turn count.
 *
 * @author nylecm, apapted by Matej Haldky
 */
public class GameService {
    private static GameService instance = null;

    private GameBoard gameBoard;
    private final PlayerService playerService;
    private SilkBag silkBag;
    private int turnCount;

    private static final String DELIMITER = "`";
    private static final String FILE_WORD_SPACER = "_";
    private static final String DATA_FILE_EXTENSION = ".txt";
    private static final String GAME_BOARD_FILE_PATH =
            "data/game_board" + DATA_FILE_EXTENSION;
    private static final String SAVE_GAME_FILE_PATH = "data/saves/";
    private static final String NO_LEVEL_WITH_SUCH_NAME_MSG =
            "No level with such name found!";
    public static final String TOO_MANY_FILES_WITH_SAME_NAME_MSG =
            "Too many files with same name!";

    private static final int MAX_NUM_OF_SAVE_FILES_WITH_SAME_NAME = Integer.MAX_VALUE;
    private static final int MAX_ROTATION_BOUND = 4;

    /**
     * Initialises java.GameService, remaking the playerService belonging to it.
     */
    private GameService() {
        playerService = PlayerService.getInstance().remake();
    }

    /**
     * Returns the current instance of java.GameService. If there isn't one, a new one is made and returned.
     *
     * @return The current or new instance of java.GameService.
     */
    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    /**
     * Sets a new instance of java.GameService to be a game that had been previously been saved to a file.
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
    }

    /**
     * Reads and returns a specified java.GameBoard with PlayerPieces in their starting positions.
     *
     * @param boardName    The name of the board to be returned
     * @param nPlayers     The number of player pieces on the board.
     * @param in           The java.GameBoard file scanner
     * @param playerPieces The player pieces on the board.
     * @return The specified java.GameBoard.
     * @throws IllegalArgumentException
     */
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

                // java.Player Pieces:
                Position[] playerPiecePositions = readPlayerPiecePositions(nPlayers, in);

                // Silk Bag:
                ArrayList<Tile> tilesForSilkBag = new ArrayList<>();
                tilesForSilkBag.addAll(floorTiles);
                tilesForSilkBag.addAll(actionTiles);

                silkBag = new SilkBag(tilesForSilkBag);

                return new GameBoard(playerPieces, playerPiecePositions, fixedTiles, fixedTilePositions,
                        floorTilesForGameBoard, nCols, nRows, boardName);
            } else {
                if (in.hasNextLine()) {
                    in.nextLine();
                    in.nextLine();
                }
            }
        }
        throw new IllegalArgumentException(NO_LEVEL_WITH_SUCH_NAME_MSG);
    }

    /**
     * Reads and returns fixed tiles and their positions on a java.GameBoard.
     *
     * @param in The scanner that reads the fix tiles and locations.
     * @return The bundle containing the fixed tiles and their corresponding positions.
     */
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

    /**
     * Reads and returns all floor tiles to stored in the java.GameBoard of the java.GameService.
     *
     * @param in The scanner that reads all the floor tiles.
     * @return The ArrayList containing all floor tiles to be stored.
     */
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

    /**
     * Returns the array of randomly rotated FloorTiles used to fill the
     * java.GameBoard of the java.GameService.
     *
     * @param nRows       Number of rows in java.GameBoard
     * @param nCols       Number of columns in java.GameBoard.
     * @param nFixedTiles Number of fixed tiles in the java.GameBoard.
     * @param floorTiles  ArrayList containing all floor tiles to be used in java.GameBoard.
     * @return The array containing all floor tiles to be used by the java.GameBoard.
     */
    private FloorTile[] getFloorTilesForGameBoard
    (int nRows, int nCols, int nFixedTiles, ArrayList<FloorTile> floorTiles) {
        FloorTile[] floorTilesForGameBoard = new FloorTile[(nRows * nCols) - nFixedTiles];

        for (int i = 0; i < (nRows * nCols) - nFixedTiles; i++) {
            int rotationAmount = ThreadLocalRandom.current().nextInt(0, MAX_ROTATION_BOUND);
            FloorTile newFloorTile = floorTiles.get(0);
            newFloorTile.rotate(rotationAmount);
            floorTilesForGameBoard[i] = newFloorTile;
            floorTiles.remove(0);
        }
        return floorTilesForGameBoard;
    }


    /**
     * Reads action tiles from a file
     *
     * @param in The scanner which reads the action tiles from the file.
     * @return ArrayList of action tiles from the file.
     */
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

    /**
     * @param nPlayers The number of players.
     * @param in       The scanner which reads java.PlayerPiece positions from a file.
     * @return Array of java.PlayerPiece positions.
     */
    private Position[] readPlayerPiecePositions(int nPlayers, Scanner in) {
        Position[] positions = new Position[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int startRow = in.nextInt();
            int startCol = in.nextInt();
            positions[i] = new Position(startRow, startCol);
        }
        return positions;
    }

    /**
     * Loads a saved instance of java.GameService.
     *
     * @param f The file used to load the instance of java.GameService.
     * @throws FileNotFoundException If the specified file cannot be found
     * @throws MalformedURLException If problem converiting file to URL.
     */
    public void loadSavedInstance(File f) throws FileNotFoundException, MalformedURLException {
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

        Position[] playerPositions = getSavedPlayerPositions(in, nPlayers);
        PlayerPiece[] playerPieces = getSavedPlayerPieceImages(in, nPlayers);
        Player[] players = getSavedInstancePlayers(in, nPlayers, playerPieces);

        playerService.setPlayers(players);
        playerService.setGameService(this);

        FloorTile[] floorTilesForGameBoard = getSavedFloorTiles(in, nRows, nCols);

        gameBoard = new GameBoard(playerPieces, playerPositions, floorTilesForGameBoard, nCols, nRows, gameBoardName);
        //java.PlayerService.getInstance().setPlayers(players);
        readSavedSilkBag(in);
        readSavedInstanceAreaEffects(in);
        in.close();
    }

    /**
     * Returns all saved player positions on a java.GameBoard.
     *
     * @param in       The scanner that reads saved player positions from a file.
     * @param nPlayers The number of players with saved positions.
     * @return The array containing all saved player positions.
     */
    private Position[] getSavedPlayerPositions(Scanner in, int nPlayers) {
        Position[] playerPositions = new Position[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int row = in.nextInt();
            int col = in.nextInt();
            playerPositions[i] = new Position(row, col);
        }
        in.nextLine();
        return playerPositions;
    }

    /**
     * Returns all saved PlayerPieces containing java.PlayerPiece images.
     *
     * @param in       The scanner that reads the PlayerPieces from a file.
     * @param nPlayers The number of players with the PlayerPieces.
     * @return The array of PlayerPieces with java.PlayerPiece images.
     * @throws MalformedURLException If there are problems converting file to url.
     */
    private PlayerPiece[] getSavedPlayerPieceImages(Scanner in, int nPlayers) throws MalformedURLException {
        PlayerPiece[] playerPieces = new PlayerPiece[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            File playerPieceImageFile = new File(in.next());
            playerPieces[i] = new PlayerPiece(playerPieceImageFile);
        }
        in.nextLine();
        return playerPieces;
    }

    /**
     * Returns all players from a saved instance.
     *
     * @param in           The scanner used to read the Players from a file.
     * @param nPlayers     The number of players in the saved instance.
     * @param playerPieces The PlayerPieces of the players in the saved instance.
     * @return The array containing all saved players from the instance.
     */
    private Player[] getSavedInstancePlayers(Scanner in, int nPlayers, PlayerPiece[] playerPieces) {
        Player[] players = new Player[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            String username = in.next();
            players[i] = new Player(username, playerPieces[i]);

            int numberOfDrawnActionTiles = in.nextInt();

            if (numberOfDrawnActionTiles > 0) {
                TileType[] drawnActionTiles = new TileType[numberOfDrawnActionTiles];
                for (int j = 0; j < numberOfDrawnActionTiles; j++) {
                    drawnActionTiles[j] = TileType.valueOf(in.next().toUpperCase());
                }
                players[i].addDrawnActionTiles(drawnActionTiles);
            }
            int numberOfPreviouslyAppliedEffects = in.nextInt();

            if (numberOfPreviouslyAppliedEffects > 0) {
                EffectType[] previouslyAppliedEffects = new EffectType[numberOfPreviouslyAppliedEffects];
                for (int j = 0; j < numberOfPreviouslyAppliedEffects; j++) {
                    previouslyAppliedEffects[j] = EffectType.valueOf(in.next().toUpperCase());
                }
                players[i].addPreviouslyAppliedEffects(previouslyAppliedEffects);
            }
            in.nextLine();
        }
        return players;
    }

    /**
     * Returns floor tiles saved in a file.
     *
     * @param in    The scanner which reads the floor tiles from the file.
     * @param nRows Thee number of rows of floor tiles.
     * @param nCols The number of columns of floor tiles.
     * @return The array of floor tiles from the saved file.
     */
    private FloorTile[] getSavedFloorTiles(Scanner in, int nRows, int nCols) {
        FloorTile[] floorTilesForGameBoard = new FloorTile[nRows * nCols];
        for (int i = 0; i < nRows * nCols; i++) {
            TileType tileType = TileType.valueOf(in.next().toUpperCase());
            boolean isFixed = in.nextBoolean();
            String rotationStr = in.next();
            int rotation = Integer.parseInt(rotationStr);
            floorTilesForGameBoard[i] = new FloorTile(tileType, isFixed, rotation);
        }
        in.nextLine();
        return floorTilesForGameBoard;
    }

    /**
     * Reads tiles from a file and puts them in a silk bag.
     *
     * @param in The scanner which reads the tiles for the silk bag.
     */
    private void readSavedSilkBag(Scanner in) {
        silkBag = new SilkBag();
        String silkBagSizeStr = in.next();
        int silkBagSize = Integer.parseInt(silkBagSizeStr);

        for (int i = 0; i < silkBagSize; i++) {
            silkBag.put(TileType.valueOf(in.next().toUpperCase()));
        }
        in.nextLine();
    }

    /**
     * Reads area effects of a saved instance.
     *
     * @param in The scanner which reads the area effects from a file.
     */
    private void readSavedInstanceAreaEffects(Scanner in) {
        while (in.hasNext()) {
            int effectRow = in.nextInt();
            int effectCol = in.nextInt();
            String durationRemainingStr = in.next();
            int durationRemaining = Integer.parseInt(durationRemainingStr);
            EffectType effectType = EffectType.valueOf(in.next().toUpperCase());

            Position effectPos = new Position(effectRow, effectCol);
            AreaEffect areaEffect = new AreaEffect(effectType, 0, durationRemaining);
            gameBoard.applyEffect(areaEffect, effectPos);
        }
        in.close();
    }

    /**
     * Moves the game onto the next turn, incrementing the turn count and decrementing the durations of all effects.
     */
    public void nextTurn() {
        gameBoard.refreshEffects();
        turnCount++;
    }

    /**
     * Saves the current instance of the game to a file.
     *
     * @param saveFileName The name of the file in which the game data is to be stored.
     * @throws IOException If a file cannot be created due to an invalid file path.
     */
    public void save(String saveFileName) throws IOException {
        File gameSaveFile = createFile(saveFileName);

        PrintWriter out = new PrintWriter(gameSaveFile);

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

    /**
     * Writes java.PlayerPiece positions (row and column number) to a file.
     *
     * @param out The writer which writes the java.PlayerPiece positions to the file.
     */
    private void writePlayerPiecePositionDetails(PrintWriter out) {
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            out.print(gameBoard.getPlayerPiecePosition(i).getRowNum());
            out.print(DELIMITER);
            out.print(gameBoard.getPlayerPiecePosition(i).getColNum());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    /**
     * Writes java.PlayerPiece details to a file including image file locations.
     *
     * @param out The writer which writes the java.PlayerPiece details to the file.
     */
    private void writePlayerPieceDetails(PrintWriter out) {
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            out.print(gameBoard.getPlayerPiece(i).getImageFile());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    /**
     * Creates and returns a file with a specified name
     *
     * @param fileName The name of the file to be created.
     * @return The newly created file.
     * @throws IOException If there are too many file names with the same name.
     */
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
            throw new IllegalArgumentException(TOO_MANY_FILES_WITH_SAME_NAME_MSG);
        }
        return gameSaveFile;
    }

    /**
     * Writes game instance details to a file, including number of players, name, number of rows and columns.
     *
     * @param out The writer which writes the game instance details to a file.
     */
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

    /**
     * Writes java.GameBoard instance details to a file including tiles and their types, fixed tile positions and tile rotations.
     *
     * @param out      The writer which writes the java.GameBoard instance details to a file.
     * @param nPlayers number of players.
     */
    private void writeGameBoardInstanceTileDetails(PrintWriter out, int nPlayers) {
        for (int i = 0; i < gameBoard.getnRows(); i++) {
            for (int j = 0; j < gameBoard.getnCols(); j++) {
                out.print(gameBoard.getTileAt(i, j).getType());
                out.print(DELIMITER);
                out.print(gameBoard.getTileAt(i, j).isFixed());
                out.print(DELIMITER);
                out.print(gameBoard.getTileAt(i, j).getRotation());
                out.print(DELIMITER);
            }
        }
        out.print('\n');
    }

    /**
     * Writes area effect details to a file, such as their positions, remaining durations and types.
     *
     * @param out The writer which writes the area effect details to a file.
     */
    private void writeAreaEffectDetails(PrintWriter out) {
        Set<Position> activeEffectPositions = gameBoard.getPositionsWithActiveEffects();

        for (Position effectPosition : activeEffectPositions) {
            out.print(effectPosition.getRowNum());
            out.print(DELIMITER);
            out.print(effectPosition.getColNum());
            out.print(DELIMITER);
            out.print(gameBoard.getEffectAt(effectPosition).getRemainingDuration());
            out.print(DELIMITER);
            out.print(gameBoard.getEffectAt(effectPosition).getEffectType().toString());
            out.print(DELIMITER);
        }
    }

    /**
     * Writes silk bag instance details to a file such as size and all tiles contained within.
     *
     * @param out The writer which writes the silk bag instance details.
     */
    private void writeSilkBagInstanceDetails(PrintWriter out) {
        out.print(silkBag.size());
        out.print(DELIMITER);
        while (!silkBag.isEmpty()) {
            out.print(silkBag.take().getType());
            out.print(DELIMITER);
        }
        out.print('\n');
    }

    /**
     * Writes player instance details to a file for all players in the game.
     *
     * @param out The writer which writes all player details to a file.
     */
    private void writePlayerInstanceDetailsForAllPlayers(PrintWriter out) {
        System.out.println(playerService.getPlayers().length);
        for (int i = 0; i < playerService.getPlayers().length; i++) {
            writePlayerInstanceDetails(out, i);
        }
    }

    /**
     * Writes java.Player instance details to a file including username, number of drawn action tiles, all action tile types previously drawn,
     * number of previously applied effects and the types of all previously applied effects.
     *
     * @param out The writer which writes the player details to a file.
     * @param i   The index of the player whose details are to be written.
     */
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

    /**
     * A bundle containing floor tiles and their corresponding java.GameBoard positions (row and column number).
     */
    private static class FloorTilePositionBundle {
        private final FloorTile[] floorTiles;
        private final Position[] positions;

        /**
         * Initialises the FloorTilePositionBundle, storing floor tiles and their corresponding positions.
         *
         * @param floorTiles The floor tiles to be stored in the FloorTilePositionBundle.
         * @param positions  The positions of the floor tiles to be stored in the FloorTilePositionBundle.
         */
        public FloorTilePositionBundle(FloorTile[] floorTiles, Position[] positions) {
            this.floorTiles = floorTiles;
            this.positions = positions;
        }

        /**
         * Returns all floor tiles in the bundle
         *
         * @return The array containing all floor tiles in the bundle.
         */
        public FloorTile[] getFloorTiles() {
            return floorTiles;
        }

        /**
         * Returns all positions of floor tiles in the bundle.
         *
         * @return The array containing all positions of floor tiles in the bundle.
         */
        public Position[] getPositions() {
            return positions;
        }
    }

    /**
     * Returns the java.PlayerService belonging to the java.GameService.
     *
     * @return The java.GameService's java.PlayerService.
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
     * Clears the current instance of java.GameService.
     */
    public void destroy() {
        instance = null;
    }

    /**
     * Returns a new instance of java.GameService.
     *
     * @return The new instance of java.GameService.
     */
    public GameService remake() {
        return new GameService();
    }

    /**
     * Returns the gameboard for the java.GameService
     *
     * @return The gameboard used for the game.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Retunrs the java.SilkBag being used by the java.GameService.
     *
     * @return The java.GameService's java.SilkBag.
     */
    public SilkBag getSilkBag() {
        return silkBag;
    }
}
