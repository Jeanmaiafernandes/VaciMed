<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Paciente" %>
<%@ page import="model.Dose" %>
<%@ page import="java.util.List" %>
<%
    Paciente paciente = (Paciente) request.getAttribute("paciente");
    List<Dose> doses = (List<Dose>) request.getAttribute("doses");
    
    if (paciente == null) {
        response.sendRedirect(request.getContextPath() + "/paciente/");
        return;
    }
    
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
    <title>Detalhes do Paciente - VaciMed</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .navbar { background: #007bff; color: white; padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        .container { max-width: 1000px; margin: 0 auto; }
        .card { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .btn { padding: 8px 15px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #0056b3; }
        .btn-secondary { background: #6c757d; }
        .btn-secondary:hover { background: #545b62; }
        .btn-success { background: #28a745; }
        .btn-success:hover { background: #218838; }
        .info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 15px; margin-bottom: 20px; }
        .info-item { margin-bottom: 10px; }
        .info-label { font-weight: bold; color: #666; }
        .info-value { color: #333; }
        .doses-table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .doses-table th, .doses-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .doses-table th { background: #f8f9fa; font-weight: bold; }
        .empty-state { text-align: center; padding: 40px; color: #666; }
        .alert { padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
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
            <h1>👤 Detalhes do Paciente</h1>
            <div>
                <a href="${pageContext.request.contextPath}/paciente/" class="btn btn-secondary">← Voltar para Lista</a>
                <a href="${pageContext.request.contextPath}/calendario" class="btn btn-success">➕ Nova Vacina</a>
            </div>
        </div>

        <!-- Informações do Paciente -->
        <div class="card">
            <h2>Informações Pessoais</h2>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Nome:</span>
                    <div class="info-value"><%= paciente.getNome() %></div>
                </div>
                <div class="info-item">
                    <span class="info-label">CPF:</span>
                    <div class="info-value"><%= paciente.getCpf() != null ? paciente.getCpf() : "Não informado" %></div>
                </div>
                <div class="info-item">
                    <span class="info-label">Data de Nascimento:</span>
                    <div class="info-value"><%= paciente.getDataNascimento() != null ? paciente.getDataNascimento() : "Não informada" %></div>
                </div>
                <div class="info-item">
                    <span class="info-label">Sexo:</span>
                    <div class="info-value"><%= paciente.getSexo() != null ? paciente.getSexo() : "Não informado" %></div>
                </div>
                <div class="info-item">
                    <span class="info-label">Telefone:</span>
                    <div class="info-value"><%= paciente.getTelefone() != null ? paciente.getTelefone() : "Não informado" %></div>
                </div>
                <div class="info-item">
                    <span class="info-label">Email:</span>
                    <div class="info-value"><%= paciente.getEmail() != null ? paciente.getEmail() : "Não informado" %></div>
                </div>
            </div>
            
            <% if (paciente.getAlergias() != null && !paciente.getAlergias().isEmpty()) { %>
                <div class="info-item">
                    <span class="info-label">Alergias:</span>
                    <div class="info-value"><%= paciente.getAlergias() %></div>
                </div>
            <% } %>
            
            <% if (paciente.getMedicamentosUso() != null && !paciente.getMedicamentosUso().isEmpty()) { %>
                <div class="info-item">
                    <span class="info-label">Medicamentos em Uso:</span>
                    <div class="info-value"><%= paciente.getMedicamentosUso() %></div>
                </div>
            <% } %>
        </div>

        <!-- Histórico de Vacinas -->
        <div class="card">
            <h2>💉 Histórico de Vacinas</h2>
            
            <% if (doses != null && !doses.isEmpty()) { %>
                <table class="doses-table">
                    <thead>
                        <tr>
                            <th>Vacina</th>
                            <th>Tipo de Dose</th>
                            <th>Data</th>
                            <th>Status</th>
                            <th>Observações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Dose dose : doses) { %>
                            <tr>
                                <td><strong>Vacina ID: <%= dose.getIdCalendarioVacina() %></strong></td>
                                <td><%= dose.getTipoDose() != null ? dose.getTipoDose() : "N/A" %></td>
                                <td><%= dose.getDataInicio() != null ? dose.getDataInicio() : "N/A" %></td>
                                <td>
                                    <span style="color: #28a745; font-weight: bold;">
                                        Aplicada
                                    </span>
                                </td>
                                <td><%= dose.getObservacoes() != null ? dose.getObservacoes() : "-" %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">💉</div>
                    <h3>Nenhuma vacina registrada</h3>
                    <p>Este paciente ainda não possui vacinas no histórico.</p>
                    <a href="${pageContext.request.contextPath}/calendario" class="btn btn-success">
                        ➕ Adicionar Primeira Vacina
                    </a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>