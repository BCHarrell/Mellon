// Let's use this class to construct a user. 
// You can define all variables that the user may use within the application. 
// Just add the varibale and setters and getters. Modify constructors as needed to implement new variables
package mellon;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MellonUser {

    private String username;
    private String password;
    private String encodedUsername;
    private String encodedPassword;
    private int id;
    private boolean accountFound = false;
    private UserAuth auth;

    public boolean isAccountFound() {
        return accountFound;
    }

    public void setAccountFound(boolean accountFound) {
        this.accountFound = accountFound;
    }


    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public MellonUser(int ID, String encodedUsername, String encodedPassword, boolean accountFound) {
        this.id = ID;
        this.encodedUsername = encodedUsername;
        this.encodedPassword = encodedPassword;
        this.username = decode(encodedUsername);
        this.password = decode(encodedPassword);
    }
    
    public MellonUser(String username, String password) {
        this.username = username;
        this.password = password;

        try {
            this.auth = new UserAuth(username, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public UserAuth getAuth() {
        return auth;
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
