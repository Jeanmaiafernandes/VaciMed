package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsável por gerenciar a conexão com o banco de dados.
public class ConexaoFactory {
    
    // Mantenha as constantes de URL, USER e PASSWORD como atributos da classe
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/vacimed_db?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // O método que será chamado pelo DAO (Linha 44 na sua stack, renomeado do conectaDB)
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Tentando carregar driver MySQL...");
            // Usamos a constante DRIVER definida globalmente
            Class.forName(DRIVER); 
            System.out.println("Driver MySQL carregado com sucesso!");
            
            System.out.println("Conectando ao banco: " + URL);
            // Usamos a constante PASSWORD definida globalmente
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão estabelecida com sucesso!");
            
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO: Driver MySQL não encontrado! ");
            throw new SQLException("Driver JDBC não encontrado: " + e.getMessage());
        } 
        // Não precisamos capturar SQLException aqui, pois o método já declara 'throws SQLException'
    }
    
    // Método para fechar a conexão (boa prática)
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}