/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import contratos.ModuloEnum;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author lucas
 */
public class Mail {

    public boolean SendEmail(MailMessage m) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Settings.getEnterpriseEmail(),
                                Settings.getEnterpriseEmailPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Settings.getEnterpriseEmail()));

            for (String email : m.addresses) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email));
            }

            message.setSubject(m.subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(m.body);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (FileMessage file : m.files) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file.file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.name);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            Log.writeError("Erro ao enviar e-mail", e.getMessage(), ModuloEnum.INTERNO, m);
            return false;
        }catch(Exception e){
            Log.writeError("Erro ao enviar e-mail", e.getMessage(), ModuloEnum.INTERNO, m);
            return false;
        }
    }
}
