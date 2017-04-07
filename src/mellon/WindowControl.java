
package mellon;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This class creates a custom window control pane so that the stage can be
 * undecorated.  Allows for dragging, minimizing, and closing of the window.
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
        setOnMouseMoved(AutoTimer.MOUSE_MOVED);

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
                final ConfirmDialog confirm = new ConfirmDialog(CONTAINER,
                "Are you sure you want to log out?");
                CONTAINER.showDialog(confirm);
               
                Task <Void> task = new Task(){
                   @Override
                   protected Object call() throws Exception {
                       synchronized(confirm){
                           while(!confirm.isClosed()){
                               try{
                                   confirm.wait();
                               } catch (InterruptedException ex){}
                           }
                       }
                       return null;
                   }  
               };
               task.setOnSucceeded(a ->{
                   if(confirm.isConfirmed()){
                       CONTAINER.logout();
                       UserInfoSingleton.getInstance().logout();
                   }
               });
               new Thread(task).start();
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
