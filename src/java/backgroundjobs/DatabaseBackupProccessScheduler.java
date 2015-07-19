package backgroundjobs;

import contratos.ModuloEnum;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import library.Log;
import library.Mail;
import library.MailMessage;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.CronScheduleBuilder.weeklyOnDayAndHourAndMinute;
import org.quartz.DateBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author lucas
 */
@WebListener
public class DatabaseBackupProccessScheduler implements ServletContextListener {

    private Scheduler sched;

    @Override
    public void contextInitialized(ServletContextEvent event) {                
        if (library.Settings.doBackgroundJobs()) {
            try {
                SchedulerFactory sf = new StdSchedulerFactory();

                sched = sf.getScheduler();

                JobDetail databaseBackupJob = newJob(DatabaseBackup.class)
                        .withIdentity("databaseBackupJob")
                        .build();

                JobDetail emailProccessJob = newJob(EmailProccess.class)
                        .withIdentity("emailProccess")
                        .build();

                JobDetail paymentAlertJob = newJob(PaymentAlert.class)
                        .withIdentity("paymentAlertJob")
                        .build();

                JobDetail summaryJob = newJob(PaymentSummary.class)
                        .withIdentity("summaryJob")
                        .build();

                Trigger databaseBackupTrigger = newTrigger()
                        .withIdentity("databaseBackupTrigger")
                        .startNow()
                        .withSchedule(dailyAtHourAndMinute(10, 00))
                        .forJob(databaseBackupJob)
                        .build();

                Trigger paymentAlertTrigger = newTrigger()
                        .withIdentity("paymentAlertTrigger")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInHours(2)
                                .repeatForever())
                        .forJob(paymentAlertJob)
                        .build();

                Trigger summaryTrigger = newTrigger()
                        .withIdentity("summaryTrigger")
                        .startNow()
                        .withSchedule(weeklyOnDayAndHourAndMinute(DateBuilder.MONDAY, 2, 00))
                        .forJob(summaryJob)
                        .build();

                Trigger emailProccessTrigger = newTrigger()
                        .withIdentity("emailProccessTrigger")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(30)
                                .repeatForever())
                        .forJob(emailProccessJob)
                        .build();

                sched.scheduleJob(databaseBackupJob, databaseBackupTrigger);
                sched.scheduleJob(paymentAlertJob, paymentAlertTrigger);
                sched.scheduleJob(summaryJob, summaryTrigger);
                //sched.scheduleJob(emailProccessJob, emailProccessTrigger);
                sched.start();
                Log.writeInfo("Iniciado Quartz.", ModuloEnum.INTERNO);
            } catch (Exception e) {
                Log.writeError("Erro ao iniciar o Quartz.", e.toString(), ModuloEnum.INTERNO);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
//        try {
//            //sched.shutdown();
//        } catch (SchedulerException ex) {
//            Logger.getLogger(DatabaseBackupProccessScheduler.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
