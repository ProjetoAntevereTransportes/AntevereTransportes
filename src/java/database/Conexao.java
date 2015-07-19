package database;

import java.sql.*;
import library.Settings;

/**
 * @author FELIPE
 */
public class Conexao {
    public static boolean isDevelopment = true;
    public static String databaseProducao = "anteveretransportes";
    public static String databaseDev = "anteveretransportesdev";

    public static Connection abrirConexao() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String database = databaseDev;
            if (!isDevelopment) {
                database = databaseProducao;
            }

            String url = "jdbc:mysql://127.0.0.1/" + database + "?user=root&password=root";
            con = DriverManager.getConnection(url);
            System.out.println("Conexão aberta.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return con;
    }

    public static void fecharConexao(Connection con) {
        try {
            con.close();
            System.out.println("Conexão Encerrada.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
