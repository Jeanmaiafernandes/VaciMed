package service;

import dao.PacienteDAO;
import model.Paciente;
import java.util.List;

public class PacienteService {
    private PacienteDAO pacienteDAO;
    
    public PacienteService() {
        this.pacienteDAO = new PacienteDAO();
    }
    
    public Paciente buscarPorId(int idPaciente) {
        return pacienteDAO.buscarPorId(idPaciente);
    }
    
    public List<Paciente> listarPorMedico(int idMedico) {
        return pacienteDAO.listarPorMedico(idMedico);
    }
    
    public List<Paciente> listarTodos() {
        return pacienteDAO.listarTodos();
    }
    
    public void salvar(Paciente paciente) {
        if (paciente.getIdPaciente() == 0) {
            // Novo paciente - usar inserir
            pacienteDAO.inserir(paciente);
        } else {
            // Paciente existente - usar atualizar
            pacienteDAO.atualizar(paciente);
        }
    }
    
    public void excluir(int idPaciente) {
        pacienteDAO.excluir(idPaciente);
    }
}