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
 * @author lucas
 */
public class Fornecedor {

    private Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public contratos.Fornecedor pegarPeloID(int fornecedorID) {
        try {
            abrir();
            String sql = "SELECT * FROM fornecedor WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, fornecedorID);

            database.Endereco endereco = new Endereco();

            ResultSet rsF = ps.executeQuery();

            rsF.next();
            contratos.Fornecedor f = new contratos.Fornecedor();
            f.setEmail(rsF.getString("email"));
            f.setEnderecoID(rsF.getInt("endereco_id"));
            f.setID(rsF.getInt("id"));
            f.setNome(rsF.getString("nome"));
            f.setTelefone(rsF.getString("telefone"));

            f.setEndereco(endereco.get(f.getEnderecoID()));

            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<contratos.Fornecedor> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM fornecedor;";

            PreparedStatement ps = con.prepareStatement(sql);

            database.Endereco endereco = new Endereco();

            ResultSet rsF = ps.executeQuery();

            List<contratos.Fornecedor> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Fornecedor f = new contratos.Fornecedor();
                f.setEmail(rsF.getString("email"));
                f.setEnderecoID(rsF.getInt("endereco_id"));
                f.setID(rsF.getInt("id"));
                f.setNome(rsF.getString("nome"));
                f.setTelefone(rsF.getString("telefone"));

                f.setEndereco(endereco.get(f.getEnderecoID()));
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

    public boolean Inserir(contratos.Fornecedor f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO fornecedor(nome, endereco_id, email, telefone)"
                    + "VALUES (?, ?, ?, ?);";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());

            database.Endereco endereco = new Endereco();
            int enderecoID = endereco.insere(f.getEndereco());

            ps.setInt(2, enderecoID);
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getTelefone());

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
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }

    }

    public boolean excluir(int id) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "DELETE FROM fornecedor WHERE id = " + id + ";";

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

    public boolean editar(contratos.Fornecedor f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE fornecedor set nome = ?, email = ?, telefone = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getEmail());
            ps.setString(3, f.getTelefone());
            ps.setInt(4, f.getID());

            database.Endereco endereco = new Endereco();
            endereco.editar(f.getEndereco());

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
