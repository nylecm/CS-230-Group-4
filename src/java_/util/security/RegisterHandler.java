package java_.util.security;

import java.io.FileWriter;
import java.io.IOException;

public class RegisterHandler {

    private static final String USERS_FILE_PATH = "users.txt";

    public static void register(String username, String email, String password) {
        String encodePassword = PasswordEncoder.encodePassword(password);
        try {
            FileWriter writer = new FileWriter(USERS_FILE_PATH);
            writer.write(username +"` " + email + "` " + encodePassword);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
