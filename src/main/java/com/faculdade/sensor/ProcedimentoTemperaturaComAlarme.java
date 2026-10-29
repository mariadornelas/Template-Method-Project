package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Class 3: reaproveita tudo de {@link ProcedimentoTemperatura} e
 * sobrescreve apenas o <b>hook</b> {@code aoClassificar} para registrar um
 * alarme em leituras críticas. Mostra o uso do ponto de extensão opcional:
 * o esqueleto continua o mesmo, só ganha um comportamento extra.
 */
public class ProcedimentoTemperaturaComAlarme extends ProcedimentoTemperatura {

    private final List<String> alarmes = new ArrayList<>();

    @Override
    protected void aoClassificar(String status, double valorCalibrado) {
        if ("CRITICO".equals(status)) {
            alarmes.add(String.format(Locale.ROOT, "ALARME: temperatura crítica %.1f", valorCalibrado));
        }
    }

    public List<String> getAlarmes() {
        return Collections.unmodifiableList(alarmes);
    }
}
