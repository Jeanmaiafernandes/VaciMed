package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/vacimed_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Senha vazia no XAMPP padrão
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL carregado com sucesso");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Erro ao carregar driver do MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        System.out.println("🔗 Tentando conectar: " + URL);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("✅ Conexão estabelecida com sucesso");
        return conn;
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Conexão fechada");
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}