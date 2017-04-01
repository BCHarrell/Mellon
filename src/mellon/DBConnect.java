/**
 * The following class will be used to connect to Oracle database and retrieve information
 * from the various tables. Multiple SQLs will be used to get and verify different type of info
 * <p>
 * WEB_ACCOUNTS will also need a password column as well, since I'm going to send encoded strings
 * up to it.
 */
package mellon;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnect {

    private static boolean ACCOUNT_FOUND;
    private static boolean ACCOUNT_CREATED;
    private static int userID;
    private Connection connection;

    // This method initiates the connection to Mellon database
    public DBConnect() {
        this.connection = getConnect();
    }

    public boolean getAccountFound() { return this.ACCOUNT_FOUND; }

    public static Connection getConnect() {
        Connection connection = null;
        String url = "jdbc:oracle:thin:@mellon.cg2xm5fvbkm2.us-east-1.rds.amazonaws.com:1521:mellon";
        String driver = "oracle.jdbc.OracleDriver";
        String userName = "Mellon";
        String password = "CMSC4952017";
        Properties properties = new Properties();
        properties.setProperty("ssl", "true");
        properties.setProperty("user", userName);
        properties.setProperty("password", password);
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    // Method to get user's creds from the login page and create a WebAccount object to be used by other methods
    // Passing in hashed username and password
    public static ArrayList<WebAccount> getCredentials(String usernameHash,
                                                       String passwordHash,
                                                       String plainPassword) {
        ArrayList<WebAccount> users = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement("SELECT WEB_USERNAME, " +
                                         "KEY, WEB_ID, ACCOUNT_NAME, EXP_DT " +
                                         "FROM WEB_ACCOUNTS WHERE USER_ID IN (" +
                                            "SELECT AM.USER_ID " +
                                            "FROM ACCOUNT_MASTER AM " +
                                            "INNER JOIN ACCOUNT_INFO AI " +
                                            "ON AM.USER_ID = AI.USER_ID " +
                                            "WHERE AM.USERNAME = ? " +
                                            "AND AI.MASTER_KEY = ?)");
            preparedStatement.setString(1, usernameHash);
            preparedStatement.setString(2, passwordHash);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String encodedWebUsername = resultSet.getString(1);
                String encodedWebPassword = resultSet.getString(2);
                int webID = resultSet.getInt(3);
                String encodedAccountName = resultSet.getString(4);
                LocalDate ouputExpiration;
                if (resultSet.getDate(5) == null) {
                    ouputExpiration = LocalDate.now();
                } else {
                    ouputExpiration = resultSet.getDate(5).toLocalDate();
                }

                users.add(new WebAccount(userID,
                                         encodedWebUsername,
                                         encodedWebPassword,
                                         webID,
                                         encodedAccountName,
                                         plainPassword,
                                         ouputExpiration));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        UserInfoSingleton.getInstance().addProfiles(users);
        return users;
    }

    // Method to check if an account exist
    // Added expected PASSWORD column and now checking both for the correct username and password
    // Inputs are expected to be hashed
    public static boolean checkUser(String username) {
        PreparedStatement stmt = null;
        ResultSet rset = null;
        Connection conn = getConnect();
        try {
            stmt = conn.prepareStatement("SELECT USER_ID " +
                                         "FROM ACCOUNT_MASTER " +
                                         "WHERE USERNAME = ?");
            stmt.setString(1, username);
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

    public static boolean authenticateUser(String username, String password) {
        PreparedStatement stmt = null;
        ResultSet rset = null;
        Connection conn = getConnect();
        try {
            stmt = conn.prepareStatement("SELECT AM.USER_ID " +
                                         "FROM ACCOUNT_MASTER AM " +
                                         "INNER JOIN ACCOUNT_INFO AI " +
                                         "ON AM.USER_ID = AI.USER_ID " +
                                         "WHERE AM.USERNAME = ? " +
                                         "AND AI.MASTER_KEY = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rset = stmt.executeQuery();
            while (rset.next()) {
                userID = rset.getInt(1);
                if (userID != 0) {
                    ACCOUNT_FOUND = true;
                    UserInfoSingleton id = UserInfoSingleton.getInstance();
                    id.setUserID(userID);
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
            stmt1 = conn.prepareStatement("INSERT INTO ACCOUNT_MASTER (USERNAME) VALUES (?)");
            stmt1.setString(1, newUsername);
            stmt1.executeQuery();
            stmt2 = conn.prepareStatement("SELECT USER_ID FROM ACCOUNT_MASTER WHERE username = ?");
            stmt2.setString(1, newUsername);
            rset = stmt2.executeQuery();
            while (rset.next()) {
                userID = rset.getInt(1);
                UserInfoSingleton id = UserInfoSingleton.getInstance();
                id.setUserID(userID);
            }
            stmt3 = conn.prepareStatement("INSERT INTO ACCOUNT_INFO (USER_ID, MASTER_KEY) VALUES (?,?)");
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
    // Inputs are expected to be hashed, returns true if new master user created
    public static boolean CreateWebAccount(int id,
                                           String inputNickname,
                                           String username,
                                           String password,
                                           LocalDate inputExpiration) {
        Date date;
        if (inputExpiration == null) {
            date = null;
        }else {
            date = Date.valueOf(inputExpiration);
        }
        PreparedStatement preparedStatement = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO WEB_ACCOUNTS (USER_ID, " +
                                                                    "ACCOUNT_NAME, " +
                                                                    "WEB_USERNAME, " +
                                                                    "KEY, " +
                                                                    "EXP_DT) " +
                                        "VALUES (?,?,?,?,to_date(?,'MM/DD/YYYY'))");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, inputNickname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setDate(5, date);
            preparedStatement.executeQuery();
            preparedStatement.close();
            connection.close();
            ACCOUNT_CREATED = true;
        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return ACCOUNT_CREATED;
    }
}
