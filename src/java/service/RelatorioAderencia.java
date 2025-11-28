// RelatorioAderencia.java - CORRIGIDO
package service;

public class RelatorioAderencia {
    private long totalDoses;
    private long dosesAplicadas;
    private long dosesPendentes;
    private long dosesAtrasadas;
    private double percentualAderencia;
    
    public RelatorioAderencia() {} // Construtor padrão
    
    public RelatorioAderencia(long total, long aplicadas, long pendentes, long atrasadas, double percentual) {
        this.totalDoses = total;
        this.dosesAplicadas = aplicadas;
        this.dosesPendentes = pendentes;
        this.dosesAtrasadas = atrasadas;
        this.percentualAderencia = percentual;
    }
    
    // Getters
    public long getTotalDoses() { return totalDoses; }
    public long getDosesAplicadas() { return dosesAplicadas; }
    public long getDosesPendentes() { return dosesPendentes; }
    public long getDosesAtrasadas() { return dosesAtrasadas; }
    public double getPercentualAderencia() { return percentualAderencia; }
    
    // Setters
    public void setTotalDoses(long totalDoses) { this.totalDoses = totalDoses; }
    public void setDosesAplicadas(long dosesAplicadas) { this.dosesAplicadas = dosesAplicadas; }
    public void setDosesPendentes(long dosesPendentes) { this.dosesPendentes = dosesPendentes; }
    public void setDosesAtrasadas(long dosesAtrasadas) { this.dosesAtrasadas = dosesAtrasadas; }
    public void setPercentualAderencia(double percentualAderencia) { this.percentualAderencia = percentualAderencia; }
}