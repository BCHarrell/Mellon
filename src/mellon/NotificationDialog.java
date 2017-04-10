
package mellon;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * Custom dialog to notify users of errors or other items (e.g. logout due to
 * timing out).
 * @author Brent H.
 */
public class NotificationDialog extends BorderPane{

    private String message;
    private final MenuContainer CONTAINER;
    private final ExternalContainer E_CONTAINER;
    private final ImageView ALERT = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/alert_icon.png")));
    
    /**
     * Constructor once logged in
     * @param c the menu container
     * @param message the message to display
     */
    public NotificationDialog (MenuContainer c, String message){
        CONTAINER = c;
        E_CONTAINER = null;
        this.message = message;
        createDialog();
    }
    
    /**
     * Constructor once logged out
     * @param c the menu container
     * @param message the message to display
     */
    public NotificationDialog (ExternalContainer c, String message){
        CONTAINER = null;
        E_CONTAINER = c;
        this.message = message;
        createDialog();
    }
    
    /**
     * Creates the dialog
     */
    private void createDialog(){
        this.setMaxSize(375, 175);
        this.getStyleClass().add("notification-dialog-box");
        
        BorderPane messageArea = new BorderPane();
        messageArea.setPrefSize(375, 125);
        
        //Holds the notification icon
        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.getChildren().add(ALERT);
        
        //Holds the dialog message
        VBox textBox = new VBox();
        textBox.setAlignment(Pos.CENTER);
        Text messageText = new Text(message);
        messageText.getStyleClass().add("dialog-text");
        messageText.setWrappingWidth(250);
        textBox.getChildren().add(messageText);
        messageArea.setLeft(iconBox);
        messageArea.setRight(textBox);
        
        //Holds the acknowledgement button
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button acknowledge = new Button("Got it");
        acknowledge.getStyleClass().add("blue-button-small");
        buttonBox.getChildren().add(acknowledge);
        
        this.setCenter(messageArea);
        this.setBottom(buttonBox);
        addDropShadow();
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        if (CONTAINER != null){
            acknowledge.setOnAction(e -> CONTAINER.closeDialog(this));
        } else {
            acknowledge.setOnAction(e -> E_CONTAINER.closeDialog(this));
        }
        
    }
    
    /**
     * Adds the drop shadow to the borderpane
     */
    private void addDropShadow(){
        DropShadow ds = new DropShadow();
        ds.setOffsetY(5);
        ds.setColor(Color.BLACK);
        this.setEffect(ds);
    }
}
