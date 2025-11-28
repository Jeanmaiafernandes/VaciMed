// Lancamento.java
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lancamento {
    private int idLancamento;
    private int idPaciente;
    private int idDose;
    private int idCalendarioVacina;
    private Integer idMedicoAplicador;
    private LocalDate dataPrevista;
    private LocalDate dataAplicacao;
    private String status;
    private String loteVacina;
    private String localAplicacao;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtores, getters e setters
    public Lancamento() {}
    
    // Getters e Setters
    public int getIdLancamento() { return idLancamento; }
    public void setIdLancamento(int idLancamento) { this.idLancamento = idLancamento; }
    
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    
    public int getIdDose() { return idDose; }
    public void setIdDose(int idDose) { this.idDose = idDose; }
    
    public int getIdCalendarioVacina() { return idCalendarioVacina; }
    public void setIdCalendarioVacina(int idCalendarioVacina) { this.idCalendarioVacina = idCalendarioVacina; }
    
    public Integer getIdMedicoAplicador() { return idMedicoAplicador; }
    public void setIdMedicoAplicador(Integer idMedicoAplicador) { this.idMedicoAplicador = idMedicoAplicador; }
    
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
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}