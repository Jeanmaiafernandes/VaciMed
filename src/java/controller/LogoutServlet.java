package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapeia o URL que é chamado pelo link do dashboard
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtém a sessão atual
        HttpSession session = request.getSession(false); 
        
        // 2. Verifica se a sessão existe e a invalida (destrói)
        if (session != null) {
            session.invalidate();
            System.out.println("Sessão invalidada com sucesso. Usuário deslogado.");
        }
        
        // 3. Redireciona o usuário para a página de login
        // (Assumindo que seu servlet de Autenticacao está mapeado para /login)
        response.sendRedirect(request.getContextPath() + "/login");
    }
}