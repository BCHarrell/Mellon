
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
    MellonFramework parent;
    
    public MainMenu(MellonFramework p) {
        parent = p;
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
        
        //Temporary solution, will probably use a StackPane and won't need
        //these on every menu.  Will also use icons
        Button settings = new Button("Settings");
        Button help = new Button("Help");
        
        HBox settingsBar = new HBox();
        settingsBar.setAlignment(Pos.BOTTOM_RIGHT);
        settingsBar.setSpacing(15);
        settingsBar.getChildren().addAll(settings, help);

        this.setBottom(settingsBar);
    }
}
