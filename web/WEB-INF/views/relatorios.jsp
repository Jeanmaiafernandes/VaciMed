<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    Integer totalPacientes = (Integer) request.getAttribute("totalPacientes");
    Integer totalVacinasAplicadas = (Integer) request.getAttribute("totalVacinasAplicadas");
    List<Object[]> vacinasMaisAplicadas = (List<Object[]>) request.getAttribute("vacinasMaisAplicadas");
    List<Object[]> coberturaVacinal = (List<Object[]>) request.getAttribute("coberturaVacinal");
    
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
    <title>Relatórios - VaciMed</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .navbar { background: #007bff; color: white; padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        .container { max-width: 1200px; margin: 0 auto; }
        .card { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background: linear-gradient(135deg, #007bff, #0056b3); color: white; padding: 25px; border-radius: 8px; text-align: center; }
        .stat-number { font-size: 2.5rem; font-weight: bold; margin-bottom: 10px; }
        .stat-label { font-size: 1rem; opacity: 0.9; }
        .table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .table th, .table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .table th { background: #f8f9fa; font-weight: bold; color: #495057; }
        .btn { padding: 8px 15px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #0056b3; }
        .cobertura-barra { background: #e9ecef; border-radius: 10px; height: 20px; margin-top: 5px; }
        .cobertura-preenchimento { background: linear-gradient(90deg, #28a745, #20c997); height: 100%; border-radius: 10px; text-align: center; color: white; font-size: 12px; line-height: 20px; }
    </style>
</head>
<body>
    <div class="navbar">
        <strong>💉 VaciMed</strong> - <span><%= nomeMedico %></span>
        <a href="${pageContext.request.contextPath}/logout" style="color: white; float: right;">Sair</a>
    </div>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px;">
            <h1>📊 Relatórios e Estatísticas</h1>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn">← Voltar ao Dashboard</a>
        </div>

        <!-- Estatísticas Principais -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number"><%= totalPacientes != null ? totalPacientes : 0 %></div>
                <div class="stat-label">👥 Total de Pacientes</div>
            </div>
            <div class="stat-card">
                <div class="stat-number"><%= totalVacinasAplicadas != null ? totalVacinasAplicadas : 0 %></div>
                <div class="stat-label">💉 Vacinas Aplicadas</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">
                    <%= totalPacientes != null && totalPacientes > 0 ? 
                        String.format("%.1f", (double) totalVacinasAplicadas / totalPacientes) : "0" %>
                </div>
                <div class="stat-label">📈 Média por Paciente</div>
            </div>
        </div>

        <!-- Vacinas Mais Aplicadas -->
        <div class="card">
            <h2>🏆 Vacinas Mais Aplicadas</h2>
            <% if (vacinasMaisAplicadas != null && !vacinasMaisAplicadas.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Vacina</th>
                            <th>Total de Aplicações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Object[] vacina : vacinasMaisAplicadas) { %>
                            <tr>
                                <td><strong><%= vacina[0] %></strong></td>
                                <td><%= vacina[1] %> aplicações</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p style="text-align: center; color: #666; padding: 20px;">
                    Nenhuma vacina aplicada ainda.
                </p>
            <% } %>
        </div>

        <!-- Cobertura Vacinal -->
        <div class="card">
            <h2>🛡️ Cobertura Vacinal</h2>
            <% if (coberturaVacinal != null && !coberturaVacinal.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Vacina</th>
                            <th>Pacientes Vacinados</th>
                            <th>Total de Pacientes</th>
                            <th>Cobertura</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Object[] cobertura : coberturaVacinal) { %>
                            <tr>
                                <td><strong><%= cobertura[0] %></strong></td>
                                <td><%= cobertura[2] %></td>
                                <td><%= cobertura[1] %></td>
                                <td style="width: 200px;">
                                    <div style="display: flex; align-items: center; gap: 10px;">
                                        <span><%= cobertura[3] %>%</span>
                                        <div class="cobertura-barra" style="flex: 1;">
                                            <div class="cobertura-preenchimento" style="width: <%= cobertura[3] %>%">
                                                <%= cobertura[3] %>%
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p style="text-align: center; color: #666; padding: 20px;">
                    Dados de cobertura vacinal não disponíveis.
                </p>
            <% } %>
        </div>

        <!-- Ações -->
        <div class="card">
            <h2>⚡ Ações Rápidas</h2>
            <div style="display: flex; gap: 15px; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/calendario" class="btn">📅 Ver Calendário Vacinal</a>
                <a href="${pageContext.request.contextPath}/paciente/" class="btn">👥 Gerenciar Pacientes</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn">🏠 Voltar ao Início</a>
            </div>
        </div>
    </div>
</body>
</html>