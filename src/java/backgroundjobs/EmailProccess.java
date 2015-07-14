/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import contratos.ModuloEnum;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import library.Imap;
import library.Log;
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
                    if(m.subject == null)
                        m.subject = "";
                    
                    switch (m.subject.trim().toLowerCase()) {
                        case "resumo": {
                            new PaymentJobs().sendSummary(new Date(), m.addresses);
                            continue;
                        }
                        default:{
                            new PaymentJobs().sendTemplates(m.addresses);
                            setDone(m, store, "SentTemplate", "Inbox");
                            continue;
                        }
                    }
                }
            }
        } else {
            System.out.println("\n\n\nStore é nulo.\n\n\n");
            Log.writeError("Store é nulo.", "Erro ao processar e-mail pois store é nulo.", ModuloEnum.INTERNO);
        }
        isExecuting = false;
    }

    public void setDone(MailMessage m, Store store, String destinationFolderName, String fromFolderName) {
        Folder f;
        try {
            Folder fromFolder = store.getFolder(fromFolderName);

            f = store.getFolder(destinationFolderName);
            if (f == null || !f.exists()) {
                if (!new library.Imap().createFolder(store, destinationFolderName)) {
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
            Log.writeError("Erro ao passar para done a mensagem.",
                    "Processamento de e-mail",
                    ModuloEnum.INTERNO,
                    m);
        }
    }
}
