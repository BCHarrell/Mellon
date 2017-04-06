
package mellon;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
        this.setBorder(new Border(new BorderStroke(Color.valueOf("#0088aa"),
                BorderStrokeStyle.SOLID, null, null)));
        content.setTop(new WindowControl());
        content.setCenter(new LoginPage(FRAMEWORK, this));
        addDropShadow();
        this.getChildren().add(content);
    }
    
    /**
     * Adds the drop shadow to this object
     */
    private void addDropShadow(){
        DropShadow ds = new DropShadow();
        ds.setOffsetX(3);
        ds.setOffsetY(3);
        ds.setColor(Color.BLACK);
        this.setEffect(ds);
    }
    
    /**
     * @return returns the content borderpane
     */
    public BorderPane getContent(){
        return content;
    }
    
    /**
     * Changes the menu
     * @param newMenu the menu to fade in
     */
    public void requestMenuChange(Node newMenu){
        fadeOut(newMenu);
    }
    
    /**
     * Fades the current menu out
     * @param newMenu The new menu to set after the fade completes
     */
    private void fadeOut(Node newMenu){
        FadeTransition ft = new FadeTransition(Duration.millis(250),
                                content.getCenter());
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            newMenu.setOpacity(0);
            content.setCenter(newMenu);
            fadeIn();
        });
        ft.play();
    }
    
    /**
     * Fades the new menu in
     */
    private void fadeIn(){
        FadeTransition ft = new FadeTransition(Duration.millis(250),
                                    content.getCenter());
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }
}
