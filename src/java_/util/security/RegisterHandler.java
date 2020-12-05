package java_.util.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

import static java_.game.controller.PurchaseHandler.addNewPlayer;
import static java_.game.controller.PurchaseHandler.buyAllFreePlayerPieces;

/**
 * Handles the process of a user registering a new account in the game.
 * The account details are stored in a file by the handler.
 *
 * @author Hladky Matej original version, nylecm major overhaul.
 */
public class RegisterHandler {

    private static final String USERS_FILE_PATH = "users.txt";
    private static final String USER_DUPLICATE_USERNAME_MSG =
            "User with such username exists!";
    private static final String DELIMITER = "`";

    /**
     * Registers the new account by storing the user account data to a file.
     *
     * @param username The username for the registered account.
     * @param email    The email for the registered account.
     * @param password The password for the registered account.
     * @throws IOException              If there is an error writing user details to the file.
     * @throws IllegalArgumentException If the user has entered a username that already exists for another account.
     */
    public static void register(String username, String email, String password) throws IOException, IllegalArgumentException {
        Scanner in = new Scanner(new File(USERS_FILE_PATH));
        in.useDelimiter(DELIMITER);
        boolean isUserFileEmpty = true;

        while (in.hasNextLine()) {
            if (in.next().equals(username)) {
                throw new IllegalArgumentException(USER_DUPLICATE_USERNAME_MSG);
            } else {
                isUserFileEmpty = false;
                in.nextLine();
            }
        }
        in.close();

        String encodePassword = PasswordEncoder.encodePassword(password);
        String userRecord = (isUserFileEmpty
                ? username + DELIMITER + email + DELIMITER + encodePassword + DELIMITER
                : "\n" + username + DELIMITER + email + DELIMITER + encodePassword + DELIMITER);

        byte[] userRecordBytes = userRecord.getBytes();
        Files.write(Paths.get(USERS_FILE_PATH), userRecordBytes, StandardOpenOption.APPEND);
        addNewPlayer(username, isUserFileEmpty);
    }
}
