package mellon;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Text;

/**
 * Created by Thomas Hodges on 3/26/2017.
 * HelpPage.java
 */
public class HelpPage extends Accordion {
    
    //Temporary, will be its own window and accessible
    //from any other menu
    private final MenuContainer CONTAINER;
    
    public HelpPage(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        this.setPadding(new Insets(10, 10, 10, 10));
        
        //Getting started
        TitledPane start = new TitledPane();
        start.setText("Getting Started");
        start.setContent(new Text("Getting started info here"));
        
        //Adding an account
        TitledPane creation = new TitledPane();
        creation.setText("Creating a Profile");
        creation.setContent(new Text("Creation instructions here"));
        
        //Retrieving a password
        TitledPane retrieve = new TitledPane();
        retrieve.setText("Retrieving a Password");
        retrieve.setContent(new Text("Retrieval instructions here"));
        
        //Editing
        TitledPane edit = new TitledPane();
        edit.setText("Editing an Existing Profile");
        edit.setContent(new Text("Editing instructions here"));
        
        //Settings
        TitledPane settings = new TitledPane();
        settings.setText("Customizing Your Settings");
        settings.setContent(new Text("Settings explanation here"));
        
        //General tips
        TitledPane general = new TitledPane();
        general.setText("General Tips");
        general.setContent(new Text("General tips here"));
        
        //Bug report
        TitledPane bugs = new TitledPane();
        bugs.setText("Have an Issue?");
        bugs.setContent(new Text("Bug reporting info here"));
        
        //TEMPORARY - BACK TO MENU
        Button back = new Button("Back to menu");
        back.setOnAction(e -> CONTAINER.setCenter(CONTAINER.getMain()));
        TitledPane backPane = new TitledPane();
        backPane.setText("Back to Main");
        backPane.setContent(back);
        
        this.getPanes().addAll(start, creation, retrieve, edit, settings,
                        general, bugs, backPane);
    }
    
}
