package mellon;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        titleBox.setRight(edit);
        
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
        
        //Copied label (Temporary)
        Text copied = new Text("Copied!");
        copied.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        copied.setStyle("-fx-fill: #ffffff");
        copied.setVisible(false);
        
        
        hb.getChildren().addAll(password, visible, copied);
        passVB.getChildren().addAll(passLabel, hb);
        
        contentBox.getChildren().addAll(userVB, passVB);
        
        this.getChildren().addAll(titleBox);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        //Edit button
        edit.setOnAction(e -> {
            CONTAINER.setCenter(new CreationPage(CONTAINER,
                        account.getAccountName(), account.getUsername(),
                        account.getPassword()));
        });
        
        //Expands and hides the profile box
        titleBox.setOnMouseClicked(e -> {
            if (this.getChildren().contains(contentBox)){
                this.getChildren().setAll(titleBox);
            } else {
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
        password.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2){
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(password.getText());
                clipboard.setContent(content);
                copied.setVisible(true);
                
                //removes label after 5 sec
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(5),
                    (ActionEvent event) -> {
                        copied.setVisible(false);
                    })
                );
                timer.play();
            }
        });
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
}
