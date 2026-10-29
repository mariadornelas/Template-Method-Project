package com.faculdade.sensor;

public record ResultadoMedicao(String tipo, double valorBruto, double valorCalibrado,
                               String unidade, String status, String mensagem) {
}
