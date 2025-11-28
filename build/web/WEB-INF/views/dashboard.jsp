<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Lancamento" %>
<%@ page import="model.Paciente" %>
<%
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
    
    List<Lancamento> proximosLancamentos = (List<Lancamento>) request.getAttribute("proximosLancamentos");
    List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
    Integer totalPacientes = (Integer) request.getAttribute("totalPacientes");
    Integer totalProximos = (Integer) request.getAttribute("totalProximos");
    if (totalPacientes == null) totalPacientes = 0;
    if (totalProximos == null) totalProximos = 0;
    
    // Calcular estatísticas SEM LAMBDAS
    long aplicacoesHoje = 0;
    long dosesAtrasadas = 0;
    
    if (proximosLancamentos != null) {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        for (Lancamento lancamento : proximosLancamentos) {
            if (lancamento.getDataPrevista().equals(hoje)) {
                aplicacoesHoje++;
            }
            if (lancamento.getDataPrevista().isBefore(hoje) && "pendente".equals(lancamento.getStatus())) {
                dosesAtrasadas++;
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - VaciMed</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/dashboard.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            <span style="font-size: 1.8rem; font-weight: bold;">💉 VaciMed</span>
        </div>
        <div class="navbar-user">
            <span style="margin-right: 1rem;"><%= nomeMedico %></span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Sair</a>
        </div>
    </nav>

    <div class="container">
        <!-- Alertas do Sistema -->
        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-error">❌ <%= request.getAttribute("erro") %></div>
        <% } %>
        
        <% if (request.getAttribute("sucesso") != null) { %>
            <div class="alert alert-success">✅ <%= request.getAttribute("sucesso") %></div>
        <% } %>

        <!-- Header do Dashboard -->
        <div class="dashboard-welcome">
            <h1>Bem-vindo ao VaciMed</h1>
            <p>Sistema de Gestão de Vacinação Inteligente</p>
        </div>

        <!-- Estatísticas -->
        <div class="stats-grid">
            <div class="stat-card" style="border-left-color: #4299e1;">
                <div class="stat-number" style="color: #4299e1;"><%= totalPacientes %></div>
                <div class="stat-label">Pacientes Cadastrados</div>
            </div>
            
            <div class="stat-card" style="border-left-color: #48bb78;">
                <div class="stat-number" style="color: #48bb78;"><%= totalProximos %></div>
                <div class="stat-label">Próximas Aplicações</div>
            </div>
            
            <div class="stat-card" style="border-left-color: #ed8936;">
                <div class="stat-number" style="color: #ed8936;"><%= aplicacoesHoje %></div>
                <div class="stat-label">Para Hoje</div>
            </div>
            
            <div class="stat-card" style="border-left-color: #e53e3e;">
                <div class="stat-number" style="color: #e53e3e;"><%= dosesAtrasadas %></div>
                <div class="stat-label">Atrasadas</div>
            </div>
        </div>

        <!-- Ações Rápidas -->
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">🚀 Ações Rápidas</h2>
            </div>
            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/paciente/novo" class="action-card">
                    <div class="action-icon">👤</div>
                    <h3>Novo Paciente</h3>
                    <p>Cadastrar novo paciente no sistema</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/paciente/" class="action-card">
                    <div class="action-icon">📋</div>
                    <h3>Listar Pacientes</h3>
                    <p>Visualizar e gerenciar pacientes</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/calendario" class="action-card">
                    <div class="action-icon">📅</div>
                    <h3>Calendário Vacinal</h3>
                    <p>Ver calendário completo de vacinas</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/relatorios" class="action-card">
                    <div class="action-icon">📊</div>
                    <h3>Relatórios</h3>
                    <p>Relatórios e estatísticas detalhadas</p>
                </a>
            </div>
        </div>

        <!-- Próximas Aplicações -->
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">⏰ Próximas Aplicações</h2>
                <a href="${pageContext.request.contextPath}/lancamento/" class="btn btn-primary btn-sm">Ver Todas</a>
            </div>
            
            <% if (proximosLancamentos != null && !proximosLancamentos.isEmpty()) { %>
                <div>
                    <% for (Lancamento lancamento : proximosLancamentos) { 
                        java.time.LocalDate hoje = java.time.LocalDate.now();
                        boolean isToday = lancamento.getDataPrevista().equals(hoje);
                        boolean isLate = lancamento.getDataPrevista().isBefore(hoje);
                        String cardClass = isToday ? "today" : (isLate ? "urgent" : "upcoming");
                        String nomeVacina = "Vacina ID " + lancamento.getIdCalendarioVacina(); // Temporário
                    %>
                        <div class="appointment-card <%= cardClass %>">
                            <div class="appointment-header">
                                <div class="appointment-title">
                                    <% if (isToday) { %>🟢<% } else if (isLate) { %>🔴<% } else { %>🔵<% } %>
                                    <%= nomeVacina %>
                                </div>
                                <div class="appointment-date">
                                    <%= lancamento.getDataPrevista() %>
                                    <% if (isToday) { %> <strong>(Hoje)</strong><% } %>
                                    <% if (isLate) { %> <strong>(Atrasada)</strong><% } %>
                                </div>
                            </div>
                            <div class="appointment-details">
                                <div class="appointment-patient">
                                    <strong>Paciente:</strong> ID <%= lancamento.getIdPaciente() %>
                                </div>
                                <div class="action-links">
                                    <a href="${pageContext.request.contextPath}/lancamento/aplicar?id=<%= lancamento.getIdLancamento() %>&pacienteId=<%= lancamento.getIdPaciente() %>" 
                                       class="btn btn-success btn-sm">💉 Aplicar</a>
                                    <a href="${pageContext.request.contextPath}/paciente/detalhes?id=<%= lancamento.getIdPaciente() %>" 
                                       class="btn btn-primary btn-sm">👁️ Ver Paciente</a>
                                </div>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">📅</div>
                    <h3>Nenhuma aplicação agendada</h3>
                    <p>Não há aplicações agendadas para os próximos dias.</p>
                    <a href="${pageContext.request.contextPath}/paciente/" class="btn btn-primary">Ver Pacientes</a>
                </div>
            <% } %>
        </div>

        <!-- Lista de Pacientes Recentes -->
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">👥 Meus Pacientes</h2>
                <a href="${pageContext.request.contextPath}/paciente/" class="btn btn-primary btn-sm">Ver Todos</a>
            </div>
            
            <% if (pacientes != null && !pacientes.isEmpty()) { %>
                <div class="pacientes-grid">
                    <% 
                    int count = 0;
                    for (Paciente paciente : pacientes) { 
                        if (count >= 5) break; // Mostrar apenas 5
                        count++;
                    %>
                        <div class="paciente-card">
                            <div class="paciente-header">
                                <h4><%= paciente.getNome() %></h4>
                                <span class="paciente-cpf"><%= paciente.getCpf() %></span>
                            </div>
                            <div class="paciente-info">
                                <div class="info-item">
                                    <span class="info-label">📅 Nasc.:</span>
                                    <span class="info-value"><%= paciente.getDataNascimento() %></span>
                                </div>
                                <% if (paciente.getTelefone() != null && !paciente.getTelefone().isEmpty()) { %>
                                    <div class="info-item">
                                        <span class="info-label">📞 Tel:</span>
                                        <span class="info-value"><%= paciente.getTelefone() %></span>
                                    </div>
                                <% } %>
                            </div>
                            <div class="paciente-actions">
                                <div class="action-links">
                                    <a href="${pageContext.request.contextPath}/paciente/detalhes?id=<%= paciente.getIdPaciente() %>" 
                                       class="btn btn-primary btn-sm">👁️ Ver</a>
                                    <a href="${pageContext.request.contextPath}/paciente/editar?id=<%= paciente.getIdPaciente() %>" 
                                       class="btn btn-warning btn-sm">✏️ Editar</a>
                                    <a href="${pageContext.request.contextPath}/dose/nova?pacienteId=<%= paciente.getIdPaciente() %>" 
                                       class="btn btn-success btn-sm">💉 + Dose</a>
                                </div>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">👤</div>
                    <h3>Nenhum paciente cadastrado</h3>
                    <p>Comece cadastrando seu primeiro paciente.</p>
                    <a href="${pageContext.request.contextPath}/paciente/novo" class="btn btn-primary">Cadastrar Primeiro Paciente</a>
                </div>
            <% } %>
        </div>

        <!-- Alertas do Sistema -->
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">⚠️ Alertas do Sistema</h2>
            </div>
            
            <div>
                <% if (dosesAtrasadas > 0) { %>
                    <div class="alert-custom error">
                        <strong>🔴 <%= dosesAtrasadas %> dose(s) atrasada(s)</strong> - Necessita atenção imediata
                    </div>
                <% } %>
                
                <% if (aplicacoesHoje > 0) { %>
                    <div class="alert-custom warning">
                        <strong>🟡 <%= aplicacoesHoje %> dose(s) para hoje</strong> - Não se esqueça das aplicações
                    </div>
                <% } %>
                
                <% if (totalPacientes == 0) { %>
                    <div class="alert-custom warning">
                        <strong>📝 Nenhum paciente cadastrado</strong> - Comece cadastrando seu primeiro paciente
                    </div>
                <% } %>
                
                <% if (dosesAtrasadas == 0 && aplicacoesHoje == 0 && totalPacientes > 0) { %>
                    <div class="alert-custom success">
                        <strong>✅ Tudo em dia!</strong> - Não há alertas pendentes
                    </div>
                <% } %>
            </div>
        </div>

        <!-- Menu de Navegação Rápida -->
        <div class="section">
            <div class="section-header">
                <h2 class="section-title">🧭 Navegação Rápida</h2>
            </div>
            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/calendario" class="action-card">
                    <div class="action-icon">📋</div>
                    <h3>Calendário Vacinal</h3>
                    <p>Consultar vacinas do calendário nacional</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/dose/" class="action-card">
                    <div class="action-icon">💊</div>
                    <h3>Gerenciar Doses</h3>
                    <p>Visualizar e gerenciar todas as doses</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/lancamento/" class="action-card">
                    <div class="action-icon">📝</div>
                    <h3>Histórico</h3>
                    <p>Ver histórico de aplicações</p>
                </a>
                
                <a href="${pageContext.request.contextPath}/relatorios" class="action-card">
                    <div class="action-icon">📈</div>
                    <h3>Estatísticas</h3>
                    <p>Relatórios e métricas do sistema</p>
                </a>
            </div>
        </div>
    </div>
</body>
</html>