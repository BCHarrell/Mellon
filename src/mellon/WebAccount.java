/**
 *
 Let's use this class to construct a user.
 You can define all variables that the user may use within the application.
 Just add the varibale and setters and getters. Modify constructors as needed to implement new variables



 */


package mellon;

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
    public WebAccount(int ID, String encodedUsername, String encodedPassword, int webID, String accountName) {
        this.id = ID;
        this.encodedUsername = encodedUsername;
        this.encodedPassword = encodedPassword;
        this.webID = webID;
        this.accountName = accountName;

        // Decode the username and passwords
        this.username = decode(encodedUsername);
        this.password = decode(encodedPassword);
    }

    // Use this constructor when creating instances of WebAccount from user input
    public WebAccount(String username, String password, String accountName) {
        this.username = username;
        this.password = password;
        this.accountName = accountName;

        // Encode the username and password
        this.encodedUsername = encode(username);
        this.encodedPassword = encode(password);
    }


    public String decode(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
        return new String(encodedBytes);
    }

    public String encode(String input) {
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
