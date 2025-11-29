package dao;

import model.Dose;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoseDAO {
    
    /**
     * Insere uma nova dose (versão simplificada)
     */
    public void inserir(Dose dose) {
        String sql = "INSERT INTO dose (id_paciente, id_calendario_vacina, tipo_dose, descricao, data_inicio, status, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, dose.getIdPaciente());
            stmt.setInt(2, dose.getIdCalendarioVacina());
            stmt.setString(3, dose.getTipoDose());
            stmt.setString(4, dose.getDescricao() != null ? dose.getDescricao() : "Dose aplicada");
            stmt.setDate(5, new java.sql.Date(dose.getDataInicio().getTime()));
            stmt.setString(6, dose.getStatus() != null ? dose.getStatus() : "Aplicada");
            stmt.setString(7, dose.getObservacoes());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                dose.setIdDose(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir dose: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca todas as doses de um médico - MÉTODO PRINCIPAL
     */
    public List<Dose> buscarPorMedico(int idMedico) {
        List<Dose> doses = new ArrayList<>();
        
        String sql = "SELECT d.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM dose d " +
                    "JOIN paciente p ON d.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE p.id_medico = ? " +
                    "ORDER BY d.data_inicio DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Dose dose = new Dose();
                
                // Campos básicos da dose
                dose.setIdDose(rs.getInt("id_dose"));
                dose.setIdPaciente(rs.getInt("id_paciente"));
                dose.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
                dose.setTipoDose(rs.getString("tipo_dose"));
                dose.setDescricao(rs.getString("descricao"));
                dose.setDataInicio(rs.getDate("data_inicio"));
                dose.setStatus(rs.getString("status"));
                dose.setObservacoes(rs.getString("observacoes"));
                
                // Campos adicionais para exibição
                dose.setNomePaciente(rs.getString("nome_paciente"));
                dose.setNomeVacina(rs.getString("nome_vacina"));
                
                doses.add(dose);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar doses do médico: " + e.getMessage(), e);
        }
        
        return doses;
    }
    
    /**
     * Busca dose por ID
     */
    public Dose buscarPorId(int idDose) {
        String sql = "SELECT d.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM dose d " +
                    "JOIN paciente p ON d.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE d.id_dose = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDose);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Dose dose = new Dose();
                dose.setIdDose(rs.getInt("id_dose"));
                dose.setIdPaciente(rs.getInt("id_paciente"));
                dose.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
                dose.setTipoDose(rs.getString("tipo_dose"));
                dose.setDescricao(rs.getString("descricao"));
                dose.setDataInicio(rs.getDate("data_inicio"));
                dose.setStatus(rs.getString("status"));
                dose.setObservacoes(rs.getString("observacoes"));
                dose.setNomePaciente(rs.getString("nome_paciente"));
                dose.setNomeVacina(rs.getString("nome_vacina"));
                
                return dose;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Busca doses por paciente
     */
    public List<Dose> buscarPorPaciente(int idPaciente) {
        List<Dose> doses = new ArrayList<>();
        String sql = "SELECT d.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM dose d " +
                    "JOIN paciente p ON d.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE d.id_paciente = ? " +
                    "ORDER BY d.data_inicio DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Dose dose = new Dose();
                dose.setIdDose(rs.getInt("id_dose"));
                dose.setIdPaciente(rs.getInt("id_paciente"));
                dose.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
                dose.setTipoDose(rs.getString("tipo_dose"));
                dose.setDescricao(rs.getString("descricao"));
                dose.setDataInicio(rs.getDate("data_inicio"));
                dose.setStatus(rs.getString("status"));
                dose.setObservacoes(rs.getString("observacoes"));
                dose.setNomePaciente(rs.getString("nome_paciente"));
                dose.setNomeVacina(rs.getString("nome_vacina"));
                
                doses.add(dose);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return doses;
    }
    
    /**
     * Atualiza uma dose
     */
    public void atualizar(Dose dose) {
        String sql = "UPDATE dose SET tipo_dose = ?, descricao = ?, data_inicio = ?, status = ?, observacoes = ? WHERE id_dose = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dose.getTipoDose());
            stmt.setString(2, dose.getDescricao());
            stmt.setDate(3, new java.sql.Date(dose.getDataInicio().getTime()));
            stmt.setString(4, dose.getStatus());
            stmt.setString(5, dose.getObservacoes());
            stmt.setInt(6, dose.getIdDose());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar dose: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exclui uma dose
     */
    public void excluir(int idDose) {
        String sql = "DELETE FROM dose WHERE id_dose = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDose);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir dose: " + e.getMessage(), e);
        }
    }
    
    /**
     * Conta total de doses por médico
     */
    public int contarPorMedico(int idMedico) {
        String sql = "SELECT COUNT(*) as total FROM dose d " +
                    "JOIN paciente p ON d.id_paciente = p.id_paciente " +
                    "WHERE p.id_medico = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}