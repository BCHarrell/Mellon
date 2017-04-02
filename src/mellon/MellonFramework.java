package mellon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 *
 * @author Bobcat
 */
public class MellonFramework extends Application {
    
    private Stage stage;
    private Scene scene;
    private final Image ICON = new Image(getClass()
            .getResourceAsStream("/resources/tray_icon.png"));
    
    @Override
    public void start(Stage primaryStage) {
        
        scene = new Scene(new LoginPage(this), 525, 650);
        
        stage = primaryStage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(ICON);
        //For future use: will need to add a top pane for closing and dragging
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public Scene getScene(){
        return scene;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
