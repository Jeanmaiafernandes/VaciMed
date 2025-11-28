<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CalendarioVacina" %>
<%@ page import="model.Paciente" %>
<%
    Paciente paciente = (Paciente) request.getAttribute("paciente");
    List<CalendarioVacina> vacinas = (List<CalendarioVacina>) request.getAttribute("vacinas");
    String vacinaIdParam = request.getParameter("vacinaId");
    
    Object medicoObj = session.getAttribute("medico");
    String nomeMedico = "Médico";
    
    if (medicoObj != null) {
        if (medicoObj instanceof String) {
            nomeMedico = (String) medicoObj;
        } else {
            try {
                Class<?> medicoClass = medicoObj.getClass();
                java.lang.reflect.Method getNome = medicoClass.getMethod("getNome");
                nomeMedico = "Dr(a). " + getNome.invoke(medicoObj);
            } catch (Exception e) {
                nomeMedico = "Dr(a). Usuário";
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Dose - VaciMed</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/forms.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">💉 VaciMed</div>
        <div class="navbar-user">
            <span><%= nomeMedico %></span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Sair</a>
        </div>
    </nav>

    <div class="container">
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">
                    <a href="${pageContext.request.contextPath}/paciente/detalhes?id=<%= paciente != null ? paciente.getIdPaciente() : "" %>" 
                       class="btn btn-sm" style="margin-right: 10px;">← Voltar</a>
                    💉 Cadastrar Nova Dose
                </h2>
            </div>

            <% if (request.getAttribute("erro") != null) { %>
                <div class="alert alert-error">❌ <%= request.getAttribute("erro") %></div>
            <% } %>

            <% if (paciente != null) { %>
                <div class="paciente-info" style="background: #f8f9fa; padding: 1rem; border-radius: 5px; margin-bottom: 1rem;">
                    <h4>Paciente: <strong><%= paciente.getNome() %></strong></h4>
                    <p>CPF: <%= paciente.getCpf() %> | Data Nasc.: <%= paciente.getDataNascimento() %></p>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/dose/" method="post">
                <input type="hidden" name="idPaciente" value="<%= paciente != null ? paciente.getIdPaciente() : "" %>">
                
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <!-- Coluna 1 -->
                    <div>
                        <div class="form-group">
                            <label for="idCalendarioVacina">Vacina/Medicamento *</label>
                            <select id="idCalendarioVacina" name="idCalendarioVacina" class="form-control" required>
                                <option value="">Selecione uma vacina...</option>
                                <% if (vacinas != null) { 
                                    for (CalendarioVacina vacina : vacinas) { 
                                        boolean selected = vacinaIdParam != null && 
                                                          Integer.toString(vacina.getIdCalendarioVacina()).equals(vacinaIdParam);
                                %>
                                    <option value="<%= vacina.getIdCalendarioVacina() %>" <%= selected ? "selected" : "" %>>
                                        <%= vacina.getNomeVacina() %> - <%= vacina.getNumeroDose() %>
                                    </option>
                                <% } 
                                } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="tipoDose">Tipo de Dose *</label>
                            <select id="tipoDose" name="tipoDose" class="form-control" required>
                                <option value="vacina">Vacina</option>
                                <option value="medicamento">Medicamento</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="periodicidadeMeses">Periodicidade (meses)</label>
                            <input type="number" id="periodicidadeMeses" name="periodicidadeMeses" 
                                   class="form-control" min="0" max="60" value="0">
                            <small>0 para dose única</small>
                        </div>
                    </div>

                    <!-- Coluna 2 -->
                    <div>
                        <div class="form-group">
                            <label for="dosesPrevistas">Doses Previstas</label>
                            <input type="number" id="dosesPrevistas" name="dosesPrevistas" 
                                   class="form-control" min="1" max="10" value="1">
                        </div>

                        <div class="form-group">
                            <label for="dataInicio">Data de Início *</label>
                            <input type="date" id="dataInicio" name="dataInicio" class="form-control" required>
                        </div>

                        <div class="form-group">
                            <label for="dataTermino">Data de Término (opcional)</label>
                            <input type="date" id="dataTermino" name="dataTermino" class="form-control">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="descricao">Descrição/Observações</label>
                    <textarea id="descricao" name="descricao" class="form-control" rows="3" 
                              placeholder="Descrição detalhada da dose, observações, etc."></textarea>
                </div>

                <div class="form-group">
                    <label for="observacoes">Observações Adicionais</label>
                    <textarea id="observacoes" name="observacoes" class="form-control" rows="2" 
                              placeholder="Observações específicas para este paciente..."></textarea>
                </div>

                <!-- Alertas de Interação -->
                <div id="alertasInteracao" style="display: none;" class="alert alert-warning">
                    <strong>⚠️ Alerta de Interação:</strong>
                    <span id="textoAlerta"></span>
                </div>

                <div style="margin-top: 2rem; text-align: center;">
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">
                        💾 Cadastrar Dose
                    </button>
                    <a href="${pageContext.request.contextPath}/paciente/detalhes?id=<%= paciente != null ? paciente.getIdPaciente() : "" %>" 
                       class="btn" style="margin-left: 1rem;">Cancelar</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Preencher data atual como padrão
        document.getElementById('dataInicio').valueAsDate = new Date();
        
        // Verificar interações medicamentosas
        document.getElementById('idCalendarioVacina').addEventListener('change', function() {
            const vacinaId = this.value;
            // Simulação de verificação de interação
            if (vacinaId && Math.random() > 0.7) { // 30% de chance de alerta para teste
                document.getElementById('alertasInteracao').style.display = 'block';
                document.getElementById('textoAlerta').textContent = 
                    'Possível interação com medicamentos em uso. Verificar contraindicações.';
            } else {
                document.getElementById('alertasInteracao').style.display = 'none';
            }
        });
        
        // Auto-selecionar vacina se veio do calendário
        <% if (vacinaIdParam != null) { %>
            document.getElementById('idCalendarioVacina').dispatchEvent(new Event('change'));
        <% } %>
    </script>
</body>
</html>