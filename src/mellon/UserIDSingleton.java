package mellon;

/**
 * Created by Thomas Hodges on 3/26/2017. UserIDSingleton.java
 */
public class UserIDSingleton {

    private static UserIDSingleton instance;
    private static int userID;
    private static String password;

    private UserIDSingleton() {
    }

    public static UserIDSingleton getInstance() {
        if (instance == null) {
            instance = new UserIDSingleton();
        }
        return instance;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userIDin) {
        userID = userIDin;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserIDSingleton.password = password;
    }
}
