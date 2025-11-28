// VacinaçãoService.java - CORRIGIDO
package service;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class VacinacaoService {
    
    private PacienteDAO pacienteDAO;
    private DoseDAO doseDAO;
    private LancamentoDAO lancamentoDAO;
    private CalendarioVacinaDAO calendarioVacinaDAO;
    private InteracaoDAO interacaoDAO;
    
    public VacinacaoService() {
        this.pacienteDAO = new PacienteDAO();
        this.doseDAO = new DoseDAO();
        this.lancamentoDAO = new LancamentoDAO();
        this.calendarioVacinaDAO = new CalendarioVacinaDAO();
        this.interacaoDAO = new InteracaoDAO();
    }
    
    // RF07 - Geração Automática de Doses Futuras
    public void gerarDosesFuturas(Lancamento lancamentoAplicado) {
        Dose dose = doseDAO.buscarPorId(lancamentoAplicado.getIdDose());
        CalendarioVacina vacina = calendarioVacinaDAO.buscarPorId(lancamentoAplicado.getIdCalendarioVacina());
        
        if (vacina.getDosesNecessarias() > 1 && vacina.getIntervaloDosesMeses() > 0) {
            // Calcular próxima dose baseada no calendário
            LocalDate proximaData = lancamentoAplicado.getDataAplicacao().plusMonths(vacina.getIntervaloDosesMeses());
            
            // Verificar se já existe lançamento para esta data
            List<Lancamento> lancamentosExistentes = lancamentoDAO.listarPorPaciente(lancamentoAplicado.getIdPaciente());
            boolean existeLancamento = lancamentosExistentes.stream()
                .anyMatch(l -> l.getIdDose() == dose.getIdDose() && 
                              l.getDataPrevista().equals(proximaData));
            
            if (!existeLancamento) {
                Lancamento proximoLancamento = new Lancamento();
                proximoLancamento.setIdPaciente(lancamentoAplicado.getIdPaciente());
                proximoLancamento.setIdDose(dose.getIdDose());
                proximoLancamento.setIdCalendarioVacina(vacina.getIdCalendarioVacina());
                proximoLancamento.setDataPrevista(proximaData);
                proximoLancamento.setStatus("pendente");
                
                lancamentoDAO.inserir(proximoLancamento);
            }
        }
    }
    
    // RF08 - Alerta de Interação Medicamento x Vacina
    public List<String> verificarInteracoes(int idPaciente, int idCalendarioVacina) {
        List<String> alertas = new ArrayList<>();
        
        Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
        
        if (paciente == null || paciente.getMedicamentosUso() == null || paciente.getMedicamentosUso().trim().isEmpty()) {
            return alertas;
        }
        
        CalendarioVacina vacina = calendarioVacinaDAO.buscarPorId(idCalendarioVacina);
        
        // Simples verificação - em implementação real, fazer parsing dos medicamentos
        String[] medicamentos = paciente.getMedicamentosUso().split(",");
        
        for (String medicamento : medicamentos) {
            if (medicamento.trim().isEmpty()) continue;
            
            List<InteracaoMedicamentoVacina> interacoes = 
                interacaoDAO.buscarPorMedicamento(medicamento.trim());
            
            for (InteracaoMedicamentoVacina interacao : interacoes) {
                if (interacao.getVacina().toLowerCase().contains(vacina.getNomeVacina().toLowerCase()) ||
                    vacina.getNomeVacina().toLowerCase().contains(interacao.getVacina().toLowerCase())) {
                    
                    alertas.add("ALERTA: " + interacao.getDescricaoInteracao() + 
                               " (Gravidade: " + interacao.getGravidade() + ")");
                }
            }
        }
        
        return alertas;
    }
    
    // RF09 - Relatório de Aderência
    public RelatorioAderencia gerarRelatorioAderencia(int idPaciente) {
        List<Lancamento> lancamentos = lancamentoDAO.listarPorPaciente(idPaciente);
        
        long total = lancamentos.size();
        long aplicadas = lancamentos.stream().filter(l -> "aplicada".equals(l.getStatus())).count();
        long pendentes = lancamentos.stream().filter(l -> "pendente".equals(l.getStatus())).count();
        long atrasadas = lancamentos.stream()
            .filter(l -> "pendente".equals(l.getStatus()) && l.getDataPrevista().isBefore(LocalDate.now()))
            .count();
        
        double percentualAderencia = total > 0 ? (double) aplicadas / total * 100 : 0;
        
        return new RelatorioAderencia(total, aplicadas, pendentes, atrasadas, percentualAderencia);
    }
    
    // Calcular idade do paciente em meses
    public int calcularIdadeMeses(Paciente paciente) {
        Period periodo = Period.between(paciente.getDataNascimento(), LocalDate.now());
        return periodo.getYears() * 12 + periodo.getMonths();
    }
    
    // Gerar calendário vacinal sugerido para paciente
    public List<CalendarioVacina> gerarCalendarioSugerido(Paciente paciente) {
        int idadeMeses = calcularIdadeMeses(paciente);
        return calendarioVacinaDAO.buscarPorIdade(idadeMeses);
    }
}