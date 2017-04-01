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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
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
    public WebAccount(int ID,
                      String encodedUsername,
                      String encodedPassword,
                      int webID,
                      String encodedAccountName,
                      String masterKey,
                      LocalDate expDate) {
        this.id = ID;
        this.encodedUsername = encodedUsername;
        this.encodedPassword = encodedPassword;
        this.encodedAccountName = encodedAccountName;
        this.webID = webID;
        this.expDate = expDate;

        // Decode the username and passwords
        this.username = decode(encodedUsername, masterKey);
        this.password = decode(encodedPassword, masterKey);
        this.accountName = decode(encodedAccountName, masterKey);
    }

    // Use this constructor when creating instances of WebAccount from user input
    public WebAccount(String username,
                      String password,
                      String accountName,
                      String masterKey,
                      LocalDate expDate) {
        this.username = username;
        this.password = password;
        this.accountName = accountName;
        this.expDate = expDate;

        // Encode the username and password
        this.encodedUsername = encode(username, masterKey);
        this.encodedPassword = encode(password, masterKey);
        this.encodedAccountName = encode(accountName, masterKey);
    }
    
    public static String decode(final String ivAndEncryptedMessageBase64,
                                final String masterPassword) {
        final byte[] symKeyData = masterPassword.getBytes();

        final byte[] ivAndEncryptedMessage = DatatypeConverter
                .parseBase64Binary(ivAndEncryptedMessageBase64);
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(Arrays.copyOf(symKeyData, 16), "AES");

            // retrieve random IV from start of the received message
            final byte[] ivData = new byte[blockSize];
            System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            // retrieve the encrypted message itself
            final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length
                    - blockSize];
            System.arraycopy(ivAndEncryptedMessage, blockSize,
                    encryptedMessage, 0, encryptedMessage.length);

            cipher.init(Cipher.DECRYPT_MODE, symKey, iv);

            final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

            // concatenate IV and encrypted message
            final String message = new String(encodedMessage,
                    Charset.forName("UTF-8"));

            return message;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (BadPaddingException e) {
            // you'd better know about padding oracle attacks
            return null;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during decryption", e);
        }
    }

    public static String encode(final String plainMessage,
                                final String masterPassword) {
        final byte[] symKeyData = masterPassword.getBytes();

        final byte[] encodedMessage = plainMessage.getBytes(Charset
                .forName("UTF-8"));
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(Arrays.copyOf(symKeyData, 16), "AES");

            // generate random IV using block size (possibly create a method for
            // this)
            final byte[] ivData = new byte[blockSize];
            final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            rnd.nextBytes(ivData);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

            final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

            // concatenate IV and encrypted message
            final byte[] ivAndEncryptedMessage = new byte[ivData.length
                    + encryptedMessage.length];
            System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
            System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
                    blockSize, encryptedMessage.length);

            final String ivAndEncryptedMessageBase64 = DatatypeConverter
                    .printBase64Binary(ivAndEncryptedMessage);

            return ivAndEncryptedMessageBase64;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during encryption", e);
        }
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
    
    public String getAccountName(){
        return accountName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
     public String getEncodedUsername() {
        return encodedUsername;
    }

    public void setEncodedUsername(String encodedUsername) {
        this.encodedUsername = encodedUsername;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedAccountName() {
        return encodedAccountName;
    }
}
