package com.faculdade.sensor;

/**
 * Resultado de uma medição, devolvido pelo template method. Imutável.
 *
 * @param tipo            nome do sensor (ex.: "TEMPERATURA")
 * @param valorBruto      valor como veio do hardware
 * @param valorCalibrado  valor depois do passo de calibração, na unidade padrão
 * @param unidade         unidade do valor calibrado
 * @param status          NORMAL, ALERTA ou CRITICO
 * @param mensagem        texto pronto para exibição
 */
public record ResultadoMedicao(String tipo, double valorBruto, double valorCalibrado,
                               String unidade, String status, String mensagem) {
}
