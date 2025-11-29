package service;

import model.Dose;
import model.Lancamento;
import dao.DoseDAO;
import dao.LancamentoDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VacinacaoService {
    private DoseDAO doseDAO;
    private LancamentoDAO lancamentoDAO;
    
    public VacinacaoService() {
        this.doseDAO = new DoseDAO();
        this.lancamentoDAO = new LancamentoDAO();
    }
    
    /**
     * Gera doses futuras baseadas no lançamento atual
     */
    public void gerarDosesFuturas(Lancamento lancamentoAtual) {
        try {
            // Buscar informações da vacina para saber quantas doses são necessárias
            // Aqui você precisaria de um método no CalendarioVacinaDAO para buscar pela ID
            
            // Exemplo: se a vacina precisa de 3 doses com intervalo de 2 meses
            int dosesNecessarias = 3; // Isso viria do calendário vacinal
            int intervaloMeses = 2;   // Isso viria do calendário vacinal
            
            for (int i = 2; i <= dosesNecessarias; i++) { // Começa da dose 2
                Lancamento proximoLancamento = new Lancamento();
                proximoLancamento.setIdPaciente(lancamentoAtual.getIdPaciente());
                proximoLancamento.setIdCalendarioVacina(lancamentoAtual.getIdCalendarioVacina());
                proximoLancamento.setDataPrevista(lancamentoAtual.getDataAplicacao().plusMonths(intervaloMeses * (i - 1)));
                proximoLancamento.setStatus("pendente");
                proximoLancamento.setObservacoes("Dose " + i + " de " + dosesNecessarias);
                
                lancamentoDAO.inserir(proximoLancamento);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar doses futuras: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica interações medicamentosas
     */
    public List<String> verificarInteracoes(int idPaciente, int idVacina) {
        List<String> alertas = new ArrayList<>();
        
        try {
            // Aqui você implementaria a lógica para verificar interações
            // Por enquanto, retornamos uma lista vazia
            
            // Exemplo de verificação:
            // 1. Buscar medicamentos em uso do paciente
            // 2. Buscar contraindicações da vacina
            // 3. Verificar interações
            
            // alertas.add("Alerta: Vacina X tem interação com medicamento Y");
            
        } catch (Exception e) {
            alertas.add("Erro ao verificar interações: " + e.getMessage());
        }
        
        return alertas;
    }
    
    /**
     * Marca um lançamento como aplicado
     */
    public void marcarComoAplicado(int idLancamento, int idMedicoAplicador, String loteVacina, 
                                  String localAplicacao, String observacoes, int doseId) {
        try {
            Lancamento lancamento = lancamentoDAO.buscarPorId(idLancamento);
            
            if (lancamento != null) {
                lancamento.setDataAplicacao(LocalDate.now());
                lancamento.setStatus("aplicada");
                lancamento.setLoteVacina(loteVacina);
                lancamento.setLocalAplicacao(localAplicacao);
                lancamento.setObservacoes(observacoes);
                lancamento.setIdMedicoAplicador(idMedicoAplicador);
                lancamento.setIdDose(doseId);
                
                lancamentoDAO.atualizar(lancamento);
                
                // Gerar próximas doses se necessário
                gerarDosesFuturas(lancamento);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao marcar como aplicado: " + e.getMessage(), e);
        }
    }
}