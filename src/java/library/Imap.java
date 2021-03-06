/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import contratos.ModuloEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author lucas
 */
public class Imap {

    public Store connect(String email, String password) {
        Properties props = new Properties();
        try {
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, password);
            return store;
        } catch (Exception e) {
            Log.writeError("Erro ao conectar store para email " + email, e.getMessage(), ModuloEnum.INTERNO);
            return null;
        }
    }

    public int GetMessageCount(Store store, String folderName) {
        try {
            Folder inbox = store.getFolder(folderName);
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();
            inbox.close(true);
            return messageCount;
        } catch (Exception e) {
            Log.writeError("Erro ao pegar número das mensagens na pasta " + folderName, e.getMessage(), ModuloEnum.INTERNO);
            return -1;
        }
    }

    public List<MailMessage> GetMessages(Store store, String folderName, int limit) {
        try {
            List<MailMessage> messages = new ArrayList<>();
            Folder inbox = store.getFolder(folderName);
            inbox.open(Folder.READ_ONLY);
            Message[] m = inbox.getMessages();
            for (int i = 0; i < limit; i++) {
                MailMessage mm = new MailMessage();
                mm.body = GetStringFromMultipart((Multipart) m[i].getContent());
                mm.subject = m[i].getSubject();
                mm.addresses = new ArrayList();
                mm.originalMessage = m[i];

                for (Address a : m[i].getFrom()) {
                    mm.addresses.add(a.toString());
                }
                messages.add(mm);
            }
            inbox.close(true);
            return messages;
        } catch (Exception e) {
            Log.writeError("Erro ao pegar mensagens da pasta " + folderName, e.getMessage(), ModuloEnum.INTERNO);
            return null;
        }
    }

    public String GetMessageSubject(Store store, String folderName, Message message) {
        try {
            String a = "";
            Folder inbox = store.getFolder(folderName);
            inbox.open(Folder.READ_ONLY);
            a = message.getSubject();
            inbox.close(true);
            return a;
        } catch (Exception e) {
            Log.writeError("Erro ao pegar o assunto do e-mail.", e.getMessage(), ModuloEnum.INTERNO);
            return null;
        }
    }

    public String GetStringFromMultipart(Multipart multipart) {
        String message = "";
        try {
            for (int j = 0; j < multipart.getCount(); j++) {

                BodyPart bodyPart = multipart.getBodyPart(j);

                String disposition = bodyPart.getDisposition();

                if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
                    System.out.println("Mail have some attachment");

                    DataHandler handler = bodyPart.getDataHandler();

                    System.out.println("file name : " + handler.getName());
                } else {
                    System.out.println("Body: " + bodyPart.getContent());
                    String content = bodyPart.getContent().toString();
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(Imap.class.getName()).log(Level.SEVERE, null, ex);
            Log.writeError("Erro ao pegar o multipart do e-mail.", message, ModuloEnum.INTERNO);
        } catch (IOException ex) {
            Log.writeError("Erro ao pegar o multipart do e-mail.", message, ModuloEnum.INTERNO);
            Logger.getLogger(Imap.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    public boolean createFolder(Store store, String folderName) {
        boolean isCreated = true;

        try {
            Folder newFolder = store.getDefaultFolder().getFolder(folderName);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("created: " + isCreated);

        } catch (Exception e) {
            Log.writeError("Erro ao criar pasta " + folderName, e.getMessage(), ModuloEnum.INTERNO);
            isCreated = false;
        }
        return isCreated;
    }
}
