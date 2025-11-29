package model;

import java.util.Date;

public class Dose {
    private int idDose;
    private int idPaciente;
    private int idCalendarioVacina;
    private String tipoDose;
    private String descricao;
    private int periodicidadeMeses;
    private int dosesPrevistas;
    private Date dataInicio;
    private Date dataTermino;
    private String status;
    private String observacoes;
    
    // Campos adicionais para exibição
    private String nomePaciente;
    private String nomeVacina;
    
    // Construtores
    public Dose() {}
    
    // Getters e Setters para todos os campos
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
    
    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    
    public Date getDataTermino() { return dataTermino; }
    public void setDataTermino(Date dataTermino) { this.dataTermino = dataTermino; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    // Novos getters e setters para campos de exibição
    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }
    
    public String getNomeVacina() { return nomeVacina; }
    public void setNomeVacina(String nomeVacina) { this.nomeVacina = nomeVacina; }
}