<?php
require_once './config.php';

// Faz a requisição GET para /eventos
$response = file_get_contents("$api_url/eventos");
$eventos = json_decode($response, true);

if (!$eventos) {
    die("Erro ao carregar eventos.");
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Eventos UniALFA</title>
</head>
<body>
    <h1>Eventos Disponíveis</h1>
    <ul>
        <?php foreach ($eventos as $evento): ?>
            <li>
                <strong><?= htmlspecialchars($evento['nome']) ?></strong><br>
                Descrição: <?= htmlspecialchars($evento['descricao']) ?><br>
                Data: <?= htmlspecialchars($evento['data_ini']) ?> a <?= htmlspecialchars($evento['fim']) ?><br>
                Local: <?= htmlspecialchars($evento['local']) ?><br>
                <a href="inscrever.php?evento_id=<?= $evento['eve_id'] ?>">Inscrever-se</a>
            </li>
            <hr>
        <?php endforeach; ?>
    </ul>
</body>
</html>