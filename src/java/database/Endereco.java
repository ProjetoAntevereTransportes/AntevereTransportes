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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipe
 */
public class Endereco {

    public Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public contratos.Endereco get(int enderecoID) {
        try {
            abrir();
            Statement st = con.createStatement();

            String sql = "select * from endereco where id = '" + enderecoID + "'";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet e = ps.executeQuery();
            e.next();

            contratos.Endereco endereco = new contratos.Endereco();
            endereco.setBairro(e.getString("bairro"));
            endereco.setCidade(e.getString("cidade"));
            endereco.setEstado(e.getString("estado"));
            endereco.setId(e.getInt("id"));
            endereco.setNumero(e.getInt("numero"));
            endereco.setPais(e.getString("pais"));
            endereco.setRua(e.getString("rua"));

            return endereco;
        } // CASO HAJA UMA EXCEÇÃO, MOSTRA ERRO NA TELA
        catch (Exception ex) {
            // grava a mensagem de erro em um log no servidor
            ex.printStackTrace(System.out);
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public int insere(contratos.Endereco e) throws SQLException {
        try {
            abrir();
            con.setAutoCommit(false);
            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into endereco(rua,bairro,numero,cidade,estado,pais) values(?,?,?,?,?,?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, e.getRua());
            ps.setString(2, e.getBairro());
            ps.setInt(3, e.getNumero());
            ps.setString(4, e.getCidade());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getPais());
            //ps.setInt(7, dono_id);
            // executa o comando sql armazenando o resultado. 0 = erro. 1 = sucesso
            ps.executeUpdate(); // pois adiciona ou altera
            ResultSet ids = ps.getGeneratedKeys();
            ids.next();
            con.commit();
// verifica se houve erro
            return ids.getInt(1);
        } // CASO HAJA UMA EXCEÇÃO, MOSTRA ERRO NA TELA
        catch (Exception ex) {
            // grava a mensagem de erro em um log no servidor
            ex.printStackTrace(System.out);
            con.rollback();
            return 0;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.Endereco e) {
        try {
            abrir();
            con.setAutoCommit(false);
            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "UPDATE endereco set rua = ?, bairro = ?, numero = ?, cidade = ?, estado = ?, pais = ? where id = ?;";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, e.getRua());
            ps.setString(2, e.getBairro());
            ps.setInt(3, e.getNumero());
            ps.setString(4, e.getCidade());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getPais());
            ps.setInt(7, e.getId());
            //ps.setInt(7, dono_id);
            // executa o comando sql armazenando o resultado. 0 = erro. 1 = sucesso
            int status = ps.executeUpdate(); // pois adiciona ou altera

            con.commit();
// verifica se houve erro
            if(status == 1)
                return true;
            else
                return false;
        } // CASO HAJA UMA EXCEÇÃO, MOSTRA ERRO NA TELA
        catch (Exception ex) {
            // grava a mensagem de erro em um log no servidor
            ex.printStackTrace(System.out);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Endereco.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
