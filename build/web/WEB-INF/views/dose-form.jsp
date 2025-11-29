<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Dose" %>
<%
    List<Dose> doses = (List<Dose>) request.getAttribute("doses");
    if (doses == null) doses = new java.util.ArrayList<>();
    
    Object medicoObj = session.getAttribute("medico");
    String nomeMedico = "Médico";
    if (medicoObj != null) {
        nomeMedico = medicoObj.toString();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Doses - VaciMed</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .navbar { background: #007bff; color: white; padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        .container { max-width: 1200px; margin: 0 auto; }
        .card { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .btn { padding: 8px 15px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #0056b3; }
        .btn-secondary { background: #6c757d; }
        .btn-success { background: #28a745; }
        .table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .table th, .table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .table th { background: #f8f9fa; font-weight: bold; }
        .empty-state { text-align: center; padding: 40px; color: #666; }
        .alert { padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .status-badge { padding: 4px 8px; border-radius: 12px; font-size: 0.875rem; font-weight: bold; }
        .status-aplicada { background: #d4edda; color: #155724; }
    </style>
</head>
<body>
    <div class="navbar">
        <strong>💉 VaciMed</strong> - <span><%= nomeMedico %></span>
        <a href="${pageContext.request.contextPath}/logout" style="color: white; float: right;">Sair</a>
    </div>

    <div class="container">
        <!-- Mensagens -->
        <% if (request.getAttribute("sucesso") != null) { %>
            <div class="alert alert-success">
                ✅ <%= request.getAttribute("sucesso") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-danger">
                ❌ <%= request.getAttribute("erro") %>
            </div>
        <% } %>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h1>💊 Gerenciar Doses</h1>
            <div>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">← Voltar</a>
                <a href="${pageContext.request.contextPath}/calendario" class="btn btn-success">➕ Nova Dose</a>
            </div>
        </div>

        <!-- Lista de Doses -->
        <div class="card">
            <h2>📋 Histórico de Doses Aplicadas</h2>
            
            <% if (!doses.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Paciente</th>
                            <th>Vacina</th>
                            <th>Tipo de Dose</th>
                            <th>Data de Aplicação</th>
                            <th>Status</th>
                            <th>Observações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Dose dose : doses) { %>
                            <tr>
                                <td>
                                    <strong>
                                        <% 
                                            String nomePaciente = dose.getNomePaciente();
                                            if (nomePaciente != null && !nomePaciente.isEmpty()) {
                                                out.print(nomePaciente);
                                            } else {
                                                out.print("Paciente ID: " + dose.getIdPaciente());
                                            }
                                        %>
                                    </strong>
                                </td>
                                <td>
                                    <% 
                                        String nomeVacina = dose.getNomeVacina();
                                        if (nomeVacina != null && !nomeVacina.isEmpty()) {
                                            out.print(nomeVacina);
                                        } else {
                                            out.print("Vacina ID: " + dose.getIdCalendarioVacina());
                                        }
                                    %>
                                </td>
                                <td>
                                    <% 
                                        String tipoDose = dose.getTipoDose();
                                        out.print(tipoDose != null ? tipoDose : "N/A");
                                    %>
                                </td>
                                <td>
                                    <% 
                                        java.util.Date dataInicio = dose.getDataInicio();
                                        out.print(dataInicio != null ? dataInicio : "N/A");
                                    %>
                                </td>
                                <td>
                                    <span class="status-badge status-aplicada">
                                        <% 
                                            String status = dose.getStatus();
                                            out.print(status != null ? status : "Aplicada");
                                        %>
                                    </span>
                                </td>
                                <td>
                                    <% 
                                        String observacoes = dose.getObservacoes();
                                        out.print(observacoes != null ? observacoes : "-");
                                    %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
                
                <div style="margin-top: 15px; color: #666; text-align: center;">
                    <strong>Total: <%= doses.size() %> dose(s) aplicada(s)</strong>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">💊</div>
                    <h3>Nenhuma dose encontrada</h3>
                    <p>Não há doses registradas no sistema.</p>
                    <a href="${pageContext.request.contextPath}/calendario" class="btn btn-success">
                        ➕ Registrar Primeira Dose
                    </a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>