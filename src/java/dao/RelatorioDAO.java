package dao;

import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {
    
    /**
     * Conta o total de pacientes do médico
     */
    public int contarPacientesPorMedico(int idMedico) {
        String sql = "SELECT COUNT(*) as total FROM paciente WHERE id_medico = ? AND ativo = TRUE";
        
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
    
    /**
     * Conta o total de vacinas aplicadas pelo médico
     */
    public int contarVacinasAplicadasPorMedico(int idMedico) {
        String sql = "SELECT COUNT(*) as total FROM dose WHERE id_medico = ?";
        
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
    
    /**
     * Retorna as vacinas mais aplicadas pelo médico
     */
    public List<Object[]> vacinasMaisAplicadasPorMedico(int idMedico) {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT cv.nome_vacina, COUNT(d.id_dose) as total " +
                    "FROM dose d " +
                    "JOIN calendario_vacina cv ON d.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE d.id_medico = ? " +
                    "GROUP BY cv.nome_vacina " +
                    "ORDER BY total DESC " +
                    "LIMIT 5";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] linha = new Object[2];
                linha[0] = rs.getString("nome_vacina");
                linha[1] = rs.getInt("total");
                resultados.add(linha);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultados;
    }
    
    /**
     * Retorna a cobertura vacinal por tipo de vacina
     */
    public List<Object[]> coberturaVacinalPorMedico(int idMedico) {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT cv.nome_vacina, " +
                    "COUNT(DISTINCT p.id_paciente) as total_pacientes, " +
                    "COUNT(DISTINCT d.id_paciente) as pacientes_vacinados, " +
                    "ROUND((COUNT(DISTINCT d.id_paciente) * 100.0 / COUNT(DISTINCT p.id_paciente)), 2) as cobertura " +
                    "FROM paciente p " +
                    "CROSS JOIN calendario_vacina cv " +
                    "LEFT JOIN dose d ON p.id_paciente = d.id_paciente AND cv.id_calendario_vacina = d.id_calendario_vacina " +
                    "WHERE p.id_medico = ? AND p.ativo = TRUE " +
                    "GROUP BY cv.nome_vacina " +
                    "ORDER BY cobertura DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] linha = new Object[4];
                linha[0] = rs.getString("nome_vacina");
                linha[1] = rs.getInt("total_pacientes");
                linha[2] = rs.getInt("pacientes_vacinados");
                linha[3] = rs.getDouble("cobertura");
                resultados.add(linha);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultados;
    }
    
    /**
     * Relatório de pacientes por faixa etária
     */
    public List<Object[]> pacientesPorFaixaEtaria(int idMedico) {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT " +
                    "CASE " +
                    "    WHEN TIMESTAMPDIFF(MONTH, data_nascimento, CURDATE()) < 12 THEN '0-1 ano' " +
                    "    WHEN TIMESTAMPDIFF(MONTH, data_nascimento, CURDATE()) BETWEEN 12 AND 23 THEN '1-2 anos' " +
                    "    WHEN TIMESTAMPDIFF(MONTH, data_nascimento, CURDATE()) BETWEEN 24 AND 59 THEN '2-5 anos' " +
                    "    ELSE 'Acima de 5 anos' " +
                    "END as faixa_etaria, " +
                    "COUNT(*) as total " +
                    "FROM paciente " +
                    "WHERE id_medico = ? AND ativo = TRUE AND data_nascimento IS NOT NULL " +
                    "GROUP BY faixa_etaria " +
                    "ORDER BY MIN(TIMESTAMPDIFF(MONTH, data_nascimento, CURDATE()))";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] linha = new Object[2];
                linha[0] = rs.getString("faixa_etaria");
                linha[1] = rs.getInt("total");
                resultados.add(linha);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultados;
    }
}