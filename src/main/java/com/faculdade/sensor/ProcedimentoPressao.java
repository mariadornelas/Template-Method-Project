package com.faculdade.sensor;

public class ProcedimentoPressao extends ProcedimentoMedicao {

    private static final double KPA_POR_BAR = 100.0;

    @Override
    protected double calibrar(double valorBruto) {
        return valorBruto / KPA_POR_BAR;
    }

    @Override
    protected double limiteAlerta() {
        return 8.0;
    }

    @Override
    protected double limiteCritico() {
        return 12.0;
    }

    @Override
    protected String nomeSensor() {
        return "PRESSAO";
    }

    @Override
    protected String unidade() {
        return "bar";
    }
}
