
package mellon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * The navigation bar remains visible once the user is logged in and can be
 * used to create a new profile, get account settings, get help, return to the
 * main menu, or log out.
 * 
 * @author Brent H.
 */
public class NavBar extends BorderPane{
    
    private final ImageView ADD = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/add_icon.png")));
    private final ImageView HELP = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/help_icon.png")));
    private final ImageView SETTINGS = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/settings_icon.png")));
    private final ImageView HOME = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/home_icon.png")));
    private final ImageView LOGOUT = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/logout_icon.png")));
    private final ImageView LOGO = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/mellon_logo_small.png")));
    
    private final MenuContainer CONTAINER;
    private final MainMenu MAIN;
    private final MellonFramework FRAMEWORK;
    
    public NavBar(MellonFramework fw, MenuContainer cont, MainMenu m){
        FRAMEWORK = fw;
        CONTAINER = cont;
        MAIN = m;
        createBar();
    }
    
    /**
     * Creates the UI elements
     */
    private void createBar(){

        this.setPadding(new Insets(15.0, 15.0, 15.0, 15.0));
        this.setStyle("-fx-background-color: #0088AA");
        
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(javafx.scene.paint.Color.GRAY);
        this.setEffect(ds);
        
        //Centers the logo on the left
        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.getChildren().add(LOGO);
        
        //Horizontal box for the icon elements
        HBox icons = new HBox();
        icons.setSpacing(10);
        icons.setStyle("-fx-background-color: #0088AA");
        icons.setAlignment(Pos.CENTER_RIGHT);
        
        //Home
        Button homeBtn = new Button();
        homeBtn.setGraphic(HOME);
        homeBtn.setBackground(Background.EMPTY);
        homeBtn.setTooltip(new Tooltip("Main Menu"));
        
        //Add
        Button addBtn = new Button();
        addBtn.setGraphic(ADD);
        addBtn.setBackground(Background.EMPTY);
        addBtn.setTooltip(new Tooltip("Create a Profile"));
        
        //Settings
        Button settingsBtn = new Button();
        settingsBtn.setGraphic(SETTINGS);
        settingsBtn.setBackground(Background.EMPTY);
        settingsBtn.setTooltip(new Tooltip("Account Settings"));
        
        //Help
        Button helpBtn = new Button();
        helpBtn.setGraphic(HELP);
        helpBtn.setBackground(Background.EMPTY);
        helpBtn.setTooltip(new Tooltip("How to use Mellon"));
        
        //Logout
        Button logoutBtn = new Button();
        logoutBtn.setGraphic(LOGOUT);
        logoutBtn.setBackground(Background.EMPTY);
        logoutBtn.setTooltip(new Tooltip("Logout"));
        
        
        icons.getChildren().addAll(homeBtn, addBtn, settingsBtn,
                                        helpBtn, logoutBtn);
        
        this.setLeft(logoBox);
        this.setRight(icons);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        homeBtn.setOnAction(e -> CONTAINER.setCenter(MAIN));
        
        addBtn.setOnAction(e -> CONTAINER
                    .setCenter(new CreationPage(CONTAINER)));
        
        helpBtn.setOnAction(e -> CONTAINER
                    .setCenter(new HelpPage(CONTAINER)));
        
        settingsBtn.setOnAction(e -> CONTAINER
                    .setCenter(new SettingsMenu(CONTAINER)));
        
        logoutBtn.setOnAction(e -> {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure"
                    + " you want to log out?", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Logout");
            
            confirm.showAndWait()
                    .filter(response -> response == ButtonType.YES)
                    .ifPresent(response -> {
                        CONTAINER.logout();
                        UserInfoSingleton.getInstance().logout();
                    });
        });
    }
    
}
