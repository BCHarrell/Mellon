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
//    private Connection connection;

    // This method initiates the connection to Mellon database
    public DBConnect() {
//        this.connection = getConnect();
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
                    ouputExpiration = null;
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement("SELECT USER_ID " +
                                                             "FROM ACCOUNT_MASTER " +
                                                             "WHERE USERNAME = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userID = resultSet.getInt(1);
                if (userID != 0) {
                    ACCOUNT_FOUND = true;
                } else {
                    ACCOUNT_FOUND = false;
                }
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return ACCOUNT_FOUND;
    }

    public static boolean authenticateUser(String username, String password) {
        ACCOUNT_FOUND = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement("SELECT AM.USER_ID " +
                                                             "FROM ACCOUNT_MASTER AM " +
                                                             "INNER JOIN ACCOUNT_INFO AI " +
                                                             "ON AM.USER_ID = AI.USER_ID " +
                                                             "WHERE AM.USERNAME = ? " +
                                                             "AND AI.MASTER_KEY = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userID = resultSet.getInt(1);
                if (userID != 0) {
                    ACCOUNT_FOUND = true;
                    UserInfoSingleton id = UserInfoSingleton.getInstance();
                    id.setUserID(userID);
                } else {
                    ACCOUNT_FOUND = false;
                }
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return ACCOUNT_FOUND;
    }

    // Inputs are expected to be hashed, returns true if new master user created
    public static boolean registerUser(String newUsername, String newPassword) {  

        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        ResultSet resultSet = null;
        Connection connection = getConnect();
        try {
            preparedStatement1 = connection.prepareStatement("INSERT INTO ACCOUNT_MASTER (USERNAME) VALUES (?)");
            preparedStatement1.setString(1, newUsername);
            preparedStatement1.executeQuery();
            preparedStatement2 = connection.prepareStatement("SELECT USER_ID FROM ACCOUNT_MASTER WHERE username = ?");
            preparedStatement2.setString(1, newUsername);
            resultSet = preparedStatement2.executeQuery();
            while (resultSet.next()) {
                userID = resultSet.getInt(1);
                UserInfoSingleton id = UserInfoSingleton.getInstance();
                id.setUserID(userID);
            }
            preparedStatement3 = connection.prepareStatement("INSERT INTO ACCOUNT_INFO (USER_ID, MASTER_KEY) VALUES (?,?)");
            preparedStatement3.setInt(1, userID);
            preparedStatement3.setString(2, newPassword);
            preparedStatement3.executeQuery();
            preparedStatement4 = connection.prepareStatement("INSERT INTO ACCOUNT_SETTINGS (USER_ID) VALUES (?)");
            preparedStatement4.setInt(1, userID);
            preparedStatement4.executeQuery();
            resultSet.close();
            preparedStatement1.close();
            preparedStatement2.close();
            preparedStatement3.close();
            preparedStatement4.close();
            connection.close();
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
        java.sql.Date date;
        if (inputExpiration == null) {
            date = null;
        }else {
            date = java.sql.Date.valueOf(inputExpiration);
        }
        PreparedStatement preparedStatement = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO WEB_ACCOUNTS (USER_ID, " +
                                                    "ACCOUNT_NAME, " +
                                                    "WEB_USERNAME, " +
                                                    "KEY, " +
                                                    "EXP_DT) " +
                        "VALUES (?,?,?,?,to_date(?))");
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

    public static boolean updateMasterPassword(int userIDin, String passwordHash) {
        if (userIDin == 0 || passwordHash.isEmpty()) {
            return false;
        }
        boolean updated = false;
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE ACCOUNT_INFO " +
                                                            "SET MASTER_KEY = ? " +
                                                            "WHERE USER_ID = ?");
            preparedStatement.setString(1, passwordHash);
            preparedStatement.setInt(2, userIDin);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            updated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public static boolean updateWebAccount(int userIDIn,
                                           int webIDIn,
                                           String accountName,
                                           String username,
                                           String password) {
        if (userIDIn == 0 || webIDIn == 0 || accountName.isEmpty()
                || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        boolean updated = false;
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE WEB_ACCOUNTS " +
                                                            "SET ACCOUNT_NAME = ?, " +
                                                                "WEB_USERNAME = ?, " +
                                                                "KEY = ?" +
                                                            "WHERE USER_ID = ? " +
                                                            "AND WEB_ID = ?");
            preparedStatement.setString(1, accountName);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, userIDIn);
            preparedStatement.setInt(5, webIDIn);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            updated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public static int existingWebAccount(int userIDIn, String accountNameIn) {
        int existingWebAccount = 0;
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT WEB_ID " +
                                                            "FROM WEB_ACCOUNTS " +
                                                            "WHERE USER_ID = ? " +
                                                            "AND ACCOUNT_NAME = ?");
            preparedStatement.setInt(1, userIDIn);
            preparedStatement.setString(2, accountNameIn);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                existingWebAccount = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existingWebAccount;
    }

    public static int getTimeoutDuration(int userIDin) {
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int timeout = 0;
        try {
            preparedStatement = connection.prepareStatement("SELECT SESSION_TIMEOUT " +
                                                            "FROM ACCOUNT_SETTINGS " +
                                                            "WHERE USER_ID = ?");
            preparedStatement.setInt(1, userIDin);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                timeout = resultSet.getInt(1);
                UserInfoSingleton.getInstance().setTimeoutDuration(timeout);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timeout;
    }

    public static int getDefaultPassLength(int userIDin) {
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int passLength = 0;
        try {
            preparedStatement = connection.prepareStatement("SELECT DEFAULT_PASS_LENGTH " +
                                                            "FROM ACCOUNT_SETTINGS " +
                                                            "WHERE USER_ID = ?");
            preparedStatement.setInt(1, userIDin);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                passLength = resultSet.getInt(1);
                UserInfoSingleton.getInstance().setDefaultPasswordLength(passLength);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passLength;
    }
    
    public static void updatePrefrenceSettings(int userIDin,
                                               int newTimeoutDuration,
                                               int newPasswordLength) {
        Connection connection = getConnect();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE ACCOUNT_SETTINGS " +
                                                            "SET SESSION_TIMEOUT = ?, " +
                                                            "DEFAULT_PASS_LENGTH = ?  " +
                                                            "WHERE USER_ID = ?");
            preparedStatement.setInt(1, newTimeoutDuration);
            preparedStatement.setInt(2, newPasswordLength);
            preparedStatement.setInt(3, userIDin);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public static void reportBug(String userEmail,String bugReport) {
        PreparedStatement preparedStatement = null;
        Connection connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO EMAIL_DATA (USER_EMAIL,DESCRIPTION) VALUES (?,TO_CLOB(?))");
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, bugReport);
            preparedStatement.executeQuery();
            preparedStatement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
