
package mellon;

import static javafx.geometry.Pos.CENTER;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Brent H.
 */
public class SignUpPage extends VBox{
    MellonFramework parent;
    LoginPage login;
    ImageView logo = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/templogo.png")));
    
    //Accepts the primary class to get the scene, login page to return
    //in case the user clicked sign up by accident, keeps all text entered
    public SignUpPage(MellonFramework p, LoginPage l){
        parent = p;
        login = l;
        addItems();
    };
    
    private void addItems(){
        //Primary VBox
        this.setMaxSize(350, 450);
        this.setAlignment(CENTER);
        this.setSpacing(45);
        
        //VBox for the fields and buttons
        VBox vb = new VBox();
        vb.setSpacing(15);
        vb.setAlignment(CENTER);
        
        TextField username = new TextField();
        username.setMaxWidth(300);
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setMaxWidth(300);
        password.setPromptText("Password");
        PasswordField verify = new PasswordField();
        verify.setMaxWidth(300);
        verify.setPromptText("Enter your password again");
        
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button back = new Button("Back to Login");
        //Returns to the login screen which kept any text entered
        back.setOnAction(e -> {
            parent.getScene().setRoot(login);
        });
        Button submit = new Button("Create Account");
        hb.getChildren().addAll(back, submit);
        vb.getChildren().addAll(username, password, verify, hb);
        
        this.getChildren().addAll(logo, vb);
    }  
}
