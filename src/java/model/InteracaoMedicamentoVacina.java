// InteracaoMedicamentoVacina.java
package model;

public class InteracaoMedicamentoVacina {
    private int idInteracao;
    private String medicamento;
    private String vacina;
    private String tipoInteracao;
    private String descricaoInteracao;
    private Integer intervaloMinimoDias;
    private String gravidade;
    
    // Construtores
    public InteracaoMedicamentoVacina() {}
    
    public InteracaoMedicamentoVacina(
            String medicamento, String vacina, String tipoInteracao,                  
            String descricaoInteracao, String gravidade) {
        this.medicamento = medicamento;
        this.vacina = vacina;
        this.tipoInteracao = tipoInteracao;
        this.descricaoInteracao = descricaoInteracao;
        this.gravidade = gravidade;
    }
    
    // Getters e Setters
    public int getIdInteracao() { return idInteracao; }
    public void setIdInteracao(int idInteracao) { this.idInteracao = idInteracao; }
    
    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    
    public String getVacina() { return vacina; }
    public void setVacina(String vacina) { this.vacina = vacina; }
    
    public String getTipoInteracao() { return tipoInteracao; }
    public void setTipoInteracao(String tipoInteracao) { this.tipoInteracao = tipoInteracao; }
    
    public String getDescricaoInteracao() { return descricaoInteracao; }
    public void setDescricaoInteracao(String descricaoInteracao) { this.descricaoInteracao = descricaoInteracao; }
    
    public Integer getIntervaloMinimoDias() { return intervaloMinimoDias; }
    public void setIntervaloMinimoDias(Integer intervaloMinimoDias) { this.intervaloMinimoDias = intervaloMinimoDias; }
    
    public String getGravidade() { return gravidade; }
    public void setGravidade(String gravidade) { this.gravidade = gravidade; }
}