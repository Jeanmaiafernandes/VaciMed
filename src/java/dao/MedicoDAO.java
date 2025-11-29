// MedicoDAO.java
package dao;
/**
 *
 * @author jeanm
 */
import model.Medico;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {
    
    public Medico autenticar(String email, String senha) {
        String sql = "SELECT * FROM medico WHERE email = ? AND senha = ? AND ativo = TRUE";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarMedico(rs);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar médico", e);
        }
        
        return null;
    }
    
    public List<Medico> listarTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medico WHERE ativo = TRUE";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                medicos.add(criarMedico(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos", e);
        }
        
        return medicos;
    }
    
    private Medico criarMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setIdMedico(rs.getInt("id_medico"));
        medico.setNome(rs.getString("nome"));
        medico.setCrm(rs.getString("crm"));
        medico.setEmail(rs.getString("email"));
        medico.setSenha(rs.getString("senha"));
        medico.setTelefone(rs.getString("telefone"));
        medico.setEspecialidade(rs.getString("especialidade"));
        medico.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        medico.setAtivo(rs.getBoolean("ativo"));
        return medico;
    }
}