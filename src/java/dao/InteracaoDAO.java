// InteracaoDAO.java
package dao;

import model.InteracaoMedicamentoVacina;
import util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteracaoDAO {
    
    public List<InteracaoMedicamentoVacina> buscarInteracoes(String medicamento, String vacina) {
        List<InteracaoMedicamentoVacina> interacoes = new ArrayList<>();
        String sql = "SELECT * FROM interacao_medicamento_vacina WHERE medicamento LIKE ? AND vacina LIKE ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + medicamento + "%");
            stmt.setString(2, "%" + vacina + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                interacoes.add(criarInteracao(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar interações", e);
        }
        
        return interacoes;
    }
    
    public List<InteracaoMedicamentoVacina> buscarPorMedicamento(String medicamento) {
        List<InteracaoMedicamentoVacina> interacoes = new ArrayList<>();
        String sql = "SELECT * FROM interacao_medicamento_vacina WHERE medicamento LIKE ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + medicamento + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                interacoes.add(criarInteracao(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar interações por medicamento", e);
        }
        
        return interacoes;
    }
    
    private InteracaoMedicamentoVacina criarInteracao(ResultSet rs) throws SQLException {
        InteracaoMedicamentoVacina interacao = new InteracaoMedicamentoVacina();
        interacao.setIdInteracao(rs.getInt("id_interacao"));
        interacao.setMedicamento(rs.getString("medicamento"));
        interacao.setVacina(rs.getString("vacina"));
        interacao.setTipoInteracao(rs.getString("tipo_interacao"));
        interacao.setDescricaoInteracao(rs.getString("descricao_interacao"));
        interacao.setIntervaloMinimoDias(rs.getInt("intervalo_minimo_dias"));
        interacao.setGravidade(rs.getString("gravidade"));
        return interacao;
    }
}