package mellon;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Thomas Hodges on 3/22/2017.
 * UserAuth.java
 */
public class UserAuth {

    private String usernameEncrypted;
    private String passwordEncrypted;
    private static boolean ACCOUNT_FOUND;
    private static int userID;
    private Connection connection;
    private ArrayList<MellonUser> userAccounts;


    public UserAuth(String username, String password) throws NoSuchAlgorithmException {
        this.usernameEncrypted = encryptString(username);
        this.passwordEncrypted = encryptString(password);

        this.userAccounts = new ArrayList<>();
        this.connection = getConnect();

        PreparedStatement statement1;
        ResultSet resultSet1;
        PreparedStatement statement2;
        ResultSet resultSet2;


        try {
            statement1 = connection.prepareStatement("SELECT USER_ID FROM ACCOUNT_MASTER WHERE USERNAME = ?");
            statement1.setString(1, username);
            resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                userID = resultSet1.getInt(1);
                if (userID != 0) {
                    System.out.println("Account found");
                    ACCOUNT_FOUND = true;
                } else {
                    System.out.println("Account NOT found");
                    ACCOUNT_FOUND = false;
                }

                statement2 = connection.prepareStatement("SELECT MASTER_KEY FROM ACCOUNT_INFO WHERE USER_ID = ?");
                statement2.setInt(1, userID);
                resultSet2 = statement2.executeQuery();
                while (resultSet2.next()) {
                    String passwordResult = resultSet2.getString(1);
                    resultSet2.close();
                    statement2.close();
                    connection.close();
                    userAccounts.add(new MellonUser(userID, username, passwordResult, ACCOUNT_FOUND));
                }
                resultSet1.close();
                statement1.close();
            }
        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
    }

    private String encryptString(String input) {

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

    // Getters only for the encrypted username and password strings
    public String getUsernameEncrypted() { return usernameEncrypted; }
    public String getPasswordEncrypted() { return passwordEncrypted; }
}
