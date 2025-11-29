package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Medico;
import model.Paciente;
import dao.PacienteDAO;
import service.PacienteService;

@WebServlet("/paciente/*")
public class PacienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PacienteService pacienteService;
    
    @Override
    public void init() throws ServletException {
        this.pacienteService = new PacienteService();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Medico medico = (Medico) session.getAttribute("medico");
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                listarPacientes(request, response, medico);
            } else if (action.equals("/novo")) {
                novoPaciente(request, response);
            } else if (action.equals("/detalhes")) {
                detalhesPaciente(request, response);
            } else if (action.equals("/editar")) {
                editarPaciente(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/pacientes.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Medico medico = (Medico) session.getAttribute("medico");
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                cadastrarPaciente(request, response, medico);
            } else if (action.equals("/atualizar")) {
                atualizarPaciente(request, response, medico);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/paciente-form.jsp").forward(request, response);
        }
    }
    
    private void listarPacientes(HttpServletRequest request, HttpServletResponse response, Medico medico) 
            throws ServletException, IOException {
        List<Paciente> pacientes = pacienteService.listarPorMedico(medico.getIdMedico());
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("/WEB-INF/views/pacientes.jsp").forward(request, response);
    }
    
    private void novoPaciente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/paciente-form.jsp").forward(request, response);
    }
    
    private void detalhesPaciente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int idPaciente = Integer.parseInt(request.getParameter("id"));
        Paciente paciente = pacienteService.buscarPorId(idPaciente);
        
        if (paciente == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Aqui você pode carregar doses, lançamentos, etc.
        request.setAttribute("paciente", paciente);
        request.getRequestDispatcher("/WEB-INF/views/paciente-detalhes.jsp").forward(request, response);
    }
    
    private void editarPaciente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int idPaciente = Integer.parseInt(request.getParameter("id"));
        Paciente paciente = pacienteService.buscarPorId(idPaciente);
        
        if (paciente == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        request.setAttribute("paciente", paciente);
        request.getRequestDispatcher("/WEB-INF/views/paciente-form.jsp").forward(request, response);
    }
    
    private void cadastrarPaciente(HttpServletRequest request, HttpServletResponse response, Medico medico) 
            throws ServletException, IOException {
        
        try {
            Paciente paciente = new Paciente();
            paciente.setIdMedico(medico.getIdMedico());
            paciente.setNome(request.getParameter("nome"));
            paciente.setCpf(request.getParameter("cpf"));
            
            // Converter data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataNascimento = LocalDate.parse(request.getParameter("dataNascimento"), formatter);
            paciente.setDataNascimento(dataNascimento);
            
            paciente.setSexo(request.getParameter("sexo"));
            paciente.setTelefone(request.getParameter("telefone"));
            paciente.setEmail(request.getParameter("email"));
            paciente.setEndereco(request.getParameter("endereco"));
            paciente.setAlergias(request.getParameter("alergias"));
            paciente.setMedicamentosUso(request.getParameter("medicamentosUso"));
            
            pacienteService.salvar(paciente);
            
            request.setAttribute("sucesso", "Paciente cadastrado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/paciente/");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao cadastrar paciente: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/paciente-form.jsp").forward(request, response);
        }
    }
    
    private void atualizarPaciente(HttpServletRequest request, HttpServletResponse response, Medico medico) 
            throws ServletException, IOException {
        
        try {
            int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
            Paciente paciente = pacienteService.buscarPorId(idPaciente);
            
            if (paciente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            // Atualizar dados
            paciente.setNome(request.getParameter("nome"));
            paciente.setCpf(request.getParameter("cpf"));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataNascimento = LocalDate.parse(request.getParameter("dataNascimento"), formatter);
            paciente.setDataNascimento(dataNascimento);
            
            paciente.setSexo(request.getParameter("sexo"));
            paciente.setTelefone(request.getParameter("telefone"));
            paciente.setEmail(request.getParameter("email"));
            paciente.setEndereco(request.getParameter("endereco"));
            paciente.setAlergias(request.getParameter("alergias"));
            paciente.setMedicamentosUso(request.getParameter("medicamentosUso"));
            
            pacienteService.salvar(paciente);
            
            request.setAttribute("sucesso", "Paciente atualizado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/paciente/");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao atualizar paciente: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/paciente-form.jsp").forward(request, response);
        }
    }
}