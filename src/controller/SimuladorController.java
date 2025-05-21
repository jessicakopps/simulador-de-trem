package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import model.Estatisticas;
import model.Trem;
import model.Trilho;
import utils.GeradorRelatorio;

public class SimuladorController {

    private final Trilho trilho;
    private int minutos = 0;
    private final int duracaoDiaMinutos = 570;
    private FileWriter logWriter;
    private final Estatisticas estatisticas;
    private final int ultimaEstacao;

    public SimuladorController(int numEstacoes) {
        this.trilho = new Trilho(numEstacoes);
        this.ultimaEstacao = numEstacoes - 1;
        this.estatisticas = new Estatisticas(numEstacoes);
        inicializarLog();
    }

    private void inicializarLog() {
        try {
            this.logWriter = new FileWriter("log_simulacao.txt");
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    public void iniciar() {
        exibirCabecalho();
        Scanner scanner = new Scanner(System.in);

        try {
            executarSimulacao(scanner);
        } finally {
            finalizarSimulacao(scanner);
        }
    }

    private void executarSimulacao(Scanner scanner) {
        while (true) {
            System.out.print("> ");  // Adicionei esta linha para mostrar onde digitar
            // Processa entrada do usuário (incluindo 'q' para sair)
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    break;
                }
            }

            // Processa o minuto atual
            System.out.println("\n--- Minuto: " + formatarHorario() + " ---");
            if (minutos <= duracaoDiaMinutos) {
                criarTrensSeNecessario();
            }
            trilho.atualizar(minutos);
            exibirEstadoAtual();
            registrarLog();

            // Verifica se todos os trens chegaram ao destino
            boolean todosChegaram = true;
            for (Trem trem : trilho.getTrens()) {
                int estacaoAtual = trem.getEstacaoAtual() - 1;
                int ultimaEstacao = trilho.getNumEstacoes() - 1;

                if ((trem.getId() % 2 == 1 && estacaoAtual != ultimaEstacao)
                        || (trem.getId() % 2 == 0 && estacaoAtual != 0)) {
                    todosChegaram = false;
                    break;
                }
            }

            // Condições de término:
            // 1. Passou das 17:30 E todos os trens chegaram
            // 2. Ou se o usuário digitou 'q'
            if (minutos > duracaoDiaMinutos && todosChegaram) {
                break;
            }

            minutos++;
        }
        estatisticas.exibirEstatisticas();
        new GeradorRelatorio().gerar(estatisticas);
    }

    private void processarEntradaUsuario(Scanner scanner) {
        System.out.print("> ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("q")) {
            minutos = duracaoDiaMinutos + 1;
        }
    }

    private void processarMinuto() {
        System.out.println("\n--- Minuto: " + formatarHorario() + " ---");
        criarTrensSeNecessario();
        trilho.atualizar(minutos);
        registrarLog();
    }

    private void criarTrensSeNecessario() {
        if (minutos == 0) {
            criarTremHorario(1);
        } else if (minutos % 30 == 0 && minutos <= 570) {
            int numTrem = (minutos / 30) + 1;

            if (!trilho.tremExiste(numTrem)) {
                if (numTrem % 2 == 1) {
                    criarTremHorario(numTrem);
                } else {
                    criarTremAntiHorario(numTrem);
                }
            }
        }
    }

    private void criarTremHorario(int numTrem) {
        Trem novoTrem = new Trem(numTrem, true, 0, trilho, estatisticas);
        if (trilho.podePartir(0)) {
            trilho.addTrem(novoTrem);
            System.out.println("Trem " + numTrem + " partiu da estacao 1 (sentido horario) com " + novoTrem.getPassageiros() + " passageiros");
        } else {
            System.out.println("Trem " + numTrem + " aguardando para partir da estacao 1");
        }
    }

    private void criarTremAntiHorario(int numTrem) {
        Trem novoTrem = new Trem(numTrem, false, ultimaEstacao * 20, trilho, estatisticas);
        if (trilho.podePartir(ultimaEstacao * 20)) {
            trilho.addTrem(novoTrem);
            System.out.println("Trem " + numTrem + " partiu da estacao " + (ultimaEstacao + 1)
                    + " (sentido anti-horario) com " + novoTrem.getPassageiros() + " passageiros");
        } else {
            System.out.println("Trem " + numTrem + " aguardando para partir da estacao " + (ultimaEstacao + 1));
        }
    }

    private void exibirCabecalho() {
        System.out.println("=== SIMULACAO DE TRENS ===");
        System.out.println("Total de estacoes: " + (ultimaEstacao + 1));
        System.out.println("Pressione ENTER para avancar 1 minuto");
        System.out.println("Digite 'Q' + ENTER para sair\n");
        exibirEstado();
        desenharMapa();
    }

    private void exibirEstadoAtual() {
        exibirEstado();
        desenharMapa();
    }

    private void exibirEstado() {
        System.out.println("ID | Pos (km) | Estacao | Status   | Passageiros | Entraram | Sairam | Tempo/Acao");
        System.out.println("--------------------------------------------------------------------------------");
        for (Trem trem : trilho.getTrens()) {
            exibirStatusTrem(trem);
        }
    }

    private void exibirStatusTrem(Trem trem) {
        System.out.printf("%2d | %7d | %7d | %-8s | %11d | %8s | %7s | %s%n",
                trem.getId(),
                trem.getPosicao(),
                trem.getEstacaoAtual(),
                trem.getStatus(),
                trem.getPassageiros(),
                (trem.getStatus().equals("VIA") || trem.getStatus().equals("AGUARD.") || trem.getStatus().equals("DESVIO")) ? "-" : trem.getUltimosQueEntraram(),
                (trem.getStatus().equals("VIA") || trem.getStatus().equals("AGUARD.") || trem.getStatus().equals("DESVIO")) ? "-" : trem.getUltimosQueSairam(),
                trem.getTempoParadoFormatado());
    }

    private void desenharMapa() {
        System.out.println("\nMAPA (E: Estacao, D: Desvio, 1-9: Trens, A-Z: Trens 10+)");
        int maxPos = ultimaEstacao * 20;
        char[] mapa = new char[maxPos + 1];
        java.util.Arrays.fill(mapa, '-');

        desenharEstacoesEDesvios(mapa, maxPos);
        desenharTrens(mapa, maxPos);

        System.out.println(new String(mapa));
    }

    private void desenharEstacoesEDesvios(char[] mapa, int maxPos) {
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
    }

    private void desenharTrens(char[] mapa, int maxPos) {
        for (Trem trem : trilho.getTrens()) {
            int posTrem = trem.getPosicao();
            if (posTrem >= 0 && posTrem <= maxPos) {
                char simbolo = (trem.getId() <= 9)
                        ? (char) ('0' + trem.getId())
                        : (char) ('A' + (trem.getId() - 10));
                mapa[posTrem] = simbolo;
            }
        }
    }

    private String formatarHorario() {
        return String.format("%02d:%02d", 8 + (minutos / 60), minutos % 60);
    }

    private void registrarLog() {
        try {
            if (logWriter != null) {
                logWriter.write("--- Minuto: " + formatarHorario() + " ---\n");
                for (Trem trem : trilho.getTrens()) {
                    logWriter.write(String.format(
                            "Trem %d - Pos: %dkm, Estacao: %d, Passageiros: %d, Entraram: %d, Sairam: %d, Status: %s\n",
                            trem.getId(), trem.getPosicao(), trem.getEstacaoAtual(), trem.getPassageiros(),
                            trem.getUltimosQueEntraram(), trem.getUltimosQueSairam(), trem.getStatus()));
                }
                logWriter.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log: " + e.getMessage());
        }
    }

    private void finalizarSimulacao(Scanner scanner) {
        scanner.close();
        try {
            if (logWriter != null) {
                logWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar arquivo de log: " + e.getMessage());
        }
    }
}