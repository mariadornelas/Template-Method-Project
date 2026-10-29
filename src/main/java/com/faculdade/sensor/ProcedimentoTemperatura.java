package com.faculdade.sensor;

/**
 * Concrete Class 1: preenche só os passos abstratos. O hardware de
 * temperatura tem um desvio conhecido de -2 graus, corrigido na calibração.
 */
public class ProcedimentoTemperatura extends ProcedimentoMedicao {

    private static final double DESVIO_DO_HARDWARE = 2.0;

    @Override
    protected double calibrar(double valorBruto) {
        return valorBruto + DESVIO_DO_HARDWARE;
    }

    @Override
    protected double limiteAlerta() {
        return 70.0;
    }

    @Override
    protected double limiteCritico() {
        return 90.0;
    }

    @Override
    protected String nomeSensor() {
        return "TEMPERATURA";
    }

    @Override
    protected String unidade() {
        return "°C";
    }
}
