package mellon;

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
       
    public HelpPage() {
        addItems();
    }
    
    private void addItems(){
        this.setFitToWidth(true);
        this.setPadding(new Insets(10, 0, 0, 0));
        Accordion accordion = new Accordion();        
        accordion.setPadding(new Insets(10, 10, 10, 10));
        
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
        bugs.setContent(new Text("Found an issue? Please report bugs via "
                + "our GitHub:\n\nhttps://github.com/brenther/Mellon/issues"));
        
        accordion.getPanes().addAll(about, creation, retrieve, edit,
                                    settings, bugs);
        this.setContent(accordion);
    }
    
    private VBox getAbout(){
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        String string = 
            "Mellon is a password generation and storage tool which creates "
            + "strong, complex passwords and securely stores usernames and "
            + "passwords to all of your accounts.\n\nMellon stores your "
            + "information in a hashed file in a secure database. We never see "
            + "any of your passwords or your master password, and even if the "
            + "database is hacked, the intruders would only obtain the "
            + "encrypted file. Encryption and decryption is done on your "
            + "computer, so your passwords and usernames are never transmitted "
            + "in a readable format.\n\nA lot of data gets transmitted on the "
            + "internet. Protect yourself and your data with as strong a "
            + "password as possible.  Mellon makes this easy, so use strongest "
            + "password the website or account will allow. We also recommend "
            + "two-factor authentication (e.g. using a cell phone or email in "
            + "addition to the password) if the site allows for it.\n\n" 
            +"Use a strong password for Mellon. Do not store a digital copy of "
            + "this password - if you need a reminder, write it down and store "
            + "it in a secure place. If you lose your master password, you will "
            + "need to start over.";
        
        Text text = new Text(string);
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
        
        String string = 
            "Sometimes you need to make a change. In Mellon, changing your "
            +"profile is easy. Simply select the pencil icon next to the "
            +"profile you want to change. This will take you to the same menu "
            +"you used to create the password, but this time will pre-populate "
            +"the fields with information from your profile.\n\n"
            +"Make changes as desired using the same methods you used to make "
            +"the profile, and click save. You can also delete your profile "
            +"from this menu.\n\nIMPORTANT: Changing your password permenantly "
            +"erases the old password from the profile. Since Mellon never "
            +"sees the password, we cannot retrieve the old one after it is "
            +"overwritten. Changing your password in Mellon does not change "
            +"the password in the actual account. Be sure to change your "
            +"password in the actual account before saving the changes in "
            +"Mellon.";
        
        Text text = new Text(string);
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
        
        String string = 
            "In Mellon, getting your passwords is as easy as a click of your "
            +"mouse.\n\nAt the main menu, find the profile you want from the "
            +"list. Clicking on the profile will automatically copy your "
            +"password to the clipboard, and you can be on your way. If you "
            +"need to copy your username, simply right-click on the profile "
            +"and it will automatically be copied.\n\nIf you want to view your "
            +"profile information, simply click the [+] to expand the profile. "
            +"Your password will initially be blurred out, so click on the eye "
            +"icon to view it. Clicking on the password field will also copy "
            +"the password to the clipboard.";
        
        ImageView pic2 = new ImageView(new Image(getClass()
                .getResourceAsStream("/resources/help_info/show_password.png")));
        
        
        Text text = new Text(string);
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
        
        String string = 
            "Mellon contains a few user settings to make things easier on you. "
            +"Click on the cog icon to open the settings menu.  From there, "
            +"you can set:\n\n- Timeout duration: This is how long the program "
            +"will sit idle before logging you out for security purposes. "
            +"The default is 10 minutes.\n\n- Password length: This is the "
            +"default password length for Mellon's password generator.  The "
            +"default is 16 characters, but you can change it to whatever you "
            +"like.  You will always have the option to choose the password "
            +"length in the creation menu if a particular account has a short "
            +"password length requirement.\n\nFrom the settings menu you can "
            +"also:\n\n- Change your master password. Be sure to pick a strong "
            +"password and write it down if you need a reminder.\n\n- Print a "
            +"physical copy of your stored passwords.  If you want a back up to "
            +"take on the road or are skeptical of technology, you can print a "
            +"copy of your passwords from the settings menu. Do not make a "
            +"digital copy of your passwords, store the physical copy in a "
            +"secure location.";
        
        Text text = new Text(string);
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
        
        String string = 
            "To create a profile, select the '+' icon from the navigation bar. "
            +"This will display the creation menu. Ensure all of the fields "
            +"are filled out, choose your password options, hit generate, and "
            +"save. It's that easy.\n\nDescription of elements:\n\n" 
            +"Nickname: This is the name you will use to find your account once "
            +"it is created.  Use something like \"Gmail\" or \"Facebook\".\n\n"
            +"Username: This is the username of the account you are adding. "
            +"For example, your email address.\n\n"
            +"Password length: This is the length of the password to generate "
            +"(if you generate one with Mellon).  You can select from a pre-set "
            +"selection, or choose \"Custom\" to enter your own length as "
            +"needed.\n\nSet Expiration: You can choose to set a reminder to "
            +"change your password. You will be reminded three days from the "
            +"expiration date, and prompted to change your password after it "
            +"expires. Note: the expiration date is simply a reminder, the "
            +"password does not actually expire in Mellon.\n\n"
            +"Quick Customize: Choose whether to allow symbols, numbers, upper, "
            +"or lowercase characters in the generated password. If your "
            +"account has restrictions on allowable characters (e.g. it only "
            +"allows letters, numbers, and '!'), you can click the "
            +"\"Advanced\" button to open a menu of all special characters. "
            +"Simply check the ones you want to allow, and save the settings.\n"
            +"\nGenerate Password: You can enter a password on your own, or "
            +"use Mellon's random generator to create a password meeting the "
            +"above settings. The choice is yours.\n\nSave Account: Saves the "
            +"profile to the database and returns you to the main menu.";
        
        Text text = new Text(string);
        text.setWrappingWidth(475);
        text.getStyleClass().add("dialog-text");
        content.getChildren().addAll(pic, text);
        
        return content;
    }    
}
