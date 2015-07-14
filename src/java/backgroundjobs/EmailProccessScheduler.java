/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import static com.sun.org.apache.bcel.internal.util.SecuritySupport.getResourceAsStream;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import library.Debug;

/**
 *
 * @author lucas
 */
@WebListener
public class EmailProccessScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        if (!Debug.isDebug()) {
            scheduler.scheduleAtFixedRate(new EmailProccess(), 0, 30, TimeUnit.SECONDS);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
