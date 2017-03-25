/**
 *
 Let's use this class to construct a user.
 You can define all variables that the user may use within the application.
 Just add the varibale and setters and getters. Modify constructors as needed to implement new variables



 */


package mellon;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WebAccount {

    private String username; // These are the plaintext strings that will be displayed on the front-end
    private String password;
    private String encodedUsername;
    private String encodedPassword;
    private String accountName;
    private int id;
    private int webID;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    // Use this constructor when creating instances of WebAccount from the database
    public WebAccount(int ID, String encodedUsername, String encodedPassword, int webID, String accountName, String masterKey) {
        this.id = ID;
        this.encodedUsername = encodedUsername;
        this.encodedPassword = encodedPassword;
        this.webID = webID;
        this.accountName = accountName;

        // Decode the username and passwords
        this.username = decode(encodedUsername, masterKey);
        this.password = decode(encodedPassword, masterKey);
    }

    // Use this constructor when creating instances of WebAccount from user input
    public WebAccount(String username, String password, String accountName, String masterKey) {
        this.username = username;
        this.password = password;
        this.accountName = accountName;

        // Encode the username and password
        this.encodedUsername = encode(username, masterKey);
        this.encodedPassword = encode(password, masterKey);
    }


    public String decode(String input, String masterKey) {
//        Key aesKey = new SecretKeySpec(masterKey.getBytes(), "AES");
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, aesKey);
//            byte[] encodedBytes = cipher.doFinal(input.getBytes());
//            String decrypted = new String(cipher.doFinal(encodedBytes));
//            return decrypted;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return "";
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
        return new String(encodedBytes);
    }

    public String encode(String input, String masterKey) {
//        Key aesKey = new SecretKeySpec(masterKey.getBytes(), "AES");
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
//            byte[] encodedBytes = cipher.doFinal(input.getBytes());
//            return new String(encodedBytes);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return "";
        byte[] decodedBytes = Base64.getDecoder().decode(input.getBytes());
        return new String(decodedBytes);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
