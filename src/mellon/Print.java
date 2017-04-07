package mellon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class Print {

    static BufferedWriter wrtr = null;
    static File file = new File("MellonUserReport.txt");

    public static boolean executePrint(ArrayList<WebAccount> accounts) {
        try {
            wrtr = new BufferedWriter(new FileWriter(file, true));
            wrtr.write("Here's a list of your stored account details ");
            wrtr.newLine();
            wrtr.newLine();
            for (WebAccount acct : accounts) {
                wrtr.write("Account nickname: " + acct.getAccountName()
                        + "\r\nUsername: " + acct.getUsername()
                        + "\r\nPassword: " + acct.getPassword()
                         + "\r\n------------------------------------------------" );        
                wrtr.newLine();
            }
        } catch (IOException io) {
            System.out.println("File IO Exception" + io.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Report Generation Failed");
            alert.setHeaderText("");
            alert.setContentText("Looks like something went wrong while "
                    + "generating your report.  Please try again or "
                    + "report this as a bug.");
            alert.showAndWait();
        } finally {
            if (wrtr != null) {
                try {
                    wrtr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

            try {
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE; 
                javax.print.PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
                javax.print.PrintService service = PrintServiceLookup.lookupDefaultPrintService();
                if (service != null) {
                    DocPrintJob job = service.createPrintJob();
                    FileInputStream fis = new FileInputStream(file);
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, das);
                    job.print(doc, pras);
                }
            } catch (Exception a) {
                System.out.println(a.getMessage()); 
            }
         file.delete();
      return true;
    }
}
