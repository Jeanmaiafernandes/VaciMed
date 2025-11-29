<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CalendarioVacina" %>
<%@ page import="model.Paciente" %>
<%
    List<CalendarioVacina> vacinas = (List<CalendarioVacina>) request.getAttribute("vacinas");
    List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes"); // Nova lista de pacientes
    
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
    <title>Calendário Vacinal - VaciMed</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/calendario.css" rel="stylesheet">
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
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-sm" style="margin-right: 10px;">← Voltar</a>
                    📅 Calendário Vacinal Nacional
                </h2>
            </div>

            <!-- Filtros -->
            <div class="filtros-calendario">
                <div class="form-group" style="max-width: 300px;">
                    <label for="idadeFiltro">Filtrar por Idade (meses):</label>
                    <input type="number" id="idadeFiltro" class="form-control" placeholder="Ex: 6" min="0" max="240">
                </div>
            </div>

            <% if (vacinas != null && !vacinas.isEmpty()) { %>
                <div class="calendario-grid">
                    <% for (CalendarioVacina vacina : vacinas) { %>
                        <div class="vacina-card" data-idade="<%= vacina.getIdadeRecomendadaMeses() %>">
                            <div class="vacina-header">
                                <h3 class="vacina-nome"><%= vacina.getNomeVacina() %></h3>
                                <span class="vacina-dose"><%= vacina.getNumeroDose() %></span>
                            </div>
                            
                            <div class="vacina-info">
                                <div class="info-item">
                                    <span class="info-label">📅 Idade Recomendada:</span>
                                    <span class="info-value">
                                        <% if (vacina.getIdadeRecomendadaMeses() < 12) { %>
                                            <%= vacina.getIdadeRecomendadaMeses() %> meses
                                        <% } else { %>
                                            <%= vacina.getIdadeRecomendadaMeses() / 12 %> anos
                                        <% } %>
                                    </span>
                                </div>
                                
                                <% if (vacina.getIntervaloDosesMeses() > 0) { %>
                                    <div class="info-item">
                                        <span class="info-label">⏱️ Intervalo:</span>
                                        <span class="info-value"><%= vacina.getIntervaloDosesMeses() %> meses</span>
                                    </div>
                                <% } %>
                                
                                <div class="info-item">
                                    <span class="info-label">💉 Doses:</span>
                                    <span class="info-value"><%= vacina.getDosesNecessarias() %> dose(s)</span>
                                </div>
                            </div>
                            
                            <div class="vacina-descricao">
                                <p><%= vacina.getDescricao() != null ? vacina.getDescricao() : "Vacina do calendário nacional." %></p>
                            </div>
                            
                            <div class="vacina-actions">
                                <button class="btn btn-sm btn-success" 
                                        onclick="adicionarAoPaciente(<%= vacina.getIdCalendarioVacina() %>, '<%= vacina.getNomeVacina() %>')">
                                    ➕ Adicionar a Paciente
                                </button>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">📅</div>
                    <h3>Nenhuma vacina encontrada</h3>
                    <p>O calendário vacinal não está disponível no momento.</p>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Modal para selecionar paciente -->
    <div id="modalPaciente" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Adicionar Vacina ao Paciente</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <p>Selecione o paciente para adicionar a vacina: <strong id="vacinaSelecionada"></strong></p>
                
                <% if (pacientes != null && !pacientes.isEmpty()) { %>
                    <div class="pacientes-lista">
                        <% for (Paciente paciente : pacientes) { %>
                            <div class="paciente-item" data-paciente-id="<%= paciente.getIdPaciente() %>">
                                <div class="paciente-info">
                                    <strong class="paciente-nome"><%= paciente.getNome() %></strong>
                                    <div class="paciente-detalhes">

                                </div>
                                <button class="btn btn-sm btn-primary" 
                                        onclick="selecionarPaciente(<%= paciente.getIdPaciente() %>, '<%= paciente.getNome() %>')">
                                    Selecionar
                                </button>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state">
                        <p>Nenhum paciente cadastrado.</p>
                        <a href="${pageContext.request.contextPath}/paciente/novo" class="btn btn-primary">
                            ➕ Cadastrar Primeiro Paciente
                        </a>
                    </div>
                <% } %>
                
                <div id="selecaoDose" style="display: none; margin-top: 20px;">
                    <div class="form-group">
                        <label for="selectDose">Selecionar Dose:</label>
                        <select class="form-control" id="selectDose">
                            <option value="1">Dose 1</option>
                            <option value="2">Dose 2</option>
                            <option value="3">Dose 3</option>
                            <option value="4">Dose de Reforço</option>
                        </select>
                    </div>
                    <div style="text-align: center; margin-top: 1rem;">
                        <button class="btn btn-primary" onclick="redirecionarParaDose()">Continuar</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn" onclick="fecharModal()">Cancelar</button>
            </div>
        </div>
    </div>

    <script>
        // Filtro por idade
        document.getElementById('idadeFiltro').addEventListener('input', function(e) {
            const idade = parseInt(e.target.value);
            const cards = document.querySelectorAll('.vacina-card');
            
            cards.forEach(card => {
                const cardIdade = parseInt(card.dataset.idade);
                if (isNaN(idade) || cardIdade <= idade) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        });

        let vacinaAtualId = null;
        let pacienteSelecionadoId = null;
        
        function adicionarAoPaciente(vacinaId, vacinaNome) {
            vacinaAtualId = vacinaId;
            document.getElementById('vacinaSelecionada').textContent = vacinaNome;
            document.getElementById('modalPaciente').style.display = 'block';
            
            // Resetar seleção
            pacienteSelecionadoId = null;
            document.getElementById('selecaoDose').style.display = 'none';
        }
        
        function selecionarPaciente(pacienteId, pacienteNome) {
            pacienteSelecionadoId = pacienteId;
            document.getElementById('selecaoDose').style.display = 'block';
            
            // Destacar paciente selecionado
            document.querySelectorAll('.paciente-item').forEach(item => {
                item.classList.remove('selected');
            });
            event.target.closest('.paciente-item').classList.add('selected');
        }
        
        function redirecionarParaDose() {
            if (pacienteSelecionadoId && vacinaAtualId) {
                const doseSelecionada = document.getElementById('selectDose').value;
                window.location.href = '${pageContext.request.contextPath}/dose/nova?pacienteId=' + pacienteSelecionadoId + 
                                      '&vacinaId=' + vacinaAtualId + '&dose=' + doseSelecionada;
            } else {
                alert('Por favor, selecione um paciente primeiro.');
            }
        }
        
        function fecharModal() {
            document.getElementById('modalPaciente').style.display = 'none';
        }
        
        // Fechar modal ao clicar no X
        document.querySelector('.close').addEventListener('click', fecharModal);
        
        // Fechar modal ao clicar fora
        window.addEventListener('click', function(event) {
            const modal = document.getElementById('modalPaciente');
            if (event.target === modal) {
                fecharModal();
            }
        });
    </script>
</body>
</html>