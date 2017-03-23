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
public class LoginPage extends VBox {

    MellonFramework parent;
    ImageView logo = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/templogo.png")));

    public LoginPage(MellonFramework p) {
        parent = p;
        addItems();
    }

    private void addItems() {
        this.setMaxSize(350, 450);
        this.setAlignment(CENTER);
        this.setSpacing(45);

        VBox vb = new VBox();
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
        vb.getChildren().addAll(username, password, hb);
        this.getChildren().addAll(logo, vb);
        
        /*I don't think these two lines are necessary but I left them
        in case I'm missing something.  GUI should run without this*/
        //vb.managedProperty().bind(login.visibleProperty());
        //vb.setVisible(true);

        //Event Listeners
        // BUTTON - login
        login.setOnAction(e -> {
            // If either field is empty
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                // Pop-up a message displaying to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Username or Password");
                alert.setContentText("Please ensure the Username and Password fields are filled in.");
                alert.showAndWait();
            } else {
                try {
                    UserAuth user = new UserAuth(username.getText(), password.getText());
                    // Don't forget to get rid of these some day...
                    System.out.println(user.getUsernameEncrypted());
                    System.out.println(user.getPasswordEncrypted());
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();

                }
            }

        });

        // BUTTON - Goes to sign up page
        signUp.setOnAction(e -> parent.getScene().setRoot(new SignUpPage(parent, this)));

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
}