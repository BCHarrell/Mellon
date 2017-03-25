package mellon;

import java.security.NoSuchAlgorithmException;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.control.Alert;
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
public class SignUpPage extends VBox {

    MellonFramework parent;
    LoginPage login;
    ImageView logo = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/templogo.png")));

    //Accepts the primary class to get the scene, login page to return
    //in case the user clicked sign up by accident, keeps all text entered
    public SignUpPage(MellonFramework p, LoginPage l) {
        parent = p;
        login = l;
        addItems();
    }

    ;
    
    private void addItems() {
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

        submit.setOnAction(e -> {
            // Store input into local variables
            String inputUsername = username.getText();
            String inputPassword = password.getText();
            String inputVerify = verify.getText();
            boolean verificationResult = false;
            // Check user input
            verificationResult = verifyInput(inputUsername, inputPassword, inputVerify);
            if (verificationResult) {
                // get user input and store them into variables to perform encryption
                getUserInput(inputUsername, inputPassword);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Error");
                alert.setContentText("Please ensure the Username and Password fields are filled in and passwords match.");
                alert.showAndWait();
            }
        });
    }

    private boolean verifyInput(String inputUsername, String inputPassword, String inputVerify) {

        boolean result = false;
        // if fields are empty, don't process anything (display error until user enter something)
        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            // Pop-up a message displaying to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter Username or Password");
            alert.setContentText("Please ensure the Username and Password fields are filled in.");
            alert.showAndWait();
            result = false;
        } else {
            // Make sure that password = verify
            if (inputPassword.equals(inputVerify)) {
                result = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password Mismatch");
                alert.setContentText("Please ensure to enter the same password");
                alert.showAndWait();
                result = false;
            }
        }
        return result;
    }

    private void getUserInput(String username, String pass) {
        String usernameHash;
        String passwordHash;
        boolean exists = false;
        boolean registered = false;
        try {
            // Check if user with the same username already exist in the database
            UserAuth user = new UserAuth(username, pass);
            usernameHash = user.getUsernameHash();
            passwordHash = user.getPasswordHash();
            exists = DBConnect.checkUser(usernameHash, passwordHash);
            // if the user exists, display error message
            if (exists) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Account already exists");
                alert.setContentText("username is already taken, please try a different username or use your credintials to login.");
                alert.showAndWait();
            } else {
                registered = DBConnect.registerUser(usernameHash, passwordHash);
                if (registered) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Account created");
                    alert.setContentText("your account has been created");
                    alert.showAndWait();
                    parent.getScene().setRoot(login);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Account creation failed");
                    alert.setContentText("Your account creation failed, please contact system administrator. ");
                    alert.showAndWait();
                }
            }
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
}
