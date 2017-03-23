package mellon;

import static javafx.geometry.Pos.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;

/**
 *
 * @author Brent H.
 */
public class LoginPage extends BorderPane {

    MellonGUI parent;
    ImageView logo = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/templogo.png")));

    public LoginPage(MellonGUI p) {
        parent = p;
        addItems();
    }

    ;
    
    private void addItems() {
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
        Text scenetitle = new Text("Welcome to Mellon, Please login to continue");
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button login = new Button("Log In");
        Button signUp = new Button("Sign Up");
        hb.getChildren().addAll(login, signUp);

        vb.getChildren().addAll(logo,scenetitle,username, password, hb);
        this.setCenter(vb);
      
        vb.managedProperty().bind(login.visibleProperty());
        vb.setVisible(true);


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
        
// Testing DBConnect and creating MellonUser object " uncomment to perform test"
//    login.setOnAction(e -> {
//      // Testing DB connect 
//       vb.setVisible(false);
//       MellonUser currentUser = new MellonUser(username.getText(), password.getText());
//       MellonUser returnedUser = DBConnect.getCredintials(currentUser.getUsername());
//       VBox vb2 = new VBox();
//       vb2.setMaxSize(150, 150);
//       vb2.setAlignment(CENTER);
//       vb2.setSpacing(10);
//       Text scenetitle2 = new Text("WELCOME: "  + returnedUser.getUsername());
//       vb2.getChildren().addAll(scenetitle2);
//       this.setCenter(vb2); 
//       vb2.managedProperty();
//       vb2.setVisible(true);
//        });
//    }


            }
