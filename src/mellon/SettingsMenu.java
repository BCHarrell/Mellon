package mellon;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;


/**
 * Settings menu appears as an overlay on any menu, called from the nav
 * bar.  Contains settings to adjust the timeout duration before forced
 * logout, default password length, and allows the user to change the 
 * master password on the account.
 * 
 * @author Brent H.
 *
 */
public class SettingsMenu extends BorderPane {

    private final MenuContainer CONTAINER;
    private VBox contentBox = new VBox();
    private int existingTimeout 
            = UserInfoSingleton.getInstance().getTimeoutDuration();
    private int existingPasswordLength 
            = UserInfoSingleton.getInstance().getDefaultPasswordLength();
    
    //Declared here so they are accessible in other methods
    private PasswordField currentPass, newPass, verify;
    Button savePass;
    private boolean meetsRequirements = false;
    
    //Text objects for notification window
    Text symbText, numText, lowerText, upperText, lengthText;
    
    //Regex patterns to verify password
    private final Pattern SYMBOL = Pattern.compile("[^a-zA-Z\\d\\s]");
    private final Pattern NUMBER = Pattern.compile("\\d");
    private final Pattern LOWER = Pattern.compile("[a-z]");
    private final Pattern UPPER = Pattern.compile("[A-Z]");
    
    public SettingsMenu(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }

    /**
     * Creates the UI elements
     */
    private void addItems() {
        this.setMaxSize(500, 260);
        this.getStyleClass().add("grey-container");
        this.setPadding(new Insets(0,0,10,0));
        
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(25);

        //Puts password reset on its own side so it does not affect the height
        BorderPane splitter = new BorderPane();
        splitter.setPadding(new Insets(10, 15, 0, 15));
        
        //Left side settings
        VBox settingsVB = new VBox();
        settingsVB.setSpacing(20);
        
        //Timeout
        HBox timeoutHB = new HBox();
        timeoutHB.setSpacing(10);
        Text timeoutLabel = new Text("Timeout duration (min):");
        timeoutLabel.getStyleClass().add("white-label");
        TextField timeoutTF = new TextField();
        timeoutTF.setText(String.valueOf(existingTimeout));
        timeoutTF.setPromptText("ex. 10");
        timeoutTF.setMaxWidth(50);
        timeoutHB.getChildren().addAll(timeoutLabel, timeoutTF);

        //Default Password Length
        HBox lengthHB = new HBox();
        lengthHB.setSpacing(10);
        Text lengthLabel = new Text("Password Length");
        lengthLabel.getStyleClass().add("white-label");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "8", "16", "24", "32", "48", new Separator(), "Custom"));
        cb.setMaxWidth(45);
        cb.setValue(String.valueOf(existingPasswordLength));
        
        //Textfield and return button if the user selects "Custom" from the
        //choice box
        HBox custLength = new HBox();
        custLength.setSpacing(5);
        TextField length = new TextField();
        length.setMaxWidth(45);
        Button goBack = new Button("Back");
        custLength.getChildren().addAll(length, goBack);
        lengthHB.getChildren().addAll(lengthLabel, cb);
        
        //Allows the user to print all stored passwords
        Button report = new Button("Print Passwords");
        report.getStyleClass().add("white-button-small");

        settingsVB.getChildren().addAll(timeoutHB, lengthHB, report);

        //Change Password
        //VBox to store height
        VBox passwordVB = new VBox();
        passwordVB.setMinHeight(150);
        
        //Password change titled pane, auto closes after completion
        TitledPane password = new TitledPane();
        password.setText("Change Master Password");
        password.setExpanded(false);
        password.setPrefWidth(225);
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(3);
        box.setPadding(new Insets(5, 5, 5, 5));
        currentPass = new PasswordField();
        currentPass.setPromptText("Enter current password");
        currentPass.setMaxWidth(225);
        newPass = new PasswordField();
        newPass.setPromptText("Enter new password");
        newPass.setMaxWidth(225);
        verify = new PasswordField();
        verify.setPromptText("Enter new password again");
        verify.setMaxWidth(225);
        savePass = new Button("Save Password");
        savePass.getStyleClass().add("blue-button-small");
        savePass.setDisable(true);
        box.getChildren().addAll(currentPass, newPass, verify, savePass);
        password.setContent(box);
        
        passwordVB.getChildren().add(password);
        splitter.setLeft(settingsVB);
        splitter.setRight(passwordVB);

        //Save changes
        Button save = new Button("Save Changes");
        save.getStyleClass().add("blue-button-large");

        contentBox.getChildren().addAll(splitter, save);
        this.setCenter(contentBox);
        
        //Pane control
        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        Button close = new Button("X");
        close.getStyleClass().add("menu-control");
        closeBox.getChildren().add(close);
        this.setTop(closeBox);
        
        //Password requirements pane
        AnchorPane requirements = getRequirementPane();

        /*****************
         *EVENT LISTENERS*
         *****************/
        
        //Section to print the stored passwords.
        report.setOnAction(e -> {
            boolean success = Print.executePrint(UserInfoSingleton
                    .getInstance().getProfiles());
            if(success){
            showNotification("File Sent to Printer");
            } else {
                CONTAINER.showDialog(new NotificationDialog(CONTAINER, 
                        "Something went wrong. Please check your printer "
                                + "connection and try again.  If the problem "
                                + "persists, please report a bug."));
            }
        });
        
        //Closes on save MOVE TO OWN METHOD
        save.setOnAction(e -> {
            String lengthText = "";
            if(lengthHB.getChildren().contains(cb)){
                lengthText = String.valueOf(cb.getValue());
            } else {
                lengthText = length.getText();
            }
            saveSettings(timeoutTF.getText(), lengthText);
        });
        
        //Closes on X
        close.setOnAction(e -> CONTAINER.closeSettings());

        //Choicebox for length selection
        cb.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val,
                        Number new_val) -> {
                    if (new_val.intValue() == 6) {
                        lengthHB.getChildren().remove(cb);
                        lengthHB.getChildren().addAll(custLength);
                        length.requestFocus();
                    }
        });
        
        //Go back to choice box from custom selection
        goBack.setOnAction(e -> {
            lengthHB.getChildren().remove(custLength);
            lengthHB.getChildren().add(cb);
            cb.getSelectionModel().select(1);
        });
        
        //Ensures text in the custom length box is a number
        TextFormatter<Integer> format = new TextFormatter<>(
            new IntegerStringConverter(), 
            null,  
            c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        length.setTextFormatter(format);
        
        //Ensures text in timeout box is a number
        TextFormatter<Integer> format2 = new TextFormatter<>(
            new IntegerStringConverter(), 
            null,  
            c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        timeoutTF.setTextFormatter(format2);
        
        
                        //NEW PASSWORD SECTION//
        //Saves the new master password
        savePass.setOnAction(e -> {
            savePassword(currentPass.getText(), newPass.getText(),
                            verify.getText());
            currentPass.clear();
            newPass.clear();
            verify.clear();
            password.setExpanded(false);
        });
        
        //Highlights the verification text field if entry does not match
        verify.setOnKeyReleased(e -> {
            verify();
        });
        
        newPass.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean>
                    observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    if(!meetsRequirements) {
                        if(!CONTAINER.getChildren().contains(requirements)){
                            CONTAINER.getChildren().add(requirements);
                        }
                        newPass.setStyle("-fx-background-color: "
                                        + "rgba(212, 170, 0, .5)");
                    }
                }
            }
        });
        
        newPass.setOnKeyReleased(e -> {
            
            //Check if all the requirements are met
            meetsRequirements = updateRequirements();
            
            //If the verify field is not empty and the password field changes
            //Remove verification status
            if (!verify.getText().isEmpty()) {
                verify();
            }
            
            //If yes, remove the requirements window and change the color
            if(meetsRequirements){
                newPass.setStyle("-fx-background-color: "
                                    + "rgba(0, 136, 170, .5);");
                CONTAINER.getChildren().remove(requirements);
            } else {
                //redisplay requirements if the user changes the password
                //after meeting the requirements
                if(!CONTAINER.getChildren().contains(requirements)){
                    CONTAINER.getChildren().add(requirements);
                }
                savePass.setDisable(true);
                newPass.setStyle("-fx-background-color: rgba(212, 170, 0, .5);");
            }
        });
    }//end addItems 
    
    /**
     * Verifies the password entries match
     */
    private void verify(){
        if(!verify.getText().equals(newPass.getText())){
            verify.setStyle("-fx-background-color: rgba(212, 170, 0, .5);");
            savePass.setDisable(true);
        } else {
            verify.setStyle("-fx-background-color: rgba(0, 136, 170, .5);");
            if(meetsRequirements && !currentPass.getText().isEmpty()){
                savePass.setDisable(false);
            }
        }
    }
    
    /**
     * Dynamically updates the requirements pane as the user inputs the
     * password.  Once all requirements are met, returns true. Otherwise, returns
     * false
     * @return true if requirements are met, false if not 
     */
    private boolean updateRequirements(){
        boolean symbol = false, number = false, lower = false,
                    upper = false, length = false;
        
        //Check for symbol
        if(SYMBOL.matcher(newPass.getText()).find()) {
            symbol = true;
            symbText.getStyleClass().add("met-requirement");
        } else {
            symbText.getStyleClass().removeAll("met-requirement");
            symbText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for number
        if(NUMBER.matcher(newPass.getText()).find()) {
            number = true;
            numText.getStyleClass().add("met-requirement");
        } else {
            numText.getStyleClass().removeAll("met-requirement");
            numText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for lower case
        if(LOWER.matcher(newPass.getText()).find()) {
            lower = true;
            lowerText.getStyleClass().add("met-requirement");
        } else {
            lowerText.getStyleClass().removeAll("met-requirement");
            lowerText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for upper case
        if(UPPER.matcher(newPass.getText()).find()) {
            upper = true;
            upperText.getStyleClass().add("met-requirement");
        } else {
            upperText.getStyleClass().removeAll("met-requirement");
            upperText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for 12 characters
        if(newPass.getText().length() >= 12) {
            length = true;
            lengthText.getStyleClass().add("met-requirement");
        } else {
            lengthText.getStyleClass().removeAll("met-requirement");
            lengthText.getStyleClass().add("unmet-requirement");
        }
        
        //If all reqs are met, return true
        if(symbol && number && lower && upper && length){
            return true;
        }
            
        return false;
    }
    
    /**
     * Creates a small window anchored to the bottom which informs the user
     * of the remaining password requirements
     * @return 
     */
    private AnchorPane getRequirementPane(){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox reqsBox = new HBox();
        reqsBox.setSpacing(10);
        reqsBox.setAlignment(Pos.CENTER);
        reqsBox.setPrefSize(140, 30);
        reqsBox.setPadding(new Insets(5, 15, 5, 15));
        reqsBox.getStyleClass().add("grey-container");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        symbText = new Text("Symbol");
        symbText.getStyleClass().add("unmet-requirement");
        numText = new Text("Number");
        numText.getStyleClass().add("unmet-requirement");
        lowerText = new Text("Lower case");
        lowerText.getStyleClass().add("unmet-requirement");
        upperText = new Text("Upper case");
        upperText.getStyleClass().add("unmet-requirement");
        
        grid.add(symbText, 0, 0);
        grid.add(numText, 1, 0);
        grid.add(lowerText, 0, 1);
        grid.add(upperText, 1, 1);
        
        lengthText = new Text("12 Characters");
        lengthText.getStyleClass().add("unmet-requirement");
        
        reqsBox.getChildren().addAll(grid, lengthText);
        
        anch.getChildren().add(reqsBox);
        AnchorPane.setBottomAnchor(reqsBox, 15.0);
        AnchorPane.setLeftAnchor(reqsBox, 131.0);
        
        return anch;
    }
    
    /**
     * Notifies the user on successful action (save, print)
     */
    private void showNotification(String message){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox notification = new HBox();
        notification.setAlignment(Pos.CENTER);
        notification.setPrefSize(140, 30);
        notification.setPadding(new Insets(5, 15, 5, 15));
        //notification.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        notification.getStyleClass().add("grey-container");
        
        Text notificationText = new Text(message);
        notificationText.getStyleClass().add("white-label");
        //notificationText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        //notificationText.setStyle("-fx-fill: #FFFFFF;");
        notification.getChildren().add(notificationText);
        
        anch.getChildren().add(notification);
        AnchorPane.setBottomAnchor(notification, 30.0);
        AnchorPane.setLeftAnchor(notification, 185.0);
        
        CONTAINER.getChildren().add(anch);
        
        FadeTransition ft = new FadeTransition(Duration.millis(2500), anch);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(500));
        ft.setOnFinished(e -> CONTAINER.getChildren().remove(anch));
        ft.play();
    }
    
    /**
     * Saves the new master password as specified by the user
     * @param currentMaster the current master password
     * @param newMaster the new master password to save
     * @param repeatNewMaster the new master password to verify correct entry
     */
    private void savePassword(String currentMaster, String newMaster, 
                                String repeatNewMaster){
        if (currentMaster.isEmpty() || newMaster.isEmpty()
                    || repeatNewMaster.isEmpty()) {
            CONTAINER.showDialog(new NotificationDialog(CONTAINER, 
                    "Please ensure the current password, new password, "
                    + "and repeated password are filled in."));
        } else {
            ArrayList<WebAccount> oldWebAccounts 
                    = UserInfoSingleton.getInstance().getProfiles();
            String newMasterPasswordHash 
                    = UserInfoSingleton.getInstance()
                        .hashString(newMaster);
            int userID = UserInfoSingleton.getInstance().getUserID();
            ArrayList<WebAccount> newWebAccounts = new ArrayList<>();
            // Updates the master account hash
            DBConnect.updateMasterPassword(userID, newMasterPasswordHash);
            // Updates all the associated web accounts
            oldWebAccounts.stream().forEach(account -> {
                newWebAccounts.add(account
                        .updatePassword(newMaster));
            });
            newWebAccounts.stream().forEach(account -> {
                DBConnect.updateWebAccount(userID,
                        account.getWebID(),
                        account.getEncodedAccountName(),
                        account.getEncodedUsername(),
                        account.getEncodedPassword());
            });
            // Update the singleton
            UserInfoSingleton.getInstance()
                    .setPassword(newMaster);
            UserInfoSingleton.getInstance().addProfiles(newWebAccounts);
            showNotification("Password Successfully Changed");
        }
    }
    
    /**
     * Saves the settings if there were changes
     * @param timeoutText the string value of the timeout textfield
     * @param lengthText  the string value of either the choice box or
     *                    custom password length textfield
     */
    private void saveSettings(String timeoutText, String lengthText){
        // Initialize values with defaults as a fall-back
            int timeout = 10;
            int passwordLength = existingPasswordLength;
            try {
                timeout = Integer.parseInt(timeoutText);
            } catch (NumberFormatException e1) {
                //only numbers accepted, no catch needed
            }
            
            try {
                passwordLength = Integer.parseInt(lengthText);
            } catch (NumberFormatException e1) {
                // only numbers accepted, no catch needed
            }
            // Detects if changes are necessary to the database
            if (existingTimeout != timeout || 
                    existingPasswordLength != passwordLength) {
                DBConnect.updatePrefrenceSettings(UserInfoSingleton
                        .getInstance().getUserID(), timeout, passwordLength);
            }
            showNotification("Settings Saved");
            CONTAINER.closeSettings();
    }
}
