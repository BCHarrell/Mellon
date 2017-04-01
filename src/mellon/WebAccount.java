/**
 *
 Let's use this class to construct a user.
 You can define all variables that the user may use within the application.
 Just add the varibale and setters and getters. Modify constructors as needed to implement new variables



 */


package mellon;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;

public class WebAccount {

    private String username; // These are the plaintext strings that will be displayed on the front-end
    private String password;
    private String encodedUsername;
    private String encodedPassword;
    private String accountName;
    private String encodedAccountName;
    private int id;
    private int webID;
    private LocalDate expDate;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    // Use this constructor when creating instances of WebAccount from the database
    // Expects encoded input with plain text master password
    public WebAccount(int IDIn,
                      String encodedUsernameIn,
                      String encodedPasswordIn,
                      int webIDIn,
                      String encodedAccountNameIn,
                      String masterKey,
                      LocalDate expDateIn) {
        this.id = IDIn;
        this.encodedUsername = encodedUsernameIn;
        this.encodedPassword = encodedPasswordIn;
        this.encodedAccountName = encodedAccountNameIn;
        this.webID = webIDIn;
        this.expDate = expDateIn;

        // Decode the username and passwords
        this.username = decode(this.encodedUsername, masterKey);
        this.password = decode(this.encodedPassword, masterKey);
        this.accountName = decode(this.encodedAccountName, masterKey);
    }

    // Use this constructor when creating instances of WebAccount from user input
    // Expects plain text input
    public WebAccount(String usernameIn,
                      String passwordIn,
                      String accountNameIn,
                      String masterKey,
                      LocalDate expDateIn) {
        this.username = usernameIn;
        this.password = passwordIn;
        this.accountName = accountNameIn;
        this.expDate = expDateIn;

        // Encode the username and password
        this.encodedUsername = encode(this.username, masterKey);
        this.encodedPassword = encode(this.password, masterKey);
        this.encodedAccountName = encode(this.accountName, masterKey);
    }

    public static SecretKey setEncryptionKey(String myKey) {
        MessageDigest sha = null;
        byte[] key = new byte[0];
        SecretKey secretKey = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    public static SecretKey setDecryptionKey(String myKey) {
        SecretKey encryptionKey = setEncryptionKey(myKey);
        SecretKey decryptionKey = new SecretKeySpec(encryptionKey.getEncoded(),
                encryptionKey.getAlgorithm());
        return decryptionKey;
    }

    public static String encode(String strToEncrypt, String secret) {
        try {
            SecretKey secretKey = setEncryptionKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decode(String strToDecrypt, String secret) {
        try {
            SecretKey secretKey = setDecryptionKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), "UTF-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }
    
    public String getAccountName(){
        return this.accountName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

     public String getEncodedUsername() {
        return this.encodedUsername;
    }

    public void setEncodedUsername(String encodedUsername) {
        this.encodedUsername = encodedUsername;
    }

    public String getEncodedPassword() {
        return this.encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedAccountName() {
        return this.encodedAccountName;
    }
}
