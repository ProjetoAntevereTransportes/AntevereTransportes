 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import contratos.ModuloEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import library.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author lucas
 */
public class PaymentAlert implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        List<String> addresses = new ArrayList<>();
        try {
            addresses = new database.Usuario().getEmails();
            PaymentJobs j = new PaymentJobs();
            
            Boolean tomorrow = j.alertTomorrow(addresses);
            if(tomorrow){
                Log.writeInfo("Envio de alerta dos pagamentos do próximo dia enviado com sucesso.",
                        ModuloEnum.INTERNO);
            }else{
                throw new Exception("Erro ao enviar alerta dos pagamentos do próximo dia. PaymentJobs.alertTomorrow().");
            }
            
            Boolean today = j.alertToday(addresses);
            if(today){
                Log.writeInfo("Envio de alerta dos pagamentos do dia enviado com sucesso.",
                        ModuloEnum.INTERNO);
            }else{
                throw new Exception("Erro ao enviar alerta dos pagamentos do dia. PaymentJobs.alertToday().");
            }
            
        } catch (Exception ex) {
                Log.writeError("Não foi possível enviar os alertas dos pagamentos no e-mails.",
                        null, ModuloEnum.INTERNO, addresses);
        }
    }
}
