// Let's use this class to construct a user. 
// You can define all variables that the user may use within the application. 
// Just add the varibale and setters and getters. Modify constructors as needed to implement new variables
package mellon;

public class MellonUser {

    private String username;
    private String password;
    private int id;
    private boolean accountFound;

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
        this.accountFound = accountFound;
    }
    
    public MellonUser(String username, String password) {
        this.username = username;
        this.password = password;
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
