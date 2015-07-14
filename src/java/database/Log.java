/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import contratos.ModuloEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Log {
    Connection con;
    
    public List<contratos.Log> getList(contratos.LogFilter f){
        try{
            List<contratos.Log> logs = new ArrayList<>();
            String sql = "select * from log where (MONTH(data) = ?)"
                    + " and (YEAR(data) = ?)\n" +
                      " and (modulo_id = ? or ? is null) order by data desc;";
            con = Conexao.abrirConexao();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, f.getMonth());
            ps.setInt(2, f.getYear());
            
            if(f.getModuloId() != 0){
                ps.setInt(3, f.getModuloId());
                ps.setInt(4, f.getModuloId());
            }else{
                ps.setNull(3, Types.INTEGER);
                ps.setNull(4, Types.INTEGER);
            }
                            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                contratos.Log l = new contratos.Log();
                l.setData(rs.getTimestamp("data"));
                l.setException(rs.getString("exception"));
                l.setId(rs.getInt("id"));
                l.setIsError(rs.getBoolean("isError"));
                l.setMessage(rs.getString("message"));
                l.setModulo(contratos.ModuloEnum.values()[rs.getInt("modulo_id")]);
                l.setObject(rs.getString("object"));
                
                logs.add(l);
            }
            
            Conexao.fecharConexao(con);
            return logs;
        }catch(Exception e){
            Conexao.fecharConexao(con);
            library.Log.writeError("Erro ao listar os logs.", e.toString(), ModuloEnum.LOG);
            return null;
        }
    }
}
