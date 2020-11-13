package security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PasswordEncoder {

    private static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;


    public static String encodePassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        String output = null;
        try {
            byte[] hash = generateHash(password, salt);
            output = bytesToHex(salt) + "/" + bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static byte[] generateHash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASHING_ALGORITHM);
        return secretKeyFactory.generateSecret(spec).getEncoded();
    }

    public static boolean validatePassword(String password, String encodedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] encodedPasswordParts = encodedPassword.split("/");
        byte[] salt = hexStringToByteArray(encodedPasswordParts[0]);
        byte[] hash = hexStringToByteArray(encodedPasswordParts[1]);
        byte[] tempHash = generateHash(password, salt);
        return Arrays.equals(hash, tempHash);
    }

    private static byte[] hexStringToByteArray(String string) {
        byte[] byteArray = new BigInteger(string, 16)
                .toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(
                    byteArray, 1, output,
                    0, output.length);
            return output;
        }
        return byteArray;
    }

    private static String bytesToHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        return bigInteger.toString(16);
    }


    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String passwordToStore = "Hello5_Hi";
        String enteredPassword = "Hello5_Hi";

        System.out.println(validatePassword(enteredPassword, encodePassword(passwordToStore)));
    }
}

