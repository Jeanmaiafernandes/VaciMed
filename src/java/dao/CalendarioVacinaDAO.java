// CalendarioVacinaDAO.java
package dao;

import model.CalendarioVacina;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarioVacinaDAO {
    
    public List<CalendarioVacina> listarTodas() {
        List<CalendarioVacina> vacinas = new ArrayList<>();
        String sql = "SELECT * FROM calendario_vacina WHERE ativo = TRUE ORDER BY nome_vacina, idade_recomendada_meses";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                vacinas.add(criarCalendarioVacina(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vacinas do calendário", e);
        }
        
        return vacinas;
    }
    
    public CalendarioVacina buscarPorId(int id) {
        String sql = "SELECT * FROM calendario_vacina WHERE id_calendario_vacina = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarCalendarioVacina(rs);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vacina por ID", e);
        }
        
        return null;
    }
    
    public List<CalendarioVacina> buscarPorIdade(int idadeMeses) {
        List<CalendarioVacina> vacinas = new ArrayList<>();
        String sql = "SELECT * FROM calendario_vacina WHERE idade_recomendada_meses <= ? AND ativo = TRUE ORDER BY idade_recomendada_meses";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idadeMeses);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vacinas.add(criarCalendarioVacina(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vacinas por idade", e);
        }
        
        return vacinas;
    }
    
    private CalendarioVacina criarCalendarioVacina(ResultSet rs) throws SQLException {
        CalendarioVacina vacina = new CalendarioVacina();
        vacina.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
        vacina.setNomeVacina(rs.getString("nome_vacina"));
        vacina.setDescricao(rs.getString("descricao"));
        vacina.setNumeroDose(rs.getString("numero_dose"));
        vacina.setIdadeRecomendadaMeses(rs.getInt("idade_recomendada_meses"));
        vacina.setIntervaloDosesMeses(rs.getInt("intervalo_doses_meses"));
        vacina.setDosesNecessarias(rs.getInt("doses_necessarias"));
        vacina.setAtivo(rs.getBoolean("ativo"));
        return vacina;
    }
}