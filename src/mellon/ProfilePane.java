package mellon;

import java.awt.Color;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/**
 * This class will create individual profile information panes to be displayed
 * on the main menu.
 */
public class ProfilePane extends TitledPane{
    
    private WebAccount account;
    private final MenuContainer CONTAINER;
    private final ImageView EYE_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/eye_icon.png")));
    
    public ProfilePane(MenuContainer c, WebAccount a){
        account = a;
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        this.setStyle("-fx-background-color: #0088AA;");
        this.setText(account.getAccountName());
        
        VBox box = new VBox();
        box.setSpacing(10);
        
        ///////Username label and field////////
        VBox userVB = new VBox();
        userVB.setSpacing(5);
        Label userLabel = new Label("Username:");
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
        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-fill: #ffffff");

        //Holds the password field and the visibility button
        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setMaxWidth(Double.MAX_VALUE);

        //used to obscure the password (temporary)
        PasswordField password = new PasswordField();
        password.setEditable(false);
        password.setPrefWidth(350);
        password.setText(account.getPassword());
        password.setEditable(false);

        //displays the password
        TextField passwordVis = new TextField();
        passwordVis.setEditable(false);
        passwordVis.setPrefWidth(350);
        passwordVis.setText(account.getPassword());
        passwordVis.setEditable(false);

        //Visibility button
        Button visible = new Button();
        visible.setOnAction(e -> {
            if (hb.getChildren().contains(password)) {
                hb.getChildren().setAll(passwordVis, visible);
            } else {
                hb.getChildren().setAll(password, visible);
            }
        });
        visible.setGraphic(EYE_ICON);
        visible.setBackground(Background.EMPTY);
        
        Button edit = new Button("Edit Profile");
        
        hb.getChildren().addAll(passwordVis, visible);
        passVB.getChildren().addAll(passLabel, hb);
        
        box.getChildren().addAll(userVB, passVB, edit);
        this.setContent(box);

        /*****************
         *EVENT LISTENERS*
         *****************/
        edit.setOnAction(e -> {
            CONTAINER.setCenter(new CreationPage(CONTAINER,
                        account.getAccountName(), account.getUsername(),
                        account.getPassword()));
        });
    }
}
