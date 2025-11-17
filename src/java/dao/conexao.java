package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author jeanm
 */
public class conexao {
     private static final String URL = "jdbc:mysql://localhost:3306/vacimed";
    private static final String USER = "root";
    private static final String PASS = ""; 

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao MySQL: " + e.getMessage());
            return null;
        }
    }
}
