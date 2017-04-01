
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
        ArrayList<WebAccount> accounts = CONTAINER.getUser()
                                            .getUserAccounts();
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
            
            TextField username = new TextField();
            username.setEditable(false);
            username.setPrefWidth(250);
            username.setText(a.getUsername());
            
            HBox hb = new HBox();
            TextField password = new TextField();
            password.setEditable(false);
            password.setPrefWidth(400);
            password.setText(a.getPassword());
            //Insert BLUR
            Button visible = new Button();
            //Insert Action Listener
            visible.setGraphic(EYE_ICON);
            visible.setBackground(Background.EMPTY);
            hb.getChildren().addAll(password, visible);
            
            vb.getChildren().addAll(username, hb);
            
            p.setContent(vb);
            this.getChildren().add(p);
        }
    }
    
}
