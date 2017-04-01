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
    private static ArrayList<WebAccount> profiles = new ArrayList<>();

    private UserInfoSingleton() {
    }

    public static UserInfoSingleton getInstance() {
        if (instance == null) {
            instance = new UserInfoSingleton();
        }
        return instance;
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
        profiles.add(profileIn);
    }

    public static ArrayList<WebAccount> getProfiles() {
        return profiles;
    }
}
