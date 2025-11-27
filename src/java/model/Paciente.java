package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * RNF01 - Camada Model (Entidade) Paciente.
 * Contém o link para o Medico responsável (crmMedico).
 */
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int id; // ID gerado automaticamente pelo banco
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String crmMedico; // Chave estrangeira para o Medico responsável

    public Paciente() {
    }

    public Paciente(String cpf, String nome, LocalDate dataNascimento, String crmMedico) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.crmMedico = crmMedico;
    }

    // Getters
    public int getId() { return id; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCrmMedico() { return crmMedico; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public void setCrmMedico(String crmMedico) { this.crmMedico = crmMedico; }

    public void setMedicoId(long aLong) {
        // Apenas para compatibilidade, se necessário, ou remova se não usar.
    // O ideal é ter um atributo 'private Long medicoId;' na classe também.
    }
}