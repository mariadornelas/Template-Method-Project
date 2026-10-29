package com.faculdade.sensor;

import java.util.Locale;

public abstract class ProcedimentoMedicao {

    public final ResultadoMedicao executar(double valorBruto) {
        validar(valorBruto);
        double valorCalibrado = calibrar(valorBruto);
        String status = classificar(valorCalibrado);
        aoClassificar(status, valorCalibrado);
        String mensagem = montarMensagem(valorBruto, valorCalibrado, status);
        return new ResultadoMedicao(nomeSensor(), valorBruto, valorCalibrado, unidade(), status, mensagem);
    }

    protected abstract double calibrar(double valorBruto);

    protected abstract double limiteAlerta();

    protected abstract double limiteCritico();

    protected abstract String nomeSensor();

    protected abstract String unidade();

    protected void aoClassificar(String status, double valorCalibrado) {
    }

    private void validar(double valorBruto) {
        if (Double.isNaN(valorBruto) || Double.isInfinite(valorBruto)) {
            throw new IllegalArgumentException("valor bruto inválido: " + valorBruto);
        }
    }

    private String classificar(double valorCalibrado) {
        if (valorCalibrado >= limiteCritico()) {
            return "CRITICO";
        }
        if (valorCalibrado >= limiteAlerta()) {
            return "ALERTA";
        }
        return "NORMAL";
    }

    private String montarMensagem(double valorBruto, double valorCalibrado, String status) {
        return String.format(Locale.ROOT, "%s: %.1f %s (bruto %.1f) -> %s",
                nomeSensor(), valorCalibrado, unidade(), valorBruto, status);
    }
}
