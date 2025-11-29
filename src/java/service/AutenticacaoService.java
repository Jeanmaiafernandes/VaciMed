package service;
/**
 *
 * @author jeanm
 */
import dao.MedicoDAO;
import model.Medico;

public class AutenticacaoService {
    
    private MedicoDAO medicoDAO;
    
    public AutenticacaoService() {
        this.medicoDAO = new MedicoDAO();
    }
    
    public Medico autenticar(String email, String senha) {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            return null;
        }
        
        return medicoDAO.autenticar(email.trim(), senha.trim());
    }
    
    public boolean validarSessao(Medico medico) {
        return medico != null && medico.getIdMedico() > 0;
    }
}