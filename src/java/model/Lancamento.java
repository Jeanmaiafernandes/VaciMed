package model;

import java.time.LocalDate;

/**
 * Representa uma aplicação de dose (realizada) ou um agendamento futuro (RF05, RF06, RF07).
 */
public class Lancamento {
    private int id;
    private int doseId; // Relação N:1 com Dose
    private int pacienteId; // Relação N:1 com Paciente (Redundante, mas útil para consultas)
    private int doseSequencia; // Ex: 1ª dose, 2ª dose
    private LocalDate dataPrevista;
    private LocalDate dataAplicacao; // Null se pendente
    private String status; // "Pendente", "Aplicada", "Atrasada"
    private String localAplicacao; // Ex: "Braço esquerdo", "SUS - Posto A"

    // Construtor Completo
    public Lancamento(int id, int doseId, int pacienteId, int doseSequencia, LocalDate dataPrevista, LocalDate dataAplicacao, String status, String localAplicacao) {
        this.id = id;
        this.doseId = doseId;
        this.pacienteId = pacienteId;
        this.doseSequencia = doseSequencia;
        this.dataPrevista = dataPrevista;
        this.dataAplicacao = dataAplicacao;
        this.status = status;
        this.localAplicacao = localAplicacao;
    }
    
    // Construtor para Criação/Agendamento
    public Lancamento(int doseId, int pacienteId, int doseSequencia, LocalDate dataPrevista, String status) {
        this.doseId = doseId;
        this.pacienteId = pacienteId;
        this.doseSequencia = doseSequencia;
        this.dataPrevista = dataPrevista;
        this.status = status;
        this.dataAplicacao = null;
        this.localAplicacao = null;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDoseId() { return doseId; }
    public void setDoseId(int doseId) { this.doseId = doseId; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getDoseSequencia() { return doseSequencia; }
    public void setDoseSequencia(int doseSequencia) { this.doseSequencia = doseSequencia; }
    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }
    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocalAplicacao() { return localAplicacao; }
    public void setLocalAplicacao(String localAplicacao) { this.localAplicacao = localAplicacao; }

    // Método auxiliar para determinar se o lançamento está atrasado
    public boolean isAtrasada() {
        return "Pendente".equals(status) && dataPrevista.isBefore(LocalDate.now());
    }
}