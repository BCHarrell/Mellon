/*
The following class will be used to connect to Oracle database and retrieve information 
from the various tables. Multiple SQLs will be used to get and verify different type of info
 */
package mellon;

import static java.lang.System.out;
import java.sql.*;

public class DBConnect {

    private static boolean ACCOUNT_FOUND;
    private static int userID;
// This method initiates the connection to Mellon database 

    public static Connection getConnect() {
        Connection conn = null;
        String url = "jdbc:oracle:thin:@mellon.cg2xm5fvbkm2.us-east-1.rds.amazonaws.com:1521:mellon";
        String driver = "oracle.jdbc.OracleDriver";
        String userName = "Mellon";
        String password = "CMSC4952017";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static MellonUser getCredintials(String username) {
        PreparedStatement stmt1 = null;
        ResultSet rset1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rset2 = null;
        MellonUser user = null;
        Connection conn = getConnect();

        try {
            stmt1 = conn.prepareStatement("SELECT USER_ID FROM ACCOUNT_MASTER WHERE USERNAME = ?");
            stmt1.setString(1, username);
            rset1 = stmt1.executeQuery();
            while (rset1.next()) {
                userID = rset1.getInt(1);
                if (userID != 0) {
                    ACCOUNT_FOUND = true;
                } else {
                    ACCOUNT_FOUND = false;
                }

                stmt2 = conn.prepareStatement("SELECT MASTER_KEY FROM ACCOUNT_INFO WHERE USER_ID = ?");
                stmt2.setInt(1, userID);
                rset2 = stmt2.executeQuery();
                while (rset2.next()) {
                    String password = rset2.getString(1);
                    rset2.close();
                    stmt2.close();
                    conn.close();
                    user = new MellonUser(userID, username, password, ACCOUNT_FOUND);
                }
                rset1.close();
                stmt1.close();
            }
        } catch (SQLException se) {
            // We may need to add another class for exceptions only
        }
        return user;
    }
}
