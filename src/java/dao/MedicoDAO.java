package dao;

import model.Medico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.ConexaoFactory;

// O DAO para a entidade Medico, implementando o acesso seguro a dados via JDBC.
public class MedicoDAO {

    /**
     * Busca um médico por e-mail e senha para autenticação (RF01).
     * Usa PreparedStatement para evitar SQL Injection (Melhoria de Segurança).
     * Usa try-with-resources para garantir o fechamento de recursos (RNF03).
     * @param email
     * @param senha
     * @return 
     */
    public Medico buscarPorCredenciais(String email, String senha) {
        // Query com placeholders '?' (PreparedStatement)
        String sql = "SELECT id, nome, crm, email FROM medico WHERE email = ? AND senha = ?";

        // O try-with-resources fecha automaticamente Connection, PreparedStatement e ResultSet
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1. Atribui os valores aos placeholders
            stmt.setString(1, email);
            stmt.setString(2, senha); // Aviso: Lembre-se de hash para a senha em produção!
            
            // 2. Executa a query
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    // Mapeamento usando métodos getX() corretos
                    Medico medico = new Medico();
                    medico.setId(rs.getInt("id"));
                    medico.setNome(rs.getString("nome"));
                    medico.setCrm(rs.getString("crm"));
                    medico.setEmail(rs.getString("email"));
                    return medico;
                }
            }
        } catch (SQLException e) {
            // RNF07 - Registro de Erros
            System.err.println("Erro SQL na autenticação do médico: " + e.getMessage());
        } 
        return null;
    }
}