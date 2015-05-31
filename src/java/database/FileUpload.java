/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.tomcat.util.http.fileupload.FileItem;

/**
 *
 * @author lucas
 */
public class FileUpload {

    private Connection con;

    private void abrir() {
        con = Conexao.abrirConexao();
    }

    public int uploadFile(FileItem file) {
        try {
            abrir();
            String sql = "INSERT INTO arquivo "
                    + "(nome, tamanho_bytes, data_criacao, arquivo) VALUES (?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            File f = new File(file.getName());

            ps.setString(1, f.getName());
            ps.setString(2, String.valueOf(file.getSize()));
            ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));

            InputStream inputStream = file.getInputStream();

            ps.setBlob(4, inputStream);

            int status = ps.executeUpdate();

            if (status == 1) {
                ResultSet result = ps.getGeneratedKeys();
                result.next();                
                return result.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public contratos.Arquivo getFile(int id) {
        try {
            abrir();
            String sql = "select * from arquivo where id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, id);
            
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                contratos.Arquivo arquivo = new contratos.Arquivo();
                
                arquivo.setNome(resultSet.getString("nome"));
                arquivo.setTamanho(resultSet.getString("tamanho_bytes"));
                arquivo.setCriacao(resultSet.getDate("data_criacao"));
                
                File file = File.createTempFile(arquivo.getNome() + " - "
                        + arquivo.getCriacao().toGMTString() , ".tmp"); 
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1];
                InputStream is = resultSet.getBinaryStream("arquivo");
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
                
                fos.close();                               
                
                arquivo.setFile(file);
                
                return arquivo;
            }
            
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao(con);
        }
    }

}
