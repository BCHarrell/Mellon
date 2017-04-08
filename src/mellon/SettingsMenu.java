package mellon;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import javafx.animation.FadeTransition;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;


/**
 * Settings menu appears as an overlay on any menu, called from the nav
 * bar.
 * 
 * @author Brent H.
 *
 */
public class SettingsMenu extends BorderPane {

    private final MenuContainer CONTAINER;
    private VBox contentBox = new VBox();
    private int existsingTimeout = UserInfoSingleton.getInstance().getTimeoutDuration();
    private String existingPasswordLength = String.valueOf(UserInfoSingleton.getInstance().getDefaultPasswordLength());
    private boolean existingCopyPassword = UserInfoSingleton.getInstance().isCopyPassword();
    
    public SettingsMenu(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }

    /**
     * Creates the UI elements
     */
    private void addItems() {
        this.setMaxSize(500, 260);
        this.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(25);

        //Puts password reset on its own side so it does not affect the height
        BorderPane splitter = new BorderPane();
        splitter.setPadding(new Insets(10, 15, 0, 15));
        
        //Left side settings
        VBox settingsVB = new VBox();
        settingsVB.setAlignment(Pos.CENTER_LEFT);
        settingsVB.setSpacing(20);
        
        //Timeout
        HBox timeoutHB = new HBox();
        timeoutHB.setSpacing(10);
        Text timeoutLabel = new Text("Timeout duration (min):");
        timeoutLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        timeoutLabel.setStyle("-fx-fill: #FFFFFF;");
        TextField timeoutTF = new TextField();
        /////////THIS WILL NEED UPDATING TO PULL USER SETTINGS/////////
        timeoutTF.setText(String.valueOf(existsingTimeout));
        timeoutTF.setPromptText("ex. 10");
        timeoutTF.setMaxWidth(45);
        timeoutHB.getChildren().addAll(timeoutLabel, timeoutTF);

        //Copy password instead of displaying
        HBox copyHB = new HBox();
        copyHB.setSpacing(5);
        Text copy = new Text("Auto copy password?");
        copy.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        copy.setStyle("-fx-fill: #FFFFFF;");
        CheckBox copyCB = new CheckBox();
        copyCB.setSelected(existingCopyPassword);
        copyHB.getChildren().addAll(copy, copyCB);

        //Default Password Length
        HBox lengthHB = new HBox();
        lengthHB.setSpacing(10);
        Text lengthLabel = new Text("Password Length");
        lengthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lengthLabel.setStyle("-fx-fill: #FFFFFF;");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "8", "16", "24", "32", "48", new Separator(), "Custom"));
        cb.setMaxWidth(45);
        cb.setValue(existingPasswordLength);

        HBox custLength = new HBox();
        custLength.setSpacing(5);
        TextField length = new TextField();
        length.setMaxWidth(45);
        Button goBack = new Button("Back");
        custLength.getChildren().addAll(length, goBack);
        lengthHB.getChildren().addAll(lengthLabel, cb);
        
        //View report
        Button report = new Button("Print Passwords");

        settingsVB.getChildren().addAll(timeoutHB, copyHB, lengthHB, report);

        //Change Password
        //VBox to store height
        VBox passwordVB = new VBox();
        passwordVB.setMinHeight(150);
        
        TitledPane password = new TitledPane();
        password.setText("Change Master Password");
        password.setExpanded(false);
        password.setPrefWidth(225);
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(3);
        box.setPadding(new Insets(5, 5, 5, 5));
        PasswordField old = new PasswordField();
        old.setPromptText("Enter current password");
        old.setMaxWidth(225);
        PasswordField newPass = new PasswordField();
        newPass.setPromptText("Enter new password");
        newPass.setMaxWidth(225);
        PasswordField repeat = new PasswordField();
        repeat.setPromptText("Enter new password again");
        repeat.setMaxWidth(225);
        Button savePass = new Button("Save Password");
        box.getChildren().addAll(old, newPass, repeat, savePass);
        password.setContent(box);
        
        passwordVB.getChildren().add(password);
        splitter.setLeft(settingsVB);
        splitter.setRight(passwordVB);

        //Save changes
        Button save = new Button("Save Changes");

        contentBox.getChildren().addAll(splitter, save);
        this.setCenter(contentBox);
        
        //Pane control
        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        Button close = new Button("X");
        close.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        close.setBackground(Background.EMPTY);
        closeBox.getChildren().add(close);
        this.setTop(closeBox);

        /**
         *****************
         *EVENT LISTENERS*
         *****************/
        
        repeat.setOnKeyReleased(e -> {
            if(!repeat.getText().equals(newPass.getText())){
                repeat.setStyle("-fx-background-color: rgba(255,0,0,.5);");
            } else {
                repeat.setStyle(null);
            }
        });
        
        
        //Closes on save
        save.setOnAction(e -> {
            //SAVE LOGIC HERE
            // Initialize values with defaults as a fall-back
            int timeout = 10;
            String passwordLength = "16";
            int passwordLengthNum = 16;
            try {
                timeout = Integer.parseInt(timeoutTF.getText());
            } catch (NumberFormatException e1) {
                // Need to display notification to enter a number
            }
            try {
                passwordLength = String.valueOf(cb.getValue());
                passwordLengthNum = Integer.parseInt(passwordLength);
            } catch (NumberFormatException e1) {
                // Display message that password length not a number
            }
            // Detects if changes are necessary to the database
            if (existsingTimeout != timeout || !existingPasswordLength.equals(passwordLength)) {
                DBConnect.updatePrefrenceSettings(UserInfoSingleton.getInstance().getUserID(),
                                                    timeout,
                                                    passwordLengthNum);
            }
            UserInfoSingleton.getInstance().setCopyPassword(copyCB.isSelected());
            showNotification("Settings Saved");
            CONTAINER.closeSettings();
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

        /*

        Thomas, how do you feel about ignoring web accounts here.
        We can hash old pass and compare with existing master password.
        If equals then issue an update using updateMasterPassword().
        if not then display alert

        Thomas response: can't ignore web accounts, all profiles are
        encrypted with the old master password, with a new master password,
        they will all need to be re-encrypted and written to the database
        with the new result.

        */
        savePass.setOnAction(e -> {
            String plainCurrentMasterPassword = old.getText();
            String plainNewMasterPassword = newPass.getText();
            String plainNewRepeatPassword = repeat.getText();
            if (plainCurrentMasterPassword.isEmpty()
                    || plainNewMasterPassword.isEmpty()
                    || plainNewRepeatPassword.isEmpty()) {
                CONTAINER.showDialog(new ErrorDialog(CONTAINER, 
                        "Please ensure the current password, new password, "
                        + "and repeated password are filled in."));
            } else {
                ArrayList<WebAccount> oldWebAccounts = UserInfoSingleton.getInstance().getProfiles();
                String newMasterPasswordHash = UserInfoSingleton.getInstance().hashString(plainNewMasterPassword);
                int userID = UserInfoSingleton.getInstance().getUserID();
                ArrayList<WebAccount> newWebAccounts = new ArrayList<>();
                // Updates the master account hash
                DBConnect.updateMasterPassword(userID, newMasterPasswordHash);
                // Updates all the associated web accounts
                oldWebAccounts.stream().forEach(account -> {
                    newWebAccounts.add(account
                            .updatePassword(plainNewMasterPassword));
                });
                newWebAccounts.stream().forEach(account -> {
                    DBConnect.updateWebAccount(userID,
                            account.getWebID(),
                            account.getEncodedAccountName(),
                            account.getEncodedUsername(),
                            account.getEncodedPassword());
                });
                // Update the singleton
                UserInfoSingleton.getInstance().setPassword(plainNewMasterPassword);
                UserInfoSingleton.getInstance().addProfiles(newWebAccounts);
                old.clear();
                newPass.clear();
                repeat.clear();
                password.setExpanded(false);
                showNotification("Password Successfully Changed");
            }

        });
        
        //Section to print the stored passwords.
        report.setOnAction(e -> {
            boolean success = Print.executePrint(UserInfoSingleton
                    .getInstance().getProfiles());
            if(success){
            showNotification("File Sent to Printer");
            } else {
                CONTAINER.showDialog(new ErrorDialog(CONTAINER, 
                        "Something went wrong. Please check your printer "
                                + "connection and try again.  If the problem "
                                + "persists, please report a bug."));
            }
        });
    }//end addItems 
    
    /**
     * Notifies the user that the password report was sent to the printer
     */
    private void showNotification(String message){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox notification = new HBox();
        notification.setAlignment(Pos.CENTER);
        notification.setPrefSize(140, 30);
        notification.setPadding(new Insets(5, 15, 5, 15));
        notification.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        Text notificationText = new Text(message);
        notificationText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        notificationText.setStyle("-fx-fill: #FFFFFF;");
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
}
