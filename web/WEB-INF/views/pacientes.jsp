<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Paciente" %>
<%
    List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Pacientes - VaciMed</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">Gestão de Vacinação</div>
        <div class="navbar-user">
            <span>Dr(a). ${medico.nome}</span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Sair</a>
        </div>
    </nav>

    <div class="container">
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-sm" style="margin-right: 10px;">← Voltar</a>
                    Meus Pacientes
                </h2>
                <a href="${pageContext.request.contextPath}/paciente/novo" class="btn btn-primary">Novo Paciente</a>
            </div>

            <% if (request.getAttribute("sucesso") != null) { %>
                <div class="alert alert-success"><%= request.getAttribute("sucesso") %></div>
            <% } %>

            <% if (request.getAttribute("erro") != null) { %>
                <div class="alert alert-error"><%= request.getAttribute("erro") %></div>
            <% } %>

            <% if (pacientes != null && !pacientes.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>CPF</th>
                            <th>Data Nasc.</th>
                            <th>Idade</th>
                            <th>Telefone</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Paciente paciente : pacientes) { %>
                            <tr>
                                <td><strong><%= paciente.getNome() %></strong></td>
                                <td><%= paciente.getCpf() %></td>
                                <td><%= paciente.getDataNascimento() %></td>
                                <td>
                                    <% 
                                        // Calcular idade
                                        if (paciente.getDataNascimento() != null) {
                                            java.time.LocalDate nascimento = paciente.getDataNascimento();
                                            java.time.LocalDate hoje = java.time.LocalDate.now();
                                            java.time.Period periodo = java.time.Period.between(nascimento, hoje);
                                            out.print(periodo.getYears() + " anos");
                                        }
                                    %>
                                </td>
                                <td><%= paciente.getTelefone() != null ? paciente.getTelefone() : "---" %></td>
                                <td>
                                    <div class="action-links">
                                        <a href="${pageContext.request.contextPath}/paciente/detalhes?id=<%= paciente.getIdPaciente() %>" 
                                           class="btn btn-primary btn-sm">Ver</a>
                                        <a href="${pageContext.request.contextPath}/paciente/editar?id=<%= paciente.getIdPaciente() %>" 
                                           class="btn btn-warning btn-sm">Editar</a>
                                        <a href="${pageContext.request.contextPath}/dose/nova?pacienteId=<%= paciente.getIdPaciente() %>" 
                                           class="btn btn-success btn-sm">+ Dose</a>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <h3>Nenhum paciente cadastrado</h3>
                    <p>Comece cadastrando seu primeiro paciente.</p>
                    <a href="${pageContext.request.contextPath}/paciente/novo" class="btn btn-primary">Cadastrar Primeiro Paciente</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>