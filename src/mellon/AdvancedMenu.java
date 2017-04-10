
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
public class AdvancedMenu extends BorderPane {
    
    private CreationPage parent;
    private final String[] OPTIONS = {"!", "@", "#", "$", "%", "^", "&", "*", 
            "(", ")", "-", "_", "=", "+", ",", ".", "<", ">", "?", "/"}; //20
    private CheckBox[] boxes = new CheckBox[OPTIONS.length];
    private VBox contentBox = new VBox();
    
    
    public AdvancedMenu(CreationPage parent) {
        this.parent = parent;
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        this.setMaxSize(350, 200);
        this.getStyleClass().add("grey-container");
        
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(15);
        contentBox.setPadding(new Insets(10, 25, 25, 25));
        
        //Checkboxes for symbol options
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        int rowCount = 0, colCount = 0;
        
        //Add the checkboxes
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
        
        //Save button
        Button save = new Button("Save Selections");
        save.getStyleClass().add("white-button-small");
        
        contentBox.getChildren().addAll(grid, save);
        this.setCenter(contentBox);
        
        //Menu control - allows for closing the menu without saving
        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        Button close = new Button("X");
        close.getStyleClass().add("menu-control");
        closeBox.getChildren().add(close);
        this.setTop(closeBox);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        //Saves and closes
        save.setOnAction(e -> {
            parent.setAllowable(getAllowable());
            parent.popAdvanced();
        });
        
        //Closes on x
        close.setOnAction(e -> {
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
