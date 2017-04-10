
package mellon;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

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
    private BorderPane bar;
    
    public NavBar(MenuContainer cont){
        CONTAINER = cont;
        this.setTop(new WindowControl(CONTAINER));
        bar = createBar();
        bar.setOpacity(0);
        this.setCenter(bar);
        fadeIn();
    }
    
    /**
     * Creates the UI elements
     */
    private BorderPane createBar(){
        BorderPane bar = new BorderPane();
        bar.setPadding(new Insets(0, 15.0, 15.0, 15.0));
        bar.getStyleClass().add("blue-container");
        bar.setPickOnBounds(true);
        
        //Temporary to come in with fade
        DropShadow ds = new DropShadow();
        ds.setOffsetY(5.0);
        ds.setColor(javafx.scene.paint.Color.GRAY);
        bar.setEffect(ds);
        
        //Centers the logo on the left
        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.getChildren().add(LOGO);
        
        //Horizontal box for the icon elements
        HBox icons = new HBox();
        icons.setSpacing(10);
        icons.setAlignment(Pos.CENTER_RIGHT);
        
        //Home
        Button homeBtn = new Button();
        homeBtn.setGraphic(HOME);
        homeBtn.getStyleClass().add("icon");
        homeBtn.setTooltip(new Tooltip("Main Menu"));
        
        //Add
        Button addBtn = new Button();
        addBtn.setGraphic(ADD);
        addBtn.getStyleClass().add("icon");
        addBtn.setTooltip(new Tooltip("Create a Profile"));
        
        //Settings
        Button settingsBtn = new Button();
        settingsBtn.setGraphic(SETTINGS);
        settingsBtn.getStyleClass().add("icon");
        settingsBtn.setTooltip(new Tooltip("Account Settings"));
        
        //Help
        Button helpBtn = new Button();
        helpBtn.setGraphic(HELP);
        helpBtn.getStyleClass().add("icon");
        helpBtn.setTooltip(new Tooltip("How to use Mellon"));
        
        //Logout
        Button logoutBtn = new Button();
        logoutBtn.setGraphic(LOGOUT);
        logoutBtn.getStyleClass().add("icon");
        logoutBtn.setTooltip(new Tooltip("Logout"));
        
        
        icons.getChildren().addAll(homeBtn, addBtn, settingsBtn,
                                        helpBtn, logoutBtn);
        
        //Close/Minimize bar to get rid of the window
        
        
        bar.setLeft(logoBox);
        bar.setRight(icons);
        /*****************
         *EVENT LISTENERS*
         *****************/
        homeBtn.setOnAction(e -> CONTAINER
                .requestMenuChange(CONTAINER.getMain()));
        
        addBtn.setOnAction(e -> CONTAINER
                .requestMenuChange(new CreationPage(CONTAINER)));
        
        helpBtn.setOnAction(e -> CONTAINER
                .requestMenuChange(new HelpPage(CONTAINER)));
        
        settingsBtn.setOnAction(e -> CONTAINER.displaySettings());
        
        logoutBtn.setOnAction(e -> {
            final ConfirmDialog confirm = new ConfirmDialog(CONTAINER,
               "Are you sure you want to log out?");
               CONTAINER.showDialog(confirm);
               
               Task <Void> task = new Task(){
                   @Override
                   protected Object call() throws Exception {
                       synchronized(confirm){
                           while(!confirm.isClosed()){
                               try{
                                   confirm.wait();
                               } catch (InterruptedException ex){}
                           }
                       }
                       return null;
                   }  
               };
               task.setOnSucceeded(a ->{
                   if(confirm.isConfirmed()){
                       CONTAINER.logout();
                       UserInfoSingleton.getInstance().logout();
                   }
               });
               new Thread(task).start();
        });
        
        return bar;
    }
    
    private void addDropShadow(){
        DropShadow ds = new DropShadow();
        ds.setOffsetY(5.0);
        ds.setColor(javafx.scene.paint.Color.GRAY);
        this.setEffect(ds);
    }
    
    private void fadeIn(){
        FadeTransition ft = new FadeTransition(Duration.millis(500), bar);
        ft.setDelay(Duration.millis(250));
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setOnFinished(e -> {
            addDropShadow();
            bar.setEffect(null);
        });
        ft.play();
    }
    
}
