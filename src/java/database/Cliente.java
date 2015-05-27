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
import database.Endereco.*;

/**
 *
 * @author felipe
 */
public class Cliente {

    private Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public List<contratos.Cliente> listar() {
        try {
            abrir();
            String sql = "select cliente.id , cliente.nome, cliente.endereco_id,"
                    + " cliente.email, cliente.telefone, cliente.cnpj, cliente.observacao,"
                    + " status_cliente.id as statusID, status_cliente.nome as statusNome"
                    + " from cliente inner join status_cliente on (cliente.status_id = status_cliente.id)";

            PreparedStatement ps = con.prepareStatement(sql);
            database.Endereco endereco = new Endereco();
            ResultSet rsF = ps.executeQuery();

            List<contratos.Cliente> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Cliente f = new contratos.Cliente();
                f.setNome(rsF.getString("nome"));
                f.setEmail(rsF.getString("email"));
                f.setEnderecoID(rsF.getInt("endereco_id"));
                f.setTelefone(rsF.getString("telefone"));
                f.setCnpj(rsF.getString("cnpj"));
                f.setObservacao(rsF.getString("observacao"));
                f.setId(rsF.getInt("id"));
                f.setStatusID(rsF.getInt("statusID"));
                f.setStatusNome(rsF.getString("statusNome"));
                
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

    public boolean Inserir(contratos.Cliente f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO cliente(nome, email, telefone, cnpj, endereco_id,observacao, status_id)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getEmail());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getCnpj());

            database.Endereco endereco = new Endereco();
            int enderecoID = endereco.insere(f.getEndereco());

            ps.setInt(5, enderecoID);

            ps.setString(6, f.getObservacao());
            ps.setInt(7, f.getStatusID());
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
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex1);
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

            String sql = "DELETE FROM cliente WHERE id = " + id + ";";

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
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.Cliente f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE cliente set nome = ?, email = ?, telefone = ?, cnpj = ?, observacao = ?, status_id = ?  WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getEmail());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getCnpj());
            ps.setString(5, f.getObservacao());
            ps.setInt(6, f.getStatusID());

            ps.setInt(7, f.getId());
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
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
