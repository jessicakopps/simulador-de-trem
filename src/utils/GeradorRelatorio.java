package utils;

import java.io.FileWriter;
import java.io.IOException;
import model.Estatisticas;

public class GeradorRelatorio {

    public void gerar(Estatisticas estatisticas) {
        try (FileWriter writer = new FileWriter("relatorio_final.txt")) {
            writer.write("=== RELATORIO FINAL ===\n");
            writer.write("Configuração inicial:\n");
            writer.write(" - Trens ímpares partem da estação: " + estatisticas.getNumEstacaoInicialImpares() + "\n");
            writer.write(" - Trens pares partem da estação: " + estatisticas.getNumEstacaoInicialPares() + "\n");
            writer.write("Colisões evitadas: " + estatisticas.getTotalColisoesEvitadas() + "\n");
            writer.write("Tempo total em desvios: " + estatisticas.getTotalTempoDesvio() + " minutos\n\n");

            writer.write("Estacao | Embarques | Desembarques\n");
            writer.write("----------------------------------\n");

            for (int i = 0; i < estatisticas.getEmbarquesPorEstacao().size(); i++) {
                writer.write(String.format("%7d | %9d | %12d\n",
                        i + 1,
                        estatisticas.getEmbarquesPorEstacao().get(i),
                        estatisticas.getDesembarquesPorEstacao().get(i)));
            }

            writer.write("\nTotais gerais:\n");
            writer.write("Total de passageiros embarcados: " + estatisticas.getTotalPassageirosEmbarcados() + "\n");
            writer.write("Total de passageiros desembarcados: " + estatisticas.getTotalPassageirosDesembarcados() + "\n");
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatorio: " + e.getMessage());
        }
    }
}