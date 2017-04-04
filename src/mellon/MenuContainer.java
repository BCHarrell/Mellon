
package mellon;

import javafx.scene.layout.*;

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
}
