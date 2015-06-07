/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Viagem {

    public Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public List<contratos.Viagem> listar() {
        try {
            abrir();
            String sql = "select viagem.id,\n"
                    + "viagem.funcionario_id,\n"
                    + "viagem.endereco_carga_id,\n"
                    + "viagem.endereco_descarga_id,\n"
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
                    + "caminhao.placa as placa_caminhao,\n"
                    + "ec.rua as rua_carga,\n"
                    + "ec.bairro as bairro_carga,\n"
                    + "ec.cidade as cidade_carga,\n"
                    + "ec.estado as estado_carga,\n"
                    + "ec.numero as numero_carga,\n"
                    + "ec.pais as pais_carga,\n"
                    + "ed.rua as rua_descarga,\n"
                    + "ed.bairro as bairro_descarga,\n"
                    + "ed.cidade as cidade_descarga,\n"
                    + "ed.estado as estado_descarga,\n"
                    + "ed.numero as numero_descarga,\n"
                    + "ed.pais as pais_descarga\n"
                    + "from viagem\n"
                    + "inner join funcionario on funcionario.id = viagem.funcionario_id\n"
                    + "inner join carga on carga.id = viagem.tipo_carga_id\n"
                    + "inner join cliente on cliente.id = viagem.cliente_id\n"
                    + "inner join status_viagem on status_viagem.id = viagem.status_viagem_id\n"
                    + "inner join caminhao on caminhao.id = viagem.caminhao_id\n"
                    + "inner join endereco as ec on ec.id = viagem.endereco_carga_id\n"
                    + "inner join endereco as ed on ed.id = viagem.endereco_descarga_id;";

            PreparedStatement ps = con.prepareCall(sql);

            ResultSet rs = ps.executeQuery();

            List<contratos.Viagem> vs = new ArrayList<contratos.Viagem>();
            while (rs.next()) {
                contratos.Viagem v = new contratos.Viagem();

                v.setAdiantamento(rs.getDouble("adiantamento"));
                v.setCaminhaoID(rs.getInt("caminhao_id"));
                v.setClienteID(rs.getInt("cliente_id"));
                v.setEnderecoCargaID(rs.getInt("endereco_carga_id"));
                v.setEnderecoDescargaID(rs.getInt("endereco_descarga_id"));
                v.setFuncionarioID(rs.getInt("functionario_id"));
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

                contratos.Endereco carga = new contratos.Endereco();
                carga.setBairro(rs.getString("bairro_carga"));
                carga.setCidade(rs.getString("cidade_carga"));
                carga.setEstado(rs.getString("estado_carga"));
                carga.setNumero(rs.getString("numero_carga"));
                carga.setPais(rs.getString("pais_carga"));
                carga.setRua(rs.getString("rua_carga"));
                v.setEnderecoCarga(carga);
                
                contratos.Endereco descarga = new contratos.Endereco();
                descarga.setBairro(rs.getString("bairro_descarga"));
                descarga.setCidade(rs.getString("cidade_descarga"));
                descarga.setEstado(rs.getString("estado_descarga"));
                descarga.setNumero(rs.getString("numero_descarga"));
                descarga.setPais(rs.getString("pais_descarga"));
                descarga.setRua(rs.getString("rua_descarga"));
                v.setEnderecoDescarga(descarga);
                
                vs.add(v);
            }

            return vs;
        } catch (Exception ex) {
            return null;
        }
    }
}
