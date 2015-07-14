/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import authentication.AES;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import library.Imap;
import library.Mail;
import library.MailMessage;

/**
 *
 * @author lucas
 */
public class EmailProccess implements Runnable {

    private static Store store;
    private static boolean isExecuting;

    @Override
    public void run() {
        
        InputStream i = 
                this.getClass().getClassLoader()
                        .getResourceAsStream("WEB-INF/classes/templates/teste.txt");
        
        if (isExecuting) {
            return;
        }
        isExecuting = true;
        Imap imap = new Imap();
       
        if (store == null || !store.isConnected()) {
            store = imap.connect(library.Settings.getEnterpriseEmail(),
                    library.Settings.getEnterpriseEmailPassword()
            );
        }

        if (store != null) {
            int inbox = imap.GetMessageCount(store, "inbox");
            System.out.println("\n\n\nINBOX: " + String.valueOf(inbox) + " mensagens.\n\n\n");

            if (inbox > 0) {
                List<MailMessage> messages = imap.GetMessages(store, "inbox", inbox);
                for (MailMessage m : messages) {
                    if (m.subject == null || m.subject.equals("")) {
                        System.out.println("\n\nEnviar templates\n\n");
                        sendTemplates(m);
                        setDone(m, store, "SentTemplate", "Inbox");
                        continue;
                    }
                    System.out.println("\n" + m.subject);
                    System.out.println("\n" + m.body);
                }
            }
        } else {
            System.out.println("\n\n\nStore é nulo.\n\n\n");
        }
        isExecuting = false;
    }

    public void sendTemplates(MailMessage m) {
        Mail mail = new Mail();
        m.body = "Templates dos arquivos.";
        m.subject = "Templates dos arquivos.";

        mail.SendEmail(m);
    }

    public void setDone(MailMessage m, Store store, String destinationFolderName, String fromFolderName) {
        Folder f;
        try {
            Folder fromFolder = store.getFolder(fromFolderName);

            f = store.getFolder(destinationFolderName);
            if (f == null || !f.exists()) {
                if (!createFolder(store, destinationFolderName)) {
                    return;
                }

                f = store.getFolder(destinationFolderName);
            }

            Message[] messages = new Message[1];
            messages[0] = m.originalMessage;

            fromFolder.open(Folder.READ_WRITE);
            f.open(Folder.READ_WRITE);

            fromFolder.copyMessages(messages, f);
            Flags deleted = new Flags(Flags.Flag.DELETED);
            fromFolder.setFlags(messages, deleted, true);

            fromFolder.close(true);
            f.close(true);

        } catch (MessagingException ex) {
            Logger.getLogger(EmailProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean createFolder(Store store, String folderName) {
        boolean isCreated = true;

        try {
            Folder newFolder = store.getDefaultFolder().getFolder(folderName);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("created: " + isCreated);

        } catch (Exception e) {
            System.out.println("Error creating folder: " + e.getMessage());
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }
}
