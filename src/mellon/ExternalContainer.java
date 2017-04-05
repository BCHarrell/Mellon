
package mellon;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * StackPane to contain the login and sign up page while maintaining the window
 * control bar.  This is to allow the window to be undecorated, giving greater
 * control of the UI.
 * 
 * @author Brent H.
 */
public class ExternalContainer extends StackPane {
    
    private BorderPane content = new BorderPane();
    private final MellonFramework FRAMEWORK;
    
    public ExternalContainer(MellonFramework fw){
        FRAMEWORK = fw;
        this.setBorder(new Border(new BorderStroke(Color.DARKGREY, 
                BorderStrokeStyle.SOLID, null, null)));
        content.setTop(new WindowControl());
        content.setCenter(new LoginPage(FRAMEWORK, this));
        addDropShadow();
        this.getChildren().add(content);
    }
    
    private void addDropShadow(){
        DropShadow ds = new DropShadow();
        ds.setOffsetX(3);
        ds.setOffsetY(3);
        ds.setColor(Color.BLACK);
        this.setEffect(ds);
    }
    
    public BorderPane getContent(){
        return content;
    }
    
}
