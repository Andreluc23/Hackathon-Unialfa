<?php
function fazerRequisicao($url, $metodo = 'GET', $dados = null) {
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    if ($metodo === 'POST') {
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($dados));
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    }

    $resposta = curl_exec($ch);

    if (curl_errno($ch)) {
        echo "Erro ao conectar à API: " . curl_error($ch);
        curl_close($ch);
        return null; // ou [] se preferir
    }

    curl_close($ch);

    $resultado = json_decode($resposta, true);

    if (json_last_error() !== JSON_ERROR_NONE) {
        echo "Erro ao decodificar JSON: " . json_last_error_msg();
        return null;
    }

    return $resultado;
}
?>
