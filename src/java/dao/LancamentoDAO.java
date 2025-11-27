package dao;

import model.Lancamento;
import util.ConexaoFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a persistência dos Lançamentos (aplicações e agendamentos).
 */
public class LancamentoDAO {

    /**
     * Insere um novo lançamento (agendamento futuro) no banco de dados.
     */
    public boolean criar(Lancamento lancamento) {
        String sql = "INSERT INTO lancamento (dose_id, paciente_id, dose_sequencia, data_prevista, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lancamento.getDoseId());
            stmt.setInt(2, lancamento.getPacienteId());
            stmt.setInt(3, lancamento.getDoseSequencia());
            stmt.setDate(4, Date.valueOf(lancamento.getDataPrevista()));
            stmt.setString(5, lancamento.getStatus());

            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        lancamento.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao criar lançamento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca um lançamento por ID.
     */
    public Lancamento buscarPorId(int id) {
        String sql = "SELECT * FROM lancamento WHERE id = ?";
        Lancamento lancamento = null;

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lancamento = new Lancamento(
                        rs.getInt("id"),
                        rs.getInt("dose_id"),
                        rs.getInt("paciente_id"),
                        rs.getInt("dose_sequencia"),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getDate("data_aplicacao") != null ? rs.getDate("data_aplicacao").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("local_aplicacao")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar lançamento por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return lancamento;
    }
    
    /**
     * Lista todos os lançamentos (aplicados e pendentes) de um paciente (RF06).
     */
    public List<Lancamento> listarPorPaciente(int pacienteId) {
        List<Lancamento> lancamentos = new ArrayList<>();
        // Ordena para que os pendentes/atrasados venham primeiro, depois os aplicados
        String sql = "SELECT * FROM lancamento WHERE paciente_id = ? ORDER BY data_prevista ASC";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lancamento lancamento = new Lancamento(
                        rs.getInt("id"),
                        rs.getInt("dose_id"),
                        rs.getInt("paciente_id"),
                        rs.getInt("dose_sequencia"),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getDate("data_aplicacao") != null ? rs.getDate("data_aplicacao").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("local_aplicacao")
                    );
                    lancamentos.add(lancamento);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar lançamentos por paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return lancamentos;
    }

    /**
     * Atualiza um lançamento com os dados de aplicação (RF05).
     */
    public boolean registrarAplicacao(int lancamentoId, LocalDate dataAplicacao, String localAplicacao) {
        String sql = "UPDATE lancamento SET data_aplicacao = ?, status = 'Aplicada', local_aplicacao = ? WHERE id = ? AND status = 'Pendente'";
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataAplicacao));
            stmt.setString(2, localAplicacao);
            stmt.setInt(3, lancamentoId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar aplicação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Encontra o último lançamento aplicado de uma Dose específica.
     */
    public Lancamento buscarUltimoAplicado(int doseId) {
        String sql = "SELECT * FROM lancamento WHERE dose_id = ? AND status = 'Aplicada' ORDER BY dose_sequencia DESC LIMIT 1";
        Lancamento lancamento = null;
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lancamento = new Lancamento(
                        rs.getInt("id"),
                        rs.getInt("dose_id"),
                        rs.getInt("paciente_id"),
                        rs.getInt("dose_sequencia"),
                        rs.getDate("data_prevista").toLocalDate(),
                        rs.getDate("data_aplicacao") != null ? rs.getDate("data_aplicacao").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("local_aplicacao")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar último aplicado: " + e.getMessage());
            e.printStackTrace();
        }
        return lancamento;
    }
}