package security;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class LoginHandler {

    private static final String USERS_FILE_PATH = "users.txt";

    public static boolean login(String username, String password) {
        File users = new File(USERS_FILE_PATH);
        try (Scanner sc = new Scanner(users)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] credentials = line.split(",");
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

    public static void main(String[] args) {
        System.out.println(login("GirionBorec", "Hello5_Hi"));
    }
}
