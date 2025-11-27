package service;

import dao.PacienteDAO;
import model.Paciente;
import java.util.List;

/**
 * Camada de Serviço para manipulação de Pacientes.
 * Contém a lógica de negócio e orquestra as operações do DAO.
 */
public class PacienteService {

    // Inicializa o DAO para comunicação com a camada de persistência
    private PacienteDAO pacienteDAO = new PacienteDAO();

    /**
     * Retorna a lista completa de todos os pacientes.
     * @return Lista de objetos Paciente.
     */
    public List<Paciente> listarTodos() {
        // Regras de negócio podem ser aplicadas aqui antes de chamar o DAO,
        // mas para uma simples listagem, chamamos diretamente.
        return pacienteDAO.listarTodos();
    }

    /**
     * Salva ou atualiza um paciente no banco de dados.
     * A lógica para decidir se é um novo cadastro (id == 0) ou atualização
     * deve residir aqui ou no DAO. Preferimos manter a validação aqui.
     * @param paciente O objeto Paciente a ser salvo.
     * @return O paciente salvo/atualizado.
     * @throws IllegalArgumentException Se alguma validação falhar.
     */
    public Paciente salvar(Paciente paciente) throws IllegalArgumentException {
        // Exemplo de Lógica de Negócio: Validação de campos
        if (paciente.getNome() == null || paciente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do paciente é obrigatório.");
        }
        if (paciente.getCpf() == null || paciente.getCpf().length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 dígitos.");
        }
        // *********************************************************************************
        // OBS: A lógica de checagem de CPF duplicado (buscarPorCpf e verificar)
        // deve ser implementada no service para evitar sobrecarga no controller.
        // *********************************************************************************

        if (paciente.getId() == 0) {
            // Novo cadastro
            return pacienteDAO.criar(paciente);
        } else {
            // Atualização
            return pacienteDAO.atualizar(paciente);
        }
    }
    
    /**
     * Busca um paciente pelo seu ID.
     * @param id O ID do paciente.
     * @return O objeto Paciente ou null se não for encontrado.
     */
    public Paciente buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return pacienteDAO.buscarPorId(id);
    }
    
    /**
     * Exclui um paciente pelo seu ID.
     * @param id O ID do paciente a ser excluído.
     */
    public void excluir(int id) {
        // Regra de negócio: Você poderia verificar aqui se o paciente tem
        // lançamentos de vacina vinculados antes de permitir a exclusão.
        pacienteDAO.excluir(id);
    }

}