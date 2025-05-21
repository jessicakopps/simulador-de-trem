package view;

import model.Trem;
import java.util.List;
import java.util.Arrays;

public class ConsoleView {

    public void exibirCabecalho(int numEstacoes) {
        System.out.println("=== SIMULACAO DE TRENS ===");
        System.out.println("Total de estacoes: " + numEstacoes);
        System.out.println("Pressione ENTER para avancar 1 minuto");
        System.out.println("Digite 'Q' + ENTER para sair\n");
    }

    public void exibirEstado(List<Trem> trens) {
        System.out.println("ID | Pos (km) | Estacao | Status   | Passageiros | Entraram | Sairam | Tempo/Acao");
        System.out.println("--------------------------------------------------------------------------------");
        for (Trem trem : trens) {
            System.out.printf("%2d | %7d | %7d | %-8s | %11d | %8s | %7s | %s%n",
                    trem.getId(),
                    trem.getPosicao(),
                    trem.getEstacaoAtual(),
                    trem.getStatus(),
                    trem.getPassageiros(),
                    (trem.getStatus().equals("VIA") || trem.getStatus().equals("AGUARD.") || trem.getStatus().equals("DESVIO")) ? "-" : trem.getUltimosQueEntraram(),
                    (trem.getStatus().equals("VIA") || trem.getStatus().equals("AGUARD.") || trem.getStatus().equals("DESVIO")) ? "-" : trem.getUltimosQueSairam(), trem.getTempoParadoFormatado());
        }
    }

    public void desenharMapa(List<Trem> trens, int ultimaEstacao) {
        System.out.println("\nMAPA (E: Estacao, D: Desvio, 1-9: Trens, A-Z: Trens 10+)");
        int maxPos = ultimaEstacao * 20;
        char[] mapa = new char[maxPos + 1];
        Arrays.fill(mapa, '-');

        for (int i = 0; i <= ultimaEstacao; i++) {
            int posEstacao = i * 20;
            if (posEstacao <= maxPos) {
                mapa[posEstacao] = 'E';
                if (posEstacao > 0) {
                    mapa[posEstacao - 1] = 'D';
                }
                if (posEstacao < maxPos) {
                    mapa[posEstacao + 1] = 'D';
                }
            }
        }

        for (Trem trem : trens) {
            int posTrem = trem.getPosicao();
            if (posTrem >= 0 && posTrem <= maxPos) {
                char simbolo = (trem.getId() <= 9)
                        ? (char) ('0' + trem.getId())
                        : (char) ('A' + (trem.getId() - 10));
                mapa[posTrem] = simbolo;
            }
        }
        System.out.println(new String(mapa));
    }

    public String formatarHorario(int minutos) {
        return String.format("%02d:%02d", 8 + (minutos / 60), minutos % 60);
    }
}
