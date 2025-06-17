<?php
require_once '../src/api.php';

// Agora o endpoint retorna eventos com alunos dentro
$eventosComAlunos = fazerRequisicao('http://localhost:3002/alunos/eventos-com-alunos');

if (!is_array($eventosComAlunos)) {
  $eventosComAlunos = [];
}
?>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <title>Certificados - UniALFA Eventos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <link rel="stylesheet" href="../css/style.css">
  <style>
    main {
      padding: 40px 20px;
      max-width: 1000px;
      margin: 0 auto;
    }

    .certificado-titulo {
      text-align: center;
      font-size: 2.5rem;
      font-weight: bold;
      margin-bottom: 30px;
      color: #fff;
    }

    .nenhum {
      text-align: center;
      color: #bbb;
      font-size: 1.2rem;
    }

    .table-container {
      background: #1a1a1a;
      border-radius: 12px;
      padding: 20px;
      margin-bottom: 50px;
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
    }

    .table {
      color: #f1f1f1;
      border-color: #444;
    }

    .table th,
    .table td {
      vertical-align: middle;
    }

    .btn-certificado {
      background-color: #7b2ff7;
      color: white;
      padding: 8px 16px;
      border-radius: 8px;
      text-decoration: none;
      transition: background-color 0.3s ease;
      font-weight: 600;
    }

    .btn-certificado:hover {
      background-color: #a53afc;
      color: #fff;
    }

    .evento-titulo {
      font-size: 1.8rem;
      color: #ddd;
      margin-bottom: 10px;
    }

    .evento-descricao {
      color: #aaa;
      margin-bottom: 20px;
    }
  </style>
</head>

<body>

  <?php include('../templates/header.php'); ?>

  <main>
    <h1 class="certificado-titulo">Certificados Emitidos</h1>

    <?php if (empty($eventosComAlunos)): ?>
      <p class="nenhum">Nenhum evento com inscrições encontradas para emissão de certificados.</p>
    <?php else: ?>
      <?php foreach ($eventosComAlunos as $evento): ?>
        <div class="table-container">
          <div class="evento-titulo"><?= htmlspecialchars($evento['nome']); ?></div>
          <div class="evento-descricao">
            <?= htmlspecialchars($evento['descricao']); ?><br>
            Local: <?= htmlspecialchars($evento['local']); ?><br>
            Data: <?= date('d/m/Y', strtotime($evento['data_ini'])) ?> a <?= date('d/m/Y', strtotime($evento['data_fim'])) ?>
          </div>

          <?php if (empty($evento['aluno'])): ?>
            <p class="nenhum">Nenhum aluno inscrito neste evento.</p>
          <?php else: ?>
            <table class="table table-striped table-bordered align-middle text-center">
              <thead class="table-dark">
                <tr>
                  <th>Nome</th>
                  <th>RA</th>
                  <th>Certificado</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($evento['aluno'] as $aluno): ?>
                  <tr>
                    <td><?= htmlspecialchars($aluno['nome']); ?></td>
                    <td><?= htmlspecialchars($aluno['matricula_ra']); ?></td>
                    <td>
                      <a href="http://localhost:3002/certificado/<?= $aluno['aluno_id'] ?? $aluno['id'] ?? '' ?>" class="btn-certificado" target="_blank">
                        Visualizar
                      </a>
                    </td>
                  </tr>
                <?php endforeach; ?>
              </tbody>
            </table>
          <?php endif; ?>
        </div>
      <?php endforeach; ?>
    <?php endif; ?>
  </main>

  <?php include('../templates/footer.php'); ?>

</body>

</html>