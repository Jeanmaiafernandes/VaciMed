package model;

// Entidade principal para o RF01 e RNF01
public class Medico {
    private int id;
    private String nome;
    private String crm; // Identificação Profissional
    private String email;
    private String senha; // A senha DEVE ser hasheada em um sistema real!

    // Construtor padrão
    public Medico() {
    }

    // Construtor para autenticação
    public Medico(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Construtor completo
    public Medico(int id, String nome, String crm, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.email = email;
        this.senha = senha;
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}