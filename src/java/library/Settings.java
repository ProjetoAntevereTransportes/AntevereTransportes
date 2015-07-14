/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import authentication.AES;
import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class Settings {

    private static List<contratos.Settings> settings;
    private static java.util.Date expiration;
    private static Connection con;

    public static String getSetting(String name) {
        if (settings == null || settings.size() == 0
                || expiration == null || expiration.before(new java.util.Date())) {
            LoadSettings();
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, 30);
            expiration = c.getTime();
        }
        try {
            for (contratos.Settings s : settings) {
                if (s.getNome().equals(name)) {
                    if (s.isCriptografado()) {
                        return AES.decrypt(s.getValor());
                    }
                    return s.getValor();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static void LoadSettings() {
        try {
            Connection con = Conexao.abrirConexao();
            String sql = "select * from settings;";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            settings = new ArrayList<>();
            while (rs.next()) {
                contratos.Settings s = new contratos.Settings();
                s.setID(rs.getInt("id"));
                s.setCriptografado(rs.getBoolean("criptografado"));
                s.setNome(rs.getString("nome"));
                s.setValor(rs.getString("valor"));

                settings.add(s);
            }
            con.close();
        } catch (Exception ex) {
            try {
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    public static String getEnterpriseEmail(){
        return getSetting("enterpriseEmail");
    }
    
    public static String getEnterpriseEmailPassword(){
        return getSetting("enterpriseEmailPassword");
    }
}
