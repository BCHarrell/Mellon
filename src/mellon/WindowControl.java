
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
    
    /**
     * Used when logged in
     * @param c the menu container to house the window controls
     */
    public WindowControl(MenuContainer c){
        CONTAINER = c;
        loggedIn = true;
        addItems();
    }
    
    /**
     * Used when logged out
     */
    public WindowControl(){
        loggedIn = false;
        CONTAINER = null;
        addItems();
    }
    
    /**
     * Adds the window control elements to the box
     */
    private void addItems(){
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setSpacing(3);
        this.setMaxHeight(10);
        this.setPadding(new Insets(0,0,5,0));
        this.getStyleClass().add("blue-container");
        
        //Close and minimize buttons
        Button close = new Button("X");
        close.getStyleClass().add("window-control");
        close.setBackground(Background.EMPTY);
        Button minimize = new Button("\u2014");
        minimize.getStyleClass().add("window-control");
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
                        UserInfoSingleton.getInstance().logout();
                        Stage stage = (Stage) close.getScene().getWindow();
                        stage.close();
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
        
        //Allows for dragging the window in absence of the normal frame
        this.setOnMouseDragged(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }
}
