package dao;

import model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import util.ConexaoFactory;

public class PacienteDAO {

    /**
     * Busca um paciente por CPF e senha para autenticação.
     * @return O objeto Paciente autenticado, ou null.
     */
    public Paciente buscarPorCredenciais(String cpf, String senha) {
        // Usamos CPF como login de exemplo
        String sql = "SELECT id, medico_id, nome, cpf, data_nascimento FROM paciente WHERE cpf = ? AND senha = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    Paciente paciente = new Paciente();
                    paciente.setId((int) rs.getLong("id"));
                    paciente.setMedicoId(rs.getLong("medico_id"));
                    paciente.setNome(rs.getString("nome"));
                    paciente.setCpf(rs.getString("cpf"));
                    
                    // ADICIONE ESTA LINHA: Converter SQL Date para LocalDate
                if (rs.getDate("data_nascimento") != null) {
                    paciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                }
    
                    return paciente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL na autenticação do paciente: " + e.getMessage());
        } 
        return null;
    }
    
    // Os métodos cadastrar, buscar por ID, etc., viriam aqui (RF02).

    public List<Paciente> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Paciente criar(Paciente paciente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Paciente atualizar(Paciente paciente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Paciente buscarPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void excluir(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}