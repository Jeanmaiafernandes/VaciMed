package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CalendarioVacina;
import model.Paciente;
import model.Medico;
import dao.CalendarioVacinaDAO;
import dao.PacienteDAO;

@WebServlet("/calendario")
public class CalendarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CalendarioVacinaDAO calendarioDAO;
    private PacienteDAO pacienteDAO;
    
    @Override
    public void init() throws ServletException {
        this.calendarioDAO = new CalendarioVacinaDAO();
        this.pacienteDAO = new PacienteDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            
            // Buscar lista de vacinas
            List<CalendarioVacina> vacinas = calendarioDAO.listarTodas();
            request.setAttribute("vacinas", vacinas);
            
            // Buscar lista de pacientes do médico logado
            Medico medico = (Medico) session.getAttribute("medico");
            List<Paciente> pacientes = null;
            
            if (medico != null) {
                pacientes = pacienteDAO.buscarPorMedico(medico.getIdMedico()); // ✅ Usando o método correto
            }
            
            request.setAttribute("pacientes", pacientes);
            request.getRequestDispatcher("/WEB-INF/views/calendario.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar calendário: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/calendario.jsp").forward(request, response);
        }
    }
}