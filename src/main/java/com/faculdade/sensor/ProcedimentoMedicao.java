package com.faculdade.sensor;

import java.util.Locale;

/**
 * Abstract Class do Template Method.
 *
 * <p>{@link #executar(double)} é o <b>template method</b>: define o esqueleto
 * fixo de toda medição e é {@code final}, então nenhuma subclasse consegue
 * mudar a ordem nem pular um passo. Os passos são de três tipos:</p>
 *
 * <ul>
 *   <li><b>Concretos</b> (já implementados aqui, iguais para todos):
 *   {@link #validar}, {@link #classificar} e {@link #montarMensagem}.</li>
 *   <li><b>Abstratos</b> (as subclasses são obrigadas a implementar):
 *   {@link #calibrar}, {@link #limiteAlerta}, {@link #limiteCritico},
 *   {@link #nomeSensor} e {@link #unidade}.</li>
 *   <li><b>Hook</b> (tem implementação vazia; a subclasse pode sobrescrever
 *   se quiser): {@link #aoClassificar}.</li>
 * </ul>
 *
 * <p>Esqueleto: validar → calibrar → classificar → hook → montar mensagem.</p>
 */
public abstract class ProcedimentoMedicao {

    /** Template method: o algoritmo completo, com ordem fixa. */
    public final ResultadoMedicao executar(double valorBruto) {
        validar(valorBruto);
        double valorCalibrado = calibrar(valorBruto);
        String status = classificar(valorCalibrado);
        aoClassificar(status, valorCalibrado);
        String mensagem = montarMensagem(valorBruto, valorCalibrado, status);
        return new ResultadoMedicao(nomeSensor(), valorBruto, valorCalibrado, unidade(), status, mensagem);
    }

    // ---- passos abstratos: o que muda de sensor para sensor ----

    /** Converte o valor bruto para a unidade padrão, corrigindo desvios do hardware. */
    protected abstract double calibrar(double valorBruto);

    protected abstract double limiteAlerta();

    protected abstract double limiteCritico();

    protected abstract String nomeSensor();

    protected abstract String unidade();

    // ---- hook: ponto de extensão opcional ----

    /** Chamado depois da classificação. Por padrão não faz nada. */
    protected void aoClassificar(String status, double valorCalibrado) {
    }

    // ---- passos concretos: iguais para todos os sensores ----

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
