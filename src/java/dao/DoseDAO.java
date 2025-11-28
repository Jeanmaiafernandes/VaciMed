// DoseDAO.java
package dao;

import model.Dose;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoseDAO {
    
    public void inserir(Dose dose) {
        String sql = "INSERT INTO dose (id_paciente, id_calendario_vacina, tipo_dose, descricao, periodicidade_meses, doses_previstas, data_inicio, data_termino, status, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, dose.getIdPaciente());
            stmt.setInt(2, dose.getIdCalendarioVacina());
            stmt.setString(3, dose.getTipoDose());
            stmt.setString(4, dose.getDescricao());
            stmt.setInt(5, dose.getPeriodicidadeMeses());
            stmt.setInt(6, dose.getDosesPrevistas());
            stmt.setDate(7, dose.getDataInicio() != null ? Date.valueOf(dose.getDataInicio()) : null);
            stmt.setDate(8, dose.getDataTermino() != null ? Date.valueOf(dose.getDataTermino()) : null);
            stmt.setString(9, dose.getStatus());
            stmt.setString(10, dose.getObservacoes());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                dose.setIdDose(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dose", e);
        }
    }
    
    public List<Dose> listarPorPaciente(int idPaciente) {
        List<Dose> doses = new ArrayList<>();
        String sql = "SELECT d.*, cv.nome_vacina FROM dose d " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE d.id_paciente = ? AND d.status != 'inativo'";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doses.add(criarDose(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar doses do paciente", e);
        }
        
        return doses;
    }
    
    public Dose buscarPorId(int idDose) {
        String sql = "SELECT d.*, cv.nome_vacina FROM dose d " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE d.id_dose = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDose);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarDose(rs);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dose por ID", e);
        }
        
        return null;
    }
    
    private Dose criarDose(ResultSet rs) throws SQLException {
        Dose dose = new Dose();
        dose.setIdDose(rs.getInt("id_dose"));
        dose.setIdPaciente(rs.getInt("id_paciente"));
        dose.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
        dose.setTipoDose(rs.getString("tipo_dose"));
        dose.setDescricao(rs.getString("descricao"));
        dose.setPeriodicidadeMeses(rs.getInt("periodicidade_meses"));
        dose.setDosesPrevistas(rs.getInt("doses_previstas"));
        
        Date dataInicio = rs.getDate("data_inicio");
        if (dataInicio != null) {
            dose.setDataInicio(dataInicio.toLocalDate());
        }
        
        Date dataTermino = rs.getDate("data_termino");
        if (dataTermino != null) {
            dose.setDataTermino(dataTermino.toLocalDate());
        }
        
        dose.setStatus(rs.getString("status"));
        dose.setObservacoes(rs.getString("observacoes"));
        return dose;
    }
}