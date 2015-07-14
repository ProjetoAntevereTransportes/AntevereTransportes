/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import database.Pagamento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import library.Mail;
import library.MailMessage;

/**
 *
 * @author lucas
 */
public class PaymentJobs {

    public void sendTemplates(List<String> addresses) {
        Mail mail = new Mail();
        MailMessage m = new MailMessage();
        m.body = "Envie um e-mail com o comando desejado:\n";
        m.body += "- RESUMO - Recebe um resumo das contas à pagar do mês.";
        m.subject = "COMANDOS";
        m.addresses = addresses;
        
        mail.SendEmail(m);
    }

    public void sendSummary(Date date, List<String> emails) {
        List<contratos.Pagamento2> payments = new Pagamento().listarTodosPagamentos(date);

        MailMessage m = new MailMessage();
        m.addresses = new ArrayList<>();
        m.addresses = emails;
        m.body = "";

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy EE");

        for (contratos.Pagamento2 p : payments) {
            m.body += "\n";
            m.body += dt.format(p.getVencimento()) + " - ";
            m.body += p.getNome() + " - ";
            m.body += p.getFornecedorNome() + " - ";
            m.body += "R$ " + String.valueOf(p.getValor()) + " - ";

            if (p.isPago()) {
                m.body += "PAGO!";
            } else {
                m.body += "NÃO PAGO!";
            }
        }

        m.subject = date.toLocaleString();

        Mail mail = new Mail();
        mail.SendEmail(m);
    }

    public void alertToday(List<String> addresses) {
        Calendar c = Calendar.getInstance();
        List<contratos.Pagamento2> pagamentos = new database.Pagamento().GetPayments(c.getTime());
        boolean allPaid = true;
        for (contratos.Pagamento2 p : pagamentos) {
            if (p.isPago()) {
                allPaid = false;
                break;
            }
        }

        if (allPaid) {
            return;
        }

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

    public void alertTomorrow(List<String> addresses) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        List<contratos.Pagamento2> pagamentos = new database.Pagamento().GetPayments(c.getTime());

        boolean allPaid = true;
        for (contratos.Pagamento2 p : pagamentos) {
            if (p.isPago()) {
                allPaid = false;
                break;
            }
        }

        if (allPaid) {
            return;
        }

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
