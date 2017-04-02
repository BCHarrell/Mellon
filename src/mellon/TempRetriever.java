
package mellon;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/**
 *
 * @author Brent H.
 */
public class TempRetriever extends Accordion {
    
    private final MenuContainer CONTAINER;
    private final Image EYE_ICON = new Image(getClass()
                    .getResourceAsStream("/resources/eye_icon.png"));
    /*private final ImageView EYE_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/eye_icon.png")));*/
    
    public TempRetriever(MenuContainer c){
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        // Couldn't get grabbing accounts from the CONTAINER to work.
//        ArrayList<WebAccount> accounts = CONTAINER.getUser().getUserAccounts();
        ArrayList<WebAccount> accounts = UserInfoSingleton.getInstance().getProfiles();
        for (WebAccount a : accounts) {
            TitledPane p = new TitledPane();
            p.setText(a.getAccountName());
            
            VBox vb = new VBox();
            vb.setSpacing(15);
            vb.setAlignment(Pos.CENTER_LEFT);
            
            VBox userVB = new VBox();
            userVB.setSpacing(5);
            Label userLabel = new Label("Username:");
            TextField username = new TextField();
            username.setEditable(false);
            username.setMaxWidth(175);
            username.setText(a.getUsername());
            username.setEditable(false);
            userVB.getChildren().addAll(userLabel, username);
            
            VBox passVB = new VBox();
            passVB.setSpacing(5);
            Label passLabel = new Label("Password:");
            
            HBox hb = new HBox();
            hb.setSpacing(5);
            hb.setMaxWidth(Double.MAX_VALUE);
            
            PasswordField password = new PasswordField();
            password.setEditable(false);
            password.setPrefWidth(350);
            password.setText(a.getPassword());
            password.setEditable(false);
            
            TextField passwordVis = new TextField();
            passwordVis.setEditable(false);
            passwordVis.setPrefWidth(350);
            passwordVis.setText(a.getPassword());
            passwordVis.setEditable(false);
            
            //Insert BLUR (Temporary: use password field
            Button visible = new Button();
            visible.setOnAction(e -> {
                if (hb.getChildren().contains(password)) {
                    hb.getChildren().setAll(passwordVis, visible);
                } else {
                    hb.getChildren().setAll(password, visible);
                }
            });
            ImageView eye = new ImageView(EYE_ICON);
            visible.setGraphic(eye);
            visible.setBackground(Background.EMPTY);
            
            hb.getChildren().addAll(password, visible);
            
            passVB.getChildren().addAll(passLabel, hb);
            
            Button edit = new Button("Edit Profile");
            edit.setOnAction(e -> {
                CONTAINER.setCenter(new CreationPage(CONTAINER,
                            a.getAccountName(), a.getUsername(),
                            a.getPassword()));
            });
            
            vb.getChildren().addAll(userVB, passVB, edit);
            
            p.setContent(vb);
            this.getPanes().add(p);
        }
    }
    
}
