
package mellon;


import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Brent H.
 */
public class MainMenu extends BorderPane{
    MellonGUI parent;
    
    public MainMenu(MellonGUI p){
        parent = p;
        addItems();
    };
    
    private void addItems(){
        //BorderPane border = new BorderPane();
        Button btn = new Button("Change to login");
        this.setCenter(btn);
    }
}
