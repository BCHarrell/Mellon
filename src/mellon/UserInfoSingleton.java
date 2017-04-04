package mellon;

import java.util.ArrayList;

/**
 * Created by Thomas Hodges on 3/26/2017. UserInfoSingleton.java
 */
public class UserInfoSingleton {

    private static UserInfoSingleton instance;
    private static int userID;
    private static String username;
    private static String password;
    private static MasterAccount masterAccount;
    private static int timeoutDuration;
    private static boolean copyPassword;
    private static int defaultPasswordLength;
    private static ArrayList<WebAccount> profiles = new ArrayList<>();

    private UserInfoSingleton() {
    }

    public static UserInfoSingleton getInstance() {
        if (instance == null) {
            instance = new UserInfoSingleton();
        }
        return instance;
    }

    public static void logout() {
        userID = 0;
        username = null;
        password = null;
        masterAccount = null;
        timeoutDuration = 999;
        copyPassword = false;
        defaultPasswordLength = 16;
        profiles.clear();
        instance = null;
    }

    public static void updateMasterAccount(MasterAccount account) {
        masterAccount = account;
        addProfiles(account.getUserAccounts());
    }

    public static MasterAccount getMasterAccount() {
        return masterAccount;
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

    public static void setPassword(String passwordIn) {
        UserInfoSingleton.password = passwordIn;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String usernameIn) {
        UserInfoSingleton.username = usernameIn;
    }

    public static void addProfiles(ArrayList<WebAccount> profilesIn) {
        if (profiles.size() != 0) {
            profiles.clear();
        }
        UserInfoSingleton.profiles.addAll(profilesIn);
    }

    public static void addSingleProfile(WebAccount profileIn) {
        profiles.stream().forEach(p -> {
            if (p.getWebID() == profileIn.getWebID()) {
                int index = profiles.indexOf(p);
                profiles.set(index, profileIn);
                return;
            }
        });
        profiles.add(profileIn);
    }

    public static ArrayList<WebAccount> getProfiles() {
        return profiles;
    }

    public static int getTimeoutDuration() {
        return timeoutDuration;
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
