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
 * This class will create individual profile information panes to be displayed
 * on the main menu.
 */
public class ProfilePane extends VBox{
    
    private WebAccount account;
    private final MenuContainer CONTAINER;
    private final ImageView EYE_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/eye_icon.png")));
    private TextField password;
    private boolean isBlurred = true;
    
    public ProfilePane(MenuContainer c, WebAccount a){
        account = a;
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        this.setSpacing(5);
        //Move to CSS file
        this.setStyle("-fx-background-color: #0088AA;");
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
        nickname.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nickname.setStyle("-fx-fill: #ffffff");
        Button edit = new Button("Edit Profile");
        titleBox.setLeft(nickname);
        
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
        
        //CONTENT BOX
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentBox.setPadding(new Insets(10, 0, 10, 10));
        
        //USERNAME Box
        VBox userVB = new VBox();
        userVB.setSpacing(5);
        Text userLabel = new Text("Username");
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userLabel.setStyle("-fx-fill: #ffffff");
        TextField username = new TextField();
        username.setEditable(false);
        username.setMaxWidth(175);
        username.setText(account.getUsername());
        username.setEditable(false);
        userVB.getChildren().addAll(userLabel, username);

        ////////Password label and field////////////
        VBox passVB = new VBox();
        passVB.setSpacing(5);
        Text passLabel = new Text("Password");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        passLabel.setStyle("-fx-fill: #ffffff");

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
        visible.setBackground(Background.EMPTY);
                
        hb.getChildren().addAll(password, visible);
        
        //Expiration notice
        Text soonToExpire = new Text("Your password will expire soon.");
        soonToExpire.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        soonToExpire.setStyle("-fx-fill: #FFFFFF;");
        
        Text expired = new Text("Your password has expired.");
        expired.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        expired.setStyle("-fx-fill: #d4aa00;");
        
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
        
        //Lightens box on highlight
        this.setOnMouseEntered(e -> {
            this.setStyle("-fx-background-color: #00A9D4;");
        });
        
        //Returns to normal color after mouse leaves
        this.setOnMouseExited(e -> {
            this.setStyle("-fx-background-color: #0088AA;");
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
        password.setOnMouseClicked(e -> copyPassword());

    }
    
    /**
     * Creates and displays a notification to the user that the password
     * was copied to the clipboard, fades after a set time.
     */
    private void createCopyNotification(){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox notification = new HBox();
        notification.setAlignment(Pos.CENTER);
        notification.setPrefSize(125, 30);
        notification.setPadding(new Insets(5, 15, 5, 15));
        notification.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        Text notificationText = new Text("Copied to Clipboard");
        notificationText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        notificationText.setStyle("-fx-fill: #FFFFFF;");
        notification.getChildren().add(notificationText);
        
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

    private void copyPassword() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(password.getText());
        clipboard.setContent(content);
        createCopyNotification();
    }
}
