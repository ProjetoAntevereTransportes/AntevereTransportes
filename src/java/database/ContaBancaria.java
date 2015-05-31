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
public class ContaBancaria {

    Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public contratos.ContaBancaria pegarPeloID(int contaBancariaID) {
        try {
            abrir();
            String sql = "SELECT * FROM conta_bancaria WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, contaBancariaID);

            ResultSet rs = ps.executeQuery();

            rs.next();
            contratos.ContaBancaria c = new contratos.ContaBancaria();
            c.setNome(rs.getString("nome"));
            c.setNumero(rs.getString("numero"));
            c.setPessoa_id(rs.getInt("pessoa_id"));
            c.setBanco_id(rs.getInt("banco_id"));

            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean Inserir(contratos.ContaBancaria c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO conta_bancaria (nome, numero, agencia, pessoa_id, banco_id) VALUES (?, ?,? , ?, ?);";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getNumero());
            ps.setString(3, c.getAgencia());
            ps.setInt(4, c.getPessoa_id());
            ps.setInt(5, c.getBanco_id());

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
                Logger.getLogger(ContaBancaria.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }

    }

    public List<contratos.ContaBancaria> listar() {
        try {
            abrir();
            String sql = "SELECT * FROM conta_bancaria AS cb INNER JOIN pessoa AS p on cb.pessoa_id = p.id inner join banco as b on cb.banco_id = b.id;";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.ContaBancaria> cts = new ArrayList<>();

            while (rs.next()) {
                contratos.ContaBancaria c = new contratos.ContaBancaria();
                c.setId(rs.getInt("cb.id"));
                c.setNome(rs.getString("cb.nome"));
                c.setAgencia(rs.getString("cb.agencia"));
                c.setNumero(rs.getString("cb.numero"));
                c.setPessoa_id(rs.getInt("cb.pessoa_id"));
                c.setBanco_id(rs.getInt("cb.banco_id"));
                c.setBanco_nome(rs.getString("b.nome"));
                c.setPessoa_nome(rs.getString("p.nome"));

                cts.add(c);
            }

            return cts;
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

            String sql = "DELETE FROM conta_bancaria WHERE id = " + id + ";";

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
                Logger.getLogger(ContaBancaria.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean editar(contratos.ContaBancaria c) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE conta_bancaria SET nome = ?, numero= ?, agencia = ?, pessoa_id = ?, banco_id = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getNumero());
            ps.setString(3, c.getAgencia());
            ps.setInt(4, c.getPessoa_id());
            ps.setInt(5, c.getBanco_id());
            ps.setInt(6, c.getId());

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
                Logger.getLogger(ContaBancaria.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
