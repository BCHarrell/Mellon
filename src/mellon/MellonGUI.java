package mellon;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bobcat
 */
public class MellonGUI extends Application {
    
    private Stage stage;
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
        
        scene = new Scene(new LoginPage(this), 525, 650);
        
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
