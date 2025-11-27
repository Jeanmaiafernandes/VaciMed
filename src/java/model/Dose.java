package model;

import java.time.temporal.ChronoUnit;

/**
 * Representa um esquema vacinal ou terapêutico (RF03).
 * Define a substância, periodicidade e número de doses.
 */
public class Dose {
    private int id;
    private String nome; // Ex: "Vacina BCG", "Amoxicilina"
    private int numDoses; // Número total de doses (ex: 3 doses)
    private int periodicidadeValor; // Ex: 3
    private String periodicidadeUnidade; // Ex: "Meses", "Semanas", "Dias"
    
    // Campo para Relação 1:N com Paciente
    private int pacienteId; 

    // Campo para RF08 (Interação) - Opcional, pode ser simples por enquanto
    private String medicamentosInteracao; 

    // Construtor Completo
    public Dose(int id, String nome, int numDoses, int periodicidadeValor, String periodicidadeUnidade, int pacienteId, String medicamentosInteracao) {
        this.id = id;
        this.nome = nome;
        this.numDoses = numDoses;
        this.periodicidadeValor = periodicidadeValor;
        this.periodicidadeUnidade = periodicidadeUnidade;
        this.pacienteId = pacienteId;
        this.medicamentosInteracao = medicamentosInteracao;
    }

    // Construtor para Criação
    public Dose(String nome, int numDoses, int periodicidadeValor, String periodicidadeUnidade, int pacienteId, String medicamentosInteracao) {
        this.nome = nome;
        this.numDoses = numDoses;
        this.periodicidadeValor = periodicidadeValor;
        this.periodicidadeUnidade = periodicidadeUnidade;
        this.pacienteId = pacienteId;
        this.medicamentosInteracao = medicamentosInteracao;
    }
    
    // Converte a unidade de periodicidade para ChronoUnit para facilitar os cálculos de data (RF07)
    public ChronoUnit getPeriodicidadeChronoUnit() {
        switch (periodicidadeUnidade.toUpperCase()) {
            case "DIAS": return ChronoUnit.DAYS;
            case "SEMANAS": return ChronoUnit.WEEKS;
            case "MESES": return ChronoUnit.MONTHS;
            case "ANOS": return ChronoUnit.YEARS;
            default: return ChronoUnit.DAYS;
        }
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getNumDoses() { return numDoses; }
    public void setNumDoses(int numDoses) { this.numDoses = numDoses; }
    public int getPeriodicidadeValor() { return periodicidadeValor; }
    public void setPeriodicidadeValor(int periodicidadeValor) { this.periodicidadeValor = periodicidadeValor; }
    public String getPeriodicidadeUnidade() { return periodicidadeUnidade; }
    public void setPeriodicidadeUnidade(String periodicidadeUnidade) { this.periodicidadeUnidade = periodicidadeUnidade; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public String getMedicamentosInteracao() { return medicamentosInteracao; }
    public void setMedicamentosInteracao(String medicamentosInteracao) { this.medicamentosInteracao = medicamentosInteracao; }
}