package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Medico;
import dao.RelatorioDAO;

@WebServlet("/relatorios")
public class RelatoriosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RelatorioDAO relatorioDAO;
    
    @Override
    public void init() throws ServletException {
        this.relatorioDAO = new RelatorioDAO();
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
            
            int idMedico = medico.getIdMedico();
            
            // Buscar dados para os relatórios
            int totalPacientes = relatorioDAO.contarPacientesPorMedico(idMedico);
            int totalVacinasAplicadas = relatorioDAO.contarVacinasAplicadasPorMedico(idMedico);
            List<Object[]> vacinasMaisAplicadas = relatorioDAO.vacinasMaisAplicadasPorMedico(idMedico);
            List<Object[]> coberturaVacinal = relatorioDAO.coberturaVacinalPorMedico(idMedico);
            
            // Adicionar atributos para o JSP
            request.setAttribute("totalPacientes", totalPacientes);
            request.setAttribute("totalVacinasAplicadas", totalVacinasAplicadas);
            request.setAttribute("vacinasMaisAplicadas", vacinasMaisAplicadas);
            request.setAttribute("coberturaVacinal", coberturaVacinal);
            
            request.getRequestDispatcher("/WEB-INF/views/relatorios.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?erro=Erro+ao+carregar+relatórios");
        }
    }
}