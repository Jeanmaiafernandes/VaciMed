package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapeia as duas rotas que seu AutenticacaoServlet está usando para redirecionar.
@WebServlet({"/dashboard/medico", "/dashboard/paciente"})
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Boa prática: se o usuário tentar acessar a URL diretamente sem sessão,
        // redireciona para o login.
        if (request.getSession().getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Encaminha para o arquivo JSP real que contém o conteúdo do dashboard.
        // Baseado na sua estrutura de arquivos, o caminho é: /WEB-INF/views/dashboard.jsp
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}