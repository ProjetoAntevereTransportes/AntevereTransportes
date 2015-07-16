/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import library.Settings;

/**
 *
 * @author lucas
 */
@WebListener
public class EmailProccessScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        
//        try {
//            GoogleDrive.teste();
//        } catch (IOException ex) {
//            Logger.getLogger(EmailProccessScheduler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
        scheduler = Executors.newSingleThreadScheduledExecutor();
        if (Settings.doBackgroundJobs()) {
            scheduler.scheduleAtFixedRate(new EmailProccess(), 0, 30, TimeUnit.SECONDS);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
