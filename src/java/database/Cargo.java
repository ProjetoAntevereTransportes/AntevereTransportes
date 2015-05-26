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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipe
 */
public class Cargo {
    public static Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }
    

    public contratos.Cargo pegarPeloID(int cargoID) {
        try {
            abrir();
            String sql = "SELECT * FROM cargo WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cargoID);

            ResultSet rs = ps.executeQuery();

            rs.next();
            contratos.Cargo c = new contratos.Cargo();
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
    public boolean Inserir(contratos.Cargo c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO cargo(nome, descricao) VALUES (?, ?);";

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
                Logger.getLogger(Fornecedor.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }

    }


    public static int insere2(String nome, String descricao) {
        try {

            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into cargo(nome,descricao) values(?,?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, nome);
            ps.setString(2, descricao);
            // executa o comando sql armazenando o resultado. 0 = erro. 1 = sucesso
            int status = ps.executeUpdate(); // pois adiciona ou altera
            // verifica se houve erro
            if (status == 0) {
                return 0;
            } else {
                return 1;
            }
        } // CASO HAJA UMA EXCEÇÃO, MOSTRA ERRO NA TELA
        catch (Exception e) {
            // grava a mensagem de erro em um log no servidor
            e.printStackTrace(System.out);
            if (e.toString().contains("Duplicate")) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    public List<contratos.Cargo> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM cargo;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.Cargo> cs = new ArrayList<>();

            while (rs.next()) {
                contratos.Cargo c = new contratos.Cargo();
                c.setID(rs.getInt("id"));
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

            String sql = "DELETE FROM cargo WHERE id = " + id + ";";

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
                Logger.getLogger(Fornecedor.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.Cargo c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE cargo SET nome = ?, descricao = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());
            ps.setInt(3, c.getID());

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
                Logger.getLogger(Fornecedor.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
            
           
}
