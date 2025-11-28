// CalendarioVacina.java
package model;

public class CalendarioVacina {
    private int idCalendarioVacina;
    private String nomeVacina;
    private String descricao;
    private String numeroDose;
    private int idadeRecomendadaMeses;
    private int intervaloDosesMeses;
    private int dosesNecessarias;
    private boolean ativo;
    
    // Construtores, getters e setters
    public CalendarioVacina() {}
    
    // Getters e Setters
    public int getIdCalendarioVacina() { return idCalendarioVacina; }
    public void setIdCalendarioVacina(int idCalendarioVacina) { this.idCalendarioVacina = idCalendarioVacina; }
    
    public String getNomeVacina() { return nomeVacina; }
    public void setNomeVacina(String nomeVacina) { this.nomeVacina = nomeVacina; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getNumeroDose() { return numeroDose; }
    public void setNumeroDose(String numeroDose) { this.numeroDose = numeroDose; }
    
    public int getIdadeRecomendadaMeses() { return idadeRecomendadaMeses; }
    public void setIdadeRecomendadaMeses(int idadeRecomendadaMeses) { this.idadeRecomendadaMeses = idadeRecomendadaMeses; }
    
    public int getIntervaloDosesMeses() { return intervaloDosesMeses; }
    public void setIntervaloDosesMeses(int intervaloDosesMeses) { this.intervaloDosesMeses = intervaloDosesMeses; }
    
    public int getDosesNecessarias() { return dosesNecessarias; }
    public void setDosesNecessarias(int dosesNecessarias) { this.dosesNecessarias = dosesNecessarias; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}