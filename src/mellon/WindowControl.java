
package mellon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Brent H.
 */
public class WindowControl extends HBox{
    
    private final MenuContainer CONTAINER;
    private boolean loggedIn;
    private double xOffset = 0, yOffset = 0;
    
    public WindowControl(MenuContainer c){
        CONTAINER = c;
        loggedIn = true;
        addItems();
    }
    
    public WindowControl(){
        loggedIn = false;
        CONTAINER = null;
        addItems();
    }
    
    private void addItems(){
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setSpacing(3);
        this.setMaxHeight(10);
        this.setPadding(new Insets(0,0,5,0));
        this.setStyle("-fx-background-color: #0088aa;");
        
        Button close = new Button("X");
        close.setStyle("-fx-text-fill: #D4AA00; -fx-font-weight: bold;");
        close.setBackground(Background.EMPTY);
        Button minimize = new Button("\u2014");
        minimize.setStyle("-fx-text-fill: #D4AA00; -fx-font-weight: bold;");
        minimize.setBackground(Background.EMPTY);
        this.getChildren().addAll(minimize, close);
        
        //Close window
        close.setOnAction(e -> {
            if (loggedIn){
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to log out?",
                        ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Confirm Logout");
                confirm.setHeaderText("");

                confirm.showAndWait()
                        .filter(response -> response == ButtonType.YES)
                        .ifPresent(response -> {
                            UserInfoSingleton.getInstance().logout();
                            Stage stage = (Stage) close.getScene().getWindow();
                            stage.close();
                });
            } else {
                Stage stage = (Stage) close.getScene().getWindow();
                stage.close();
            }
        });
        
        //Minimize window
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        
        //Window drag and drop
        this.setOnMousePressed(e ->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        
        this.setOnMouseDragged(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }
}
