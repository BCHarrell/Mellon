
package mellon;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * Custom confirmation dialog.  Must be used with a Task object in
 * the calling menu in order to retrieve the user's input.
 * 
 * @author Brent H.
 */
public class ConfirmDialog extends BorderPane{

    private String message;
    private final MenuContainer CONTAINER;
    private final ImageView ICON = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/confirm_icon.png")));
    boolean confirmed = false, closed = false;
    
    /**
     * Constructs a confirmation dialog.  Must be used with a Task object in
     * the calling menu in order to retrieve the user's input.
     * @param c the menu container
     * @param message the message to display
     */
    public ConfirmDialog(MenuContainer c, String message){
        CONTAINER = c;
        this.message = message;
        createDialog();
    }
    
    /**
     * Creates the dialog
     */
    private void createDialog(){
        //Replace with CSS
        this.setMaxSize(375, 175);
        this.getStyleClass().add("confirmation-dialog-box");
        
        BorderPane messageArea = new BorderPane();
        messageArea.setPrefSize(375, 125);
        
        //Holds the notification icon
        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.getChildren().add(ICON);
        
        //Holds the dialog message
        VBox textBox = new VBox();
        textBox.setAlignment(Pos.CENTER);
        Text messageText = new Text(message);
        messageText.getStyleClass().add("dialog-text");
        messageText.setWrappingWidth(250);
        textBox.getChildren().add(messageText);
        messageArea.setLeft(iconBox);
        messageArea.setRight(textBox);
        
        //Holds the yes/no buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button yes = new Button("Yes");
        yes.getStyleClass().add("blue-button-small");
        Button no = new Button("No");
        no.getStyleClass().add("blue-button-small");
        buttonBox.getChildren().addAll(yes, no);
        
        this.setCenter(messageArea);
        this.setBottom(buttonBox);
        addDropShadow();
        
        /*****************
         *EVENT LISTENERS*
         *****************/
        
        yes.setOnAction(e -> {
            confirmed = true;
            closed = true;
            CONTAINER.closeDialog(this);
            synchronized(this){
                notifyAll();
            }
        });
        
        no.setOnAction(e -> {
            CONTAINER.closeDialog(this);
            closed = true;
            synchronized(this){
                notifyAll();
            }
        });
    }
    
    /**
     * @return true if the dialog has been closed (used for monitoring task)
     */
    public boolean isClosed(){
        return closed;
    }
    
    /**
     * @return true if the yes button was selected
     */
    public boolean isConfirmed(){
        return confirmed;
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
