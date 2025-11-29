package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Lancamento;
import model.Medico;
import dao.LancamentoDAO;

@WebServlet("/lancamento/*")
public class LancamentoListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LancamentoDAO lancamentoDAO;
    
    @Override
    public void init() throws ServletException {
        this.lancamentoDAO = new LancamentoDAO();
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
            List<Lancamento> lancamentos;
            
            // Verificar se há filtro por status
            String statusFiltro = request.getParameter("status");
            if (statusFiltro != null && !statusFiltro.trim().isEmpty()) {
                // Normalizar o status para maiúsculo
                String statusNormalizado = statusFiltro.toUpperCase();
                lancamentos = lancamentoDAO.buscarPorStatus(idMedico, statusNormalizado);
            } else {
                lancamentos = lancamentoDAO.buscarPorMedico(idMedico);
            }
            
            request.setAttribute("lancamentos", lancamentos);
            
            // Mensagens
            String sucesso = request.getParameter("sucesso");
            String erro = request.getParameter("erro");
            if (sucesso != null) request.setAttribute("sucesso", sucesso);
            if (erro != null) request.setAttribute("erro", erro);
            
            request.getRequestDispatcher("/WEB-INF/views/lancamento-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?erro=Erro+ao+carregar+histórico: " + e.getMessage());
        }
    }
}