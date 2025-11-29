// Medico.java
package model;
/**
 *
 * @author jeanm
 */
import java.time.LocalDateTime;

public class Medico {
    private int idMedico;
    private String nome;
    private String crm;
    private String email;
    private String senha;
    private String telefone;
    private String especialidade;
    private LocalDateTime dataCadastro;
    private boolean ativo;
    
    // Construtores, getters e setters
    public Medico() {}
    
    public Medico(String nome, String crm, String email, String senha) {
        this.nome = nome;
        this.crm = crm;
        this.email = email;
        this.senha = senha;
    }
    
    // Getters e Setters
    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}