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
public class Caminhao {
 
    private Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public List<contratos.Caminhao> listar() {
        try {
            abrir();
            String sql = "select * from caminhao;";

            PreparedStatement ps = con.prepareStatement(sql);
            
            ResultSet rsF = ps.executeQuery();

            List<contratos.Caminhao> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Caminhao f = new contratos.Caminhao();
                f.setId(rsF.getInt("id"));
                f.setNome(rsF.getString("nome"));
                f.setPlaca(rsF.getString("placa"));
                f.setRenavam(rsF.getString("renavam"));
                f.setCor(rsF.getString("cor"));
                f.setMarca(rsF.getString("marca"));
                f.setModelo(rsF.getString("modelo"));
                f.setData_compra(rsF.getDate("data_compra"));
                f.setAno(rsF.getInt("ano_modelo"));
                f.setGasto(rsF.getInt("gasto_kilometros"));
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

    public boolean Inserir(contratos.Caminhao f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO caminhao(nome, placa, renavam, cor, marca,modelo, data_compra, ano_modelo,gasto_kilometros)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?,?,?);";

            java.sql.Date sqlData_compra = new java.sql.Date(f.getData_compra().getDate());
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getPlaca());
            ps.setString(3, f.getRenavam());
            ps.setString(4, f.getCor());
            ps.setString(5, f.getMarca());
            ps.setString(6, f.getModelo());
            ps.setDate(7, sqlData_compra);
            ps.setInt(8, f.getAno());
            ps.setInt(9, f.getGasto());
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

            String sql = "DELETE FROM caminhao WHERE id = " + id + ";";

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

    public boolean editar(contratos.Caminhao f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE caminhao set nome = ?, placa = ?, renavam = ?, cor = ?, marca = ?,modelo = ?, data_compra = ?, ano_modelo = ?,gasto_kilometros = ?  WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
           java.sql.Date sqlData_compra = new java.sql.Date(f.getData_compra().getDate());
            ps.setString(1, f.getNome());
            ps.setString(2, f.getPlaca());
            ps.setString(3, f.getRenavam());
            ps.setString(4, f.getCor());
            ps.setString(5, f.getMarca());
            ps.setString(6, f.getModelo());
            ps.setDate(7, sqlData_compra);            
            ps.setInt(8, f.getAno());
            ps.setInt(9, f.getGasto());
            ps.setInt(10, f.getId());

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
