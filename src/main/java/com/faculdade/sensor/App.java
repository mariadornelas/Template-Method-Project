package com.faculdade.sensor;

/**
 * Cliente de demonstração. Trata todos os procedimentos pelo tipo
 * abstrato {@link ProcedimentoMedicao} e chama sempre o mesmo método,
 * {@code executar}.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== Mesmo esqueleto, passos diferentes ===");
        mostrar(new ProcedimentoTemperatura(), 50.0);
        mostrar(new ProcedimentoTemperatura(), 70.0);   // 70 bruto + 2 de calibração = 72 -> ALERTA
        mostrar(new ProcedimentoPressao(), 500.0);      // 500 kPa = 5 bar -> NORMAL
        mostrar(new ProcedimentoPressao(), 1300.0);     // 1300 kPa = 13 bar -> CRITICO

        System.out.println();
        System.out.println("=== Hook sobrescrito: registra alarme em leitura crítica ===");
        ProcedimentoTemperaturaComAlarme comAlarme = new ProcedimentoTemperaturaComAlarme();
        mostrar(comAlarme, 50.0);
        mostrar(comAlarme, 95.0);
        System.out.println("Alarmes registrados: " + comAlarme.getAlarmes());

        System.out.println();
        System.out.println("=== Validação (passo concreto, igual para todos) ===");
        try {
            new ProcedimentoPressao().executar(Double.NaN);
        } catch (IllegalArgumentException ex) {
            System.out.println("Recusado: " + ex.getMessage());
        }
    }

    private static void mostrar(ProcedimentoMedicao procedimento, double valorBruto) {
        System.out.println(procedimento.executar(valorBruto).mensagem());
    }
}
