<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar Dose</title>
</head>
<body>
    <h2>Cadastrar Dose/Vacina</h2>

    <form action="dose" method="post">
        <input type="hidden" name="acao" value="cadastrar">

        <label>Nome da vacina:</label><br>
        <input type="text" name="nome" required><br><br>

        <label>Número de doses:</label><br>
        <input type="number" name="num_doses" required><br><br>

        <label>Periodicidade:</label><br>
        <input type="number" name="periodicidade_valor" required>
        <select name="periodicidade_unidade">
            <option value="dias">Dias</option>
            <option value="meses">Meses</option>
            <option value="anos">Anos</option>
        </select>
        <br><br>

        <label>Medicamentos que têm interação:</label><br>
        <textarea name="medicamentos_interacao"></textarea><br><br>

        <button type="submit">Salvar</button>
    </form>

    <br><a href="dashboard_medico.jsp">Voltar</a>
</body>
</html>
