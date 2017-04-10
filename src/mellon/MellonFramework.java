package mellon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * The primary stage for the application.  Houses the main scene and creates
 * the login page.
 * 
 * @author Brent H.
 */
public class MellonFramework extends Application {
    
    private Stage stage;
    private Scene scene;
    private final Image ICON = new Image(getClass()
            .getResourceAsStream("/resources/tray_icon.png"));
    
    @Override
    public void start(Stage primaryStage) {
        
        scene = new Scene(new ExternalContainer(this), 525, 650);
        scene.getStylesheets().add(MellonFramework.class
                .getResource("mellon_stylesheet.css").toExternalForm());
        
        stage = primaryStage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(ICON);
        stage.initStyle(StageStyle.UNDECORATED);
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
