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
public class Funcionario {
    public static Connection con;    
    public static int insere(String nome, String sobrenome, String telefone, String email, int cargo_id, String cpf, String rg){
        try {

            // prepara o statement para execução de um novo comaando
            Statement st = con.createStatement();
            // cria o comando SQL para ser executado
            String sql = "insert into funcionario(nome,sobrenome,telefone,email,cargo_id,cpf,rg) values(?,?,?,?,?,?,?)";
            // prepara o comando para execução, indicando que haverá "?" a substituir
            PreparedStatement ps = con.prepareStatement(sql);
            // substitui os "?" pelos respectivos valores
            ps.setString(1, nome);
            ps.setString(2, sobrenome);
            ps.setString(3, telefone);
            ps.setString(4, email);
            ps.setInt(5, cargo_id);
            ps.setString(6, cpf);
            ps.setString(7, rg);
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

    public boolean editar(contratos.Funcionario f) {
        try {
            abrir();
            con.setAutoCommit(false);

            int enderecoID = 0;

            String sql = "UPDATE FUNCIONARIO SET nome = ?, sobrenome = ?, telefone = ?, email = ?, cargo_id = ?, "
                    + "cpf = ?, rg = ?, endereco_id = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getSobrenome());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getEmail());
            ps.setInt(5, f.getCargo_id());
            ps.setString(6, f.getCpf());
            ps.setString(7, f.getRg());

            int x = enderecoExiste(f.getEndereco());
            if (x == 0) {
                //Endereço ñ existe. Salvar novo Endereço
                database.Endereco endereco = new Endereco();
                enderecoID = endereco.insere(f.getEndereco());
                ps.setInt(8, enderecoID);
            } else {
                //Endereço existe. Não salvar novo Endereço
                ps.setInt(8, x);
            }

            ps.setInt(9, f.getId());

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
                System.out.println("Funcionario editar - Problemas com o SQL");
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    private int enderecoExiste(contratos.Endereco e) {
        try {
            abrir();
            String sql = "SELECT * FROM ENDERECO E "
                    + "WHERE rua = ? AND bairro = ? AND numero = ? AND cidade = ? AND estado = ? AND pais = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, e.getRua());
            ps.setString(2, e.getBairro());
            ps.setString(3, e.getNumero());
            ps.setString(4, e.getCidade());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getPais());

            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                return rs.getInt("id");
            }
            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

}
