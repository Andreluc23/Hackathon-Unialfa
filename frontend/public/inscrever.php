<?php
require_once './config.php';

// Verifica se o ID do evento foi passado
$evento_id = isset($_GET['evento_id']) ? (int)$_GET['evento_id'] : null;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Processa o formulário
    $dados = [
        'nome' => $_POST['nome'],
        'email' => $_POST['email'],
        'matricula_ra' => (int)$_POST['matricula_ra'],
        'eve_id' => (int)$_POST['evento_id']
    ];

    // Envia para a API Node.js (POST /alunos)
    $ch = curl_init("$api_url/alunos");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($dados));
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    $response = curl_exec($ch);
    curl_close($ch);

    $resultado = json_decode($response, true);
    if (isset($resultado['mensagem'])) {
        echo "<p>{$resultado['mensagem']}</p>";
    } else {
        echo "<p>Erro ao enviar inscrição.</p>";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Inscrever-se</title>
</head>
<body>
    <h1>Inscrever-se em Evento</h1>
    <form method="POST">
        <input type="hidden" name="evento_id" value="<?= $evento_id ?>">
        
        <label>Nome:</label>
        <input type="text" name="nome" required><br>
        
        <label>Email:</label>
        <input type="email" name="email" required><br>
        
        <label>Matrícula/RA:</label>
        <input type="number" name="matricula_ra" required><br>
        
        <button type="submit">Enviar</button>
    </form>
    <a href="index.php">Voltar para eventos</a>
</body>
</html>