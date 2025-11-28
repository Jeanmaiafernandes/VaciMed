// LancamentoServlet.java - CORRIGIDO
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

import model.Medico;
import model.Lancamento;
import model.Dose;
import dao.LancamentoDAO;
import dao.DoseDAO;
import service.VacinacaoService;

@WebServlet("/lancamento/*")
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
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect("login");
            return;
        }
        
        Medico medico = (Medico) session.getAttribute("medico");
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                listarLancamentosPorPaciente(request, response);
            } else if (action.equals("/aplicar")) {
                aplicarDose(request, response, medico);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/lancamentos.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("medico") == null) {
            response.sendRedirect("login");
            return;
        }
        
        Medico medico = (Medico) session.getAttribute("medico");
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                registrarAplicacao(request, response, medico);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/lancamento-form.jsp").forward(request, response);
        }
    }
    
    private void listarLancamentosPorPaciente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int idPaciente = Integer.parseInt(request.getParameter("pacienteId"));
        
        List<Lancamento> lancamentos = lancamentoDAO.listarPorPaciente(idPaciente);
        
        request.setAttribute("lancamentos", lancamentos);
        request.setAttribute("pacienteId", idPaciente);
        request.getRequestDispatcher("/lancamentos.jsp").forward(request, response);
    }
    
    private void aplicarDose(HttpServletRequest request, HttpServletResponse response, Medico medico) 
            throws ServletException, IOException {
        int idLancamento = Integer.parseInt(request.getParameter("id"));
        
        // Precisamos do método buscarPorId no LancamentoDAO - vamos criar um lançamento temporário
        // Lancamento lancamento = lancamentoDAO.buscarPorId(idLancamento);
        
        // Criando um lançamento temporário para demonstração
        Lancamento lancamento = new Lancamento();
        lancamento.setIdLancamento(idLancamento);
        lancamento.setIdPaciente(Integer.parseInt(request.getParameter("pacienteId")));
        lancamento.setIdDose(Integer.parseInt(request.getParameter("doseId")));
        lancamento.setIdCalendarioVacina(Integer.parseInt(request.getParameter("vacinaId")));
        lancamento.setDataPrevista(LocalDate.now());
        
        // Buscar a dose
        Dose dose = doseDAO.buscarPorId(lancamento.getIdDose());
        
        if (dose == null) {
            request.setAttribute("erro", "Dose não encontrada");
            request.getRequestDispatcher("/lancamentos.jsp").forward(request, response);
            return;
        }
        
        // RF08 - Verificar interações
        List<String> alertas = vacinacaoService.verificarInteracoes(
            lancamento.getIdPaciente(), 
            lancamento.getIdCalendarioVacina()
        );
        
        request.setAttribute("lancamento", lancamento);
        request.setAttribute("dose", dose);
        request.setAttribute("alertas", alertas);
        request.setAttribute("medico", medico);
        
        request.getRequestDispatcher("/lancamento-aplicar.jsp").forward(request, response);
    }
    
    private void registrarAplicacao(HttpServletRequest request, HttpServletResponse response, Medico medico) 
            throws ServletException, IOException {
        
        try {
            int idLancamento = Integer.parseInt(request.getParameter("idLancamento"));
            int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
            
            // Criar lançamento atualizado
            Lancamento lancamento = new Lancamento();
            lancamento.setIdLancamento(idLancamento);
            lancamento.setIdPaciente(idPaciente);
            lancamento.setDataAplicacao(LocalDate.now());
            lancamento.setStatus("aplicada");
            lancamento.setLoteVacina(request.getParameter("loteVacina"));
            lancamento.setLocalAplicacao(request.getParameter("localAplicacao"));
            lancamento.setObservacoes(request.getParameter("observacoes"));
            lancamento.setIdMedicoAplicador(medico.getIdMedico());
            
            // Atualizar no banco (precisamos do método atualizar)
            lancamentoDAO.atualizar(lancamento);
            
            // RF07 - Gerar doses futuras
            vacinacaoService.gerarDosesFuturas(lancamento);
            
            request.setAttribute("sucesso", "Aplicação registrada com sucesso!");
            response.sendRedirect(request.getContextPath() + "/lancamento/?pacienteId=" + idPaciente);
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao registrar aplicação: " + e.getMessage());
            request.getRequestDispatcher("/lancamento-aplicar.jsp").forward(request, response);
        }
    }
}