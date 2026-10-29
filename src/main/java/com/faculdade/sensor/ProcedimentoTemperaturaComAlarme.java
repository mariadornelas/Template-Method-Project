package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
