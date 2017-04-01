package mellon;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 *
 * @author Brent H.
 */
public class CreationPage extends VBox {
    
    private final MenuContainer CONTAINER;
    private ArrayList<Character> allowedSymbols;
    private AdvancedMenu adv;

    public CreationPage(MenuContainer c) {
        CONTAINER = c;
        addItems();
    }
    
    private void addItems() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(75);
        adv = new AdvancedMenu(CONTAINER, this);
        
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
        
        //Expiration
        VBox expirationBox = new VBox();
        expirationBox.setAlignment(Pos.CENTER);
        expirationBox.setSpacing(5);
        CheckBox expireCB = new CheckBox();
        Label expireLabel = new Label("Set password expiration?");
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
        
        //Generate
        VBox generateVB = new VBox();
        generateVB.setAlignment(Pos.CENTER);
        generateVB.setSpacing(15);
        Button generate = new Button("Generate Password");
        TextField output = new TextField();
        output.setMaxWidth(400);
        output.setAlignment(Pos.CENTER);
        Button toMain = new Button("Back to Main (will be anchored bottom)");
        Button save = new Button("Save Account");
        generateVB.getChildren().addAll(generate, output, toMain, save);
        
        this.getChildren().addAll(topHB, expirationBox, generateVB);
        
        
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
            output.setText(password.getPasswordString());
        });

        save.setOnAction(e -> {
            if (nickname.getText().isEmpty() ||
                    username.getText().isEmpty() ||
                    output.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid account details");
                alert.setContentText("Please ensure the Nickname, Username, and Password fields are filled in.");
                alert.showAndWait();
            } else {
                WebAccount newAccount = null;
                UserIDSingleton.getInstance();
                int id = UserIDSingleton.getUserID();
                String masterKey = UserIDSingleton.getPassword();
                String inputNickname = nickname.getText();
                String inputUsername = username.getText();
                String key = output.getText();
                LocalDate inputExpiration;
                if(expireCB.isSelected()){
                inputExpiration = expiration.getValue();}
                else {
                    inputExpiration = null;
                }
                newAccount = new WebAccount (inputUsername,key,inputNickname,masterKey,inputExpiration);
                DBConnect.CreateWebAccount (id,inputNickname,newAccount.getEncodedUsername(),newAccount.getEncodedPassword(),inputExpiration);
                //System.out.println(id);
            }
            CONTAINER.setCenter(CONTAINER.getMain());
        });
        
        
        goBack.setOnAction(e -> {
           lengthVB.getChildren().remove(custLength);
           lengthVB.getChildren().add(cb);
        });
        
        toMain.setOnAction(e -> {
            CONTAINER.setCenter(CONTAINER.getMain());
        });
        
        advanced.setOnAction(e -> {
            if(!symb.isSelected())
                adv.deselect();
            
            CONTAINER.setCenter(adv);
        });
        
        expireCB.selectedProperty().addListener(e -> {
            if(expireCB.isSelected()){
                expiration.setVisible(true);
            } else {
                expiration.setVisible(false);
            }
        });
        
    } //End addItems()
    
    public void setAllowable(ArrayList<Character> list) {
        allowedSymbols = list;
    }
}

