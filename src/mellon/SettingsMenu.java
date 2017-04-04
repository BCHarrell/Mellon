package mellon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import javafx.scene.text.*;


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
        timeoutTF.setText("10");
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
        copyHB.getChildren().addAll(copy, copyCB);

        //Default Password Length
        HBox lengthHB = new HBox();
        lengthHB.setSpacing(10);
        Text lengthLabel = new Text("Default Password Length");
        lengthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lengthLabel.setStyle("-fx-fill: #FFFFFF;");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "8", "16", "24", "32", "48", "Custom"));
        cb.setMaxWidth(45);
        cb.setValue("16");

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
        
        passwordVB.getChildren().add(password);
        splitter.setLeft(settingsVB);
        splitter.setRight(passwordVB);

        //Save changes
        Button save = new Button("Save Changes");

        contentBox.getChildren().addAll(splitter, save);
        this.setCenter(contentBox);
        
        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        Button close = new Button("X");
        close.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        close.setBackground(Background.EMPTY);
        closeBox.getChildren().add(close);
        this.setTop(closeBox);

        /**
         * **************
         * EVENT LISTENERS **************
         */
        //Closes on save
        save.setOnAction(e -> CONTAINER.closeSettings());
        
        //Closes on X
        close.setOnAction(e -> CONTAINER.closeSettings());

        //Choicebox for length selection
        cb.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val,
                        Number new_val) -> {
                    if (new_val.intValue() == 5) {
                        lengthHB.getChildren().remove(cb);
                        lengthHB.getChildren().addAll(custLength);
                        length.requestFocus();
                    }
                });

        //Go back to choice box from custom selection
        goBack.setOnAction(e -> {
            lengthHB.getChildren().remove(custLength);
            lengthHB.getChildren().add(cb);
            cb.setValue(16);
        });

        // Thomas, how do you feel about ignoring web accounts here. We can hash old pass and compare with 
        // existing master password. If equals then issue an update using updateMasterPassword(). if not then display alert
        savePass.setOnAction(e -> {
            String plainCurrentMasterPassword = old.getText();
            String plainNewMasterPassword = newPass.getText();
            String plainNewRepeatPassword = repeat.getText();
            if (plainCurrentMasterPassword.isEmpty()
                    || plainNewMasterPassword.isEmpty()
                    || plainNewRepeatPassword.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid password input");
                alert.setContentText("Please ensure the current password,"
                        + " new password, and repeated password fields are"
                        + " filled in.");
                alert.showAndWait();
            } else {
                MasterAccount oldMasterAccount = UserInfoSingleton.getInstance().getMasterAccount();
                String newMasterPasswordHash = oldMasterAccount.hashString(plainNewMasterPassword);
                ArrayList<WebAccount> oldWebAccounts = UserInfoSingleton.getInstance().getProfiles();
                int userID = UserInfoSingleton.getInstance().getUserID();
                ArrayList<WebAccount> newWebAccounts = new ArrayList<>();
                // Updates the master account hash
                DBConnect.updateMasterPassword(userID, newMasterPasswordHash);
                // Updates all the associated web accounts
                oldWebAccounts.stream().forEach(account -> {
                    newWebAccounts.add(account.updatePassword(plainNewMasterPassword));
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Password changed");
                alert.setContentText("Your master password has been updated.");
                alert.showAndWait();
            }

        });

        report.setOnAction(e -> {
            BufferedWriter wrtr = null;
            File file = new File("MellonUserReport.txt");
            ArrayList<WebAccount> WebAccounts = UserInfoSingleton.getInstance().getProfiles();
            try {
                wrtr = new BufferedWriter(new FileWriter(file, true));
                wrtr.write("Here's a list of your stored account details ");
                wrtr.newLine();
                for (WebAccount acct : WebAccounts) {
                    wrtr.write("Account name: " + acct.getAccountName() + " , Account username: " + acct.getUsername() + " , Account password: " + acct.getPassword()
                            + " , Password Expiration Date: " + acct.getExpDate());
                    wrtr.newLine();
                };
                wrtr.write("Thank you for using Mellon Password Storage");

                if (wrtr != null) {
                    wrtr.close();
                }

            } catch (IOException io) {
                System.out.println("File IO Exception" + io.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Your account report generation failed");
                alert.setContentText("You must have at least one account stored to request the report");
                alert.showAndWait();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Your account report is completed");
            alert.setContentText("Please retrieve you account report from printer");
            alert.showAndWait();
            // This is where the file should be sent to the printer
            
            // file.delete(); This commented out for now since you'll be looking at the file output
        });
    }//end addItems 
}
