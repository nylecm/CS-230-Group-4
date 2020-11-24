package java_.controller;

import java_.game.tile.*;
import java_.util.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Reader {

    private File file;
    private Scanner in;
    private static final String GAME_BOARD_FILE_PATH = "data/game_board.txt";
    private static final String DELIMITER = "` ";

    public List<String> readGameBoardNames() throws FileNotFoundException {
        List<String> output = new ArrayList<>();
        this.file = new File(GAME_BOARD_FILE_PATH);
        in = new Scanner(file);
        in.useDelimiter(DELIMITER);
        int lineCount = 1;
        while (in.hasNextLine()) {
            if (lineCount % 2 == 1) {
                output.add(in.next());
            }
            in.nextLine();
            lineCount++;
        }
        return output;
    }

    public File[] readFileNames(String path) {
        File folder = new File(path);
        return folder.listFiles();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Reader reader = new Reader();
        reader.readGameBoardNames();
    }
}
