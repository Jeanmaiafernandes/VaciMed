<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Medico"%>
<%@page import="model.Paciente"%>

<%
    // 1. Bloco de scriptlet para evitar que usuários logados acessem a tela de login.
    Object usuarioAutenticado = session.getAttribute("usuarioAutenticado");

    if (usuarioAutenticado != null) {
        // Redirecionamento baseado no tipo de objeto na sessão
        String rotaDashboard = "";
        
        if (usuarioAutenticado instanceof Medico) {
            rotaDashboard = "/dashboard/medico";
        } else if (usuarioAutenticado instanceof Paciente) {
            rotaDashboard = "/dashboard/paciente";
        }

        if (!rotaDashboard.isEmpty()) {
            response.sendRedirect(request.getContextPath() + rotaDashboard);
            return; // Interrompe o processamento da página
        }
    }
    
    // 2. Verifica se há uma mensagem de erro vinda do Servlet
    String erroLogin = (String) request.getAttribute("erroLogin");
    // Se o erro veio via redirect (ex: erro=1), o JSP anterior usava query string.
    // Agora preferimos usar o request.getAttribute('erroLogin') ou session, 
    // mas mantivemos o parâmetro de erro simples para compatibilidade
%>

<!doctype html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/login.css">
    <title>Login - VaciMed</title>
</head>

<body>

<div class="card">
    <div class="hero">
        <div class="brand">
            <div class="logo">VM</div>
            <div>
                <strong>VaciMed</strong>
                <div class="muted">Gestão de vacinação</div>
            </div>
        </div>

        <h1>Login no Sistema</h1>
        <p class="lead">    
            Acesse sua conta como Médico (E-mail) ou Paciente (CPF).
        </p>
    </div>

    <form id="loginForm" method="POST" action="${pageContext.request.contextPath}/login">

        <div class="form-grid">

            <div>
                <label for="tipoLogin">Entrar como</label>
                <select id="tipoLogin" name="tipoLogin" required>
                    <option value="MEDICO" selected>Médico</option>
                    <option value="PACIENTE">Paciente</option>
                </select>
            </div>
            
            <div id="groupMedico" class="login-group">
                <label for="email">E-mail (Médico)</label>
                <input type="email" id="email" name="usuario" required placeholder="seu@exemplo.com">
            </div>

            <div id="groupPaciente" class="login-group" style="display:none">
                <label for="cpf">CPF (Paciente)</label>
                <input type="text" id="cpf" name="usuario" placeholder="CPF (Ex: 111.222.333-44)">
            </div>
            
            <div>
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required minlength="6" placeholder="Sua senha">
            </div>

        </div>

        <button class="btn" type="submit">Entrar</button>

        <div id="feedback" class="msg error">
            <% if (erroLogin != null && !erroLogin.isEmpty()) { %>
                <%= erroLogin %>
            <% } %>
        </div>

    </form>
</div>

<script>
    const tipoSelect = document.getElementById("tipoLogin");
    const medicoGroup = document.getElementById("groupMedico");
    const pacienteGroup = document.getElementById("groupPaciente");
    
    const emailInput = document.getElementById("email");
    const cpfInput = document.getElementById("cpf");

    // Função para alternar a visibilidade e o atributo 'name' (chave da correção)
    const toggleLoginFields = (tipo) => {
        if (tipo === "MEDICO") {
            // Mostra Médico, Esconde Paciente
            medicoGroup.style.display = "block";
            pacienteGroup.style.display = "none";
            
            // Ativa o campo 'email' como 'usuario' e 'required'
            emailInput.name = "usuario";
            emailInput.required = true;
            
            // Desativa o campo 'cpf' para não ser enviado e não ser required
            cpfInput.name = "cpf_temp"; // Renomeia para não ser enviado como 'usuario'
            cpfInput.required = false;
            
        } else if (tipo === "PACIENTE") {
            // Mostra Paciente, Esconde Médico
            medicoGroup.style.display = "none";
            pacienteGroup.style.display = "block";

            // Ativa o campo 'cpf' como 'usuario' e 'required'
            cpfInput.name = "usuario";
            cpfInput.required = true;
            
            // Desativa o campo 'email'
            emailInput.name = "email_temp";
            emailInput.required = false;
        }
    };

    // 1. Inicializa o estado (assume Médico como padrão)
    toggleLoginFields(tipoSelect.value);

    // 2. Adiciona o listener para mudança
    tipoSelect.addEventListener("change", () => {
        toggleLoginFields(tipoSelect.value);
    });

</script>

</body>
</html>