package dao;

import model.Lancamento;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LancamentoDAO {
    
    /**
     * Busca todos os lançamentos de um médico
     */
    public List<Lancamento> buscarPorMedico(int idMedico) {
        List<Lancamento> lancamentos = new ArrayList<>();
        
        String sql = "SELECT l.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM lancamento l " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE p.id_medico = ? " +
                    "ORDER BY l.data_prevista DESC, p.nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Lancamento lancamento = criarLancamentoBasico(rs);
                lancamentos.add(lancamento);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar lançamentos do médico: " + e.getMessage(), e);
        }
        
        return lancamentos;
    }
    
    /**
     * Busca lançamentos por status
     */
    public List<Lancamento> buscarPorStatus(int idMedico, String status) {
        List<Lancamento> lancamentos = new ArrayList<>();
        
        String sql = "SELECT l.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM lancamento l " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE p.id_medico = ? AND l.status = ? " +
                    "ORDER BY l.data_prevista DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Lancamento lancamento = criarLancamentoBasico(rs);
                lancamentos.add(lancamento);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar lançamentos por status: " + e.getMessage(), e);
        }
        
        return lancamentos;
    }
    
    /**
     * Busca lançamento por ID
     */
    public Lancamento buscarPorId(int idLancamento) {
        String sql = "SELECT l.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM lancamento l " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE l.id_lancamento = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLancamento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return criarLancamentoBasico(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lista lançamentos por paciente
     */
    public List<Lancamento> listarPorPaciente(int idPaciente) {
        List<Lancamento> lancamentos = new ArrayList<>();
        
        String sql = "SELECT l.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM lancamento l " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE l.id_paciente = ? " +
                    "ORDER BY l.data_prevista DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lancamentos.add(criarLancamentoBasico(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lancamentos;
    }
    
    /**
     * Atualiza um lançamento completo
     */
    public void atualizar(Lancamento lancamento) {
        String sql = "UPDATE lancamento SET data_aplicacao = ?, status = ?, lote_vacina = ?, " +
                    "local_aplicacao = ?, observacoes = ?, id_medico_aplicador = ?, id_dose = ? " +
                    "WHERE id_lancamento = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Data de aplicação
            if (lancamento.getDataAplicacao() != null) {
                stmt.setDate(1, java.sql.Date.valueOf(lancamento.getDataAplicacao()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            
            stmt.setString(2, lancamento.getStatus());
            stmt.setString(3, lancamento.getLoteVacina());
            stmt.setString(4, lancamento.getLocalAplicacao());
            stmt.setString(5, lancamento.getObservacoes());
            
            // Médico aplicador
            if (lancamento.getIdMedicoAplicador() > 0) {
                stmt.setInt(6, lancamento.getIdMedicoAplicador());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            // ID da dose
            if (lancamento.getIdDose() > 0) {
                stmt.setInt(7, lancamento.getIdDose());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setInt(8, lancamento.getIdLancamento());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar lançamento: " + e.getMessage(), e);
        }
    }
    
    /**
     * Atualiza apenas o status de um lançamento
     */
    public void atualizarStatus(int idLancamento, String status) {
        String sql = "UPDATE lancamento SET status = ? WHERE id_lancamento = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, idLancamento);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar status do lançamento: " + e.getMessage(), e);
        }
    }
    
    /**
     * Insere um novo lançamento
     */
    public void inserir(Lancamento lancamento) {
        String sql = "INSERT INTO lancamento (id_paciente, id_calendario_vacina, data_prevista, status, observacoes) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, lancamento.getIdPaciente());
            stmt.setInt(2, lancamento.getIdCalendarioVacina());
            stmt.setDate(3, java.sql.Date.valueOf(lancamento.getDataPrevista()));
            stmt.setString(4, lancamento.getStatus());
            stmt.setString(5, lancamento.getObservacoes());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lancamento.setIdLancamento(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir lançamento: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca lançamentos próximos (próximos 7 dias)
     */
    public List<Lancamento> buscarProximos(int idMedico) {
        List<Lancamento> lancamentos = new ArrayList<>();
        
        String sql = "SELECT l.*, p.nome as nome_paciente, cv.nome_vacina " +
                    "FROM lancamento l " +
                    "JOIN paciente p ON l.id_paciente = p.id_paciente " +
                    "JOIN calendario_vacina cv ON l.id_calendario_vacina = cv.id_calendario_vacina " +
                    "WHERE p.id_medico = ? AND l.data_prevista BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                    "ORDER BY l.data_prevista ASC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lancamentos.add(criarLancamentoBasico(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lancamentos;
    }
    
    /**
     * Cria objeto Lancamento básico a partir do ResultSet
     */
    private Lancamento criarLancamentoBasico(ResultSet rs) throws SQLException {
        Lancamento lancamento = new Lancamento();
        
        // Campos básicos
        lancamento.setIdLancamento(rs.getInt("id_lancamento"));
        lancamento.setIdPaciente(rs.getInt("id_paciente"));
        lancamento.setIdCalendarioVacina(rs.getInt("id_calendario_vacina"));
        lancamento.setIdDose(rs.getInt("id_dose"));
        lancamento.setIdMedicoAplicador(rs.getInt("id_medico_aplicador"));
        
        // Datas
        java.sql.Date dataPrevista = rs.getDate("data_prevista");
        if (dataPrevista != null) {
            lancamento.setDataPrevista(dataPrevista.toLocalDate());
        }
        
        java.sql.Date dataAplicacao = rs.getDate("data_aplicacao");
        if (dataAplicacao != null) {
            lancamento.setDataAplicacao(dataAplicacao.toLocalDate());
        }
        
        // Outros campos
        lancamento.setStatus(rs.getString("status"));
        lancamento.setLoteVacina(rs.getString("lote_vacina"));
        lancamento.setLocalAplicacao(rs.getString("local_aplicacao"));
        lancamento.setObservacoes(rs.getString("observacoes"));
        
        // Campos para exibição
        lancamento.setNomePaciente(rs.getString("nome_paciente"));
        lancamento.setNomeVacina(rs.getString("nome_vacina"));
        
        return lancamento;
    }
}