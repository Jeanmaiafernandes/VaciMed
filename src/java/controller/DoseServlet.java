package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Dose;
import model.Medico;
import dao.DoseDAO;

@WebServlet("/dose/nova")
public class DoseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoseDAO doseDAO;
    
    @Override
    public void init() throws ServletException {
        this.doseDAO = new DoseDAO();
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
            
            String pacienteId = request.getParameter("pacienteId");
            String vacinaId = request.getParameter("vacinaId");
            String doseStr = request.getParameter("dose");
            
            // Validação mais robusta
            if (pacienteId == null || pacienteId.trim().isEmpty() || 
                vacinaId == null || vacinaId.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/calendario?erro=Parâmetros+inválidos");
                return;
            }
            
            int numeroDose = 1;
            if (doseStr != null && !doseStr.trim().isEmpty()) {
                try {
                    numeroDose = Integer.parseInt(doseStr);
                } catch (NumberFormatException e) {
                    // Mantém o valor padrão 1
                }
            }
            
            request.setAttribute("pacienteId", pacienteId);
            request.setAttribute("vacinaId", vacinaId);
            request.setAttribute("numeroDose", numeroDose);
            
            request.getRequestDispatcher("/WEB-INF/views/dose-form.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/calendario?erro=Erro+ao+carregar+formulário");
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
            
            // Validação dos parâmetros
            String pacienteIdStr = request.getParameter("pacienteId");
            String vacinaIdStr = request.getParameter("vacinaId");
            String tipoDose = request.getParameter("tipoDose");
            String dataAplicacao = request.getParameter("dataAplicacao");
            String descricao = request.getParameter("descricao");
            String observacoes = request.getParameter("observacoes");
            
            if (pacienteIdStr == null || vacinaIdStr == null || dataAplicacao == null) {
                response.sendRedirect(request.getContextPath() + "/calendario?erro=Dados+obrigatórios+ausentes");
                return;
            }
            
            Dose dose = new Dose();
            dose.setIdPaciente(Integer.parseInt(pacienteIdStr));
            dose.setIdCalendarioVacina(Integer.parseInt(vacinaIdStr));
            dose.setTipoDose(tipoDose != null ? tipoDose : "Dose única");
            dose.setDataInicio(java.sql.Date.valueOf(dataAplicacao));
            dose.setStatus("APLICADA"); // Use maiúsculo para consistência
            dose.setDescricao(descricao != null && !descricao.trim().isEmpty() ? descricao : "Dose aplicada");
            dose.setObservacoes(observacoes);
            
            doseDAO.inserir(dose);
            
            response.sendRedirect(request.getContextPath() + "/dose/?sucesso=Dose+registrada+com+sucesso");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/calendario?erro=Erro+ao+registrar+dose: " + e.getMessage());
        }
    }
}