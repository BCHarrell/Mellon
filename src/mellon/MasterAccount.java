package mellon;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Thomas Hodges on 3/22/2017.
 * MasterAccount.java
 *
 * This class is going to hold the Master account info. All sub-accounts (associated to web
 * sites, etc.) will be in the ArrayList<WebAccount> variable.
 *
 * Something to keep in mind, a hash is one-way, meaning it can't be un-hashed.
 * We're going to be checking and storing hashes based on user input only.
 */
public class MasterAccount {

    private String usernameHash;
    private String passwordHash;
    private Connection connection;
    private ArrayList<WebAccount> userAccounts;
    private boolean authenticated;
    private int timeoutDuration;
    private boolean copyPassword;
    private int defaultPasswordLength;


    public MasterAccount(String username, String password) throws NoSuchAlgorithmException {
        this.usernameHash = hashString(username);
        this.passwordHash = hashString(password);
        UserInfoSingleton.getInstance().setPassword(password);
        this.userAccounts = new ArrayList<>();
        this.connection = DBConnect.getConnect();
        this.authenticated = DBConnect.authenticateUser(this.usernameHash, this.passwordHash);
        this.userAccounts = DBConnect.getCredentials(this.usernameHash, this.passwordHash, password);
    }

    public static String hashString(String input) {

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

    public ArrayList<WebAccount> getUserAccounts() {
        return this.userAccounts;
    }

    public boolean getAuthenticated() { return this.authenticated; }

    // Getters only for the encrypted username and password strings
    public String getUsernameHash() { return usernameHash; }
    public String getPasswordHash() { return passwordHash; }
}
