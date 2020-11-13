package security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordEncoder {

    private static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int STRENGTH = 65536;
    private static final int KEY_LENGTH = 128;

    public static String encodePassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        String output = null;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, STRENGTH, KEY_LENGTH);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASHING_ALGORITHM);
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
            output = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void main(String[] args) {
        encodePassword("Ahoj");
        encodePassword("Ah0j");
        encodePassword("Ahoi");
        encodePassword("ahoj");
        encodePassword("df15m3/45sGhgf45");
    }
}

