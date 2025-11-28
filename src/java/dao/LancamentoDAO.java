// LancamentoDAO.java
package dao;

import model.Lancamento;
import util.ConnectionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoDAO {
    
    public void inserir(Lancamento lancamento) {
        String sql = "INSERT INTO lancamento (id_paciente, id_dose, id_calendario_vacina, id_medico_aplicador, data_prevista, data_aplicacao, status, lote_vacina, local_aplicacao, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, lancamento.getIdPaciente());
            stmt.setInt(2, lancamento.getIdDose());
            stmt.setInt(3, lancamento.getIdCalendarioVacina());
            
            if (lancamento.getIdMedicoAplicador() != null) {
                stmt.setInt(4, lancamento.getIdMedicoAplicador());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setDate(5, Date.valueOf(lancamento.getDataPrevista()));
            
            if (lancamento.getDataAplicacao() != null) {
                stmt.setDate(6, Date.valueOf(lancamento.getDataAplicacao()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.setString(7, lancamento.getStatus());
            stmt.setString(8, lancamento.getLoteVacina());
            stmt.setString(9, lancamento.getLocalAplicacao());
            stmt.setString(10, lancamento.getObservacoes());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lancamento.setIdLancamento(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir lançamento", e);
        }
    }
    
    public void atualizar(Lancamento lancamento) {
        String sql = "UPDATE lancamento SET data_aplicacao = ?, status = ?, lote_vacina = ?, local_aplicacao = ?, observacoes = ?, id_medico_aplicador = ?, data_atualizacao = CURRENT_TIMESTAMP WHERE id_lancamento = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (lancamento.getDataAplicacao() != null) {
                stmt.setDate(1, Date.valueOf(lancamento.getDataAplicacao()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            
            stmt.setString(2, lancamento.getStatus());
            stmt.setString(3, lancamento.getLoteVacina());
            stmt.setString(4, lancamento.getLocalAplicacao());
            stmt.setString(5, lancamento.getObservacoes());
            
            if (lancamento.getIdMedicoAplicador() != null) {
                stmt.setInt(6, lancamento.getIdMedicoAplicador());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setInt(7, lancamento.getIdLancamento());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar lançamento", e);
        }
    }
    
    // Adicionar este método no LancamentoDAO.java
    public Lancamento buscarPorId(int idLancamento) {
        String sql = "SELECT l.*, cv.nome_vacina, m.nome as nome_medico " +
                    "FROM lancamento l " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "LEFT JOIN medico m ON l.id_medico_aplicador = m.id_medico " +
                    "WHERE l.id_lancamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLancamento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return criarLancamento(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lançamento por ID", e);
        }

        return null;
    }
    
    public List<Lancamento> listarPorPaciente(int idPaciente) {
        List<Lancamento> lancamentos = new ArrayList<>();
        String sql = "SELECT l.*, cv.nome_vacina, m.nome as nome_medico " +
                    "FROM lancamento l " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "LEFT JOIN medico m ON l.id_medico_aplicador = m.id_medico " +
                    "WHERE l.id_paciente = ? " +
                    "ORDER BY l.data_prevista DESC, l.status";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lancamentos.add(criarLancamento(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar lançamentos do paciente", e);
        }
        
        return lancamentos;
    }
    
    // Adicionar este método no LancamentoDAO.java
    public List<Lancamento> listarPendentesPorDose(int idDose) {
        List<Lancamento> lancamentos = new ArrayList<>();
        String sql = "SELECT l.*, cv.nome_vacina FROM lancamento l " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE l.id_dose = ? AND l.status = 'pendente' " +
                    "ORDER BY l.data_prevista ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDose);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lancamentos.add(criarLancamento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar lançamentos pendentes por dose", e);
        }

        return lancamentos;
    }
    
    public List<Lancamento> listarPendentesPorPaciente(int idPaciente) {
        List<Lancamento> lancamentos = new ArrayList<>();
        String sql = "SELECT l.*, cv.nome_vacina FROM lancamento l " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE l.id_paciente = ? AND l.status = 'pendente' " +
                    "ORDER BY l.data_prevista ASC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lancamentos.add(criarLancamento(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar lançamentos pendentes", e);
        }
        
        return lancamentos;
    }
    
    public List<Lancamento> listarProximosPorMedico(int idMedico, int dias) {
        List<Lancamento> lancamentos = new ArrayList<>();
        String sql = "SELECT l.*, cv.nome_vacina, p.nome as nome_paciente " +
                    "FROM lancamento l " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "WHERE p.id_medico = ? AND l.status = 'pendente' " +
                    "AND l.data_prevista BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                    "ORDER BY l.data_prevista ASC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            stmt.setInt(2, dias);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lancamentos.add(criarLancamento(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar próximos lançamentos", e);
        }
        
        return lancamentos;
    }
    
    private Lancamento criarLancamento(ResultSet rs) throws SQLException {
        Lancamento lancamento = new Lancamento();
        lancamento.setIdLancamento(rs.getInt("id_lancamento"));
        lancamento.setIdPaciente(rs.getInt("id_paciente"));
        lancamento.setIdDose(rs.getInt("id_dose"));
        lancamento.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
        
        int idMedicoAplicador = rs.getInt("id_medico_aplicador");
        if (!rs.wasNull()) {
            lancamento.setIdMedicoAplicador(idMedicoAplicador);
        }
        
        lancamento.setDataPrevista(rs.getDate("data_prevista").toLocalDate());
        
        Date dataAplicacao = rs.getDate("data_aplicacao");
        if (dataAplicacao != null) {
            lancamento.setDataAplicacao(dataAplicacao.toLocalDate());
        }
        
        lancamento.setStatus(rs.getString("status"));
        lancamento.setLoteVacina(rs.getString("lote_vacina"));
        lancamento.setLocalAplicacao(rs.getString("local_aplicacao"));
        lancamento.setObservacoes(rs.getString("observacoes"));
        lancamento.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        
        Timestamp dataAtualizacao = rs.getTimestamp("data_atualizacao");
        if (dataAtualizacao != null) {
            lancamento.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }
        
        return lancamento;
    }
}