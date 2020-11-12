package security;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoginHandler {

    private static final String USERS_FILE_PATH = "users.txt";

    public boolean login(String username, String password) {
        File users = new File(USERS_FILE_PATH);
        try (Scanner sc = new Scanner(users)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] credentials = line.split(",");
                String savedUsername = credentials[0];
                String savedPassword = credentials[2];
                if (username.equals(savedUsername) && PasswordEncoder.encodePassword(password).equals(savedPassword)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
