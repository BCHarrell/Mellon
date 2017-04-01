
package mellon;

import javafx.scene.layout.BorderPane;

/**
 *
 * @author Brent H.
 */
public class MenuContainer extends BorderPane{
    
    private final MellonFramework FRAMEWORK;
    private final MainMenu MAIN;
    private final MasterAccount USER;
    
    public MenuContainer(MellonFramework fw){
        FRAMEWORK = fw;
        MAIN = new MainMenu(this);
        USER = UserInfoSingleton.getInstance().getMasterAccount();
        this.setTop(new NavBar(FRAMEWORK, this, MAIN));
        this.setCenter(MAIN);
    }
    
    public MainMenu getMain(){
        return MAIN;
    }
    
    public MasterAccount getUser(){
        return USER;
    }
}
