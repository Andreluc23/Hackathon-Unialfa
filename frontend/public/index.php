<?php
require_once '../src/api.php';

try {
    $eventos = fazerRequisicao('http://localhost:3002/eventos');

    if (!is_array($eventos)) {
        throw new Exception("Resposta inválida da API de eventos");
    }

    foreach ($eventos as &$evento) {
        if (!isset($evento['eve_id']) || empty($evento['eve_id'])) {
            $evento['palestrante'] = 'Palestrante não informado';
            continue;
        }

        try {
            $palestrantes = fazerRequisicao("http://localhost:3002/palestrantes/evento/{$evento['eve_id']}");
            if (($palestrantes)  && isset($palestrantes['nome'])) {
                $evento['palestrante'] = $palestrantes['nome'];
            } else {
                $evento['palestrante'] = 'Palestrante não informado';
            }
        } catch (Exception $e) {
            $evento['palestrante'] = 'Palestrante não informado';
            error_log("Erro ao buscar palestrante: " . $e->getMessage());
        }
    }
    unset($evento);
} catch (Exception $e) {
    $eventos = [];
    $mensagemErro = "Erro ao carregar eventos: " . $e->getMessage();
    error_log($mensagemErro);
}
?>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Eventos UniALFA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="../css/style.css">
</head>

<body class="bg-dark text-light">
    <?php include('../templates/header.php'); ?>

    <section class="container my-5">
        <div class="position-relative rounded overflow-hidden" style="height: 400px; background-color: rgba(255,255,255,0.05);">
            <img src="../img/banner2.jpg" class="w-100 h-100 position-absolute top-0 start-0" style="object-fit: cover; opacity: 0.2;">
            <div class="position-relative h-100 d-flex flex-column justify-content-center align-items-start p-5">
                <h1 class="display-4 fw-bold">Bem-vindo ao UniALFA Eventos</h1>
                <p class="lead">Participe de palestras, workshops e mesas redondas com especialistas</p>
                <a href="#eventos" class="btn btn-primary">Ver eventos</a>
            </div>
        </div>
    </section>

    <?php if (isset($mensagemErro)): ?>
        <div class="container alert alert-danger">
            <?= htmlspecialchars($mensagemErro) ?>
        </div>
    <?php endif; ?>

    <section class="container mb-4">
        <div class="row g-3 align-items-center">
            <div class="col-md-6">
                <input type="text" id="busca" class="form-control" placeholder="Buscar por título">
            </div>
            <div class="col-md-3">
                <select id="filtroTipo" class="form-select">
                    <option value="">Filtrar por tipo</option>
                    <option value="palestra">Palestra</option>
                    <option value="workshop">Workshop</option>
                    <option value="mesa redonda">Mesa Redonda</option>
                </select>
            </div>
            <div class="col-md-3">
                <select id="filtroMes" class="form-select">
                    <option value="">Filtrar por mês</option>
                    <option value="01">Janeiro</option>
                    <option value="02">Fevereiro</option>
                    <option value="03">Março</option>
                    <option value="04">Abril</option>
                    <option value="05">Maio</option>
                    <option value="06">Junho</option>
                </select>
            </div>
        </div>
    </section>

    <section class="container my-5" id="eventos">
        <h1 class="text-center mb-4">Lista de Eventos</h1>

        <?php if (empty($eventos)): ?>
            <div class="alert alert-info">
                Nenhum evento disponível no momento.
            </div>
        <?php else: ?>
            <div class="row g-4" id="eventosLista">
                <?php foreach ($eventos as $evento): ?>
                    <?php
                    $eve_id = $evento['eve_id'];
                    $nome = $evento['nome'] ?? 'Evento sem nome';
                    $descricao = $evento['descricao'] ?? 'Descrição não disponível';
                    $data = $evento['data_ini'] ?? ($evento['data'] ?? 'Data não informada');
                    $palestrante = $evento['palestrante'] ?? 'Palestrante não informado';

                    $dataFormatada = '';
                    if (!empty($data)) {
                        try {
                            $dataObj = new DateTime($data);
                            $dataFormatada = $dataObj->format('d/m/Y');
                        } catch (Exception $e) {
                            $dataFormatada = $data;
                        }
                    }
                    ?>

                    <div class="col-md-6 col-lg-4 evento-card"
                        data-titulo="<?= htmlspecialchars(strtolower($nome)); ?>"
                        data-tipo="<?= htmlspecialchars(strtolower($evento['tipo'] ?? '')); ?>"
                        data-mes="<?= !empty($data) ? date('m', strtotime($data)) : '' ?>">
                        <div class="card h-100 bg-light text-dark">
                            <div class="card-body">
                                <h5 class="card-title"><?= htmlspecialchars($nome); ?></h5>
                                <p class="card-text"><?= htmlspecialchars($descricao); ?></p>
                                <ul class="list-unstyled small">
                                    <li><strong>Data:</strong> <?= htmlspecialchars($dataFormatada); ?></li>
                                    <li><strong>Palestrante:</strong> <?= htmlspecialchars($palestrante); ?></li>
                                </ul>
                                <a href="inscrever.php?eve_id=<?= $eve_id ?>" class="btn btn-primary mt-3 w-100">Inscreva-se</a>

                            </div>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </section>

    <?php include('../templates/footer.php'); ?>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const busca = document.getElementById('busca');
            const filtroTipo = document.getElementById('filtroTipo');
            const filtroMes = document.getElementById('filtroMes');
            const cards = document.querySelectorAll('.evento-card');

            function aplicarFiltros() {
                const termo = busca.value.toLowerCase();
                const tipo = filtroTipo.value.toLowerCase();
                const mes = filtroMes.value;

                cards.forEach(card => {
                    const titulo = card.dataset.titulo;
                    const cardTipo = card.dataset.tipo;
                    const cardMes = card.dataset.mes;

                    const matchBusca = titulo.includes(termo);
                    const matchTipo = !tipo || cardTipo.includes(tipo);
                    const matchMes = !mes || cardMes === mes;

                    card.style.display = (matchBusca && matchTipo && matchMes) ? 'block' : 'none';
                });
            }

            busca.addEventListener('input', aplicarFiltros);
            filtroTipo.addEventListener('change', aplicarFiltros);
            filtroMes.addEventListener('change', aplicarFiltros);
            aplicarFiltros();
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>