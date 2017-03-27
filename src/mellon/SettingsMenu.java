package mellon;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * 
 * 
 */
public class SettingsMenu extends VBox {
    
    //Temporary, will eventually open in new window
    MellonFramework framework;
    MainMenu main;
    
    public SettingsMenu(MellonFramework fw, MainMenu main){
        framework = fw;
        this.main = main;
        addItems();
    }
    
    private void addItems() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(45);
        
        VBox topVB = new VBox();
        topVB.setAlignment(Pos.CENTER_LEFT);
        topVB.setPadding(new Insets(0, 0, 0, 20));
        topVB.setSpacing(10);
        
        //Timeout
        HBox timeoutHB = new HBox();
        timeoutHB.setSpacing(10);
        Label timeoutLabel = new Label("Timeout duration (minutes):");
        TextField timeoutTF = new TextField();
        /////////THIS WILL NEED UPDATING TO PULL USER SETTINGS/////////
        timeoutTF.setText("10");
        timeoutTF.setPromptText("ex. 10");
        timeoutTF.setMaxWidth(45);
        timeoutHB.getChildren().addAll(timeoutLabel, timeoutTF);
        
        //Copy password instead of displaying
        Label copy = new Label("Copy password instead of displaying?   ");
        CheckBox copyCB = new CheckBox();
        copy.setGraphic(copyCB);
        copy.setContentDisplay(ContentDisplay.RIGHT);
        
        //Default Password Length
        HBox lengthHB = new HBox();
        lengthHB.setSpacing(10);
        Label lengthLabel = new Label("Default Password Length");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
        "8", "16", "24", "32", "48", "Custom"));
        cb.setMaxWidth(45);
        cb.setValue("16");
        
        HBox custLength = new HBox();
        custLength.setSpacing(5);
        TextField length = new TextField();
        length.setMaxWidth(45);
        Button goBack = new Button("Back to Selection");
        custLength.getChildren().addAll(length, goBack);
        lengthHB.getChildren().addAll(lengthLabel, cb);
        
        //Change Password
        TitledPane password = new TitledPane();
        password.setText("Change Master Password");
        password.setExpanded(false);
        password.setMaxWidth(250);
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(3);
        box.setPadding(new Insets(5, 5, 5, 5));
        TextField old = new TextField();
        old.setPromptText("Enter current password");
        old.setMaxWidth(225);
        TextField newPass = new TextField();
        newPass.setPromptText("Enter new password");
        newPass.setMaxWidth(225);
        TextField repeat = new TextField();
        repeat.setPromptText("Enter new password again");
        repeat.setMaxWidth(225);
        Button savePass = new Button("Save Password");
        box.getChildren().addAll(old, newPass, repeat, savePass);
        password.setContent(box);
        
        //View report
        Button report = new Button("View Passwords");
        
        topVB.getChildren().addAll(timeoutHB, copy, lengthHB,
                                    password, report);
        
        //Save changes
        Button save = new Button("Save Changes");
        
        this.getChildren().addAll(topVB, save);
        
        /****************
         *EVENT LISTENERS
         ****************/
        
        //TEMPORARY, WILL BE OWN WINDOW
        save.setOnAction(e -> framework.getScene().setRoot(main));
        
        //Choicebox for length selection
        cb.getSelectionModel().selectedIndexProperty().addListener(
            (ObservableValue<? extends Number> ov, Number old_val,
            Number new_val) -> {
                if (new_val.intValue() == 5){
                    lengthHB.getChildren().remove(cb);
                    lengthHB.getChildren().addAll(custLength);
                    length.requestFocus();
                }
        });
        
        //Go back to choice box from custom selection
        goBack.setOnAction(e -> {
           lengthHB.getChildren().remove(custLength);
           lengthHB.getChildren().add(cb);
        });
    } //end addItems 
}
