package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Dublê de teste: subclasse mínima que registra, na ordem, cada passo
 * abstrato e o hook que o template method chama. Não faz parte do padrão.
 * Valores fixos: calibrar soma 1, alerta em 10, crítico em 20.
 */
class ProcedimentoEspiao extends ProcedimentoMedicao {

    private final List<String> chamadas = new ArrayList<>();

    @Override
    protected double calibrar(double valorBruto) {
        chamadas.add("calibrar");
        return valorBruto + 1.0;
    }

    @Override
    protected double limiteAlerta() {
        chamadas.add("limiteAlerta");
        return 10.0;
    }

    @Override
    protected double limiteCritico() {
        chamadas.add("limiteCritico");
        return 20.0;
    }

    @Override
    protected String nomeSensor() {
        chamadas.add("nomeSensor");
        return "ESPIAO";
    }

    @Override
    protected String unidade() {
        chamadas.add("unidade");
        return "u";
    }

    @Override
    protected void aoClassificar(String status, double valorCalibrado) {
        chamadas.add("aoClassificar:" + status);
    }

    List<String> getChamadas() {
        return chamadas;
    }
}
