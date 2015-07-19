/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import contratos.ModuloEnum;
import database.Pagamento;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import library.Log;
import library.Mail;
import library.MailMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author lucas
 */
public class PaymentSummary implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            Boolean b = new PaymentJobs().sendSummary(new Date(), new database.Usuario().getEmails());
            if (!b) {
                throw new Exception("Retorno FALSE do método de PaymentJobs().sendSummary().");
            }else{
                Log.writeInfo("Resumo dos pagamentos enviados com sucesso.", ModuloEnum.INTERNO);
            }
        } catch (Exception ex) {
            Log.writeError("Não foi executar o PaymentSummary para envio do resumo de pagamentos.",
                    ex.toString(), ModuloEnum.INTERNO);
        }
    }
}
