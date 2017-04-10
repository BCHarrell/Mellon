
package mellon;


import java.util.ArrayList;
import java.util.Comparator;
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
        
        accounts = UserInfoSingleton.getInstance().getProfiles();

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setPrefSize(425, 500);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox innerBox = new VBox();
        innerBox.setSpacing(15);
        
        //Sorts by name
        accounts.sort(Comparator.comparing(WebAccount::getAccountName));
        //Adds profiles to ProfilePane
        accounts.stream().forEach(a -> innerBox.getChildren()
                                    .add(new ProfilePane(CONTAINER, a)));
        
        scroll.setContent(innerBox);
        this.getChildren().setAll(scroll);
    }
}
