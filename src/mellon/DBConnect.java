/**
 * The following class will be used to connect to Oracle database and retrieve information
 * from the various tables. Multiple SQLs will be used to get and verify different type of info
 * <p>
 * WEB_ACCOUNTS will also need a password column as well, since I'm going to send encoded strings
 * up to it.
 */
package mellon;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnect {

    private static boolean ACCOUNT_FOUND;
    private static int userID;
    private Connection connection;

    // This method initiates the connection to Mellon database
    public DBConnect() {
        this.connection = getConnect();
    }

    public boolean getAccountFound() { return this.ACCOUNT_FOUND; }

    public static Connection getConnect() {
        Connection conn = null;
        String url = "jdbc:oracle:thin:@mellon.cg2xm5fvbkm2.us-east-1.rds.amazonaws.com:1521:mellon";
        String driver = "oracle.jdbc.OracleDriver";
        String userName = "Mellon";
        String password = "CMSC4952017";
        Properties props = new Properties();
        props.setProperty("ssl", "true");
        props.setProperty("user", userName);
        props.setProperty("password", password);
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

// Method to get user's creds from the login page and create a WebAccount object to be used by other methods

    public static ArrayList<WebAccount> getCredentials(String username, String password) {
        ArrayList<WebAccount> users = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rset = null;
        Connection conn = getConnect();

        try {
            // Validates that the username and password is correct
            ACCOUNT_FOUND = checkUser(username, password);
            if (ACCOUNT_FOUND) {
                // If master account is correct, need to populate web accounts
                stmt = conn.prepareStatement("SELECT WEB_USERNAME, KEY, WEB_ID, ACCOUNT_NAME FROM WEB_ACCOUNTS WHERE USER_ID = ?");
                stmt.setInt(1, userID);
                rset = stmt.executeQuery();
                while (rset.next()) {
                    String webUsername = rset.getString(1);
                    String key = rset.getString(2);
                    int webID = rset.getInt(3);
                    String accountName = rset.getString(4);
                    users.add(new WebAccount(userID, webUsername, key, webID, accountName, password));
                }
                rset.close();
                stmt.close();
                conn.close();
            } else {
                // will be handled in the interface for now. 
            }
        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }

        return users;
    }

    // Method to check if an account exist
    // Added expected PASSWORD column and now checking both for the correct username and password
    // Inputs are expected to be hashed
    public static boolean checkUser(String username, String password) {
        PreparedStatement stmt = null;
        ResultSet rset = null;
        Connection conn = getConnect();
        try {
            stmt = conn.prepareStatement("SELECT AM.USER_ID FROM ACCOUNT_MASTER AM INNER JOIN ACCOUNT_INFO AI ON AM.USER_ID = AI.USER_ID WHERE AM.USERNAME = ? AND AI.MASTER_KEY = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rset = stmt.executeQuery();
            while (rset.next()) {
                userID = rset.getInt(1);
                if (userID != 0) {
                    ACCOUNT_FOUND = true;
                } else {
                    ACCOUNT_FOUND = false;
                }
            }
            rset.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return ACCOUNT_FOUND;
    }

    // Inputs are expected to be hashed, returns true if new master user created
    public static boolean registerUser(String newUsername, String newPassword) {

        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rset = null;
        Connection conn = getConnect();
        try {
            stmt1 = conn.prepareStatement("INSERT INTO ACCOUNT_MASTER (USERNAME) values (?)");
            stmt1.setString(1, newUsername);
            stmt1.executeQuery();
            stmt2 = conn.prepareStatement("SELECT USER_ID FROM ACCOUNT_MASTER WHERE username = ?");
            stmt2.setString(1, newUsername);
            rset = stmt2.executeQuery();
            while (rset.next()) {
                userID = rset.getInt(1);
            }
            stmt3 = conn.prepareStatement("INSERT INTO ACCOUNT_INFO (USER_ID,MASTER_KEY) values (?,?)");
            stmt3.setInt(1, userID);
            stmt3.setString(2, newPassword);
            stmt3.executeQuery();
            rset.close();
            stmt1.close();
            stmt2.close();
            stmt3.close();
            conn.close();
            ACCOUNT_FOUND = true;

        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return ACCOUNT_FOUND;
    }
}
