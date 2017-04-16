package mellon;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

public class EmailService {
    public static void sendEmail(String email, String userEmail, String bugReport){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mellon.bug.report", "Mellon2017");
            }
        });

        try {

            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mellon.bug.report"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Bug Reported FROM: " + userEmail);
            message.setText(bugReport);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
        DBConnect.reportBug(userEmail,bugReport);
    }
   

}
