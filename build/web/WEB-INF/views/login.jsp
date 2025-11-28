<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String erro = (String) request.getAttribute("erro");
    String email = request.getParameter("email");
    if (email == null) email = "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestão de Vacinação - Login</title>
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
</head>
<body class="login-page">
    <div class="login-container">
        <div class="login-header">
            <h1>Gestão de Vacinação</h1>
            <p>Acesso Restrito para Médicos</p>
        </div>
        
        <% if (erro != null && !erro.isEmpty()) { %>
            <div class="alert alert-error">
                <%= erro %>
            </div>
        <% } %>
        
        <form action="<%= request.getContextPath() %>/login" method="post">
            <div class="form-group">
                <label for="email">E-mail:</label>
                <input type="email" id="email" name="email" class="form-control" required value="<%= email %>">
            </div>
            
            <div class="form-group">
                <label for="senha">Senha:</label>
                <input type="password" id="senha" name="senha" class="form-control" required>
            </div>
            
            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem;">Entrar</button>
        </form>
        
        <div class="text-center mt-2" style="color: #666; font-size: 0.9rem;">
            Sistema de Gestão de Vacinação v1.0
        </div>
    </div>
</body>
</html>