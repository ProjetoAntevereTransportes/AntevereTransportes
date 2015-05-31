/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felipe
 */
public class Pessoa {

    public static Connection con;

    public void abrir() {
        con = Conexao.abrirConexao();
    }

    public List<contratos.Pessoa> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM pessoa;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rsF = ps.executeQuery();

            List<contratos.Pessoa> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Pessoa f = new contratos.Pessoa();
                f.setNome(rsF.getString("nome"));
                f.setId(rsF.getInt("id"));
                fs.add(f);
            }

            return fs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
