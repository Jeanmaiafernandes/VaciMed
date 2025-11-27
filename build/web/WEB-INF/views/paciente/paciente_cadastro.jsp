<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Medico"%>

<%
    Medico medico = (Medico) session.getAttribute("medico");
    if (medico == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar Paciente</title>
</head>
<body>
    <h2>Cadastrar Paciente</h2>

    <form action="paciente" method="post">
        <input type="hidden" name="acao" value="cadastrar">

        <label>Nome:</label><br>
        <input type="text" name="nome" required><br><br>

        <label>CPF:</label><br>
        <input type="text" name="cpf" required><br><br>

        <label>Data de Nascimento:</label><br>
        <input type="date" name="data_nascimento" required><br><br>

        <label>Senha:</label><br>
        <input type="password" name="senha" required><br><br>

        <input type="hidden" name="medico_id" value="<%= medico.getId() %>">

        <button type="submit">Salvar</button>
    </form>

    <br>
    <a href="dashboard_medico.jsp">Voltar</a>
</body>
</html>
