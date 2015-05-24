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
 * @author Felipe_Botelho
 */
public class Usuario {
      public Connection con;

    public void abrir(){
        con = Conexao.abrirConexao();
    }
 public List<contratos.Usuario> listar() {
        try {
            abrir();
            String sql = "select usuario.id, usuario.nome, usuario.email, tipo_usuario.nome as tipo, status_usuario.nome as statusu from usuario "
                    + "inner join tipo_usuario on (usuario.tipo_usuario_id = tipo_usuario.id) "
                    + "inner join status_usuario on (usuario.status_id = status_usuario.id);";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rsF = ps.executeQuery();

            List<contratos.Usuario> fs = new ArrayList<>();

            while (rsF.next()) {
                contratos.Usuario u = new contratos.Usuario();
                u.setId(rsF.getInt("id"));
                u.setNome(rsF.getString("nome"));
                u.setEmail(rsF.getString("email"));
                u.setTipoUsuarioNome(rsF.getString("tipo"));
                u.setStatusNome(rsF.getString("statusu"));
                fs.add(u);
            }

            return fs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
 
     public boolean Excluir(int id) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "DELETE FROM usuario WHERE id = " + id + ";";

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
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
   
    
        public boolean Inserir(contratos.Usuario f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "INSERT INTO usuario(nome, email, senha, pergunta_id,resposta,tipo_usuario_id,status_id)"
                    + "VALUES (?, ?, ?, ? , ? , ? , ? );";

            PreparedStatement ps = con.prepareStatement(sql);
            String senha  = f.getSenha();
            authentication.MD5 md5 = new authentication.MD5();
            senha = md5.gerar(senha);
            
            ps.setString(1, f.getNome());
            ps.setString(2, f.getEmail());
            ps.setString(3, senha);
            ps.setInt(4,f.getPerguntaID());
            ps.setString(5,f.getResposta());
            ps.setInt(6,f.getTipoUsuarioID());
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
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }

    }
        
        public boolean editar(contratos.Usuario f) {
        try {
            abrir();
            con.setAutoCommit(false);

            String sql = "UPDATE usuario set nome = ?, email = ?, senha = ? WHERE id = ?;";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, f.getNome());
            ps.setString(2, f.getEmail());
            ps.setString(3, f.getSenha());
            ps.setInt(4, f.getId());

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
    

    public contratos.Usuario get(String login, String senha) {
        try {
            Statement st = con.createStatement();
            String sql = "select * from usuario"
                    + " where email = '" + login + "' and senha = '" + senha + "';";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            rs.next();
            contratos.Usuario u = new contratos.Usuario();
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            u.setId(rs.getInt("id"));
            u.setStatusID(rs.getInt("status_id"));
            u.setTipoUsuarioID(rs.getInt("tipo_usuario_id"));
            u.setEmail(rs.getString("email"));
            
            return u;
        } catch (Exception e) {
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public boolean ValidaUsuario(String usuario, String senha) {
        try {
            Statement st = con.createStatement();
            String sql = "select * from usuario"
                    + " where email = '" + usuario + "' and senha = '" + senha + "';";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs == null) {
                return false;
            }

            int count = 0;

            while (rs.next()) {
                ++count;
            }

            if (count != 1) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            Conexao.fecharConexao(con);
        }
    }
}
