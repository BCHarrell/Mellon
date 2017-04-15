package mellon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Contains useful information about operating the program and reporting bugs.
 * 
 * @author Brent H.
 */
public class HelpPage extends ScrollPane {
    
    InputStreamReader ABOUT = new InputStreamReader(getClass()
            .getResourceAsStream("/resources/help_info/about_mellon.txt"));
    InputStreamReader CREATE = new InputStreamReader(getClass()
            .getResourceAsStream("/resources/help_info/profile_creation.txt"));
    InputStreamReader EDIT = new InputStreamReader(getClass()
            .getResourceAsStream("/resources/help_info/edit_profile.txt"));
    InputStreamReader RETRIEVE = new InputStreamReader(getClass()
            .getResourceAsStream("/resources/help_info/password_retrieval.txt"));
    InputStreamReader SETTINGS = new InputStreamReader(getClass()
            .getResourceAsStream("/resources/help_info/settings.txt"));
    
    private String aboutText, createText, editText, retrieveText, settingsText;

    private final MenuContainer CONTAINER;
    
    public HelpPage(MenuContainer c) {
        CONTAINER = c;
        getStrings();
        addItems();
    }
    
    private void addItems(){
        this.setFitToWidth(true);
        this.setPadding(new Insets(-1, 0, 0, 0));
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        Accordion accordion = new Accordion();        
        
        //Getting started
        TitledPane about = new TitledPane();
        about.setText("About Mellon");
        about.setContent(getAbout());
        
        //Adding an account
        TitledPane creation = new TitledPane();
        creation.setText("Creating a Profile");
        creation.setContent(getCreation());
        
        //Retrieving a password
        TitledPane retrieve = new TitledPane();
        retrieve.setText("Retrieving a Password");
        retrieve.setContent(getRetrieval());
        
        //Editing
        TitledPane edit = new TitledPane();
        edit.setText("Editing an Existing Profile");
        edit.setContent(getEdit());
        
        //Settings
        TitledPane settings = new TitledPane();
        settings.setText("Customizing Your Settings");
        settings.setContent(getSettings());
        
        //Bug report
        TitledPane bugs = new TitledPane();
        bugs.setText("Have an Issue?");
        bugs.setContent(getBugReportForm());
        
        accordion.getPanes().addAll(about, creation, retrieve, edit,
                                    settings, bugs);
        this.setContent(accordion);
    }
    
    private VBox getAbout(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        Text text = new Text(aboutText);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().add(text);
        
        return content;
    }
    
    private VBox getEdit(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        ImageView pic = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/edit.png")));
        
        Text text = new Text(editText);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().addAll(pic, text);
        
        return content;
    }
    
    private VBox getRetrieval(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        ImageView pic = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/retrieve.png")));
        
        ImageView pic2 = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/show_password.png")));
        
        
        Text text = new Text(retrieveText);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().addAll(pic, text, pic2);
        
        return content;
    }
    
    private VBox getSettings(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        ImageView pic = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/settings.png")));
        
        Text text = new Text(settingsText);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().addAll(pic, text);
        
        return content;
    }
    
    private VBox getCreation(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        ImageView pic = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/creation.png")));
        
        Text text = new Text(createText);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().addAll(pic, text);
        
        return content;
    }
    
    /**
     * Reads the text files containing the help page info from
     * /src/resources/help_info
     */
    private void getStrings(){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(ABOUT);
            String str = br.readLine();
            
            while(str != null){
                sb.append(str);
                sb.append(System.lineSeparator());
                str = br.readLine();
            }
            aboutText = sb.toString();
        } catch (IOException ex) {
            aboutText = "About file not found";
        }
        
        try{
            sb = new StringBuilder();
            br = new BufferedReader(CREATE);
            String str = br.readLine();
            
            while(str != null){
                sb.append(str);
                sb.append(System.lineSeparator());
                str = br.readLine();
            }
            createText = sb.toString();
        } catch (IOException ex) {
            createText = "Create password file not found";
        }
        
        try{
            sb = new StringBuilder();
            br = new BufferedReader(EDIT);
            String str = br.readLine();
            
            while(str != null){
                sb.append(str);
                sb.append(System.lineSeparator());
                str = br.readLine();
            }
            editText = sb.toString();
        } catch (IOException ex) {
            editText = "Edit profile file not found";
        }
        
        try{
            sb = new StringBuilder();
            br = new BufferedReader(RETRIEVE);
            String str = br.readLine();
            
            while(str != null){
                sb.append(str);
                sb.append(System.lineSeparator());
                str = br.readLine();
            }
            retrieveText = sb.toString();
        } catch (IOException ex) {
            retrieveText = "Retrieve password file not found";
        }
        
        try{
            sb = new StringBuilder();
            br = new BufferedReader(SETTINGS);
            String str = br.readLine();
            
            while(str != null){
                sb.append(str);
                sb.append(System.lineSeparator());
                str = br.readLine();
            }
            settingsText = sb.toString();
        } catch (IOException ex) {
            settingsText = "Settings file not found";
        }
    }
    
    /**
     * Creates a bug report form
     * @return a VBox containing the required fields
     */
    private VBox getBugReportForm(){
        VBox content = new VBox();
        content.setSpacing(45);
        content.setAlignment(Pos.CENTER);
        
        VBox fieldBox = new VBox();
        fieldBox.setSpacing(15);
        
        HBox emailBox = new HBox();
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.setSpacing(5);
        TextField email = new TextField();
        email.setPromptText("Enter your email address");
        Text optional = new Text("(optional)");
        optional.getStyleClass().add("dialog-text");
        emailBox.getChildren().addAll(email, optional);
        
        VBox reportBox = new VBox();
        reportBox.setSpacing(3);
        TextArea report = new TextArea();
        report.setWrapText(true);
        report.setPrefSize(400, 250);
        report.setPromptText("Please enter detailed information about the "
                + "problem you are experiencing. Without sufficient details "
                + "we may not be able to recreate (and therefore solve) the "
                + "problem.  Enter a minimum of 200 characters.\n\nEntering a "
                + "valid email address will help us follow up on your problem "
                + "or to notify you of fixes.");
        Text charCount = new Text("Character count: 0");
        charCount.getStyleClass().add("dialog-text");
        reportBox.getChildren().addAll(report, charCount);
        
        fieldBox.getChildren().addAll(emailBox, reportBox);
        
        Button submit = new Button("Submit Bug Report");
        submit.setDisable(true);
        submit.getStyleClass().add("blue-button-large");
        
        content.getChildren().addAll(fieldBox, submit);
        
        /****************
         *EVENT LISTENER*
         ****************/
        
        report.setOnKeyReleased(e -> {
            int count = report.getText().length();
            if(count > 200){
                submit.setDisable(false);
            } else {
                submit.setDisable(true);
            }
            charCount.setText("Character count: " + count);
        });
        
        submit.setOnAction(e -> {
            String userEmail;
            if(report.getText().isEmpty()){
                report.setStyle("-fx-background-color: rgba(255,0,0,.5)");
            } else {
                if(email.getText().isEmpty()){
                    userEmail = "NO EMAIL PROVIDED";
                }else{
                    userEmail = email.getText();
                }
                
                EmailService.sendEmail("mellon.bug.report@gmail.com",userEmail ,report.getText());
            }
        });
        
        return content;
    }
}
