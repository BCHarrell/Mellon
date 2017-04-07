
package mellon;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
//    private final MasterAccount USER;
    private final BorderPane content = new BorderPane();
    private SettingsMenu settings = new SettingsMenu(this);
    private boolean settingsDisplayed = false;
    
    public MenuContainer(MellonFramework fw){
        FRAMEWORK = fw;
        MAIN = new MainMenu(this);
//        USER = UserInfoSingleton.getInstance().getMasterAccount();
        content.setTop(new NavBar(this));
        content.setCenter(MAIN);
        MAIN.setOpacity(0);
        content.setCenter(MAIN);
        this.setBorder(new Border(new BorderStroke(Color.valueOf("#0088aa"),
                BorderStrokeStyle.SOLID, null, null)));
        loginFade();
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
//    public MasterAccount getUser(){
//        return USER;
//    }
    
    /**
     * Returns to the login page
     */
    public void logout(){
        FRAMEWORK.getScene().setRoot(new ExternalContainer(FRAMEWORK));
    }
    
    /**
     * @return Gets the content pane in the container
     */
    public BorderPane getContent(){
        return content;
    }
    
    /**
     * Handles the initial fade in for the login
     */
    private void loginFade(){
        FadeTransition ft = new FadeTransition(Duration.millis(500), MAIN);
        ft.setDelay(Duration.millis(750));
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }
    
    /**
     * Shows the specified dialog
     * @param d the dialog to display
     */
    public void showDialog(Node d){
       this.getChildren().add(d);
       blur();
        FadeTransition ft = new FadeTransition(Duration.millis(250), d);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }
    
    /**
     * Closes the specified dialog menu
     * @param d the dialog to close
     */
    public void closeDialog(Node d){
        FadeTransition ft = new FadeTransition(Duration.millis(250), d);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            this.getChildren().remove(d);
            if (!settingsDisplayed){
                unBlur();
            }
        });
        ft.play();
    }
    
    /**
     * Displays the settings menu as an overlay
     */
    public void displaySettings(){
        if(!settingsDisplayed) {
            this.getChildren().add(settings);
            blur();
            FadeTransition ft = new FadeTransition(Duration.millis(250), 
                                                        settings);
            ft.setFromValue(.25);
            ft.setToValue(1.0);
            ft.play();
            settingsDisplayed = true;
        }
    }
    
    /**
     * Closes the settings menu
     */
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
     * is open.  Fades the menus out and in.
     * @param newMenu the new menu to change to
     */
    public void requestMenuChange(Node newMenu){
        if (settingsDisplayed) {
            closeSettings(); 
        }
        //Only change menus if the menu is different than currently displayed
        if(newMenu.getClass() != content.getCenter().getClass()) {
            fadeOut(newMenu);
        }
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
