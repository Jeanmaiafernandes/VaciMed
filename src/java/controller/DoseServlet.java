// DoseServlet.java
package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Medico;
import model.Paciente;
import model.Dose;
import model.CalendarioVacina;
import dao.DoseDAO;
import dao.PacienteDAO;
import dao.CalendarioVacinaDAO;
import service.VacinacaoService;

@WebServlet("/dose/*")
public class DoseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoseDAO doseDAO;
    private PacienteDAO pacienteDAO;
    private CalendarioVacinaDAO calendarioVacinaDAO;
    private VacinacaoService vacinacaoService;
    
    @Override
    public void init() throws ServletException {
        this.doseDAO = new DoseDAO();
        this.pacienteDAO = new PacienteDAO();
        this.calendarioVacinaDAO = new CalendarioVacinaDAO();
        this.vacinacaoService = new VacinacaoService();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                listarDosesPorPaciente(request, response);
            } else if (action.equals("/nova")) {
                novaDose(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/doses.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                cadastrarDose(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/dose-form.jsp").forward(request, response);
        }
    }
    
    private void listarDosesPorPaciente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int idPaciente = Integer.parseInt(request.getParameter("pacienteId"));
        
        List<Dose> doses = doseDAO.listarPorPaciente(idPaciente);
        Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
        
        request.setAttribute("doses", doses);
        request.setAttribute("paciente", paciente);
        request.getRequestDispatcher("/doses.jsp").forward(request, response);
    }
    
    private void novaDose(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int idPaciente = Integer.parseInt(request.getParameter("pacienteId"));
        
        Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
        List<CalendarioVacina> vacinas = calendarioVacinaDAO.listarTodas();
        
        request.setAttribute("paciente", paciente);
        request.setAttribute("vacinas", vacinas);
        request.getRequestDispatcher("/dose-form.jsp").forward(request, response);
    }
    
    private void cadastrarDose(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Dose dose = new Dose();
            dose.setIdPaciente(Integer.parseInt(request.getParameter("idPaciente")));
            dose.setIdCalendarioVacina(Integer.parseInt(request.getParameter("idCalendarioVacina")));
            dose.setTipoDose(request.getParameter("tipoDose"));
            dose.setDescricao(request.getParameter("descricao"));
            dose.setPeriodicidadeMeses(Integer.parseInt(request.getParameter("periodicidadeMeses")));
            dose.setDosesPrevistas(Integer.parseInt(request.getParameter("dosesPrevistas")));
            dose.setDataInicio(LocalDate.parse(request.getParameter("dataInicio")));
            dose.setStatus("ativo");
            
            doseDAO.inserir(dose);
            
            request.setAttribute("sucesso", "Dose cadastrada com sucesso!");
            response.sendRedirect(request.getContextPath() + "/dose/?pacienteId=" + dose.getIdPaciente());
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar dose: " + e.getMessage());
            request.getRequestDispatcher("/dose-form.jsp").forward(request, response);
        }
    }
}