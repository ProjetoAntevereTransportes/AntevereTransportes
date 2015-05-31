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
 * @author Bruno
 */
public class Funcionario {

    public static Connection con;
    public static Connection conEndereco;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public contratos.Funcionario pegarPeloID(int funcionarioID) {
        try {
            abrir();
            String sql = "SELECT * FROM funcionario WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, funcionarioID);

            ResultSet rs = ps.executeQuery();

            rs.next();
            //Funcionario
            contratos.Funcionario f = new contratos.Funcionario();
            f.setNome(rs.getString("f.nome"));
            f.setSobrenome(rs.getString("f.sobrenome"));
            f.setTelefone(rs.getString("f.telefone"));
            f.setEmail(rs.getString("f.email"));
            f.setCargo_id(rs.getInt("f.cargo_id"));
            f.setCpf(rs.getString("f.cpf"));
            f.setRg(rs.getString("f.rg"));
            f.setEndereco_id(rs.getInt("f.endereco_id"));

            //Endereço
            database.Endereco endereco = new Endereco();
            f.setEndereco(endereco.get(f.getEndereco_id()));

            //Cargo
            database.Cargo cargo = new Cargo();
            f.setCargo(cargo.pegarPeloID(f.getCargo_id()));

            return f;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Funcionario pegarPeloID - Problemas no SQL");
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean inserir(contratos.Funcionario f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO funcionario (NOME, SOBRENOME, TELEFONE, EMAIL, CARGO_ID, CPF, RG, ENDERECO_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, f.getNome());
            ps.setString(2, f.getSobrenome());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getEmail());
            ps.setInt(5, f.getCargo().getId());//cargo
            ps.setString(6, f.getCpf());
            ps.setString(7, f.getRg());

            //Endereço
            database.Endereco endereco = new Endereco();
            int enderecoID = endereco.insere(f.getEndereco());
            ps.setInt(8, enderecoID);
            
            int status = ps.executeUpdate();

            if (status != 1) {
                con.rollback();
                return false;
            } else {
                con.commit();
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Fornecedor.class.getName()).log(Level.SEVERE, null, ex1);
                System.out.println("Funcionario Inserir - Problemas com o SQL");
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<contratos.Funcionario> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM funcionario;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.Funcionario> fs = new ArrayList<>();
            
            while (rs.next()) {
                //Funcionario
                contratos.Funcionario f = new contratos.Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setSobrenome(rs.getString("sobrenome"));
                f.setTelefone(rs.getString("telefone"));
                f.setEmail(rs.getString("email"));
                f.setCargo_id(rs.getInt("cargo_id"));
                f.setCpf(rs.getString("cpf"));
                f.setRg(rs.getString("rg"));
                f.setEndereco_id(rs.getInt("endereco_id"));

                //Endereço
                database.Endereco endereco = new Endereco();
                f.setEndereco(endereco.get(f.getEndereco_id()));

                //Cargo
                database.Cargo cargo = new Cargo();
                f.setCargo(cargo.pegarPeloID(f.getCargo_id()));

                fs.add(f);
            }

            return fs;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Funcionario listar - Problemas com o SQL");
            return null;
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

            String sql = "DELETE FROM funcionario WHERE id = " + id + ";";

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
                System.out.println("Funcionario excluir - Problemas com o SQL");
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.Funcionario f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE FUNCIONARIO SET nome = ?, sobrenome = ?, telefone = ?, email = ?, cargo_id = ?, "
                    + "cpf = ?, rg = ?, endereco_id = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getSobrenome());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getEmail());
            ps.setInt(5, f.getCargo().getId());
            ps.setString(6, f.getCpf());
            ps.setString(7, f.getRg());
            
            //Endereço
            database.Endereco endereco = new Endereco();
            int enderecoID = endereco.insere(f.getEndereco());
            ps.setInt(8, enderecoID);

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

}
