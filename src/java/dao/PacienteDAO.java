// PacienteDAO.java - COMPLETO E CORRIGIDO
package dao;

import model.Paciente;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    
    public void inserir(Paciente paciente) {
        String sql = "INSERT INTO paciente (id_medico, nome, cpf, data_nascimento, sexo, telefone, email, endereco, alergias, medicamentos_uso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, paciente.getIdMedico());
            stmt.setString(2, paciente.getNome());
            stmt.setString(3, paciente.getCpf());

            if (paciente.getDataNascimento() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(paciente.getDataNascimento()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, paciente.getSexo());
            stmt.setString(6, paciente.getTelefone());
            stmt.setString(7, paciente.getEmail());
            stmt.setString(8, paciente.getEndereco());
            stmt.setString(9, paciente.getAlergias());
            stmt.setString(10, paciente.getMedicamentosUso());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                paciente.setIdPaciente(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 👈 Adicionar para debug
            throw new RuntimeException("Erro ao inserir paciente: " + e.getMessage(), e);
        }
    }
    
    public void atualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, data_nascimento = ?, sexo = ?, telefone = ?, email = ?, endereco = ?, alergias = ?, medicamentos_uso = ? WHERE id_paciente = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(4, paciente.getSexo());
            stmt.setString(5, paciente.getTelefone());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getEndereco());
            stmt.setString(8, paciente.getAlergias());
            stmt.setString(9, paciente.getMedicamentosUso());
            stmt.setInt(10, paciente.getIdPaciente());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
    }
    
    public Paciente buscarPorId(int idPaciente) {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ? AND ativo = TRUE";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarPaciente(rs);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente por ID", e);
        }
        
        return null;
    }
    
    public List<Paciente> listarPorMedico(int idMedico) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE id_medico = ? AND ativo = TRUE ORDER BY nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pacientes.add(criarPaciente(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes por médico", e);
        }
        
        return pacientes;
    }
    
    public List<Paciente> listarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE ativo = TRUE ORDER BY nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pacientes.add(criarPaciente(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os pacientes", e);
        }
        
        return pacientes;
    }
    
    public void excluir(int idPaciente) {
        String sql = "UPDATE paciente SET ativo = FALSE WHERE id_paciente = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir paciente", e);
        }
    }
    
    private Paciente criarPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setIdMedico(rs.getInt("id_medico"));
        paciente.setNome(rs.getString("nome"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        paciente.setSexo(rs.getString("sexo"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setEmail(rs.getString("email"));
        paciente.setEndereco(rs.getString("endereco"));
        paciente.setAlergias(rs.getString("alergias"));
        paciente.setMedicamentosUso(rs.getString("medicamentos_uso"));
        paciente.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        paciente.setAtivo(rs.getBoolean("ativo"));
        return paciente;
    }
}