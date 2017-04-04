
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
        ScrollPane scroll = new ScrollPane();
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setFitToWidth(true);
        scroll.setPrefSize(425, 500);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //ADD TO CSS TO SPEED UP, .scroll-pane > .viewport{}
        
        VBox innerBox = new VBox();
        innerBox.setSpacing(15);
        
        //Sorts by name
        accounts.sort((o1, o2) -> 
                o1.getAccountName().compareTo(o2.getAccountName()));
        
        for (WebAccount a : accounts) {
            innerBox.getChildren().add(new ProfilePane(CONTAINER, a));
        }
        
        scroll.setContent(innerBox);
        this.getChildren().setAll(scroll);
        
        //DEBUG
        for (WebAccount x : accounts){
            System.out.println(x.getAccountName() + " // " + x.getUsername() +
                    " // " + x.getPassword());
        }
        System.out.println("*****");
    }
}
