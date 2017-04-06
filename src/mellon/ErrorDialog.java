
package mellon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * Custom dialog to alert user to errors.
 * @author Brent H.
 */
public class ErrorDialog extends BorderPane{

    private String message;
    private final MenuContainer CONTAINER;
    private final ImageView ALERT = new ImageView(new Image(getClass()
            .getResourceAsStream("/resources/alert_icon.png")));
    
    /**
     * Constructor once logged in
     * @param c the menu container
     * @param message the message to display
     */
    public ErrorDialog(MenuContainer c, String message){
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
        this.setStyle("-fx-background-color: white; ");
        this.setBorder(new Border(new BorderStroke(Color.valueOf("#d4aa00"),
                BorderStrokeStyle.SOLID, null, null)));
        this.setPadding(new Insets(15,15,15,15));
        
        BorderPane messageArea = new BorderPane();
        messageArea.setPrefSize(375, 125);
        
        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.getChildren().add(ALERT);
        
        VBox textBox = new VBox();
        textBox.setAlignment(Pos.CENTER);
        Text messageText = new Text(message);
        messageText.setWrappingWidth(250);
        messageText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        textBox.getChildren().add(messageText);
        messageArea.setLeft(iconBox);
        messageArea.setRight(textBox);
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button acknowledge = new Button("Got it");
        buttonBox.getChildren().add(acknowledge);
        
        this.setCenter(messageArea);
        this.setBottom(buttonBox);
        addDropShadow();
        
        acknowledge.setOnAction(e -> CONTAINER.closeDialog(this));
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
