package controller;

import model.Medico;
import model.Paciente;
import service.AutenticacaoService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login") 
public class AutenticacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Instancia a Service uma única vez (melhor para Servlets)
    private final AutenticacaoService autenticacaoService = new AutenticacaoService(); 

    // --- MÉTODOS HTTP ---

    /**
     * Exibe a página de login (GET) e verifica se o usuário já está logado.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Tenta obter a sessão existente

        if (session != null && session.getAttribute("usuarioAutenticado") != null) {
            // Se já houver uma sessão válida, redireciona para o dashboard
            Object usuarioAutenticado = session.getAttribute("usuarioAutenticado");

            if (usuarioAutenticado instanceof Medico) {
                response.sendRedirect(request.getContextPath() + "/dashboard/medico");
            } else if (usuarioAutenticado instanceof Paciente) {
                response.sendRedirect(request.getContextPath() + "/dashboard/paciente");
            }
            return;
        }
        
        // Se não estiver logado, exibe o JSP de login
        // ATENÇÃO: Verifique se o caminho do JSP está correto
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Processa a tentativa de login (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String usuario = request.getParameter("usuario"); // E-mail ou CPF
        String senha = request.getParameter("senha");

        // 1. Tenta autenticar via Service
        Object usuarioAutenticado = autenticacaoService.autenticar(usuario, senha);

        if (usuarioAutenticado != null) {
            // 2. Autenticação bem-sucedida: Cria/Obtém a sessão e define atributos
            HttpSession session = request.getSession(true); // Cria nova sessão se não existir
            session.setAttribute("usuarioAutenticado", usuarioAutenticado);
            
            String tipoUsuario;
            String rotaDashboard;

            if (usuarioAutenticado instanceof Medico) {
                tipoUsuario = "MEDICO";
                rotaDashboard = "/dashboard/medico";
            } else if (usuarioAutenticado instanceof Paciente) {
                tipoUsuario = "PACIENTE";
                rotaDashboard = "/dashboard/paciente";
            } else {
                // Erro de tipo inesperado (Impossível se o service estiver correto)
                request.setAttribute("erroLogin", "Erro interno de tipo de usuário.");     
                // 🚨 ERRO AQUI: 'logiaan.jsp'
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);         
                return;
            }

            // Define o tipo e redireciona para a rota específica
            session.setAttribute("tipoUsuario", tipoUsuario);
            response.sendRedirect(request.getContextPath() + rotaDashboard);
            
        } else {
            // 3. Autenticação falhou: Retorna para o JSP com mensagem de erro
            request.setAttribute("erroLogin", "Credenciais inválidas. Verifique usuário e senha.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}