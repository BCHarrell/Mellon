
package mellon;

import static javafx.geometry.Pos.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Brent H.
 */
public class LoginPage extends BorderPane{
    
    MellonGUI parent;
    ImageView logo = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/templogo.png")));
    
    public LoginPage(MellonGUI p){
        parent = p;
        addItems();
    };
    
    private void addItems(){
        VBox vb = new VBox();
        vb.setMaxSize(350, 400);
        vb.setAlignment(CENTER);
        
        vb.setSpacing(15);
        
        TextField username = new TextField();
        username.setMaxWidth(300);
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setMaxWidth(300);
        password.setPromptText("Password");
        
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button login = new Button("Log In");
        Button signUp = new Button("Sign Up");
        hb.getChildren().addAll(login, signUp);
        
        vb.getChildren().addAll(logo, username, password, hb);
        this.setCenter(vb);

        // Doing this just to test...
        login.setOnAction(e -> {
            try {
                UserAuth user = new UserAuth(username.getText(), password.getText());
                System.out.println(user.getUsernameEncrypted());
                System.out.println(user.getPasswordEncrypted());
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();

            }

        });
    }    
}
