package mellon;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    private static Desktop desktop = Desktop.getDesktop();

    public static boolean executePrint(ArrayList<WebAccount> accounts) {
        try {
            wrtr = new BufferedWriter(new FileWriter(file, true));
            wrtr.write("Stored passwords:");
            wrtr.newLine();
            wrtr.newLine();
            for (WebAccount acct : accounts) {
                wrtr.write("Account nickname: " + acct.getAccountName()
                        + "\r\nUsername: " + acct.getUsername()
                        + "\r\nPassword: " + acct.getPassword()
                        + "\r\n------------------------------------------------");
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
            desktop.print(file);
        } catch (Exception a) {
            System.out.println(a.getMessage());
        } finally {
            Runnable taskFileDelete = new Runnable() {
                @Override
                public void run() {
                    deleteFile();
                }
            };
            new Thread(taskFileDelete).start();
        }
        
        return true;
    }

    private static boolean deleteFile() {
        // Waits 10 seconds then deletes file
        try {
            TimeUnit.SECONDS.sleep(10);
            file.delete();
            System.out.println("File Deleted");
            return true;
        } catch (Exception e) {
            System.out.println("File not Deleted");
            return false;
        }
    }

}
