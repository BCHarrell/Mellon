
package mellon;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author Brent H.
 */
public class AdvancedMenu extends VBox {
    
    private CreationPage parent;
    private final String[] OPTIONS = {"!", "@", "#", "$", "%", "^", "&", "*", 
            "(", ")", "-", "_", "=", "+", ",", ".", "<", ">", "?", "/"}; //20
    private CheckBox[] boxes = new CheckBox[OPTIONS.length];
    
    
    public AdvancedMenu(CreationPage parent) {
        this.parent = parent;
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        this.setMaxSize(350, 200);
        this.setPadding(new Insets(25, 25, 25, 25));
        this.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        //Options
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        int rowCount = 0, colCount = 0;
        
        for (int i = 1; i < 21; i++){
            CheckBox cb = boxes[i-1] = new CheckBox(OPTIONS[i-1]);
            cb.setSelected(true);
            grid.add(cb, colCount, rowCount);
            colCount++;
            
            if (i != 0 && i % 5 == 0){
                rowCount++;
                colCount = 0;
            }
        }
        
        Button save = new Button("Save Selections");
        
        this.getChildren().addAll(grid, save);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        save.setOnAction(e -> {
            //CONTAINER.setCenter(parent);
            parent.setAllowable(getAllowable());
            parent.popAdvanced();
        });
    }//End addItems
    
    /**
     * Called if the Symbol checkbox in the main creation page is not checked.
     * Deselects all checkboxes in this menu
     */
    public void deselect(){
        for (CheckBox c : boxes){
            c.setSelected(false);
        }
    }
    
    /**
     * @return An ArrayList of all selected characters
     */
    private ArrayList<Character> getAllowable() {
        ArrayList<Character> allowable = new ArrayList<>();
        
        for (CheckBox c : boxes){
            if (c.isSelected()) {
                allowable.add(c.getText().charAt(0));
                ///*TESTING*/System.out.println(c.getText().charAt(0));
            }
        }
        
        return allowable;
    }
}
