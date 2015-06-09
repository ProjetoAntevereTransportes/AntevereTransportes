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
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class Viagem {

    public Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }
    
    public contratos.Viagem ler(int viagemID){
        try {
            abrir();
            String sql = "select viagem.id,\n"
                    + "viagem.funcionario_id,\n"
                    + "viagem.valor,\n"
                    + "viagem.tipo_carga_id,\n"
                    + "viagem.cliente_id,\n"
                    + "viagem.adiantamento,\n"
                    + "viagem.status_viagem_id,\n"
                    + "viagem.motivo_cancelamento,\n"
                    + "viagem.caminhao_id,\n"
                    + "funcionario.nome as nome_funcionario,\n"
                    + "carga.nome as nome_carga,\n"
                    + "cliente.nome as nome_cliente,\n"
                    + "status_viagem.nome as nome_status_viagem,\n"
                    + "caminhao.nome as nome_caminhao,\n"
                    + "caminhao.placa as placa_caminhao\n"
                    + "from viagem\n"
                    + "inner join funcionario on funcionario.id = viagem.funcionario_id\n"
                    + "inner join carga on carga.id = viagem.tipo_carga_id\n"
                    + "inner join cliente on cliente.id = viagem.cliente_id\n"
                    + "inner join status_viagem on status_viagem.id = viagem.status_viagem_id\n"
                    + "inner join caminhao on caminhao.id = viagem.caminhao_id"
                    + " where viagem.id = ?\n";

            PreparedStatement ps = con.prepareCall(sql);
            
            ps.setInt(1, viagemID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contratos.Viagem v = new contratos.Viagem();

                v.setAdiantamento(rs.getDouble("adiantamento"));
                v.setCaminhaoID(rs.getInt("caminhao_id"));
                v.setClienteID(rs.getInt("cliente_id"));                
                v.setFuncionarioID(rs.getInt("funcionario_id"));
                v.setID(rs.getInt("id"));
                v.setMotivo(rs.getString("motivo_cancelamento"));
                v.setStatusViagemID(rs.getInt("status_viagem_id"));
                v.setTipoCargaID(rs.getInt("tipo_carga_id"));
                v.setValor(rs.getDouble("valor"));
                v.setNomeCaminhao(rs.getString("nome_caminhao"));
                v.setNomeCarga(rs.getString("nome_carga"));
                v.setNomeCliente(rs.getString("nome_cliente"));
                v.setNomeFunctionario(rs.getString("nome_funcionario"));
                v.setNomeStatusViagem(rs.getString("nome_status_viagem"));
                v.setPlacarCaminhao(rs.getString("placa_caminhao"));

                String sqlRota = "select * from endereco_viagem where viagem_id = ?;";
                PreparedStatement psRota = con.prepareStatement(sqlRota);
                psRota.setInt(1, v.getID());

                ResultSet rsRota = psRota.executeQuery();
                List<contratos.Rota> rotas = new ArrayList<contratos.Rota>();
                if (rsRota != null) {
                    while (rsRota.next()) {
                        contratos.Rota r = new contratos.Rota();
                        
                        r.setDestino(rsRota.getString("destino"));
                        r.setMetros(rsRota.getString("metros"));
                        r.setPartida(rsRota.getString("partida"));
                        r.setSegundos(rsRota.getString("segundos"));
                        
                        rotas.add(r);
                    }
                }

                v.setRotas(rotas);
                return v;
            }

            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<contratos.Viagem> listar() {
        try {
            abrir();
            String sql = "select viagem.id,\n"
                    + "viagem.funcionario_id,\n"
                    + "viagem.valor,\n"
                    + "viagem.tipo_carga_id,\n"
                    + "viagem.cliente_id,\n"
                    + "viagem.adiantamento,\n"
                    + "viagem.status_viagem_id,\n"
                    + "viagem.motivo_cancelamento,\n"
                    + "viagem.caminhao_id,\n"
                    + "funcionario.nome as nome_funcionario,\n"
                    + "carga.nome as nome_carga,\n"
                    + "cliente.nome as nome_cliente,\n"
                    + "status_viagem.nome as nome_status_viagem,\n"
                    + "caminhao.nome as nome_caminhao,\n"
                    + "caminhao.placa as placa_caminhao\n"
                    + "from viagem\n"
                    + "inner join funcionario on funcionario.id = viagem.funcionario_id\n"
                    + "inner join carga on carga.id = viagem.tipo_carga_id\n"
                    + "inner join cliente on cliente.id = viagem.cliente_id\n"
                    + "inner join status_viagem on status_viagem.id = viagem.status_viagem_id\n"
                    + "inner join caminhao on caminhao.id = viagem.caminhao_id\n";

            PreparedStatement ps = con.prepareCall(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.Viagem> vs = new ArrayList<contratos.Viagem>();
            while (rs.next()) {
                contratos.Viagem v = new contratos.Viagem();

                v.setAdiantamento(rs.getDouble("adiantamento"));
                v.setCaminhaoID(rs.getInt("caminhao_id"));
                v.setClienteID(rs.getInt("cliente_id"));                
                v.setFuncionarioID(rs.getInt("funcionario_id"));
                v.setID(rs.getInt("id"));
                v.setMotivo(rs.getString("motivo_cancelamento"));
                v.setStatusViagemID(rs.getInt("status_viagem_id"));
                v.setTipoCargaID(rs.getInt("tipo_carga_id"));
                v.setValor(rs.getDouble("valor"));
                v.setNomeCaminhao(rs.getString("nome_caminhao"));
                v.setNomeCarga(rs.getString("nome_carga"));
                v.setNomeCliente(rs.getString("nome_cliente"));
                v.setNomeFunctionario(rs.getString("nome_funcionario"));
                v.setNomeStatusViagem(rs.getString("nome_status_viagem"));
                v.setPlacarCaminhao(rs.getString("placa_caminhao"));

                String sqlRota = "select * from endereco_viagem where viagem_id = ?;";
                PreparedStatement psRota = con.prepareStatement(sqlRota);
                psRota.setInt(1, v.getID());

                ResultSet rsRota = psRota.executeQuery();
                List<contratos.Rota> rotas = new ArrayList<contratos.Rota>();
                if (rsRota != null) {
                    while (rsRota.next()) {
                        contratos.Rota r = new contratos.Rota();
                        
                        r.setDestino(rsRota.getString("destino"));
                        r.setMetros(rsRota.getString("metros"));
                        r.setPartida(rsRota.getString("partida"));
                        r.setSegundos(rsRota.getString("segundos"));
                        
                        rotas.add(r);
                    }
                }

                v.setRotas(rotas);
                vs.add(v);
            }

            return vs;
        } catch (Exception ex) {
            return null;
        }
    }

    public Boolean inserir(contratos.Viagem viagem) {
        try {
            abrir();
            con.setAutoCommit(false);
            
            if(viagem.getID() != 0){
                String removeEndereco = "delete from endereco_viagem where viagem_id = ?;";
                String removeViagem = "delete from viagem where id = ?;";
                
                PreparedStatement psEndereco = con.prepareStatement(removeEndereco);
                psEndereco.setInt(1, viagem.getID());
                
                int status = psEndereco.executeUpdate();
                if(status == 0){
                    con.rollback();
                    return false;
                }
                
                PreparedStatement psViagem = con.prepareStatement(removeViagem);
                psViagem.setInt(1, viagem.getID());
                
                int statusViagem = psViagem.executeUpdate();
                if(statusViagem == 0){
                    con.rollback();
                    return false;
                }                
            }
            
            String sql = "insert into viagem(funcionario_id, valor, tipo_carga_id, cliente_id, adiantamento,"
                    + " status_viagem_id, caminhao_id)\n"
                    + "values(?,?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, viagem.getFuncionarioID());
            ps.setDouble(2, viagem.getValor());
            ps.setInt(3, viagem.getTipoCargaID());
            ps.setInt(4, viagem.getClienteID());
            ps.setDouble(5, viagem.getAdiantamento());
            ps.setInt(6, 1);
            ps.setInt(7, viagem.getCaminhaoID());

            int status = ps.executeUpdate();

            if (status == 1) {
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                int viagemID = keys.getInt(1);
                String rotaSql = "insert into endereco_viagem(viagem_id, partida, destino, metros, segundos) values\n"
                        + "(?,?,?,?,?);";

                for (contratos.Rota r : viagem.getRotas()) {
                    PreparedStatement pRota = con.prepareStatement(rotaSql);
                    pRota.setInt(1, viagemID);
                    pRota.setString(2, r.getPartida());
                    pRota.setString(3, r.getDestino());
                    pRota.setString(4, r.getMetros());
                    pRota.setString(5, r.getSegundos());

                    int statusRota = pRota.executeUpdate();
                    if (statusRota != 1) {
                        con.rollback();
                        return false;
                    }
                }

                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Viagem.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
    }
}
