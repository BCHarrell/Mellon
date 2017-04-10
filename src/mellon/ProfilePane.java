package mellon;


import java.time.LocalDate;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;

/**
 * This class will create individual profile information panes to be displayed.
 * Initially, only the title pane containing the profile nickname and two
 * buttons is displayed. Clicking this pane (left click) copies the password
 * to the clipboard, right clicking the pane copies the username. Clicking the
 * pencil takes the user to the edit menu to adjust the profile's details,
 * and clicking the plus sign expands the profile to display the information.
 * The plus sign is replaced with a minus sign once expanded, which reverses
 * the process.
 * 
 * @author Brent H.
 */
public class ProfilePane extends VBox{
    
    private WebAccount account;
    private final MenuContainer CONTAINER;
    private final ImageView EYE_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/eye_icon.png")));
    private final ImageView EDIT_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/edit_icon.png")));
    private final ImageView EXPAND_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/expand_icon.png")));
    private final ImageView MINI_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/mini_icon.png")));
    
    private TextField password, username;
    private boolean isBlurred = true;
    
    private enum Data {PASSWORD, USERNAME};
    
    public ProfilePane(MenuContainer c, WebAccount a){
        account = a;
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        this.setSpacing(5);
        this.getStyleClass().add("profile-pane");
        this.setMinHeight(50);
        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setColor(javafx.scene.paint.Color.GRAY);
        this.setEffect(ds);
        
        //TITLE
        BorderPane titleBox = new BorderPane();
        titleBox.setPadding(new Insets(10, 10, 0, 10));
        titleBox.setPrefHeight(35);
        Text nickname = new Text(account.getAccountName());
        nickname.getStyleClass().add("white-title");
        
        //Buttons
        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        Button edit = new Button();
        edit.getStyleClass().add("icon");
        edit.setGraphic(EDIT_ICON);
        edit.setTooltip(new Tooltip("Edit this profile"));
        Button expansion = new Button();
        expansion.getStyleClass().add("icon");
        expansion.setTooltip(new Tooltip("Opens and closes the profile"
                                            + " information"));

        expansion.setGraphic(EXPAND_ICON);
        buttonsBox.getChildren().addAll(expansion, edit);
        
        titleBox.setLeft(nickname);
        titleBox.setRight(buttonsBox);
        
        //CONTENT BOX
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentBox.setPadding(new Insets(10, 0, 10, 10));
        
        //USERNAME Box
        VBox userVB = new VBox();
        userVB.setSpacing(5);
        Text userLabel = new Text("Username");
        userLabel.getStyleClass().add("white-label");
        username = new TextField();
        username.setEditable(false);
        username.setMaxWidth(175);
        username.setText(account.getUsername());
        username.setEditable(false);
        userVB.getChildren().addAll(userLabel, username);

        ////////Password label and field////////////
        VBox passVB = new VBox();
        passVB.setSpacing(5);
        Text passLabel = new Text("Password");
        passLabel.getStyleClass().add("white-label");

        //Holds the password field and the visibility button
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setSpacing(5);
        hb.setMaxWidth(Double.MAX_VALUE);
        
        //Password field
        password = new TextField();
        password.setEditable(false);
        password.setPrefWidth(350);
        password.setText(account.getPassword());
        password.setEditable(false);
        blur();

        //Visibility button
        Button visible = new Button();
        visible.setGraphic(EYE_ICON);
        visible.getStyleClass().add("icon");
        visible.setTooltip(new Tooltip("Unblurs the password field"));
                
        hb.getChildren().addAll(password, visible);
        
        //Expiration notice
        Text soonToExpire = new Text("Your password will expire soon.");
        soonToExpire.getStyleClass().add("expiring-soon-notification");
        Text expired = new Text("Your password has expired.");
        expired.getStyleClass().add("expired-notification");
        
        passVB.getChildren().addAll(passLabel, hb);
        
        contentBox.getChildren().addAll(userVB, passVB);
        
        //Check Expiration Date
        if(account.getExpDate() != null){
            if (account.getExpDate().equals(LocalDate.now())
             || account.getExpDate().isBefore(LocalDate.now())){
                contentBox.getChildren().add(expired);
            } else if (account.getExpDate().minusDays(3).isBefore(LocalDate.now())
                    || account.getExpDate().minusDays(3).equals(LocalDate.now())){
                contentBox.getChildren().add(soonToExpire);
            }
        }
        
        this.getChildren().addAll(titleBox);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        //Edit button
        edit.setOnAction(e -> {
            CONTAINER.requestMenuChange(new CreationPage(CONTAINER,
                        account.getAccountName(), account.getUsername(),
                        account.getPassword()));
        });
        
        /*
            Single left-click = copy password
            Single right-click = copy username
            Also displays a pop-up if the password is expired/soon to expire
        */
        titleBox.setOnMouseClicked(e ->{
            if (e.getButton() == MouseButton.PRIMARY){
                copyToClipboard(Data.PASSWORD);
                if(account.getExpDate() != null){
                    if (account.getExpDate().equals(LocalDate.now())
                        || account.getExpDate().isBefore(LocalDate.now())){
                        CONTAINER.showDialog(new NotificationDialog(CONTAINER,
                                "According to the expiration date you "
                                + "provided, your password has expired"));
                    } else if (account.getExpDate().minusDays(3)
                                .isBefore(LocalDate.now())
                            || account.getExpDate().minusDays(3)
                                .equals(LocalDate.now())){
                        CONTAINER.showDialog(new NotificationDialog(CONTAINER,
                                "According to the expiration date you "
                                + "provided, your password will expire soon."));
                    }
                }
            } else if (e.getButton() == MouseButton.SECONDARY){
                copyToClipboard(Data.USERNAME);
            }
        });
        
        //Expand button used when the user has auto copy enabled
        expansion.setOnAction(e ->{
            if(this.getChildren().contains(contentBox)){
                expansion.setGraphic(EXPAND_ICON);
                this.getChildren().setAll(titleBox);
            } else {
                expansion.setGraphic(MINI_ICON);
                this.getChildren().setAll(titleBox, contentBox);
            }
        });
        
        //Blurs and unblurs the text
        visible.setOnAction(e -> {
            if (isBlurred){
                unBlur();
            } else {
                blur();
            }
        });

        //Copies text to clipboard and alerts the user
        password.setOnMouseClicked(e -> copyToClipboard(Data.PASSWORD));

    }
    
    /**
     * Creates and displays a notification to the user that the password
     * was copied to the clipboard, fades after a set time.
     */
    private void createCopyNotification(Data type){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox notification = new HBox();
        notification.setAlignment(Pos.CENTER);
        notification.setPrefSize(125, 30);
        notification.setPadding(new Insets(5, 15, 5, 15));
        notification.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        Text notificationText = new Text();
        notificationText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        notificationText.setStyle("-fx-fill: #FFFFFF;");
        notification.getChildren().add(notificationText);
        
        switch (type){
            case PASSWORD:
                notificationText.setText("Copied Password");
                break;
            case USERNAME:
                notificationText.setText("Copied Username");
                break;
        }
        
        anch.getChildren().add(notification);
        AnchorPane.setBottomAnchor(notification, 30.0);
        AnchorPane.setLeftAnchor(notification, 186.0);
        
        CONTAINER.getChildren().add(anch);
        
        FadeTransition ft = new FadeTransition(Duration.millis(2500), anch);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(1500));
        ft.setOnFinished(e -> CONTAINER.getChildren().remove(anch));
        ft.play();
    }
    
    /**
     * Blurs the password field
     */
    private void blur(){
        BoxBlur blur = new BoxBlur();
        blur.setWidth(10);
        blur.setHeight(10);
        blur.setIterations(3);
        password.setEffect(blur);
        isBlurred = true;
    }
    
    /**
     * Unblurs the password field
     */
    private void unBlur(){
        password.setEffect(null);
        isBlurred = false;
    }

    private void copyToClipboard(Data type) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        
        switch(type){
            case PASSWORD:
                content.putString(password.getText());
                break;
            case USERNAME:
                content.putString(username.getText());
                break;
        }
        
        clipboard.setContent(content);
        createCopyNotification(type);
    }
}

/* OLD CODE USING AUTO COPY SETTING
        Button expand = new Button("Open Profile");
        //Expand button if auto-copy password
        if(UserInfoSingleton.getInstance().isCopyPassword()){
            HBox buttonsBox = new HBox();
            buttonsBox.setSpacing(10);
            buttonsBox.setAlignment(Pos.CENTER_RIGHT);
            buttonsBox.getChildren().addAll(expand, edit);
            titleBox.setRight(buttonsBox);
        } else {
            titleBox.setRight(edit);
        }
            //LISTENERS
        //Expands and hides the profile box
        titleBox.setOnMouseClicked(e -> {
            if (UserInfoSingleton.getInstance().isCopyPassword()) {
                    copyPassword();
            } else if (this.getChildren().contains(contentBox)){
                this.getChildren().setAll(titleBox);
            } else {
                this.getChildren().setAll(titleBox, contentBox);
            }
        });
        
        //Expand button used when the user has auto copy enabled
        expand.setOnAction(e ->{
            if(this.getChildren().contains(contentBox)){
                expand.setText("Open Profile");
                this.getChildren().setAll(titleBox);
            } else {
                expand.setText("Close Profile");
                this.getChildren().setAll(titleBox, contentBox);
            }
        });
    */
