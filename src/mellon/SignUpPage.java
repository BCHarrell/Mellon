package mellon;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
    
    //Declared here so they are reachable across methods
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private PasswordField verify = new PasswordField();
    private Button submit;
    private String notificationText = "";
    private boolean meetsRequirements = false;
    
    //Text objects for notification window
    Text symbText, numText, lowerText, upperText, lengthText;
    
    //Regex patterns to verify password
    private final Pattern SYMBOL = Pattern.compile("[^a-zA-Z\\d\\s]");
    private final Pattern NUMBER = Pattern.compile("\\d");
    private final Pattern LOWER = Pattern.compile("[a-z]");
    private final Pattern UPPER = Pattern.compile("[A-Z]");

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
        submit = new Button("Create Account");
        submit.getStyleClass().add("blue-button-small");
        submit.setDisable(true);
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
        
        //Password requirements pane
        AnchorPane requirements = getRequirementPane();
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        //Tries to submit the information
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
        
        //Returns to login screen
        back.setOnAction(e -> {
            CONTAINER.requestMenuChange(LOGIN);
        });
        
        //Highlights the verify box if it does not match the password box
        verify.setOnKeyReleased(e -> {
            verify();
        });
        
        //Show the password requirements on focus
        password.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean>
                    observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    if(!meetsRequirements) {
                        if(!CONTAINER.getChildren().contains(requirements)){
                            CONTAINER.getChildren().add(requirements);
                        }
                        password.setStyle("-fx-background-color: "
                                        + "rgba(212, 170, 0, .5)");
                    }
                }
            }
        });
        
        //Verify password meets requirement in real time
        password.setOnKeyReleased(e -> {
            
            //Check if all the requirements are met
            meetsRequirements = updateRequirements();
            
            //If the verify field is not empty and the password field changes
            //Remove verification status
            if (!verify.getText().isEmpty()) {
                verify();
            }
            
            //If yes, remove the requirements window and change the color
            if(meetsRequirements){
                password.setStyle("-fx-background-color: "
                                    + "rgba(0, 136, 170, .5);");
                CONTAINER.getChildren().remove(requirements);
            } else {
                //redisplay requirements if the user changes the password
                //after meeting the requirements
                if(!CONTAINER.getChildren().contains(requirements)){
                    CONTAINER.getChildren().add(requirements);
                }
                submit.setDisable(true);
                password.setStyle("-fx-background-color: rgba(212, 170, 0, .5);");
            }
        });
    }
    
    /**
     * Verifies the password entries match
     */
    private void verify(){
        if(!verify.getText().equals(password.getText())){
            verify.setStyle("-fx-background-color: rgba(212, 170, 0, .5);");
            submit.setDisable(true);
        } else {
            verify.setStyle("-fx-background-color: rgba(0, 136, 170, .5);");
            if(meetsRequirements && !username.getText().isEmpty()){
                submit.setDisable(false);
            }
        }
    }
    
    /**
     * Dynamically updates the requirements pane as the user inputs the
     * password.  Once all requirements are met, returns true. Otherwise, returns
     * false
     * @return true if requirements are met, false if not 
     */
    private boolean updateRequirements(){
        boolean symbol = false, number = false, lower = false,
                    upper = false, length = false;
        
        //Check for symbol
        if(SYMBOL.matcher(password.getText()).find()) {
            symbol = true;
            symbText.getStyleClass().add("met-requirement");
        } else {
            symbText.getStyleClass().removeAll("met-requirement");
            symbText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for number
        if(NUMBER.matcher(password.getText()).find()) {
            number = true;
            numText.getStyleClass().add("met-requirement");
        } else {
            numText.getStyleClass().removeAll("met-requirement");
            numText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for lower case
        if(LOWER.matcher(password.getText()).find()) {
            lower = true;
            lowerText.getStyleClass().add("met-requirement");
        } else {
            lowerText.getStyleClass().removeAll("met-requirement");
            lowerText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for upper case
        if(UPPER.matcher(password.getText()).find()) {
            upper = true;
            upperText.getStyleClass().add("met-requirement");
        } else {
            upperText.getStyleClass().removeAll("met-requirement");
            upperText.getStyleClass().add("unmet-requirement");
        }
        
        //Check for 12 characters
        if(password.getText().length() >= 12) {
            length = true;
            lengthText.getStyleClass().add("met-requirement");
        } else {
            lengthText.getStyleClass().removeAll("met-requirement");
            lengthText.getStyleClass().add("unmet-requirement");
        }
        
        //If all reqs are met, return true
        if(symbol && number && lower && upper && length){
            return true;
        }
            
        return false;
    }
    
    /**
     * Creates a small window anchored to the bottom which informs the user
     * of the remaining password requirements
     * @return 
     */
    private AnchorPane getRequirementPane(){
        AnchorPane anch = new AnchorPane();
        anch.setPickOnBounds(false);
        
        HBox reqsBox = new HBox();
        reqsBox.setSpacing(10);
        reqsBox.setAlignment(Pos.CENTER);
        reqsBox.setPrefSize(140, 30);
        reqsBox.setPadding(new Insets(5, 15, 5, 15));
        reqsBox.getStyleClass().add("grey-container");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        symbText = new Text("Symbol");
        symbText.getStyleClass().add("unmet-requirement");
        numText = new Text("Number");
        numText.getStyleClass().add("unmet-requirement");
        lowerText = new Text("Lower case");
        lowerText.getStyleClass().add("unmet-requirement");
        upperText = new Text("Upper case");
        upperText.getStyleClass().add("unmet-requirement");
        
        grid.add(symbText, 0, 0);
        grid.add(numText, 1, 0);
        grid.add(lowerText, 0, 1);
        grid.add(upperText, 1, 1);
        
        lengthText = new Text("12 Characters");
        lengthText.getStyleClass().add("unmet-requirement");
        
        reqsBox.getChildren().addAll(grid, lengthText);
        
        anch.getChildren().add(reqsBox);
        AnchorPane.setBottomAnchor(reqsBox, 15.0);
        AnchorPane.setLeftAnchor(reqsBox, 131.0);
        
        return anch;
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
    
    
    /**
     * Gets the user input, checks if the username is already taken
     * @param username the entered username
     * @param pass the entered password
     * @throws Exception thrown if account creation fails or the username is
     *                   taken, triggers the failed code to display the right
     *                   notification.
     */
    private void getUserInput(String username, String pass) throws Exception {
        String usernameHash;
        String passwordHash;
        boolean exists = false;
        boolean registered = false;
        try {
            // Check if user with the same username already exist in the database
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
