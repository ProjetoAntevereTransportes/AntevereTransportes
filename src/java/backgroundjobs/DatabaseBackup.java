/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgroundjobs;

import com.google.api.services.drive.Drive;
import contratos.ModuloEnum;
import database.Conexao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author lucas
 */
public class DatabaseBackup implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        String databaseName = Conexao.databaseProducao;
        if (Conexao.isDevelopment) {
            databaseName = Conexao.databaseDev;
        }
        String databaseUser = "root";
        String databasePassword = "root";
        String path = library.Settings.getSetting("DatabaseBackupPath");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        java.util.Date date = new java.util.Date();
        String name = f.format(date) + ".sql";
        File file = new File(path + "/" + name);

        PrintWriter writer;
        String scriptPath = "/tmp/script_backup_database.sh";
        String scriptContent = "";
        try {
            writer = new PrintWriter(scriptPath, "UTF-8");
            scriptContent = library.Settings.getSetting("scriptBackupDatabase");
            writer.println(scriptContent);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseBackup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DatabaseBackup.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            file.createNewFile();
            file.setWritable(true, false);
            file.setReadable(true, false);
            file.setExecutable(true, false);

            new File(scriptPath).setExecutable(true, true);

            String command = String.format("%s %s %s %s %s", scriptPath,
                    databaseUser, databasePassword, databaseName, file.getAbsolutePath());

            Process runtimeProcess;
            runtimeProcess = Runtime.getRuntime().exec(command);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                Drive service = library.GoogleDrive.getDriveService();
                Log.writeInfo("Início do upload do backup do banco de dados.", ModuloEnum.INTERNO);
                Boolean success = library.GoogleDrive.insertFile(service,
                        name, "", file,
                        library.Settings.getGoogleDriveDatabaseBackupFolderName(), "application/sql");
                if (success) {
                    Log.writeInfo(String.format("Backup do banco de dados realizado com sucesso pesando %s Mbs.",
                            file.length() / 1024), ModuloEnum.INTERNO);
                } else {
                    Log.writeError("Erro ao realizar o upload do backup do banco de dados pesando " + String.valueOf(file.length() / 1024) + " mbs.", "", ModuloEnum.INTERNO);
                }

                file.delete();
            } else {
                file.delete();
                Log.writeError("Não foi possível executar o script de backup do banco de dados.",
                        String.format("Banco: %s Usuário: %s ScriptPath: %s BackupPath: %s ScriptContent: %s",
                                databaseName, databaseUser, scriptPath, file.getAbsoluteFile(), scriptContent), ModuloEnum.INTERNO);
            }

            runtimeProcess.destroy();
        } catch (IOException ex) {
            Log.writeError("Não foi possível executar o script de backup do banco de dados.",
                    String.format("Banco: %s Usuário: %s ScriptPath: %s BackupPath: %s ScriptContent: %s",
                            databaseName, databaseUser, scriptPath, file.getAbsoluteFile(), scriptContent) + ex.toString(), ModuloEnum.INTERNO);
            file.delete();
        } catch (InterruptedException ex) {
            file.delete();
            Log.writeError("Não foi possível executar o script de backup do banco de dados.",
                    String.format("Banco: %s Usuário: %s ScriptPath: %s BackupPath: %s ScriptContent: %s",
                            databaseName, databaseUser, scriptPath, file.getAbsoluteFile(), scriptContent) + ex.toString(), ModuloEnum.INTERNO);
        }
    }

}
