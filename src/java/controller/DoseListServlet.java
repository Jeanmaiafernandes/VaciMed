package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Dose;
import model.Medico;
import dao.DoseDAO;

@WebServlet("/dose/*")
public class DoseListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoseDAO doseDAO;
    
    @Override
    public void init() throws ServletException {
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
            
            int idMedico = medico.getIdMedico();
            
            // Buscar todas as doses do médico
            List<Dose> doses = doseDAO.buscarPorMedico(idMedico);
            
            request.setAttribute("doses", doses);
            
            // Mensagens
            String sucesso = request.getParameter("sucesso");
            String erro = request.getParameter("erro");
            if (sucesso != null) request.setAttribute("sucesso", sucesso);
            if (erro != null) request.setAttribute("erro", erro);
            
            request.getRequestDispatcher("/WEB-INF/views/dose-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, mostra lista vazia
            request.setAttribute("doses", new ArrayList<Dose>());
            request.setAttribute("erro", "Erro ao carregar doses: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dose-list.jsp").forward(request, response);
        }
    }
}