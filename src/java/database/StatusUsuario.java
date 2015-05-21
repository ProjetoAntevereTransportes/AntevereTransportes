/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import static database.Pergunta.con;
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
public class StatusUsuario {
    public static Connection con;
    
    public void abrir(){
        con = Conexao.abrirConexao();
    
    }
    
    public int insere(String nome){
        try {
            abrir();
            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into status_usuario(nome) values(?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, nome);
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
        }finally{
            Conexao.fecharConexao(con);
        }
    }
    public List<contratos.StatusUsuario> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM status_usuario;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rsF = ps.executeQuery();

            List<contratos.StatusUsuario> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.StatusUsuario f = new contratos.StatusUsuario();
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
