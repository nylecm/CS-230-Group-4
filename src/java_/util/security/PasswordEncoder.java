package java_.util.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Encodes an entered password using the
 * PBKDF2 algorithm.
 *
 * @author Matej Hladky
 */
public class PasswordEncoder {

    /**
     * The used algorithm.
     */
    private static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * The splitter used for the encoded parts.
     */
    private static final String PASSWORD_ENCODER_SPLITTER = "/";

    /**
     * Salting bytes.
     */
    private static final int SALT_BYTES = 16;

    /**
     * Number of iterations of the algorithm.
     */
    private static final int ITERATIONS = 65536;

    /**
     * The length of the key.
     */
    private static final int KEY_LENGTH = 128;

    /**
     * Encodes the password using other methods
     * @param password - the raw password
     * @return encoded password
     */
    public static String encodePassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        String output = null;
        try {
            byte[] hash = generateHash(password, salt);
            output = bytesToHex(salt) + PASSWORD_ENCODER_SPLITTER + bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Generates the hash part for the encoded password.
     * @param password - the password
     * @param salt - the salt
     * @return - generated hash
     * @throws InvalidKeySpecException if invalid key is used
     * @throws NoSuchAlgorithmException if algorithm does not exist
     */
    public static byte[] generateHash(String password, byte[] salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec
                (password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory secretKeyFactory =
                SecretKeyFactory.getInstance(HASHING_ALGORITHM);
        return secretKeyFactory.generateSecret(spec).getEncoded();
    }

    /**
     * Validates the passed password.
     * @param password - the password
     * @param encodedPassword - the encoded password for comparison.
     * @return - true if the password is correct, false otherwise
     * @throws InvalidKeySpecException  if invalid key is used
     * @throws NoSuchAlgorithmException if algorithm does not exist
     */
    public static boolean validatePassword(String password, String encodedPassword)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] encodedPasswordParts = encodedPassword.split(PASSWORD_ENCODER_SPLITTER);
        byte[] salt = hexStringToByteArray(encodedPasswordParts[0]);
        byte[] hash = hexStringToByteArray(encodedPasswordParts[1]);
        byte[] tempHash = generateHash(password, salt);
        return Arrays.equals(hash, tempHash);
    }

    /**
     * Converts a hexadecimal value into a byte array
     * @param string - the hexadecimal value
     * @return the byte array
     */
    private static byte[] hexStringToByteArray(String string) {
        byte[] byteArray = new BigInteger(string, 16).toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, output, 0, output.length);
            return output;
        }
        return byteArray;
    }

    /**
     * Converts a byte arras to a hexadecimal value
     * @param array - the byte array
     * @return the hexadecimal value
     */
    private static String bytesToHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        return bigInteger.toString(16);
    }
}

