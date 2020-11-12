import java.net.URL;
import java.util.Scanner;

/**
 * This class represents the message of the day for the game that will be displayed on startup
 *
 * @author Samuel Cox
 * @version 1.1
 */
public class MessageOfTheDayService {


    /**
     * Returns the message of the day
     *
     * @return String containing message of the day. If motd is unable to be retrieved then an empty string.
     * @throws Exception
     */
    public static String getMessage() throws Exception {
        try {

            Scanner in = readURL("http://cswebcat.swansea.ac.uk/puzzle");
            String puzzle = in.nextLine();
            String shiftedPuzzle = shiftPuzzle(puzzle);
            String courseCode = "CS-230";
            String codeAndShiftedPuzzle = courseCode + shiftedPuzzle;
            String puzzleSolution = codeAndShiftedPuzzle + codeAndShiftedPuzzle.length();

            Scanner solIn = readURL("http://cswebcat.swansea.ac.uk/message?solution=" + puzzleSolution);
            String message = solIn.nextLine();
            return message;

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Reads the contents of the web page specified by url.
     *
     * @param urlName The url for the webpage that is to be read from
     * @return A scanner containing the contents of the web page
     * @throws Exception
     */
    private static Scanner readURL(String urlName) throws Exception {

        URL url = new URL(urlName);
        Scanner in = new Scanner(url.openStream());

        return in;
    }

    /**
     * Applies incrementing caesar shift to each character in revolving directions.
     * I.e. first letter shifted 1 backwards, second letter shifted 2 forwards,
     * third letter shifted 3 backwards...
     *
     * @param puzzle The string that the caesar shift is to be applied to
     * @return The string in which each character has been shifted
     */
    private static String shiftPuzzle(String puzzle) {

        final int A_ASCII_VALUE = 65;
        final int Z_ASCII_VALUE = 90;
        final int ALPHABET_SIZE = 26;

        char[] puzzleLetters = puzzle.toCharArray();
        for (int i = 0; i < puzzleLetters.length; i += 2) {
            puzzleLetters[i] = (char) (puzzleLetters[i] - (i + 1));
            if (puzzleLetters[i] < A_ASCII_VALUE) {
                puzzleLetters[i] = (char) (puzzleLetters[i] + 26);
            }
        }
        for (int i = 1; i < puzzleLetters.length; i += 2) {
            puzzleLetters[i] = (char) (puzzleLetters[i] + (i + 1));
            if (puzzleLetters[i] > Z_ASCII_VALUE) {
                puzzleLetters[i] = (char) (puzzleLetters[i] - 26);
            }
        }

        //System.out.println(String.valueOf(puzzleLetters));
        return String.valueOf(puzzleLetters);

    }


    public static void main(String[] args) throws Exception {

        System.out.println(getMessage());
    }

}
