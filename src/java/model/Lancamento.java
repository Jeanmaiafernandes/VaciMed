package model;

import java.time.LocalDate;

public class Lancamento {
    private int idLancamento;
    private int idPaciente;
    private int idCalendarioVacina;
    private int idDose;
    private int idMedicoAplicador;
    private LocalDate dataPrevista;
    private LocalDate dataAplicacao;
    private String status;
    private String loteVacina;
    private String localAplicacao;
    private String observacoes;
    
    // Campos adicionais para exibição
    private String nomePaciente;
    private String nomeVacina;
    private String nomeMedicoAplicador;
    
    // Construtores
    public Lancamento() {}
    
    public Lancamento(int idPaciente, int idCalendarioVacina, LocalDate dataPrevista, String status) {
        this.idPaciente = idPaciente;
        this.idCalendarioVacina = idCalendarioVacina;
        this.dataPrevista = dataPrevista;
        this.status = status;
    }
    
    // Getters e Setters para TODOS os campos
    public int getIdLancamento() { return idLancamento; }
    public void setIdLancamento(int idLancamento) { this.idLancamento = idLancamento; }
    
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    
    public int getIdCalendarioVacina() { return idCalendarioVacina; }
    public void setIdCalendarioVacina(int idCalendarioVacina) { this.idCalendarioVacina = idCalendarioVacina; }
    
    public int getIdDose() { return idDose; }
    public void setIdDose(int idDose) { this.idDose = idDose; }
    
    public int getIdMedicoAplicador() { return idMedicoAplicador; }
    public void setIdMedicoAplicador(int idMedicoAplicador) { this.idMedicoAplicador = idMedicoAplicador; }
    
    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }
    
    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLoteVacina() { return loteVacina; }
    public void setLoteVacina(String loteVacina) { this.loteVacina = loteVacina; }
    
    public String getLocalAplicacao() { return localAplicacao; }
    public void setLocalAplicacao(String localAplicacao) { this.localAplicacao = localAplicacao; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    // Campos para exibição
    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }
    
    public String getNomeVacina() { return nomeVacina; }
    public void setNomeVacina(String nomeVacina) { this.nomeVacina = nomeVacina; }
    
    public String getNomeMedicoAplicador() { return nomeMedicoAplicador; }
    public void setNomeMedicoAplicador(String nomeMedicoAplicador) { this.nomeMedicoAplicador = nomeMedicoAplicador; }
}