package dao;

import model.Dose;
import util.ConexaoFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a persistência das Doses/Esquemas Terapêuticos.
 */
public class DoseDAO {

    /**
     * Insere um novo esquema de dose no banco de dados (RF03).
     * @return O ID gerado ou -1 em caso de falha.
     */
    public int criar(Dose dose) {
        String sql = "INSERT INTO dose (nome, num_doses, periodicidade_valor, periodicidade_unidade, paciente_id, medicamentos_interacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, dose.getNome());
            stmt.setInt(2, dose.getNumDoses());
            stmt.setInt(3, dose.getPeriodicidadeValor());
            stmt.setString(4, dose.getPeriodicidadeUnidade());
            stmt.setInt(5, dose.getPacienteId());
            stmt.setString(6, dose.getMedicamentosInteracao());

            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        dose.setId(rs.getInt(1));
                        return dose.getId();
                    }
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("Erro ao criar dose: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Lista todos os esquemas de dose de um paciente (RF04).
     */
    public List<Dose> listarPorPaciente(int pacienteId) {
        List<Dose> doses = new ArrayList<>();
        String sql = "SELECT * FROM dose WHERE paciente_id = ? ORDER BY nome";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dose dose = new Dose(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("num_doses"),
                        rs.getInt("periodicidade_valor"),
                        rs.getString("periodicidade_unidade"),
                        rs.getInt("paciente_id"),
                        rs.getString("medicamentos_interacao")
                    );
                    doses.add(dose);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar doses por paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return doses;
    }
    
    /**
     * Busca uma dose por ID.
     */
    public Dose buscarPorId(int id) {
        String sql = "SELECT * FROM dose WHERE id = ?";
        Dose dose = null;

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dose = new Dose(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("num_doses"),
                        rs.getInt("periodicidade_valor"),
                        rs.getString("periodicidade_unidade"),
                        rs.getInt("paciente_id"),
                        rs.getString("medicamentos_interacao")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dose por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return dose;
    }

    public List<Dose> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}