/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import database.Pagamento;
import java.io.File;
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
public class PaymentSummary implements Runnable {
    private static Date date;
    
    @Override
    public void run() {        
        Date d = new Date();
        
         if (date != null && d.getDay() != 1 &&
                 d.getDate() == date.getDate() && d.getMonth() == date.getMonth()
                && d.getYear() == date.getYear()) {
            return;
        }        
        
        date = d;
                
        List<contratos.Pagamento2> payments = new Pagamento().listarTodosPagamentos(date);        
                
        MailMessage m = new MailMessage();
        m.addresses = new ArrayList<>();
        m.addresses.add("lucas.antevere@gmail.com");
        m.body = "";
        
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy EE");
        
        for(contratos.Pagamento2 p : payments){
            m.body += "\n";
            m.body += dt.format(p.getVencimento()) + " - ";
            m.body += p.getNome() + " - ";
            m.body += p.getFornecedorNome()+ " - ";
            m.body += "R$ " +  String.valueOf(p.getValor()) + " - ";
            
            if(p.isPago()){
                m.body += "PAGO!";
            }else{
                m.body += "NÃO PAGO!";
            }
            
            /*if(!p.getComprovante().isEmpty()){
                m.AddFile(p.getNome(), new database.FileUpload().getFile(p.getComprovanteID()).getFile());                
            }*/
        }
        
        m.subject = date.toLocaleString();        
                
        Mail mail = new Mail();
        mail.SendEmail(m);
    }
}
