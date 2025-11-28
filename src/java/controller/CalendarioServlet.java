package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CalendarioVacina;
import dao.CalendarioVacinaDAO;

@WebServlet("/calendario")
public class CalendarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CalendarioVacinaDAO calendarioDAO;
    
    @Override
    public void init() throws ServletException {
        this.calendarioDAO = new CalendarioVacinaDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            List<CalendarioVacina> vacinas = calendarioDAO.listarTodas();
            request.setAttribute("vacinas", vacinas);
            request.getRequestDispatcher("/WEB-INF/views/calendario.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar calendário: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/calendario.jsp").forward(request, response);
        }
    }
}