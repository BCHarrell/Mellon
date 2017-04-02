package mellon;

import static javafx.geometry.Pos.*;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;

/**
 * The login page is initially called by MellonFramework.  This UI accepts
 * a username and password combination and verifies the information when
 * submit is selected.  The user can also choose to create a new account via
 * the Sign Up button.
 * 
 * @author Brent H.
 */
public class LoginPage extends VBox {

    private final MellonFramework FRAMEWORK;
    private final ImageView LOGO = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/mellon_logo_large.png")));

    public LoginPage(MellonFramework fw) {
        FRAMEWORK = fw;
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        this.setMaxSize(350, 450);
        this.setAlignment(CENTER);
        this.setSpacing(45);
        
        //Vertical box housing all of the input elements
        VBox vb = new VBox();
        vb.setAlignment(CENTER);
        vb.setSpacing(15);
        TextField username = new TextField();
        username.setMaxWidth(300);
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setMaxWidth(300);
        password.setPromptText("Password");
        
        //Horizontal box housing the buttons
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button login = new Button("Log In");
        Button signUp = new Button("Sign Up");
        
        //Add the items to appropriate containers
        hb.getChildren().addAll(login, signUp);
        vb.getChildren().addAll(username, password, hb);
        this.getChildren().addAll(LOGO, vb);

        /*****************
         *EVENT LISTENERS*
         *****************/
        //Pressing enter in the password field submits
        password.setOnKeyPressed(e -> {
           if (e.getCode().equals(KeyCode.ENTER)) { 
               login.fireEvent(new ActionEvent());
           }
        });
        
        username.setOnKeyPressed (e ->{
            if (e.getCode().equals(KeyCode.ENTER)) { 
                   login.fireEvent(new ActionEvent());
            }
        });
        
        //Login
        login.setOnAction(e -> {
            // If either field is empty
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                // Pop-up a message displaying to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Username or Password");
                alert.setContentText("Please ensure the Username and Password "
                        + "fields are filled in.");
                alert.showAndWait();
            } else {
                try {
                    MasterAccount user = new MasterAccount(username.getText(),
                                                password.getText());

                    if (user.getAuthenticated()) {
                        UserInfoSingleton.getInstance().updateMasterAccount(user);
                        // Go to the Main Menu page
                        FRAMEWORK.getScene()
                                .setRoot(new MenuContainer(FRAMEWORK));
                    } else {
                        // Pop-up a message displaying to the user
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Username or Password");
                        alert.setContentText("Incorrect Username or Password."
                                + " Please try again.");
                        alert.showAndWait();
                    }
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();

                }
            }

        });

        // BUTTON - Goes to sign up page
        signUp.setOnAction(e -> FRAMEWORK.getScene()
                .setRoot(new SignUpPage(FRAMEWORK, this)));

    }
}
