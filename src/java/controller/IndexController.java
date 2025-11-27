package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// CORREÇÃO: Mapeie apenas o índice explícito. 
// Isso evita que ele intercepte o CSS, JS, etc.
@WebServlet({"", "/index"}) 
public class IndexController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redireciona o acesso à raiz para o Servlet de login.
        response.sendRedirect(request.getContextPath() + "/login"); 
    }
}