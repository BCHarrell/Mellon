
package mellon;


import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * The main menu contains all of the existing profiles for the logged-in
 * account.  Users can select an existing profile to view the stored username
 * and password combinations.
 * 
 * @author Brent H.
 */
public class MainMenu extends VBox {
    
    private final MenuContainer CONTAINER;
    private ArrayList<WebAccount> accounts; //not final so it can be updated
    public MainMenu(MenuContainer c) {
        CONTAINER = c;
        accounts = UserInfoSingleton.getInstance().getProfiles();
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 50, 0, 50));
        update();
    }
    
    /**
     * Creates the UI elements
     */
    public void update() {
        Accordion accordion = new Accordion();
        
        for (WebAccount a : accounts) {
            accordion.getPanes().add(new ProfilePane(CONTAINER, a));
        }
        
        ScrollPane scroll = new ScrollPane();
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setFitToWidth(true);
        scroll.setPrefSize(425, 500);
        scroll.setContent(accordion);
        this.getChildren().setAll(scroll);
        
        /************************
         *EVENT LISTENER SECTION*
         ************************/
        
    }
}
