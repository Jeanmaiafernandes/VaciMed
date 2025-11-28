package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Medico;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Login hardcoded para teste
    private boolean autenticar(String email, String senha) {
        return "joao.silva@hospital.com".equals(email) && "senha123".equals(senha);
    }
    
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String email = request.getParameter("email");
    String senha = request.getParameter("senha");
    
    if (autenticar(email, senha)) {
        HttpSession session = request.getSession();
        
        // 🔥 CORREÇÃO: Criar objeto Medico real
        Medico medico = new Medico();
        medico.setIdMedico(1);
        medico.setNome("Dr. João Silva");
        medico.setEmail(email);
        medico.setCrm("CRM-SP12345");
        
        session.setAttribute("medico", medico); // ✅ Agora é um objeto Medico
        session.setMaxInactiveInterval(30 * 60);
        
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    } else {
        request.setAttribute("erro", "Email ou senha inválidos");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== LOGIN GET ACIONADO ===");
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("medico") != null) {
            System.out.println("✅ Sessão existe - Redirecionando para /dashboard");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            System.out.println("📄 Mostrando página de login");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}