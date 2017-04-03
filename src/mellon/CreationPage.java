package mellon;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Callback;

/**
 * @author Brent H.
 */
public class CreationPage extends BorderPane {

    private final MenuContainer CONTAINER;
    private ArrayList<Character> allowedSymbols;
    private AdvancedMenu adv;
    private boolean edit;
    private String currentNick, currentUser, currentPass;

    public CreationPage(MenuContainer c) {
        CONTAINER = c;
        edit = false;
        addItems();
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
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        adv = new AdvancedMenu(CONTAINER, this);
        
        VBox mainArea = new VBox();
        mainArea.setAlignment(Pos.CENTER);
        mainArea.setSpacing(75);
        mainArea.setPadding(new Insets(0, 30, 0, 30));
        
        //Top Horizontal Box
        HBox topHB = new HBox();
        topHB.setAlignment(Pos.CENTER);
        topHB.setSpacing(200);
        
        //Vertical box to profile elements
        VBox settingsVB = new VBox();
        settingsVB.setSpacing(15);

        //Nickname
        VBox nickVB = new VBox();
        nickVB.setSpacing(3);
        Text nickLabel = new Text("Nickname");
        nickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField nickField = new TextField();
        nickField.setMaxWidth(350);
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
        TextField userField = new TextField();
        userField.setMaxWidth(350);
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
        CheckBox expireCB = new CheckBox();
        Label expireLabel = new Label("Set password expiration?");
        expireLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        expireLabel.setGraphic(expireCB);
        expireLabel.setContentDisplay(ContentDisplay.RIGHT);
        DatePicker expiration = new DatePicker();
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
        topHB.getChildren().addAll(settingsVB, custVB);

        //Generate Area
        VBox generateVB = new VBox();
        generateVB.setAlignment(Pos.CENTER);
        generateVB.setSpacing(15);
        Button generate = new Button("Generate Password");
        TextField generatedWebPassword = new TextField();
        generatedWebPassword.setMaxWidth(400);
        generatedWebPassword.setAlignment(Pos.CENTER);
        if (edit) {
            generatedWebPassword.setText(currentPass);
        }
        
        generateVB.getChildren().addAll(generatedWebPassword, generate);
        
        //Save anchored to bottom
        HBox saveHB = new HBox();
        saveHB.setAlignment(Pos.CENTER);
        saveHB.setPadding(new Insets(0, 0, 15, 0));
        Button save = new Button("Save Account");
        saveHB.getChildren().add(save);
        
        mainArea.getChildren().addAll(topHB, generateVB);
        this.setCenter(mainArea);
        this.setBottom(saveHB);


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
            generatedWebPassword.setText(password.getPasswordString());
        });
        
        //Save account
        save.setOnAction(e -> {
            if (nickField.getText().isEmpty() ||
                    userField.getText().isEmpty() ||
                    generatedWebPassword.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid account details");
                alert.setContentText("Please ensure the Nickname, Username,"
                        + " and Password fields are filled in.");
                alert.showAndWait();
            } else {
                WebAccount newAccount = null;
                UserInfoSingleton.getInstance();
                int id = UserInfoSingleton.getUserID();
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
                boolean accountCreated = DBConnect.CreateWebAccount(id,
                        newAccount.getEncodedAccountName(),
                        newAccount.getEncodedUsername(),
                        newAccount.getEncodedPassword(),
                        inputExpiration);
                if (accountCreated) {
                    UserInfoSingleton.getInstance().addSingleProfile(newAccount);
                } else if (!accountCreated) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Account not created");
                    alert.setContentText("The profile was not created,"
                            + " please try again.");
                    alert.showAndWait();
                }
            }
            CONTAINER.getMain().update();
            CONTAINER.setCenter(CONTAINER.getMain());
        });
        
        //Opens advanced symbol menu
        advanced.setOnAction(e -> {
            if (!symb.isSelected())
                adv.deselect();

            CONTAINER.setCenter(adv);
        });
        
        //Expiration date checkbox
        expireCB.selectedProperty().addListener(e -> {
            if (expireCB.isSelected()) {
                expiration.setVisible(true);
            } else {
                expiration.setVisible(false);
            }
        });

    } //End addItems()
    
    /**
     * @param list List of allowable characters from the advanced menu
     */
    public void setAllowable(ArrayList<Character> list) {
        allowedSymbols = list;
    }
}

