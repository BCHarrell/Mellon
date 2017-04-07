package mellon;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.*;

/**
 * Menu to create and edit profiles stored on the account.
 * 
 * @author Brent H.
 */
public class CreationPage extends StackPane {

    private final MenuContainer CONTAINER;
    private ArrayList<Character> allowedSymbols;
    private AdvancedMenu adv;
    private boolean edit, passwordChanged = false;
    private String currentNick, currentUser, currentPass;
    private BorderPane bp = new BorderPane();
    
    //elements used to save
    private TextField nickField, userField, generatedWebPassword;
    private DatePicker expiration;
    private CheckBox expireCB;

    public CreationPage(MenuContainer c) {
        CONTAINER = c;
        edit = false;
        createBorderPane();
    }
    
    /**
     * Alternate constructor for edit mode.  Pre-populates information already
     * stored in the profile
     * @param c The MenuContainer
     * @param nick the profile's nickname
     * @param user the profile's stored password
     * @param pass the profile's stored password
     */
    public CreationPage(MenuContainer c, String nick, String user,
                            String pass) {
        CONTAINER = c;
        edit = true;
        currentNick = nick;
        currentUser = user;
        currentPass = pass;
        createBorderPane();
    }
    
    /**
     * Displays the advanced menu after a fade transition
     */
    private void addAdvMenu() {
        this.getChildren().add(adv);
        blur();
        FadeTransition ft = new FadeTransition(Duration.millis(250), adv);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();  
    }
    
    /**
     * Creates the UI elements
     */
    private void createBorderPane() {
        adv = new AdvancedMenu(this);
        
        VBox mainArea = new VBox();
        mainArea.setAlignment(Pos.CENTER);
        mainArea.setSpacing(75);
        mainArea.setPadding(new Insets(0, 30, 0, 30));
        
        //Top Horizontal Box
        BorderPane settingsArea = new BorderPane();
        settingsArea.setStyle("-fx-background-color: #0088AA;");
        settingsArea.setPadding(new Insets(15, 15, 15, 15));
        
        //Vertical box to profile elements
        VBox settingsVB = new VBox();
        settingsVB.setSpacing(15);

        //Nickname
        VBox nickVB = new VBox();
        nickVB.setSpacing(3);
        Text nickLabel = new Text("Nickname");
        nickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nickField = new TextField();
        nickField.setMaxWidth(400);
        nickField.setPromptText("Enter account nickname");
        Tooltip nickTip = new Tooltip("This is the name you will use to get "
                + "the password in the future. For example: \"Gmail\"");
        nickField.setTooltip(nickTip);
        if (edit){
            nickField.setText(currentNick);
        }
        nickVB.getChildren().addAll(nickLabel, nickField);

        //Username
        VBox userVB = new VBox();
        userVB.setSpacing(3);
        Text userLabel = new Text("Username");
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        userField = new TextField();
        userField.setMaxWidth(400);
        userField.setPromptText("Enter username");
        Tooltip userTip = new Tooltip("This is the username for your account. "
                + "For example, your email address or \"MellonUser\"");
        userField.setTooltip(userTip);
        if (edit) {
            userField.setText(currentUser);
        }
        userVB.getChildren().addAll(userLabel, userField);

        //Length selection
        VBox lengthVB = new VBox();
        lengthVB.setSpacing(3);
        Text lengthLabel = new Text("Password Length");
        lengthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "8", "16", "24", "32", "48", new Separator(), "Custom"));
        cb.setMaxWidth(350);
        cb.setValue("16");

        HBox custLength = new HBox();
        custLength.setSpacing(5);
        TextField length = new TextField();
        length.setMaxWidth(40);
        Button goBack = new Button("Back to Selection");
        custLength.getChildren().addAll(length, goBack);
        lengthVB.getChildren().addAll(lengthLabel, cb);
        
        //Expiration
        VBox expirationBox = new VBox();
        expirationBox.setSpacing(3);
        expireCB = new CheckBox();
        Label expireLabel = new Label("Set expiration? ");
        expireLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        expireLabel.setGraphic(expireCB);
        expireLabel.setContentDisplay(ContentDisplay.RIGHT);
        expiration = new DatePicker();
        expiration.setValue(LocalDate.now());
        Callback<DatePicker, DateCell> cellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(LocalDate.now())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #AFAFAF;");
                            }
                        }
                    };
                }
            };
        expiration.setDayCellFactory(cellFactory);
        expiration.setVisible(false);
        expirationBox.getChildren().addAll(expireLabel, expiration);

        settingsVB.getChildren().addAll(nickVB, userVB, lengthVB, expirationBox);

        //CUSTOMIZATION box
        VBox custVB = new VBox();
        custVB.setAlignment(Pos.CENTER_RIGHT);
        custVB.setSpacing(5);
        custVB.setPadding(new Insets(5, 5, 5, 5));

        //Quick customize box
        Text customize = new Text("Quick Customize");
        customize.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox innerVB = new VBox();
        innerVB.setPadding(new Insets(10, 10, 10, 10));
        innerVB.setSpacing(10);
        
        VBox optionVB = new VBox();
        optionVB.setAlignment(Pos.CENTER_LEFT);
        optionVB.setSpacing(8);
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
        HBox centeringHB = new HBox();
        centeringHB.setAlignment(Pos.CENTER);
        Button advanced = new Button("Advanced");
        centeringHB.getChildren().add(advanced);
        
        innerVB.getChildren().addAll(optionVB, centeringHB);

        //ADD
        custVB.getChildren().addAll(customize, innerVB);
        settingsArea.setLeft(settingsVB);
        settingsArea.setRight(custVB);
        addDropShadow(settingsArea);

        //Generate Area
        VBox generateVB = new VBox();
        generateVB.setStyle("-fx-background-color: #0088AA;");
        generateVB.setPadding(new Insets(15, 15, 15, 15));
        generateVB.setAlignment(Pos.CENTER);
        generateVB.setSpacing(15);
        Button generate = new Button("Generate Password");
        generatedWebPassword = new TextField();
        generatedWebPassword.setMaxWidth(400);
        generatedWebPassword.setAlignment(Pos.CENTER);
        if (edit) {
            generatedWebPassword.setText(currentPass);
        }
        
        generateVB.getChildren().addAll(generatedWebPassword, generate);
        addDropShadow(generateVB);
        
        //Save anchored to bottom
        HBox saveHB = new HBox();
        saveHB.setSpacing(10);
        saveHB.setAlignment(Pos.CENTER);
        saveHB.setPadding(new Insets(0, 0, 15, 0));
        
        Button save = new Button("Save Account");
        Button delete = new Button("DELETE ACCOUNT");
        delete.setStyle("-fx-background-color: #D4AA00;");
        saveHB.getChildren().add(save);
        if (edit){
            saveHB.getChildren().add(delete);
        }
        
        mainArea.getChildren().addAll(settingsArea, generateVB);
        bp.setCenter(mainArea);
        bp.setBottom(saveHB);
        this.getChildren().add(bp);


        /*****************
         *EVENT LISTENERS*
         *****************/
        //Choice box switch-out
        cb.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val,
                 Number new_val) -> {
                    if (new_val.intValue() == 5) {
                        lengthVB.getChildren().remove(cb);
                        lengthVB.getChildren().addAll(custLength);
                        length.requestFocus();
                    }
        });
        
        //Returns to the choice box
        goBack.setOnAction(e -> {
            lengthVB.getChildren().remove(custLength);
            lengthVB.getChildren().add(cb);
        });
        
        //Password generation
        generate.setOnAction(e -> {
            int pwLength = 0;
            try {
                pwLength = Integer.parseInt(String.valueOf(cb.getValue()));
            } catch (NumberFormatException e1) {
                // Password length was not a number.
            }
            if (symb.isSelected() && allowedSymbols == null) {
                allowedSymbols = new ArrayList<>();
            }
            Password password = new Password.PasswordBuilder(pwLength)
                    .includeCapitals(upper.isSelected())
                    .includeLowers(lower.isSelected())
                    .includeNumbers(numbers.isSelected())
                    .includeSpecialCharacters(symb.isSelected())
                    .includeAllowedSymbols(allowedSymbols)
                    .build();
            
            if (edit) {
                passwordChanged = true;
            }
            
            generatedWebPassword.setText(password.getPasswordString());
        });
        
        //Save account
        //THIS CAN BE MOVED TO ITS OWN METHOD FOR CLARITY
        save.setOnAction(e -> {
            if (passwordChanged) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                    "It looks like you're saving a new password for this "
                    + "profile. Changing your password here does not change "
                    + "the password in the actual account and you may still "
                    + "need your old password. If you save now, you will not "
                    + "be able to get your old password back.\n\nAre you sure "
                    + "you want to save?",
                    ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Just Checking");
                confirm.setHeaderText("");
            
                confirm.showAndWait()
                    .filter(response -> response == ButtonType.YES)
                    .ifPresent(response -> {
                        save();
                    });
            } else {
                save();
            }
        
        });
        
        //Opens advanced symbol menu
        advanced.setOnAction(e -> {
            if (!symb.isSelected())
                adv.deselect();

            addAdvMenu();
        });
        
        //Expiration date checkbox
        expireCB.selectedProperty().addListener(e -> {
            if (expireCB.isSelected()) {
                expiration.setVisible(true);
            } else {
                expiration.setVisible(false);
            }
        });
        
        //Delete button
        delete.setOnAction(e -> {
            //Temporary alert
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                    "Are you sure you want to delete this account?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("");
            
            confirm.showAndWait()
                    .filter(response -> response == ButtonType.YES)
                    .ifPresent(response -> {
                        //DELETION CODE GOES HERE
            });
        });

    } //End addItems()
    
    /**
     * @param list List of allowable characters from the advanced menu
     */
    public void setAllowable(ArrayList<Character> list) {
        allowedSymbols = list;
    }
    
    private void save() {
        if (nickField.getText().isEmpty() ||
                userField.getText().isEmpty() ||
                generatedWebPassword.getText().isEmpty()) {
            CONTAINER.showDialog(new ErrorDialog(CONTAINER, "Please ensure "
                    + "the nickname, username, and password fields are "
                    + "filled in."));
        } else {
            WebAccount newAccount = null;
            UserInfoSingleton.getInstance();
            int userID = UserInfoSingleton.getUserID();
            String masterKey = UserInfoSingleton.getPassword();
            String inputNickname = nickField.getText();
            String inputUsername = userField.getText();
            String webAccountPassword = generatedWebPassword.getText();
            LocalDate inputExpiration;
            
            if (expireCB.isSelected()) {
                inputExpiration = expiration.getValue();
            } else {
                inputExpiration = null;
            }
            
            newAccount = new WebAccount(inputUsername,
                    webAccountPassword,
                    inputNickname,
                    masterKey,
                    inputExpiration);
            boolean accountCreated = false;
            int existingWebAccount = DBConnect.existingWebAccount(userID,
                                     newAccount.getEncodedAccountName());
            
            if (existingWebAccount > 0) {
                // Web account already exists, update it
                newAccount.setWebID(existingWebAccount);
                DBConnect.updateWebAccount(userID,
                        existingWebAccount,
                        newAccount.getEncodedAccountName(),
                        newAccount.getEncodedUsername(),
                        newAccount.getEncodedPassword());
                UserInfoSingleton.getInstance().addSingleProfile(newAccount);
            } else {
                // Web account doesn't exist, create it
                accountCreated = DBConnect.CreateWebAccount(userID,
                        newAccount.getEncodedAccountName(),
                        newAccount.getEncodedUsername(),
                        newAccount.getEncodedPassword(),
                        inputExpiration);
                if (accountCreated) {
                    UserInfoSingleton.getInstance().addNewProfile(newAccount);
                } else if (!accountCreated) {
                    CONTAINER.showDialog(new ErrorDialog(CONTAINER, "The profile "
                            + "was not created.  Please try again.  If the error "
                            + "persists, please report a bug."));
                }
            }

            CONTAINER.getMain().update();
            CONTAINER.requestMenuChange(CONTAINER.getMain());
        } 
    }
    
    /**
     * Removes the advanced menu after animation
     */
    public void popAdvanced() {
        //Check just to be sure
        if (this.getChildren().contains(adv)) {
            FadeTransition ft = new FadeTransition(Duration.millis(250), adv);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished(e -> {
                this.getChildren().remove(adv);
                unBlur();
            });
            ft.play(); 
        }
    }
    
    /**
     * Adds drop shadows to menu boxes
     */
    private void addDropShadow(Node n) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3);
        ds.setColor(Color.GRAY);
        n.setEffect(ds);
    }
    
    /**
     * Blurs the background
     */
    private void blur() {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5);
        blur.setHeight(5);
        blur.setIterations(3);
        bp.setEffect(blur);
    }
    
    /**
     * Unblurs the background
     */
    private void unBlur() {
        bp.setEffect(null);
    }
}

