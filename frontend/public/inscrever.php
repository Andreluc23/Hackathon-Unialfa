<?php
require_once '../src/api.php';

if (!isset($_GET['eve_id']) || !is_numeric($_GET['eve_id'])) {
  header("Location: index.php?erro=evento_invalido");
  exit;
}

$eve_id = (int)$_GET['eve_id'];

try {
  $eventoSelecionado = fazerRequisicao("http://localhost:3002/eventos/{$eve_id}");

  if (!$eventoSelecionado) {
    throw new Exception("Resposta vazia da API");
  }

  if (isset($eventoSelecionado['erro'])) {
    throw new Exception($eventoSelecionado['erro']);
  }

  if (!isset($eventoSelecionado['eve_id'])) {
    throw new Exception("Estrutura de dados inesperada");
  }
} catch (Exception $e) {
  error_log("Erro no inscrever.php: " . $e->getMessage());
  header("Location: index.php?erro=evento_nao_encontrado");
  exit;
}

try {
  $palestranteResponse = fazerRequisicao("http://localhost:3002/palestrantes/evento/{$eve_id}");
  $nomePalestrante = $palestranteResponse['nome'] ?? 'Palestrante não informado';
} catch (Exception $e) {
  $nomePalestrante = 'Palestrante não informado';
  error_log("Erro ao buscar palestrante: " . $e->getMessage());
}

$mensagem = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  try {
    $requiredFields = ['nome', 'email', 'matricula_ra'];
    foreach ($requiredFields as $field) {
      if (empty($_POST[$field])) {
        throw new Exception("O campo " . ucfirst($field) . " é obrigatório");
      }
    }

    $dadosInscricao = [
      'nome' => htmlspecialchars($_POST['nome']),
      'email' => filter_var($_POST['email'], FILTER_SANITIZE_EMAIL),
      'matricula_ra' => (int)$_POST['matricula_ra'],
      'eve_id' => $eve_id,
      'password' => 'senhapadrao'
    ];

    $resposta = fazerRequisicao('http://localhost:3002/alunos', 'POST', $dadosInscricao);

    if (isset($resposta['mensagem'])) {
      $mensagem = htmlspecialchars($resposta['mensagem']);
    } else {
      throw new Exception("Resposta inesperada da API");
    }
  } catch (Exception $e) {
    $mensagem = "Erro ao processar inscrição: " . $e->getMessage();
    error_log("Erro no formulário: " . $e->getMessage());
  }
}
?>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <title>Inscrição - <?= htmlspecialchars($eventoSelecionado['nome'] ?? 'Evento'); ?></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="../css/style.css">
</head>

<body>

  <?php include('../templates/header.php'); ?>

  <main class="container my-5">
    <h1 class="text-center mb-4">Inscrição no Evento</h1>

    <div class="evento-card mb-5">
      <div class="card">
        <div class="card-body">
          <h3 class="card-title"><?= htmlspecialchars($eventoSelecionado['nome']); ?></h3>
          <p class="card-text"><?= htmlspecialchars($eventoSelecionado['descricao'] ?? ''); ?></p>
          <ul class="list-unstyled">
            <li><strong>Data:</strong> <?= $eventoSelecionado['data_ini'] ?? 'Data não informada'; ?></li>
            <li><strong>Palestrante:</strong> <?= htmlspecialchars($nomePalestrante); ?></li>
            <li><strong>Local:</strong> <?= $eventoSelecionado['local'] ?? 'Local a definir'; ?></li>
          </ul>
        </div>
      </div>
    </div>

    <?php if (!empty($mensagem)) : ?>
      <div class="alert alert-info text-center"><?= $mensagem; ?></div>
    <?php endif; ?>

    <form method="POST" class="bg-dark p-4 rounded shadow" style="max-width: 600px; margin: 0 auto;">
      <div class="mb-3">
        <label for="nome" class="form-label">Nome Completo</label>
        <input type="text" class="form-control" id="nome" name="nome" placeholder="Digite seu nome completo" required>
      </div>

      <div class="mb-3">
        <label for="email" class="form-label">E-mail Institucional</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="nome@alfa.br" required>
      </div>

      <div class="mb-3">
        <label for="matricula_ra" class="form-label">RA (Matrícula)</label>
        <input type="text" class="form-control" id="matricula_ra" name="matricula_ra" placeholder="Ex: 123456" required>
      </div>

      <div class="d-flex justify-content-between">
        <a href="index.php" class="btn btn-outline-light">Voltar</a>
        <button type="submit" class="btn btn-primary">Inscrever-se</button>
      </div>
    </form>
  </main>

  <?php include('../templates/footer.php'); ?>
</body>

</html>
