// Dose.java
package model;

import java.time.LocalDate;

public class Dose {
    private int idDose;
    private int idPaciente;
    private int idCalendarioVacina;
    private String tipoDose;
    private String descricao;
    private int periodicidadeMeses;
    private int dosesPrevistas;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private String status;
    private String observacoes;
    
    // Construtores, getters e setters
    public Dose() {}
    
    // Getters e Setters
    public int getIdDose() { return idDose; }
    public void setIdDose(int idDose) { this.idDose = idDose; }
    
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    
    public int getIdCalendarioVacina() { return idCalendarioVacina; }
    public void setIdCalendarioVacina(int idCalendarioVacina) { this.idCalendarioVacina = idCalendarioVacina; }
    
    public String getTipoDose() { return tipoDose; }
    public void setTipoDose(String tipoDose) { this.tipoDose = tipoDose; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public int getPeriodicidadeMeses() { return periodicidadeMeses; }
    public void setPeriodicidadeMeses(int periodicidadeMeses) { this.periodicidadeMeses = periodicidadeMeses; }
    
    public int getDosesPrevistas() { return dosesPrevistas; }
    public void setDosesPrevistas(int dosesPrevistas) { this.dosesPrevistas = dosesPrevistas; }
    
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataTermino() { return dataTermino; }
    public void setDataTermino(LocalDate dataTermino) { this.dataTermino = dataTermino; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}