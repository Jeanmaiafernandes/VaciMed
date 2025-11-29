package controller;
/**
 *
 * @author jeanm
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== DASHBOARD GET ACIONADO ===");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            System.out.println("❌ Sem sessão - Redirecionando para login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        System.out.println("✅ Sessão válida - Mostrando dashboard");
        
        // Dados simples para teste
        request.setAttribute("medico", session.getAttribute("medico"));
        request.setAttribute("totalPacientes", 5);
        request.setAttribute("totalProximos", 3);
        
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}