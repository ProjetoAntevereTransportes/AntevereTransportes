/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author lucas
 */
public class Log {

    public static void writeInfo(String message, contratos.ModuloEnum modulo) {
        contratos.Log l = new contratos.Log();
        l.setIsError(false);
        l.setMessage(message);
        l.setModulo(modulo);

        save(l);
    }

    public static void writeError(String message, String exception, contratos.ModuloEnum modulo) {
        writeError(message, exception, modulo, null);
    }

    public static void writeError(String message, String exception, contratos.ModuloEnum modulo, Object object) {
        contratos.Log l = new contratos.Log();
        l.setIsError(true);
        l.setException(exception);
        l.setMessage(message);
        l.setModulo(modulo);

        if (object != null) {
            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
            l.setObject(g.toJson(object));
        }
        
        save(l);
    }

    private static void save(contratos.Log log) {
        Connection con;
        try {
            con = Conexao.abrirConexao();
            String sql = "insert into log(data, message, exception, isError, modulo_id, object) values (?,?,?,?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            ps.setString(1, sdf.format(new Date()));
            ps.setString(2, log.getMessage());
            ps.setString(3, log.getException());
            ps.setBoolean(4, log.getIsError());
            ps.setInt(5, log.getModulo().getModulo());
            ps.setString(6, log.getObject());

            ps.executeUpdate();
        } catch (Exception ex) {

        }
    }
}
