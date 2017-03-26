
package mellon;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 *
 * @author Brent H.
 */
public class CreationPage extends VBox {
    
    private MellonFramework framework;
    private MainMenu main;
    private ArrayList<Character> allowedSymbols;
    private AdvancedMenu adv;

    public CreationPage(MellonFramework framework, MainMenu main) {
        this.framework = framework;
        this.main = main;
        addItems();
    }
    
    private void addItems() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(75);
        adv = new AdvancedMenu(framework, this);
        
        /*Main container
        VBox primaryVB = new VBox();
        primaryVB.setSpacing(15);
        primaryVB.setAlignment(Pos.CENTER);*/
        
        HBox topHB = new HBox();
        topHB.setAlignment(Pos.CENTER);
        topHB.setSpacing(200);
        
        VBox namingVB = new VBox();
        namingVB.setSpacing(10);
        
        //Nickname
        VBox nickVB = new VBox();
        Label nickLabel = new Label("Nickname");
        TextField nickname = new TextField();
        nickname.setMaxWidth(350);
        nickname.setPromptText("Enter account nickname");
        Tooltip nickTip = new Tooltip("This is the name you will use to get "
                + "the password in the future. For example: \"Gmail\"");
        nickname.setTooltip(nickTip);
        nickVB.getChildren().addAll(nickLabel, nickname);
        
        //Username
        VBox userVB = new VBox();
        Label userLabel = new Label("Username");
        TextField username = new TextField();
        username.setMaxWidth(350);
        username.setPromptText("Enter username");
        Tooltip userTip = new Tooltip("This is the username for your account. "
                + "For example, your email address or \"MellonUser\"");
        username.setTooltip(userTip);
        userVB.getChildren().addAll(userLabel, username);
        
        //Length selection
        VBox lengthVB = new VBox();
        Label lengthLabel = new Label("Password Length");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
        "8", "16", "24", "32", "48", "Custom"));
        cb.setMaxWidth(350);
        cb.setValue("16");
        
        HBox custLength = new HBox();
        custLength.setSpacing(5);
        TextField length = new TextField();
        length.setMaxWidth(40);
        Button goBack = new Button("Back to Selection");
        custLength.getChildren().addAll(length, goBack);
        lengthVB.getChildren().addAll(lengthLabel, cb);
        
        namingVB.getChildren().addAll(nickVB, userVB, lengthVB);
        
        //CUSTOMIZATION
        VBox custVB = new VBox();
        custVB.setAlignment(Pos.CENTER_RIGHT);
        custVB.setSpacing(5);
        
        //Quick customize
        Label customize = new Label("Quick Customize");
        
        VBox optionVB = new VBox();
        optionVB.setAlignment(Pos.CENTER_LEFT);
        optionVB.setSpacing(3);
        CheckBox upper = new CheckBox("Uppercase");
        upper.setSelected(true);
        CheckBox lower = new CheckBox("Lowercase");
        lower.setSelected(true);
        CheckBox symb = new CheckBox("Symbols");
        symb.setSelected(true);
        CheckBox numbers = new CheckBox("Numbers");
        numbers.setSelected(true);
        optionVB.getChildren().addAll(upper, lower, symb, numbers);
        
        //Advanced menu
        Button advanced = new Button("Advanced");
        
        //ADD
        custVB.getChildren().addAll(customize, optionVB, advanced);
        topHB.getChildren().addAll(namingVB, custVB);
        
        //Generate
        VBox generateVB = new VBox();
        generateVB.setAlignment(Pos.CENTER);
        generateVB.setSpacing(15);
        Button generate = new Button("Generate Password");
        TextField output = new TextField();
        output.setMaxWidth(500);
        Button toMain = new Button("Back to Main (will be anchored bottom)");
        Button save = new Button("Save Account");
        generateVB.getChildren().addAll(generate, output, toMain, save);
        
        this.getChildren().addAll(topHB, generateVB);
        
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        cb.getSelectionModel().selectedIndexProperty().addListener(
            (ObservableValue<? extends Number> ov, Number old_val,
            Number new_val) -> {
                if (new_val.intValue() == 5){
                    lengthVB.getChildren().remove(cb);
                    lengthVB.getChildren().addAll(custLength);
                    length.requestFocus();
                }
        });
        
        goBack.setOnAction(e -> {
           lengthVB.getChildren().remove(custLength);
           lengthVB.getChildren().add(cb);
        });
        
        toMain.setOnAction(e -> {
            framework.getScene().setRoot(main);
        });
        
        advanced.setOnAction(e -> {
            framework.getScene().setRoot(adv);
        });
    } //End addItems()
    
    public void setAllowable(ArrayList<Character> list) {
        allowedSymbols = list;
    }
}
