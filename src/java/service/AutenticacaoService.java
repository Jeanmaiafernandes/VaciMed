package service;

import dao.MedicoDAO;
import dao.PacienteDAO;
import model.Medico;
import model.Paciente;

public class AutenticacaoService {

    private final MedicoDAO medicoDAO;
    private final PacienteDAO pacienteDAO;

    public AutenticacaoService() {
        this.medicoDAO = new MedicoDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    /**
     * Tenta autenticar um usuário como Médico ou Paciente (Lógica Universal).
     * @param usuario O email (para Medico) ou CPF (para Paciente).
     * @param senha A senha.
     * @return Um objeto (Medico ou Paciente) autenticado, ou null.
     */
    public Object autenticar(String usuario, String senha) {
        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.isEmpty()) {
            return null;
        }

        // 1. Tenta autenticar como Médico (Usando email)
        // Assumimos que o campo 'usuario' contém o email
        Medico medico = medicoDAO.buscarPorCredenciais(usuario, senha);
        if (medico != null) {
            return medico; // Retorna o objeto Médico
        }

        // 2. Se falhar, tenta autenticar como Paciente (Usando CPF)
        // Assumimos que o campo 'usuario' contém o CPF
        Paciente paciente = pacienteDAO.buscarPorCredenciais(usuario, senha);
        if (paciente != null) {
            return paciente; // Retorna o objeto Paciente
        }

        // 3. Falha na autenticação
        return null;
    }
}