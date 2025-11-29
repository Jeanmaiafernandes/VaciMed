<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String pacienteId = (String) request.getAttribute("pacienteId");
    String vacinaId = (String) request.getAttribute("vacinaId");
    Integer numeroDose = (Integer) request.getAttribute("numeroDose");
    
    if (pacienteId == null) pacienteId = request.getParameter("pacienteId");
    if (vacinaId == null) vacinaId = request.getParameter("vacinaId");
    if (numeroDose == null) {
        String doseParam = request.getParameter("dose");
        numeroDose = (doseParam != null) ? Integer.parseInt(doseParam) : 1;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Dose - VaciMed</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .btn { padding: 10px 15px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .btn-success { background: #28a745; }
        .btn-secondary { background: #6c757d; }
    </style>
</head>
<body>
    <div class="container">
        <h1>💉 Registrar Nova Dose</h1>
        
        <% if (request.getAttribute("erro") != null) { %>
            <div style="background: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                ❌ <%= request.getAttribute("erro") %>
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/dose/nova" method="post">
            <input type="hidden" name="pacienteId" value="<%= pacienteId %>">
            <input type="hidden" name="vacinaId" value="<%= vacinaId %>">
            
            <div class="form-group">
                <label>Número da Dose:</label>
                <input type="text" value="Dose <%= numeroDose %>" readonly>
            </div>
            
            <div class="form-group">
                <label for="tipoDose">Tipo de Dose:</label>
                <select id="tipoDose" name="tipoDose" required>
                    <option value="Dose única">Dose única</option>
                    <option value="Primeira dose">Primeira dose</option>
                    <option value="Segunda dose">Segunda dose</option>
                    <option value="Terceira dose">Terceira dose</option>
                    <option value="Dose de reforço">Dose de reforço</option>
                    <option value="Dose anual">Dose anual</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="dataAplicacao">Data de Aplicação:</label>
                <input type="date" id="dataAplicacao" name="dataAplicacao" required 
                       value="<%= java.time.LocalDate.now() %>">
            </div>
            
            <div class="form-group">
                <label for="descricao">Descrição:</label>
                <input type="text" id="descricao" name="descricao" 
                       placeholder="Ex: Vacina contra COVID-19 - Dose 1">
            </div>
            
            <div class="form-group">
                <label for="observacoes">Observações:</label>
                <textarea id="observacoes" name="observacoes" rows="3" 
                          placeholder="Observações sobre a aplicação..."></textarea>
            </div>
            
            <div style="display: flex; gap: 10px;">
                <button type="submit" class="btn btn-success">💾 Registrar Dose</button>
                <a href="${pageContext.request.contextPath}/calendario" class="btn btn-secondary">← Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>