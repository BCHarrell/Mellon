package mellon;

import java.sql.Timestamp;
import java.util.Date;
import javafx.scene.control.Alert;

public class AutoTimer {

    static Date currentTime;
    static Date timeoutMax;
    protected static Timestamp userTime;
    static long userTimeoutDuration = UserInfoSingleton.getTimeoutDuration();
    private final MenuContainer CONTAINER;
    
    public AutoTimer(MenuContainer c){
        CONTAINER = c;
    }
    
    public void update(){
        System.out.println("called");
        if (timeoutMax == null) {
            timeoutMax = new Date(System.currentTimeMillis() +
                    userTimeoutDuration * 60 * 1000);
        } else {
            currentTime = new Date(System.currentTimeMillis());
            if (currentTime.after(timeoutMax)) {
                UserInfoSingleton.logout();
                CONTAINER.timeout();
                currentTime = null;
                timeoutMax = null;

            } else {
                currentTime = new Date(System.currentTimeMillis());
                timeoutMax = new Date(System.currentTimeMillis()
                        + userTimeoutDuration * 60 * 1000);
            }
        }
    }
}
