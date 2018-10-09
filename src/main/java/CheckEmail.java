import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class CheckEmail {

    /**
     *
     * @param host
     * @param user
     * @param password
     * @return The first number in the last email
     */
    static int getCode(String host, String user,
                       String password)
    {
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message message = emailFolder.getMessage(emailFolder.getMessageCount());
            System.out.println("---------------------------------");
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + message.getContent().toString());
            Matcher matcher = Pattern.compile("\\d+").matcher(message.getContent().toString());
            matcher.find();
            int code = Integer.valueOf(matcher.group());
            System.out.println("Code: " + code);

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

            return code;

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void main(String[] args) {

        String host = "mail.bilkent.edu.tr";// change accordingly
        String mailStoreType = "pop3";
        String username = System.getenv("webmail_username"); // change accordingly
        String password = System.getenv("webmail_password"); // change accordingly

        getCode(host, username, password);

        // to pause the cmd
        new Scanner(System.in).nextLine();
    }

}