package mellon;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thomas Hodges on 3/22/2017.
 * UserAuth.java
 */
public class UserAuth {

    private String usernameEncrypted;
    private String passwordEncrypted;


    public UserAuth(String username, String password) throws NoSuchAlgorithmException {
        this.usernameEncrypted = encryptString(username);
        this.passwordEncrypted = encryptString(password);
    }

    private String encryptString(String input) {

        try {

            // Using MessageDigest to encrypt the inputs
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            // Create a byte array to hold the raw data
            byte[] bytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();

            // Need to loop over the byte array so that we can get a string out of it
            for (int i = 0; i < bytes.length; i++) {
                stringBuffer.append(Integer.toHexString(0xff & bytes[i]));
            }

            // return the result
            return stringBuffer.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Need to find a way to NOT return null...
            return null;
        }
    }

    // Getters only for the encrypted username and password strings
    public String getUsernameEncrypted() { return usernameEncrypted; }
    public String getPasswordEncrypted() { return passwordEncrypted; }
}
