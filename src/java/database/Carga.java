/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipe
 */
public class Carga {
     public static Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }
    

    public contratos.Carga pegarPeloID(int cargaID) {
        try {
            abrir();
            String sql = "SELECT * FROM carga WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cargaID);

            ResultSet rs = ps.executeQuery();

            rs.next();
            contratos.Carga c = new contratos.Carga();
            c.setNome(rs.getString("nome"));
            c.setDescricao(rs.getString("descricao"));

            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    
    //Esta igual a inserir do Fornecedor
    public boolean Inserir(contratos.Carga c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO carga(nome, descricao) VALUES (?, ?);";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());

            int status = ps.executeUpdate();

            con.commit();

            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Carga.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }

    }


    public List<contratos.Carga> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM carga;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.Carga> cs = new ArrayList<>();

            while (rs.next()) {
                contratos.Carga c = new contratos.Carga();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setDescricao(rs.getString("descricao"));

                cs.add(c);
            }

            return cs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean excluir(int id) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "DELETE FROM carga WHERE id = " + id + ";";

            PreparedStatement ps = con.prepareStatement(sql);

            int status = ps.executeUpdate();

            con.commit();

            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Carga.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.Carga c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE carga SET nome = ?, descricao = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());
            ps.setInt(3, c.getId());

            int status = ps.executeUpdate();

            con.commit();

            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Carga.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
            
           
}


