package mellon;

import java.security.NoSuchAlgorithmException;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.geometry.*;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;

/**
 * This class creates the UI for master account creation.  It contains three
 * text fields for username, and password with verification.  An alert is shown
 * if the form is not correctly filled out, and returns to the main menu
 * when the back button or successful account creation are reached.
 * @author Brent H.
 */
public class SignUpPage extends VBox {

    private final ExternalContainer CONTAINER;
    private final LoginPage LOGIN;
    private final ImageView LOGO = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/mellon_logo_large.png")));
    
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private PasswordField verify = new PasswordField();
    private String notificationText = "";

    //Accepts the primary class to get the scene, login page to return
    //in case the user clicked sign up by accident, keeps all text entered
    public SignUpPage(ExternalContainer c, LoginPage l) {
        CONTAINER = c;
        LOGIN = l;
        addItems();
    }
    
    /**
     * Creates the UI elements
     */
    private void addItems() {
        //Primary VBox
        this.setMaxSize(350, 450);
        this.setAlignment(CENTER);
        this.setSpacing(45);
        
        //Input fields area
        //VBox for the fields and buttons
        VBox vb = new VBox();
        vb.setSpacing(15);
        vb.setAlignment(CENTER);
        
        //Fields
        username = new TextField();
        username.setMaxWidth(300);
        username.setPromptText("Username");
        password = new PasswordField();
        password.setMaxWidth(300);
        password.setPromptText("Password");
        verify = new PasswordField();
        verify.setMaxWidth(300);
        verify.setPromptText("Enter your password again");
        
        //Submit and back buttons
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button back = new Button("Back to Login");
        back.getStyleClass().add("blue-button-small");
        Button submit = new Button("Create Account");
        submit.getStyleClass().add("blue-button-small");
        hb.getChildren().addAll(back, submit);
        
        //progress indicator
        HBox authenticationBox = new HBox();
        authenticationBox.setAlignment(CENTER);
        authenticationBox.setPrefHeight(25);
        authenticationBox.setSpacing(10);
        
        //Indeterminate indicator
        ProgressIndicator prog = new ProgressIndicator(-1.0f);
        Text authenticating = new Text("Authenticating...");
        authenticationBox.getChildren().addAll(prog, authenticating);
        authenticationBox.setVisible(false);
        
        //Notification text for failures
        Text notification = new Text();
        notification.getStyleClass().add("error-notification");
        
        vb.getChildren().addAll(username, password, verify, hb,
                                    authenticationBox);

        this.getChildren().addAll(LOGO, vb);
        
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        //Highlights the verify box if it does not match the password box
        verify.setOnKeyReleased(e -> {
            if(!verify.getText().equals(password.getText())){
                verify.setStyle("-fx-background-color: rgba(255,0,0,.5);");
            } else {
                verify.setStyle(null);
            }
        });
        
        back.setOnAction(e -> {
            CONTAINER.requestMenuChange(LOGIN);
        });
        
        submit.setOnAction(e -> {
            authenticationBox.getChildren().setAll(prog, authenticating);
            authenticationBox.setVisible(true);
            
            Task authenticate = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    submit(username.getText(), password.getText(),
                            verify.getText());
                    return null;
                }
            };
            authenticate.setOnSucceeded(a -> {
                authenticationBox.setVisible(false);
                displaySuccess();
            });
            
            authenticate.setOnFailed(b -> {
                notification.setText(notificationText);
                authenticationBox.getChildren().setAll(notification);
            });
            
            new Thread(authenticate).start(); 
        });
    }
    
    /**
     * Displays a notification that the account was successfully created, then
     * transitions to login screen after brief delay
     */
    private void displaySuccess(){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox notification = new HBox();
        notification.setAlignment(Pos.CENTER);
        notification.setPrefSize(125, 30);
        notification.setPadding(new Insets(5, 15, 5, 15));
        notification.setStyle("-fx-background-color: rgba(75, 75, 75, 0.9);");
        
        Text message = new Text("Account Created");
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        message.setStyle("-fx-fill: #FFFFFF;");
        notification.getChildren().add(message);
        
        anch.getChildren().add(notification);
        AnchorPane.setBottomAnchor(notification, 30.0);
        AnchorPane.setLeftAnchor(notification, 186.0);
        
        CONTAINER.getChildren().add(anch);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1500), anch);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(1500));
        ft.setOnFinished(e -> {
            CONTAINER.getChildren().remove(anch);
            CONTAINER.requestMenuChange(LOGIN);
        });
        ft.play();
    }
    
    /**
     * Performs the operation of submitting a new account
     * @param username the submitted username
     * @param password the submitted password
     * @param verify the repeated password
     */
    private void submit(String username, String password, String verify) 
                                    throws Exception {
        // Store input into local variables
        String inputUsername = username;
        String inputPassword = password;
        String inputVerify = verify;
        boolean verificationResult = false;
        // Check user input
        verificationResult = verifyInput(inputUsername, inputPassword, inputVerify);
        if (verificationResult) {
            // get user input and store them into variables to perform encryption
            getUserInput(inputUsername, inputPassword);
        } else {
            notificationText = "Please fill in all fields";
            throw new Exception();
        }
    }
    
    /**
     * Verifies the inputed information
     * @param inputUsername the inputted username
     * @param inputPassword the inputted password
     * @param inputVerify password verification
     * @return 
     */
    private boolean verifyInput(String inputUsername, String inputPassword,
            String inputVerify) throws Exception {

        boolean result = false;
        // if fields are empty, don't process anything (display error until user
        //enter something)
        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            notificationText = "Please fill in all fields";
            throw new Exception();
        } else {
            // Make sure that password = verify
            if (inputPassword.equals(inputVerify)) {
                result = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText("");
                alert.setContentText("Please ensure to enter the same password");
                alert.showAndWait();
                result = false;
            }
        }
        return result;
    }

    private void getUserInput(String username, String pass) throws Exception {
        String usernameHash;
        String passwordHash;
        boolean exists = false;
        boolean registered = false;
        try {
            // Check if user with the same username already exist in the database
//            MasterAccount user = new MasterAccount(username, pass);
            UserInfoSingleton.getInstance().setUpNewUser(username, pass);
            usernameHash = UserInfoSingleton.getInstance().getUsernameHash();
            passwordHash = UserInfoSingleton.getInstance().getPasswordHash();
            exists = DBConnect.checkUser(usernameHash);
            // if the user exists, display error message
            if (exists) {
                notificationText = "Username already taken";
                throw new Exception();
            } else {
                registered = DBConnect.registerUser(usernameHash, passwordHash);
                if (!registered) {
                    notificationText = "Account creation failed.";
                    throw new Exception();
                }
            }
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
}
