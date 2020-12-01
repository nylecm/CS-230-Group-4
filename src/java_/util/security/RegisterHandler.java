package java_.util.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 *
 * @author Hladky Matej original version, nylecm major overhaul.
 */
public class RegisterHandler {

    private static final String USERS_FILE_PATH = "users.txt";
    private static final String USER_DUPLICATE_USERNAME_MSG =
            "User with such username exists!";
    private static final String DELIMITER = "`";

    public static void register(String username, String email, String password) throws IOException, IllegalArgumentException {
        Scanner in = new Scanner(new File(USERS_FILE_PATH));
        in.useDelimiter("`");
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
    }
}
