package java_;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class responsible for handling common file reading operations.
 *
 * @author nylecm
 */
public class Reader {
    private File file;
    private Scanner in;
    private static final String GAME_BOARD_FILE_PATH = "data/game_board.txt";
    private static final int GAME_BOARD_DEFINITION_LINE_FREQUENCY = 2;

    private static final String DELIMITER = "`";

    /**
     * Reads the names of all the game boards in the game.
     *
     * @return a list of all game boards that have been defined in the file.
     * @throws FileNotFoundException if game board file does not exist.
     */
    public List<String> readGameBoardNames() throws FileNotFoundException {
        List<String> output = new ArrayList<>();
        this.file = new File(GAME_BOARD_FILE_PATH);
        in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        int lineCount = 1;
        while (in.hasNextLine()) {
            if (lineCount % GAME_BOARD_DEFINITION_LINE_FREQUENCY == 1) {
                output.add(in.next());
            }
            in.nextLine();
            lineCount++;
        }
        return output;
    }

    /**
     * Reads the names of the files in a given folder.
     *
     * @param path the folder where the names of all files ought to be read from.
     * @return a File array of all the files in that folder.
     */
    public File[] readFileNames(String path) {
        File folder = new File(path);
        return folder.listFiles();
    }
}
