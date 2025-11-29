package controller;

import java.io.IOException;
import java.time.LocalDate;
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
import dao.DoseDAO;
import service.VacinacaoService;

@WebServlet("/lancamento/aplicar")
public class LancamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LancamentoDAO lancamentoDAO;
    private DoseDAO doseDAO;
    private VacinacaoService vacinacaoService;
    
    @Override
    public void init() throws ServletException {
        this.lancamentoDAO = new LancamentoDAO();
        this.doseDAO = new DoseDAO();
        this.vacinacaoService = new VacinacaoService();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idStr = request.getParameter("id");
            
            if (idStr == null) {
                response.sendRedirect(request.getContextPath() + "/lancamento/?erro=ID+inválido");
                return;
            }
            
            int idLancamento = Integer.parseInt(idStr);
            Lancamento lancamento = lancamentoDAO.buscarPorId(idLancamento);
            
            if (lancamento == null) {
                response.sendRedirect(request.getContextPath() + "/lancamento/?erro=Lançamento+não+encontrado");
                return;
            }
            
            // Verificar interações medicamentosas
            List<String> alertas = vacinacaoService.verificarInteracoes(
                lancamento.getIdPaciente(), 
                lancamento.getIdCalendarioVacina()
            );
            
            request.setAttribute("lancamento", lancamento);
            request.setAttribute("alertas", alertas);
            
            request.getRequestDispatcher("/WEB-INF/views/lancamento-aplicar.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lancamento/?erro=Erro+ao+carregar+aplicação");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            Medico medico = (Medico) session.getAttribute("medico");
            
            if (medico == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            String idLancamentoStr = request.getParameter("idLancamento");
            String doseIdStr = request.getParameter("doseId");
            String loteVacina = request.getParameter("loteVacina");
            String localAplicacao = request.getParameter("localAplicacao");
            String observacoes = request.getParameter("observacoes");
            
            if (idLancamentoStr == null) {
                response.sendRedirect(request.getContextPath() + "/lancamento/?erro=ID+inválido");
                return;
            }
            
            int idLancamento = Integer.parseInt(idLancamentoStr);
            int doseId = doseIdStr != null ? Integer.parseInt(doseIdStr) : 0;
            
            // Marcar como aplicado usando o serviço
            vacinacaoService.marcarComoAplicado(
                idLancamento,
                medico.getIdMedico(),
                loteVacina,
                localAplicacao,
                observacoes,
                doseId
            );
            
            response.sendRedirect(request.getContextPath() + "/lancamento/?sucesso=Aplicação+registrada+com+sucesso");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lancamento/?erro=Erro+ao+registrar+aplicação");
        }
    }
}