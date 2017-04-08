package mellon;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Thomas Hodges on 3/26/2017. UserInfoSingleton.java
 */
public class UserInfoSingleton {

    private static UserInfoSingleton instance;
    private static int userID;
    private static String username;
    private static String password;
    private static String usernameHash;
    private static String passwordHash;
    private static int timeoutDuration;
    private static boolean copyPassword;
    private static boolean authenticated;
    private static int defaultPasswordLength;
    private static Connection connection;
    private static ArrayList<WebAccount> profiles = new ArrayList<>();

    private UserInfoSingleton() {
    }

    public static UserInfoSingleton getInstance() {
        if (instance == null) {
            instance = new UserInfoSingleton();
        }
        return instance;
    }

    public static void resetNoLogout() {
        authenticated = false;
        userID = 0;
        username = null;
        password = null;
        usernameHash = null;
        passwordHash = null;
        timeoutDuration = 999;
        copyPassword = false;
        defaultPasswordLength = 16;
        connection = null;
        profiles.clear();
    }

    public static void logout() {
        authenticated = false;
        userID = 0;
        username = null;
        password = null;
        timeoutDuration = 0;
        copyPassword = false;
        defaultPasswordLength = 16;
        connection = null;
        profiles.clear();
        instance = null;
    }

    public static boolean init(String usernameIn, String passwordIn) {
        resetNoLogout();
        username = usernameIn;
        password = passwordIn;
        usernameHash = hashString(usernameIn);
        passwordHash = hashString(passwordIn);
        setConnection();
        authenticated = DBConnect.authenticateUser(usernameHash, passwordHash);
        if (authenticated) {
            profiles = DBConnect.getCredentials(usernameHash, passwordHash, password);
            return true;
        } else {
            logout();
            return false;
        }
    }

    public static void setUpNewUser(String usernameIn, String passwordIn) {
        resetNoLogout();
        username = usernameIn;
        password = passwordIn;
        usernameHash = hashString(usernameIn);
        passwordHash = hashString(passwordIn);
    }

    public static String hashString(String input) {
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

    private static void setConnection() {
        UserInfoSingleton.connection = DBConnect.getConnect();
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userIDin) {
        UserInfoSingleton.userID = userIDin;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String passwordIn) {
        UserInfoSingleton.password = passwordIn;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String usernameIn) {
        UserInfoSingleton.username = usernameIn;
    }

    public static String getUsernameHash() {
        return usernameHash;
    }

    public static String getPasswordHash() {
        return passwordHash;
    }

    public static void addProfiles(ArrayList<WebAccount> profilesIn) {
        profiles = new ArrayList<>();
        UserInfoSingleton.profiles.addAll(profilesIn);
    }

    public static void addSingleProfile(WebAccount profileIn) {
        final int[] index = new int[1];
        profiles.stream().forEach(p -> {
            if (p.getWebID() == profileIn.getWebID()) {
                index[0] = profiles.indexOf(p);
            }
        });
        profiles.remove(index[0]);
        profiles.add(profileIn);
    }

    public static void addNewProfile(WebAccount profileIn) {
        profiles.add(profileIn);
    }

    public static ArrayList<WebAccount> getProfiles() {
        return profiles;
    }

    public static int getTimeoutDuration() {
        return DBConnect.getTimeoutDuration(userID);
    }

    public static void setTimeoutDuration(int timeoutDuration) {
        UserInfoSingleton.timeoutDuration = timeoutDuration;
    }

    public static boolean isCopyPassword() {
        return copyPassword;
    }

    public static void setCopyPassword(boolean copyPassword) {
        UserInfoSingleton.copyPassword = copyPassword;
    }

    public static int getDefaultPasswordLength() {
        return defaultPasswordLength;
    }

    public static void setDefaultPasswordLength(int defaultPasswordLength) {
        UserInfoSingleton.defaultPasswordLength = defaultPasswordLength;
    }
}
