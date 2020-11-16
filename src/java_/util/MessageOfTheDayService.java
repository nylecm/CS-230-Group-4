package java_.util;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class represents the message of the day for the game that will be displayed on startup
 *
 * @author SamCox7500, minor improvements by nylecm
 */
public class MessageOfTheDayService {
    /**
     * Gets the message of the day.
     *
     * @return String containing message of the day.
     * @throws IOException when the URL is malformed.
     */
    public static String getMessage() throws IOException {
        Scanner puzzleIn = readURL("http://cswebcat.swansea.ac.uk/puzzle");
        String puzzle = puzzleIn.nextLine();
        String shiftedPuzzle = shiftPuzzle(puzzle);

        String courseCode = "CS-230";
        String codeAndShiftedPuzzle = courseCode + shiftedPuzzle;

        String puzzleSolution = codeAndShiftedPuzzle + codeAndShiftedPuzzle.length();

        Scanner messageIn = readURL("http://cswebcat.swansea.ac.uk/message?solution=" + puzzleSolution);
        return messageIn.nextLine();
    }

    /**
     * Reads the contents of the web page specified by url.
     *
     * @param urlName The url for the webpage that is to be read from
     * @return A scanner reading the contents of the web page
     * @throws IOException when the URL is malformed.
     */
    private static Scanner readURL(String urlName) throws IOException {
        URL url = new URL(urlName);
        return new Scanner(url.openStream());
    }

    /**
     * Applies a caesar shift to a message, in alternating directions. Eg. one
     * I.e. first letter shifted 1 backwards, second letter shifted 2 forwards,
     * third letter shifted 3 backwards...
     *
     * @param puzzle The string that the caesar shift is to be applied to
     * @return The decrypted string
     */
    private static String shiftPuzzle(String puzzle) {
        final int A_ASCII_VALUE = 65;
        final int Z_ASCII_VALUE = 90;
        final int ALPHABET_SIZE = 26;

        char[] puzzleLetters = puzzle.toCharArray();
        for (int i = 0; i < puzzleLetters.length; i += 2) {
            puzzleLetters[i] = (char) (puzzleLetters[i] - (i + 1));
            if (puzzleLetters[i] < A_ASCII_VALUE) {
                puzzleLetters[i] = (char) (puzzleLetters[i] + ALPHABET_SIZE);
            }
        }
        for (int i = 1; i < puzzleLetters.length; i += 2) {
            puzzleLetters[i] = (char) (puzzleLetters[i] + (i + 1));
            if (puzzleLetters[i] > Z_ASCII_VALUE) {
                puzzleLetters[i] = (char) (puzzleLetters[i] - ALPHABET_SIZE);
            }
        }
        return String.valueOf(puzzleLetters);
    }
}
