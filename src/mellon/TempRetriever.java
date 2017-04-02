
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
    private final ImageView EYE_ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/eye_icon.png")));
    
    public TempRetriever(MenuContainer c){
        CONTAINER = c;
        addItems();
    }
    
    private void addItems(){
        // Couldn't get grabbing accounts from the CONTAINER to work.
//        ArrayList<WebAccount> accounts = CONTAINER.getUser().getUserAccounts();
        ArrayList<WebAccount> accounts = UserInfoSingleton.getInstance().getProfiles();
        System.out.println("accounts: " + accounts.size());
        for (WebAccount x : accounts){
            System.out.println(x.getAccountName());
            System.out.println(x.getUsername());
            System.out.println(x.getPassword());
        }
        
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
            userVB.getChildren().addAll(userLabel, username);
            
            VBox passVB = new VBox();
            passVB.setSpacing(5);
            Label passLabel = new Label("Password:");
            HBox hb = new HBox();
            hb.setSpacing(5);
            TextField password = new TextField();
            password.setEditable(false);
            password.setMaxWidth(400);
            password.setText(a.getPassword());
            //Insert BLUR
            Button visible = new Button();
            //Insert Action Listener
            visible.setGraphic(EYE_ICON);
            //visible.setBackground(Background.EMPTY);
            hb.getChildren().addAll(password, visible);
            passVB.getChildren().addAll(passLabel, hb);
            
            vb.getChildren().addAll(userVB, passVB);
            
            p.setContent(vb);
            this.getPanes().add(p);
        }
        
        TitledPane test = new TitledPane();
        test.setText("Test");
        //Button testBtn = new Button();
        //testBtn.setGraphic(EYE_ICON);
        test.setContent(EYE_ICON);
    }
    
}
