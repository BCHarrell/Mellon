package mellon;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

public class AutoTimer {

    static Date currentTime;
    static Date timeoutMax;
    protected static Timestamp userTime;
    static long userTimeoutDuration = 1;

    static EventHandler<MouseEvent> MOUSE_MOVED = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (timeoutMax == null) {
                timeoutMax = new Date(System.currentTimeMillis() + userTimeoutDuration * 60 * 1000);
            } else {
                currentTime = new Date(System.currentTimeMillis());
                if (currentTime.after(timeoutMax)) {
                    UserInfoSingleton.logout();
                    MenuContainer.logout();
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Session Timeout");
                    confirm.setTitle("Session Timeout");
                    confirm.setHeaderText("Your session ended, please login again.");
                    confirm.showAndWait();
                    currentTime = null;
                    timeoutMax = null;

                } else {
                    currentTime = new Date(System.currentTimeMillis());
                    timeoutMax = new Date(System.currentTimeMillis() + userTimeoutDuration * 60 * 1000);
                }
            }
        }

    };
}
