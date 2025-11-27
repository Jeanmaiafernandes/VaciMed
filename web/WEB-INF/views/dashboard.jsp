<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Medico"%>
<%@page import="model.Paciente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
    // 1. Verifica se o usuário está autenticado
    Object usuarioAutenticado = session.getAttribute("usuarioAutenticado");

    if (usuarioAutenticado == null) {
        // Se a sessão expirou ou foi acessada diretamente, redireciona para o login
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    // Variáveis para exibição
    String nomeUsuario = "";
    String tipoUsuario = "";
    String saudacao = "";

    // 2. Define o tipo e o nome
    if (usuarioAutenticado instanceof Medico) {
        Medico medico = (Medico) usuarioAutenticado;
        nomeUsuario = medico.getNome();
        tipoUsuario = "Médico";
        saudacao = "Bem-vindo(a), Dr(a). " + nomeUsuario.split(" ")[0] + "!";
    } else if (usuarioAutenticado instanceof Paciente) {
        Paciente paciente = (Paciente) usuarioAutenticado;
        nomeUsuario = paciente.getNome();
        tipoUsuario = "Paciente";
        saudacao = "Olá, " + nomeUsuario.split(" ")[0] + "!";
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/dashboard.css">
    <title>Dashboard - VaciMed</title>
</head>

<body>

<div class="header">
    <h1>VaciMed - Dashboard</h1>
    <div>
        <span>Logado como: **<%= tipoUsuario %>**</span>
        <a href="<%= request.getContextPath() %>/logout">Sair</a>
    </div>
</div>

<div class="container">
    <h2><%= saudacao %></h2>
    <p>Você está na área restrita de **<%= tipoUsuario %>**.</p>
    
    <% if (usuarioAutenticado instanceof Medico) { %>
        <div class="card medico-content">
            <h3>Área do Médico 👨‍⚕️</h3>
            <p>**Funcionalidades:** Gerenciar agendamentos, registrar vacinas e visualizar históricos de pacientes.</p>
            <ul>
                <li>Pacientes para hoje: (Em desenvolvimento)</li>
                <li>Total de vacinas registradas: (Em desenvolvimento)</li>
            </ul>
        </div>
        
    <% } else if (usuarioAutenticado instanceof Paciente) { %>
        <div class="card paciente-content">
            <h3>Área do Paciente 🩹</h3>
            <p>**Funcionalidades:** Visualizar carteira de vacinação, agendar novas doses e verificar próximos lembretes.</p>
            <% Paciente paciente = (Paciente) usuarioAutenticado; %>
            <p>Seu CPF: **<%= paciente.getCpf() %>**</p>
            <p>Última dose registrada: (Em desenvolvimento)</p>
            <p>Próximo agendamento: (Em desenvolvimento)</p>
        </div>
        
    <% } %>
</div>

</body>
</html>