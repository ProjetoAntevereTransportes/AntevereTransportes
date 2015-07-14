 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import library.Mail;
import library.MailMessage;

/**
 *
 * @author lucas
 */


public class PaymentAlert implements Runnable {

    private static Date date;

    @Override
    public void run() {
        Date d = new Date();

        if (date != null && d.getDate() == date.getDate() && d.getMonth() == date.getMonth()
                && d.getYear() == date.getYear()) {
            return;
        }

        date = d;

        List<String> addresses = new database.Usuario().getEmails();
        AlertTomorrow(addresses);
        AlertToday(addresses);
    }

    public void AlertToday(List<String> addresses) {
        Calendar c = Calendar.getInstance();
        List<contratos.Pagamento2> pagamentos = new database.Pagamento().GetPayments(c.getTime());
        boolean allPaid = true;
        for(contratos.Pagamento2 p : pagamentos)
        {
           if(p.isPago()){
               allPaid = false;
               break;
           }
        }
        
        if(allPaid)
            return;
        
        Mail mail = new Mail();
        MailMessage message = new MailMessage();
        message.addresses = addresses;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy EE");

        if (pagamentos.size() == 0) {
            message.subject = String.format("[HOJE - %s] Não há pagamentos", dt.format(c.getTime()).toString());
            message.body = "";
        } else {
            Double value = 0.0;

            for (contratos.Pagamento2 p : pagamentos) {
                message.body += p.getNome() + " - ";
                message.body += p.getFornecedorNome() + " - ";
                message.body += "R$ " + String.valueOf(p.getValor()) + " - ";

                if (p.isPago()) {
                    message.body += "PAGO!";
                } else {
                    message.body += "NÃO PAGO!";
                }

                value += p.getValor();
            }

            message.subject = String.format("[HOJE - %s] %s pagamentos totalizando %s",
                    dt.format(c.getTime()), pagamentos.size(), value.toString());
        }

        mail.SendEmail(message);
    }

    public void AlertTomorrow(List<String> addresses) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        List<contratos.Pagamento2> pagamentos = new database.Pagamento().GetPayments(c.getTime());

        boolean allPaid = true;
        for(contratos.Pagamento2 p : pagamentos)
        {
           if(p.isPago()){
               allPaid = false;
               break;
           }
        }
        
        if(allPaid)
            return;
        
        Mail mail = new Mail();
        MailMessage message = new MailMessage();
        message.addresses = addresses;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy EE");
        message.body = "";

        if (pagamentos.size() == 0) {
            message.subject = String.format("[%s] Não há pagamentos", dt.format(c.getTime()).toString());
            message.body = "";
        } else {
            Double value = 0.0;

            for (contratos.Pagamento2 p : pagamentos) {
                message.body += p.getNome() + " - ";
                message.body += p.getFornecedorNome() + " - ";
                message.body += "R$ " + String.valueOf(p.getValor()) + " - ";

                if (p.isPago()) {
                    message.body += "PAGO!";
                } else {
                    message.body += "NÃO PAGO!";
                }

                value += p.getValor();
            }

            message.subject = String.format("[%s] %s pagamentos totalizando R$ %s",
                    dt.format(c.getTime()), pagamentos.size(), value.toString());
        }

        mail.SendEmail(message);
    }
}
