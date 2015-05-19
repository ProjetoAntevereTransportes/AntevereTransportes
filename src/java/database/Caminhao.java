/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author felipe
 */
public class Caminhao {
    public static Connection con;
    public static int insere(String nome, String placa, String renavam, String cor, String marca, String modelo, String data_compra, int ano_modelo, int gasto_kilometros){
        try {

            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into caminhao(nome,placa,renavam,cor,marca,modelo,data_compra,ano_modelo,gasto_kilometros) values(?,?,?,?,?,?,?,?,?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql);
            // substitui os "?" pelos respectivos valores
            ps.setString(1,nome);
            ps.setString(2,placa);
            ps.setString(3,renavam);
            ps.setString(4,cor);
            ps.setString(5,marca);
            ps.setString(6,modelo);
            ps.setString(7,data_compra);
            ps.setInt(8,ano_modelo);
            ps.setInt(9,gasto_kilometros);
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
}
