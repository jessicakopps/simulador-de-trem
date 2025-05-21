package model;

import java.util.HashMap;
import java.util.Map;

public class Estatisticas {

    private int totalPassageirosEmbarcados = 0;
    private int totalPassageirosDesembarcados = 0;
    private int totalColisoesEvitadas = 0;
    private int totalTempoDesvio = 0;
    private final Map<Integer, Integer> embarquesPorEstacao;
    private final Map<Integer, Integer> desembarquesPorEstacao;
    private final int numEstacaoInicialImpares;
    private final int numEstacaoInicialPares;

    public Estatisticas(int numEstacoes) {
        this.embarquesPorEstacao = new HashMap<>();
        this.desembarquesPorEstacao = new HashMap<>();
        for (int i = 0; i < numEstacoes; i++) {
            embarquesPorEstacao.put(i, 0);
            desembarquesPorEstacao.put(i, 0);
        }
        this.numEstacaoInicialImpares = 1;
        this.numEstacaoInicialPares = numEstacoes;
    }

    public void registrarEmbarque(int estacao, int quantidade) {
        if (quantidade > 0) {
            totalPassageirosEmbarcados += quantidade;
            embarquesPorEstacao.merge(estacao, quantidade, Integer::sum);
        }
    }

    public void registrarDesembarque(int estacao, int quantidade) {
        if (quantidade > 0) {
            totalPassageirosDesembarcados += quantidade;
            desembarquesPorEstacao.merge(estacao, quantidade, Integer::sum);
        }
    }

    public void registrarColisaoEvitada() {
        totalColisoesEvitadas++;
    }

    public void registrarTempoDesvio(int minutos) {
        totalTempoDesvio += minutos;
    }

    public Map<Integer, Integer> getEmbarquesPorEstacao() {
        return embarquesPorEstacao;
    }

    public Map<Integer, Integer> getDesembarquesPorEstacao() {
        return desembarquesPorEstacao;
    }

    public int getNumEstacaoInicialImpares() {
        return numEstacaoInicialImpares;
    }

    public int getNumEstacaoInicialPares() {
        return numEstacaoInicialPares;
    }

    public int getTotalColisoesEvitadas() {
        return totalColisoesEvitadas;
    }

    public int getTotalTempoDesvio() {
        return totalTempoDesvio;
    }

    public int getTotalPassageirosEmbarcados() {
        return totalPassageirosEmbarcados;
    }

    public int getTotalPassageirosDesembarcados() {
        return totalPassageirosDesembarcados;
    }

    public void exibirEstatisticas() {
        System.out.println("\n=== ESTATISTICAS DA SIMULACAO ===");
        System.out.println("Configuracao inicial:");
        System.out.println(" - Trens impares partem da estacao: " + numEstacaoInicialImpares);
        System.out.println(" - Trens pares partem da estacao: " + numEstacaoInicialPares);
        System.out.println("Colisoes evitadas: " + totalColisoesEvitadas);
        System.out.println("Tempo total em desvios: " + totalTempoDesvio + " minutos");

        System.out.println("\nPassageiros por estacao:");
        for (int i = 0; i < embarquesPorEstacao.size(); i++) {
            System.out.printf("Estacao %d - Total embarcados: %d; Total desembarcados: %d%n",
                    i + 1,
                    embarquesPorEstacao.get(i),
                    desembarquesPorEstacao.get(i));
        }

        System.out.println("\nTotais gerais:");
        System.out.println("Total de passageiros embarcados: " + totalPassageirosEmbarcados);
        System.out.println("Total de passageiros desembarcados: " + totalPassageirosDesembarcados);
    }
}