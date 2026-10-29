package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcedimentosConcretosTest {

    @Test
    void temperaturaDeveCalibrarSomandoODesvioDoHardware() {
        ResultadoMedicao r = new ProcedimentoTemperatura().executar(50.0);

        assertEquals(50.0, r.valorBruto());
        assertEquals(52.0, r.valorCalibrado());
        assertEquals("°C", r.unidade());
        assertEquals("TEMPERATURA", r.tipo());
    }

    @Test
    void temperaturaDeveClassificarOValorCalibradoENaoOBruto() {
        ProcedimentoTemperatura p = new ProcedimentoTemperatura();

        assertEquals("NORMAL", p.executar(67.0).status());
        assertEquals("ALERTA", p.executar(68.0).status());
        assertEquals("ALERTA", p.executar(87.0).status());
        assertEquals("CRITICO", p.executar(88.0).status());
    }

    @Test
    void temperaturaDeveMontarAMensagemComUnidadeEValorBruto() {
        assertEquals("TEMPERATURA: 72.0 °C (bruto 70.0) -> ALERTA",
                new ProcedimentoTemperatura().executar(70.0).mensagem());
    }

    @Test
    void pressaoDeveConverterKpaParaBar() {
        ResultadoMedicao r = new ProcedimentoPressao().executar(500.0);

        assertEquals(500.0, r.valorBruto());
        assertEquals(5.0, r.valorCalibrado());
        assertEquals("bar", r.unidade());
        assertEquals("PRESSAO", r.tipo());
    }

    @Test
    void pressaoDeveClassificarOValorJaConvertidoParaBar() {
        ProcedimentoPressao p = new ProcedimentoPressao();

        assertEquals("NORMAL", p.executar(799.0).status());
        assertEquals("ALERTA", p.executar(800.0).status());
        assertEquals("ALERTA", p.executar(1199.0).status());
        assertEquals("CRITICO", p.executar(1200.0).status());
    }

    @Test
    void pressaoDeveMontarAMensagemComUnidadeEValorBruto() {
        assertEquals("PRESSAO: 13.0 bar (bruto 1300.0) -> CRITICO",
                new ProcedimentoPressao().executar(1300.0).mensagem());
    }

    @Test
    void mesmoNumeroBrutoDeveTerSignificadoDiferenteEmCadaSensor() {

        assertEquals("CRITICO", new ProcedimentoTemperatura().executar(500.0).status());
        assertEquals("NORMAL", new ProcedimentoPressao().executar(500.0).status());
    }

    @Test
    void ambosDevemRecusarValoresInvalidosPeloMesmoPassoConcretoDaBase() {
        assertThrows(IllegalArgumentException.class, () -> new ProcedimentoTemperatura().executar(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new ProcedimentoPressao().executar(Double.NaN));
    }


    @Test
    void semSobrescreverOHookOResultadoDeveSerIgualAoDaClassePai() {
        ResultadoMedicao daPai = new ProcedimentoTemperatura().executar(95.0);
        ResultadoMedicao dofilho = new ProcedimentoTemperaturaComAlarme().executar(95.0);

        assertEquals(daPai, dofilho);
    }

    @Test
    void hookSobrescritoDeveRegistrarAlarmeApenasEmLeituraCritica() {
        ProcedimentoTemperaturaComAlarme p = new ProcedimentoTemperaturaComAlarme();

        p.executar(50.0);
        p.executar(75.0);
        assertTrue(p.getAlarmes().isEmpty());

        p.executar(95.0);
        assertEquals(1, p.getAlarmes().size());
        assertEquals("ALARME: temperatura crítica 97.0", p.getAlarmes().get(0));
    }

    @Test
    void cadaLeituraCriticaDeveGerarUmNovoAlarme() {
        ProcedimentoTemperaturaComAlarme p = new ProcedimentoTemperaturaComAlarme();

        p.executar(95.0);
        p.executar(99.0);

        assertEquals(2, p.getAlarmes().size());
    }

    @Test
    void leituraInvalidaNaoDeveGerarAlarme() {
        ProcedimentoTemperaturaComAlarme p = new ProcedimentoTemperaturaComAlarme();

        assertThrows(IllegalArgumentException.class, () -> p.executar(Double.NaN));

        assertTrue(p.getAlarmes().isEmpty());
    }

    @Test
    void alarmesDevolvidosDevemSerSomenteLeitura() {
        ProcedimentoTemperaturaComAlarme p = new ProcedimentoTemperaturaComAlarme();
        p.executar(95.0);

        assertThrows(UnsupportedOperationException.class, () -> p.getAlarmes().add("forjado"));
    }

    @Test
    void doisProcedimentosDevemTerAlarmesIndependentes() {
        ProcedimentoTemperaturaComAlarme a = new ProcedimentoTemperaturaComAlarme();
        ProcedimentoTemperaturaComAlarme b = new ProcedimentoTemperaturaComAlarme();

        a.executar(95.0);

        assertEquals(1, a.getAlarmes().size());
        assertTrue(b.getAlarmes().isEmpty());
    }
}
