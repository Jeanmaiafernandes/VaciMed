package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Paciente;
import model.Dose;
import model.Medico;
import dao.PacienteDAO;
import dao.DoseDAO;

@WebServlet("/paciente/detalhes")
public class PacienteDetalhesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PacienteDAO pacienteDAO;
    private DoseDAO doseDAO;
    
    @Override
    public void init() throws ServletException {
        this.pacienteDAO = new PacienteDAO();
        this.doseDAO = new DoseDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            Medico medico = (Medico) session.getAttribute("medico");
            
            if (medico == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            String idStr = request.getParameter("id");
            
            if (idStr == null) {
                response.sendRedirect(request.getContextPath() + "/paciente/");
                return;
            }
            
            int idPaciente = Integer.parseInt(idStr);
            
            // Buscar paciente (verificando se pertence ao médico)
            Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
            if (paciente == null || paciente.getIdMedico() != medico.getIdMedico()) {
                response.sendRedirect(request.getContextPath() + "/paciente/?erro=Paciente+não+encontrado");
                return;
            }
            
            // Buscar doses do paciente
            List<Dose> doses = doseDAO.buscarPorPaciente(idPaciente);
            
            request.setAttribute("paciente", paciente);
            request.setAttribute("doses", doses);
            
            // Mensagens de sucesso/erro
            String sucesso = request.getParameter("sucesso");
            String erro = request.getParameter("erro");
            if (sucesso != null) request.setAttribute("sucesso", sucesso);
            if (erro != null) request.setAttribute("erro", erro);
            
            request.getRequestDispatcher("/WEB-INF/views/paciente-detalhes.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/paciente/?erro=" + e.getMessage());
        }
    }
}