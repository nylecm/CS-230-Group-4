package java_.util.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class RegisterHandler {

    private static final String USERS_FILE_PATH = "users.txt";
    private static final String DELIMITER = "`";

    public static void register(String username, String email, String password) throws IOException, IllegalArgumentException {
        Scanner in = new Scanner(new File(USERS_FILE_PATH));
        in.useDelimiter("`");

        while (in.hasNextLine()) {
            if (in.next().equals(username)) {
                throw new IllegalArgumentException("User with such username exists!");
            } else {
                in.nextLine();
            }
        }
        in.close();

        String encodePassword = PasswordEncoder.encodePassword(password);

        //todo empty first line...
        Files.write(Paths.get(USERS_FILE_PATH),
                ("\n" + username + DELIMITER + email + DELIMITER + encodePassword + DELIMITER).getBytes(),
                StandardOpenOption.APPEND);
    }
}
