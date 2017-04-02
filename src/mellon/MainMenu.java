
package mellon;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * The main menu contains all of the existing profiles for the logged-in
 * account.  Users can select an existing profile to view the stored username
 * and password combinations.
 * 
 * @author Brent H.
 */
public class MainMenu extends BorderPane {
   private final MenuContainer CONTAINER;
    
    public MainMenu(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        Button retrieve = new Button("Retrieve a Password");
        
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(15);
        hb.getChildren().add(retrieve);
        
        this.setCenter(hb);
        
        /************************
         *EVENT LISTENER SECTION*
         ************************/
        retrieve.setOnAction (e -> {
            CONTAINER.setCenter(new TempRetriever(CONTAINER));
        });
    }
}
