package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o que caracteriza o Template Method: o esqueleto é fixo e não pode
 * ser alterado, e os passos são chamados na ordem definida pela classe-base.
 */
class ProcedimentoMedicaoEsqueletoTest {

    private final ProcedimentoEspiao espiao = new ProcedimentoEspiao();

    @Test
    void templateMethodDeveSerFinalParaNaoPoderSerSobrescrito() throws NoSuchMethodException {
        Method executar = ProcedimentoMedicao.class.getMethod("executar", double.class);

        assertTrue(Modifier.isFinal(executar.getModifiers()));
    }

    @Test
    void passosConcretosDevemSerPrivadosEntaoNaoPodemSerAlterados() {
        for (String nome : List.of("validar", "classificar", "montarMensagem")) {
            boolean encontrado = false;
            for (Method m : ProcedimentoMedicao.class.getDeclaredMethods()) {
                if (m.getName().equals(nome)) {
                    encontrado = true;
                    assertTrue(Modifier.isPrivate(m.getModifiers()), nome + " deveria ser privado");
                }
            }
            assertTrue(encontrado, "passo não encontrado: " + nome);
        }
    }

    @Test
    void deveChamarOsPassosNaOrdemDoEsqueleto() {
        espiao.executar(5.0);   // calibrado 6.0 -> NORMAL

        List<String> chamadas = espiao.getChamadas();
        int calibrar = chamadas.indexOf("calibrar");
        int hook = chamadas.indexOf("aoClassificar:NORMAL");
        int primeiraDeMensagem = chamadas.indexOf("unidade");

        assertEquals(0, calibrar, "calibrar deve ser o primeiro passo abstrato");
        assertTrue(chamadas.indexOf("limiteCritico") > calibrar, "classificar vem depois de calibrar");
        assertTrue(chamadas.indexOf("limiteAlerta") > calibrar, "classificar vem depois de calibrar");
        assertTrue(hook > chamadas.indexOf("limiteAlerta"), "o hook vem depois da classificação");
        assertTrue(primeiraDeMensagem > hook, "a mensagem é montada depois do hook");
    }

    @Test
    void cadaPassoAbstratoDeveSerChamadoPeloMenosUmaVez() {
        espiao.executar(5.0);

        for (String passo : List.of("calibrar", "limiteAlerta", "limiteCritico", "nomeSensor", "unidade")) {
            assertTrue(espiao.getChamadas().contains(passo), "passo não chamado: " + passo);
        }
    }

    @Test
    void calibrarDeveSerChamadoExatamenteUmaVezPorExecucao() {
        espiao.executar(5.0);

        assertEquals(1, espiao.getChamadas().stream().filter("calibrar"::equals).count());
    }

    @Test
    void valorInvalidoDeveSerRecusadoANTESDeQualquerPassoAbstrato() {
        assertThrows(IllegalArgumentException.class, () -> espiao.executar(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> espiao.executar(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> espiao.executar(Double.NEGATIVE_INFINITY));

        assertTrue(espiao.getChamadas().isEmpty(), "nenhum passo deveria ter rodado");
    }

    @Test
    void hookDeveReceberOStatusEJaCalculadoPelaClasseBase() {
        espiao.executar(5.0);    // 6.0 -> NORMAL
        espiao.executar(10.0);   // 11.0 -> ALERTA
        espiao.executar(25.0);   // 26.0 -> CRITICO

        List<String> hooks = espiao.getChamadas().stream()
                .filter(c -> c.startsWith("aoClassificar")).toList();
        assertEquals(List.of("aoClassificar:NORMAL", "aoClassificar:ALERTA", "aoClassificar:CRITICO"), hooks);
    }

    @Test
    void classificacaoDeveUsarOsLimitesFornecidosPelaSubclasseNoValorCalibrado() {
        assertEquals("NORMAL", espiao.executar(8.0).status());    // 9.0
        assertEquals("ALERTA", espiao.executar(9.0).status());    // 10.0 = limite de alerta
        assertEquals("ALERTA", espiao.executar(18.0).status());   // 19.0
        assertEquals("CRITICO", espiao.executar(19.0).status());  // 20.0 = limite crítico
    }

    @Test
    void resultadoDeveReunirOQueCadaPassoProduziu() {
        ResultadoMedicao r = espiao.executar(5.0);

        assertEquals("ESPIAO", r.tipo());
        assertEquals(5.0, r.valorBruto());
        assertEquals(6.0, r.valorCalibrado());
        assertEquals("u", r.unidade());
        assertEquals("NORMAL", r.status());
        assertEquals("ESPIAO: 6.0 u (bruto 5.0) -> NORMAL", r.mensagem());
    }

    @Test
    void clienteDeveFuncionarConhecendoApenasATipoAbstrato() {
        ProcedimentoMedicao[] procedimentos = {
                new ProcedimentoTemperatura(), new ProcedimentoPressao(),
                new ProcedimentoTemperaturaComAlarme(), new ProcedimentoEspiao() };

        for (ProcedimentoMedicao p : procedimentos) {
            ResultadoMedicao r = assertDoesNotThrow(() -> p.executar(1.0));
            assertNotNull(r.mensagem());
        }
    }
}
