
package mellon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 *
 * @author Brent H.
 */
public class NavBar extends BorderPane{
    
    private final ImageView HELP = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/help_icon.png")));
    private final ImageView SETTINGS = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/settings_icon.png")));
    private final ImageView HOME = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/home_icon.png")));
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
    
    private void createBar(){

        this.setPadding(new Insets(15.0, 15.0, 15.0, 15.0));
        this.setStyle("-fx-background-color: #0088AA");
        
        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.getChildren().add(LOGO);
        
        HBox icons = new HBox();
        icons.setSpacing(15);
        icons.setStyle("-fx-background-color: #0088AA");
        icons.setAlignment(Pos.CENTER_RIGHT);
        
        //Home
        Button homeBtn = new Button();
        homeBtn.setGraphic(HOME);
        homeBtn.setBackground(Background.EMPTY);
        
        //Settings
        Button settingsBtn = new Button();
        settingsBtn.setGraphic(SETTINGS);
        settingsBtn.setBackground(Background.EMPTY);
        
        //Help
        Button helpBtn = new Button();
        helpBtn.setGraphic(HELP);
        helpBtn.setBackground(Background.EMPTY);
        
        icons.getChildren().addAll(homeBtn, settingsBtn, helpBtn);
        
        this.setLeft(logoBox);
        this.setRight(icons);
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        homeBtn.setOnAction(e -> CONTAINER.setCenter(MAIN));
        
        helpBtn.setOnAction(e -> CONTAINER
                    .setCenter(new HelpPage(CONTAINER)));
        
        settingsBtn.setOnAction(e -> CONTAINER
                    .setCenter(new SettingsMenu(CONTAINER)));
    }
    
}
