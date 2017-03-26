
package mellon;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author Brent H.
 */
public class AdvancedMenu extends VBox {
    
    private MellonFramework framework;
    private CreationPage parent;
    private final String[] OPTIONS = {"!", "@", "#", "$", "%", "^", "&", "*", 
            "(", ")", "-", "_", "=", "+", ",", ".", "<", ">", "?", "/"}; //20
    private CheckBox[] boxes = new CheckBox[OPTIONS.length];
    private ArrayList<Character> available = new ArrayList<>();
    
    public AdvancedMenu(MellonFramework fw, CreationPage parent) {
        framework = fw;
        this.parent = parent;
        addItems();
    }
    
    private void addItems() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        
        //Options
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        int rowCount = 0, colCount = 0;
        
        for (int i = 0; i < 20; i++){
            CheckBox cb = boxes[i] = new CheckBox(OPTIONS[i]);
            cb.setSelected(true);
            grid.add(cb, colCount, rowCount);
            colCount++;
            
            if (i != 0 && i % 3 == 0){
                rowCount++;
                colCount = 0;
            }
        }
        
        Button save = new Button("Save Selections");
        
        this.getChildren().addAll(grid, save);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
    }
}
