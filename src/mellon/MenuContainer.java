
package mellon;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * This class is the main UI class for managing the displayed menus.  It
 * consists of a BorderPane with the navigation bar pinned to the top.  Changes
 * of menu are accomplished by setting the center content.
 * 
 * @author Brent H.
 */
public class MenuContainer extends StackPane{
    
    private final MellonFramework FRAMEWORK;
    private final MainMenu MAIN;
    private final MasterAccount USER;
    private final BorderPane content = new BorderPane();
    private SettingsMenu settings = new SettingsMenu(this);
    private boolean settingsDisplayed = false;
    
    public MenuContainer(MellonFramework fw){
        FRAMEWORK = fw;
        MAIN = new MainMenu(this);
        USER = UserInfoSingleton.getInstance().getMasterAccount();
        content.setTop(new NavBar(FRAMEWORK, this, MAIN));
        content.setCenter(MAIN);
        this.getChildren().add(content);
    }
    
    /**
     * @return the main menu for menus that may need access to the main
     */
    public MainMenu getMain(){
        return MAIN;
    }
    
    /**
     * @return the logged in user
     */
    public MasterAccount getUser(){
        return USER;
    }
    
    /**
     * Returns to the login page
     */
    public void logout(){
        FRAMEWORK.getScene().setRoot(new LoginPage(FRAMEWORK));
    }
    
    /**
     * @return Gets the content pane in the container
     */
    public BorderPane getContent(){
        return content;
    }
    
    /**
     * Displays the settings menu as an overlay
     */
    public void displaySettings(){
        this.getChildren().add(settings);
        blur();
        FadeTransition ft = new FadeTransition(Duration.millis(250), settings);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
        settingsDisplayed = true;
    }
    
    public void closeSettings(){
        if (this.getChildren().contains(settings)){
            FadeTransition ft = 
                    new FadeTransition(Duration.millis(250), settings);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                this.getChildren().remove(settings);
                unBlur();
                settingsDisplayed = false;
            });
            ft.play();
        }
    }
    
    /**
     * Handles all menu changes to ensure the settings menu gets closed if it
     * is open
     * @param newMenu the new menu to change to
     */
    public void requestMenuChange(Node newMenu){
        if (settingsDisplayed) {
            closeSettings(); 
        }
        content.setCenter(newMenu);
    }
    
    /**
     * Blurs the content.  Used for overlay of settings menu
     */
    private void blur(){
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5);
        blur.setHeight(5);
        blur.setIterations(3);
        content.setEffect(blur);
    }
    
    /**
     * Removes the blur from the content when settings menu is closed
     */
    private void unBlur(){
        content.setEffect(null);
    }
}
