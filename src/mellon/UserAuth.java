package mellon;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Thomas Hodges on 3/22/2017.
 * UserAuth.java
 *
 * This class is going to hold the Master account info. All sub-accounts (associated to web
 * sites, etc.) will be in the ArrayList<MellonUser> variable.
 *
 * Something to keep in mind, a hash is one-way, meaning it can't be un-hashed.
 * We're going to be checking and storing hashes based on user input only.
 */
public class UserAuth {

    private String usernameHash;
    private String passwordHash;
//    private static boolean ACCOUNT_FOUND;
//    private static int userID;
    private Connection connection;
    private ArrayList<MellonUser> userAccounts;


    public UserAuth(String username, String password) throws NoSuchAlgorithmException {
        this.usernameHash = hashString(username);
        this.passwordHash = hashString(password);

        this.userAccounts = new ArrayList<>();
        this.connection = DBConnect.getConnect();
        this.userAccounts = DBConnect.getCredentials(this.usernameHash, this.passwordHash);
    }

    private String hashString(String input) {

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

    public ArrayList<MellonUser> getUserAccounts() {
        return this.userAccounts;
    }

    // Getters only for the encrypted username and password strings
    public String getUsernameHash() { return usernameHash; }
    public String getPasswordHash() { return passwordHash; }
}
