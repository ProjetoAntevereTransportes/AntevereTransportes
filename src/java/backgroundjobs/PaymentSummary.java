/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import java.util.Date;

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
        new PaymentJobs().sendSummary(date, new database.Usuario().getEmails());
    }
}
