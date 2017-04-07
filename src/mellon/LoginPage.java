package mellon;

import static javafx.geometry.Pos.*;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.security.NoSuchAlgorithmException;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    private final ExternalContainer CONTAINER;
    private final ImageView LOGO = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/mellon_logo_large.png")));

    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private String notificationText = "";

    public LoginPage(MellonFramework fw, ExternalContainer ec) {
        FRAMEWORK = fw;
        CONTAINER = ec;
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
        username = new TextField();
        username.setMaxWidth(300);
        username.setPromptText("Username");
        password = new PasswordField();
        password.setMaxWidth(300);
        password.setPromptText("Password");

        //Horizontal box housing the buttons
        HBox hb = new HBox();
        hb.setAlignment(CENTER);
        hb.setSpacing(15);
        Button login = new Button("Log In");
        Button signUp = new Button("Sign Up");

        //Horizontal box for progress indicator
        HBox authenticationBox = new HBox();
        authenticationBox.setAlignment(CENTER);
        authenticationBox.setPrefHeight(25);
        authenticationBox.setSpacing(10);
        ProgressIndicator prog = new ProgressIndicator(-1.0f);
        Text authenticating = new Text("Authenticating...");
        authenticationBox.getChildren().addAll(prog, authenticating);
        authenticationBox.setVisible(false);

        //Notification to user for failure
        Text notification = new Text();
        notification.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        notification.setStyle("-fx-fill: red;");

        //Add the items to appropriate containers
        hb.getChildren().addAll(login, signUp);
        vb.getChildren().addAll(username, password, hb);
        this.getChildren().addAll(LOGO, vb, authenticationBox);

        /*****************
         *EVENT LISTENERS*
         *****************/
        //Pressing enter in the password field submits
        password.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                login.fireEvent(new ActionEvent());
            }
        });

        username.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                login.fireEvent(new ActionEvent());
            }
        });

        //Login
        login.setOnAction(e -> {
            authenticationBox.getChildren().setAll(prog, authenticating);
            authenticationBox.setVisible(true);

            Task authenticate = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    login();
                    return null;
                }
            };
            authenticate.setOnSucceeded(a -> {
                transition();
            });

            authenticate.setOnFailed(a -> {
                notification.setText(notificationText);
                authenticationBox.getChildren().setAll(notification);
            });

            new Thread(authenticate).start();
        });


        // BUTTON - Goes to sign up page
        signUp.setOnAction(e -> CONTAINER
                .requestMenuChange(new SignUpPage(CONTAINER, this)));

    }

    private void transition() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), this);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            FRAMEWORK.getScene().setRoot(new MenuContainer(FRAMEWORK));
        });
        ft.play();
    }

    /**
     * Performs the login authentication
     */
    private void login() throws Exception {
        // If either field is empty
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            notificationText = "Please fill in all fields";
            throw new Exception();
        } else {
            if (UserInfoSingleton.getInstance().init(username.getText(), password.getText())) {
                username.clear();
                password.clear();
            } else {
                throw new Exception();
            }
        }
    }
}
