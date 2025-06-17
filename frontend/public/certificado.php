<?php
$ra = $_GET['matricula_ra'] ?? 0;
$dados = file_get_contents("http://localhost:3002/alunos");
$alunos = json_decode($dados, true);

$alunoId = null;
foreach ($alunos as $aluno) {
    if ($aluno['matricula_ra'] == $ra) {
        $alunoId = $aluno['aluno_id'];
        break;
    }
}

if ($alunoId): ?>
    <iframe src="http://localhost:3002/certificado/<?= $alunoId ?>" width="100%" height="600px"></iframe>
<?php else: ?>
    <p>Certificado não encontrado para RA informado.</p>
<?php endif; ?>
