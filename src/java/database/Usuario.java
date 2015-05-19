/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felipe
 */
public class Usuario {

    public Connection con;

    public void abrir(){
        con = Conexao.abrirConexao();
    }
 public List<contratos.Usuario> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM usuario;";

            PreparedStatement ps = con.prepareStatement(sql);

            database.Pergunta pergunta = new Pergunta();
            database.TipoUsuario tipoUsuario = new TipoUsuario();
            database.StatusUsuario status = new StatusUsuario();

            ResultSet rsF = ps.executeQuery();

            List<contratos.Usuario> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Usuario u = new contratos.Usuario();
                u.setNome(rsF.getString("nome"));
                u.setEmail(rsF.getString("email"));
                u.setTipoUsuarioID(rsF.getInt("tipo_usuario_id"));
                u.setId(rsF.getInt("id"));
                u.setStatusID(rsF.getInt("status_id"));
                fs.add(u);
            }

            return fs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
    public int insere(String login, String senha) {

        // TENTA EXECUTAR OS COMANDOS NO BD
        try {
            abrir();
            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into usuario(login,senha) values(?,?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, login);
            ps.setString(2, senha);
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

    public contratos.Usuario get(String login, String senha) {
        try {
            Statement st = con.createStatement();
            String sql = "select * from usuario"
                    + " where email = '" + login + "' and senha = '" + senha + "';";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            rs.next();
            contratos.Usuario u = new contratos.Usuario();
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            u.setId(rs.getInt("id"));
            u.setStatusID(rs.getInt("status_id"));
            u.setTipoUsuarioID(rs.getInt("tipo_usuario_id"));
            u.setEmail(rs.getString("email"));
            
            return u;
        } catch (Exception e) {
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean ValidaUsuario(String usuario, String senha) {
        try {
            Statement st = con.createStatement();
            String sql = "select * from usuario"
                    + " where email = '" + usuario + "' and senha = '" + senha + "';";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs == null) {
                return false;
            }

            int count = 0;

            while (rs.next()) {
                ++count;
            }

            if (count != 1) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
