<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Paciente - VaciMed</title>
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
                    ${empty paciente ? 'Cadastrar Novo Paciente' : 'Editar Paciente'}
                </h2>
            </div>

            <% if (request.getAttribute("erro") != null) { %>
                <div class="alert alert-error"><%= request.getAttribute("erro") %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/paciente/${empty paciente ? '' : 'atualizar'}" method="post">
                <% if (request.getAttribute("paciente") != null) { %>
                    <input type="hidden" name="idPaciente" value="${paciente.idPaciente}">
                <% } %>
                
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <!-- Coluna 1 -->
                    <div>
                        <div class="form-group">
                            <label for="nome">Nome Completo *</label>
                            <input type="text" id="nome" name="nome" class="form-control" required 
                                   value="${paciente.nome != null ? paciente.nome : ''}">
                        </div>

                        <div class="form-group">
                            <label for="cpf">CPF *</label>
                            <input type="text" id="cpf" name="cpf" class="form-control" required 
                                   value="${paciente.cpf != null ? paciente.cpf : ''}">
                        </div>

                        <div class="form-group">
                            <label for="dataNascimento">Data de Nascimento *</label>
                            <input type="date" id="dataNascimento" name="dataNascimento" class="form-control" required 
                                   value="${paciente.dataNascimento != null ? paciente.dataNascimento : ''}">
                        </div>

                        <div class="form-group">
                            <label for="sexo">Sexo</label>
                            <select id="sexo" name="sexo" class="form-control">
                                <option value="">Selecione</option>
                                <option value="M" ${paciente.sexo == 'M' ? 'selected' : ''}>Masculino</option>
                                <option value="F" ${paciente.sexo == 'F' ? 'selected' : ''}>Feminino</option>
                                <option value="OUTRO" ${paciente.sexo == 'OUTRO' ? 'selected' : ''}>Outro</option>
                            </select>
                        </div>
                    </div>

                    <!-- Coluna 2 -->
                    <div>
                        <div class="form-group">
                            <label for="telefone">Telefone</label>
                            <input type="tel" id="telefone" name="telefone" class="form-control"
                                   value="${paciente.telefone != null ? paciente.telefone : ''}">
                        </div>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-control"
                                   value="${paciente.email != null ? paciente.email : ''}">
                        </div>

                        <div class="form-group">
                            <label for="endereco">Endereço</label>
                            <textarea id="endereco" name="endereco" class="form-control" rows="3">${paciente.endereco != null ? paciente.endereco : ''}</textarea>
                        </div>
                    </div>
                </div>

                <!-- Informações de Saúde -->
                <div style="margin-top: 1rem;">
                    <h3 style="border-bottom: 1px solid #eee; padding-bottom: 0.5rem;">Informações de Saúde</h3>
                    
                    <div class="form-group">
                        <label for="alergias">Alergias Conhecidas</label>
                        <textarea id="alergias" name="alergias" class="form-control" rows="2" 
                                  placeholder="Liste as alergias separadas por vírgula">${paciente.alergias != null ? paciente.alergias : ''}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="medicamentosUso">Medicamentos em Uso</label>
                        <textarea id="medicamentosUso" name="medicamentosUso" class="form-control" rows="2"
                                  placeholder="Liste os medicamentos separados por vírgula">${paciente.medicamentosUso != null ? paciente.medicamentosUso : ''}</textarea>
                    </div>
                </div>

                <div style="margin-top: 2rem; text-align: center;">
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">
                        ${empty paciente ? 'Cadastrar Paciente' : 'Atualizar Paciente'}
                    </button>
                    <a href="${pageContext.request.contextPath}/paciente/" class="btn" style="margin-left: 1rem;">Cancelar</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Máscara para CPF
        document.getElementById('cpf').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length <= 11) {
                value = value.replace(/(\d{3})(\d)/, '$1.$2')
                            .replace(/(\d{3})(\d)/, '$1.$2')
                            .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            }
            e.target.value = value;
        });

        // Máscara para telefone
        document.getElementById('telefone').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length <= 11) {
                value = value.replace(/(\d{2})(\d)/, '($1) $2')
                            .replace(/(\d{5})(\d)/, '$1-$2');
            }
            e.target.value = value;
        });
    </script>
</body>
</html>