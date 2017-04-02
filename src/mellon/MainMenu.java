
package mellon;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Brent H.
 */
public class MainMenu extends BorderPane {
   private final MenuContainer CONTAINER;
    
    public MainMenu(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }
    
    private void addItems() {
        Button create = new Button("Create a Password");
        Button retrieve = new Button("Retrieve a Password");
        
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(15);
        hb.getChildren().addAll(create, retrieve);
        
        this.setCenter(hb);
        
        /************************
         *EVENT LISTENER SECTION*
         ************************/
        create.setOnAction(e -> CONTAINER
                .setCenter(new CreationPage(CONTAINER)));
        retrieve.setOnAction (e -> CONTAINER
                .setCenter(new TempRetriever(CONTAINER)));
    }
}
