package java_.util.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

/**
 * Handles the user logging in process to verify that they have an existing account and can play the game.
 */
public class LoginHandler {

    private static final String USERS_FILE_PATH = "users.txt";
    private static final String DELIMITER = "`";

    /**
     * Determines if a user is allowed access to the game. When trying to login, if the user has entered a username
     * for an account that exists in the system as well as the correct password for that account then they will be allowed
     * access.
     * @param username The username the user has entered to attempt to login.
     * @param password The password the user has entered to attempt to login.
     * @return True if the user has entered a valid username and password combination.
     */
    public static boolean login(String username, String password) {
        File users = new File(USERS_FILE_PATH);
        try (Scanner sc = new Scanner(users)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] credentials = line.split(DELIMITER);
                String savedUsername = credentials[0];
                String encodedPassword = credentials[2];
                if (username.equals(savedUsername) && PasswordEncoder.validatePassword(password, encodedPassword)) {
                    return true;
                }
            }
        } catch (FileNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }
}
