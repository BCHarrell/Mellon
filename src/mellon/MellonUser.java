// Let's use this class to construct a user. 
// You can define all variables that the user may use within the application. 
// Just add the varibale and setters and getters. Modify constructors as needed to implement new variables
package mellon;

import java.security.NoSuchAlgorithmException;

public class MellonUser {

    private String username; // This will be deleted once encryption is completed (userAuth will be used instead)
    private String password; // This will be deleted once encryption is completed (userAuth will be used instead)
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

    public MellonUser(int ID, String username, String password, boolean accountFound) {
        this.id = ID;
        this.username = username;
        this.password = password;

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
