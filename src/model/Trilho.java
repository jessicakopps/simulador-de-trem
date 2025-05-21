package model;

import java.util.ArrayList;
import java.util.List;

public class Trilho {

    private NoTrilho inicio;
    private NoTrilho fim;
    private final int numEstacoes;
    private final List<Trem> trens = new ArrayList<>();
    private final GerenciadorPrioridade gerenciadorPrioridade = new GerenciadorPrioridade();

    public Trilho(int numEstacoes) {
        this.numEstacoes = numEstacoes;
        construirTrilho();
    }

    private void construirTrilho() {
        NoTrilho anterior = null;
        int totalNos = (numEstacoes - 1) * 20 + 1;

        for (int pos = 0; pos < totalNos; pos++) {
            ElementoTrilho elemento;

            if (pos % 20 == 0) {
                elemento = new Estacao(pos);
            } else if (pos % 20 == 1 || (pos % 20 == 19 && pos + 1 < totalNos)) {
                elemento = new Desvio(pos);
            } else {
                elemento = new Via(pos);
            }

            NoTrilho novoNo = new NoTrilho(elemento);

            if (inicio == null) {
                inicio = novoNo;
            } else {
                anterior.setProximo(novoNo);
                novoNo.setAnterior(anterior);
            }

            anterior = novoNo;
            fim = novoNo;
        }
    }

    public NoTrilho getNoNaPosicao(int posicao) {
        NoTrilho atual = inicio;
        while (atual != null) {
            if (atual.getElemento().getPosicao() == posicao) {
                return atual;
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public int getNumEstacoes() {
        return numEstacoes;
    }

    public List<Trem> getTrens() {
        return new ArrayList<>(trens);
    }

    public boolean tremExiste(int id) {
        return trens.stream().anyMatch(t -> t.getId() == id);
    }

    public boolean podePartir(int posicao) {
        return trens.stream().noneMatch(t -> t.getPosicao() == posicao);
    }

    public void addTrem(Trem trem) {
        if (!tremExiste(trem.getId())) {
            this.trens.add(trem);
        }
    }

    public void atualizar(int minutoAtual) {
        verificarColisoes();
        for (Trem trem : trens) {
            trem.atualizar(minutoAtual);
        }
    }

    private void verificarColisoes() {
        for (int i = 0; i < trens.size(); i++) {
            Trem trem1 = trens.get(i);

            // Se o trem1 já chegou ao destino, não verifica colisões para ele
            if (trem1.chegouAoDestinoFinal) {
                continue;
            }

            List<Integer> trajetoriaTrem1 = calcularTrajetoria(trem1, 19);

            for (int j = i + 1; j < trens.size(); j++) {
                Trem trem2 = trens.get(j);

                // Se o trem2 já chegou ao destino, pula
                if (trem2.chegouAoDestinoFinal) {
                    continue;
                }

                verificarColisaoEntreTrens(trem1, trem2, trajetoriaTrem1);
            }
        }
    }

    private void verificarColisaoEntreTrens(Trem trem1, Trem trem2, List<Integer> trajetoriaTrem1) {
        if (trajetoriaTrem1.contains(trem2.getPosicao())) {
            int estacaoId = trem1.getPosicao() / 20;
            int posDesvioAntes = estacaoId * 20 - 1;
            int posDesvioDepois = estacaoId * 20 + 1;

            Trem tremParaDesvio = determinarTremParaDesvio(trem1, trem2, posDesvioAntes, posDesvioDepois);

            if (tremParaDesvio != null) {
                int tempoEspera = calcularTempoEspera(tremParaDesvio == trem1 ? trem2 : trem1);
                tremParaDesvio.entrarDesvio(tempoEspera);
            }
        }
    }

    private Trem determinarTremParaDesvio(Trem trem1, Trem trem2, int posDesvioAntes, int posDesvioDepois) {
        boolean trem1PodeDesvioAntes = trem1.getPosicao() == posDesvioAntes;
        boolean trem1PodeDesvioDepois = trem1.getPosicao() == posDesvioDepois;
        boolean trem2PodeDesvioAntes = trem2.getPosicao() == posDesvioAntes;
        boolean trem2PodeDesvioDepois = trem2.getPosicao() == posDesvioDepois;

        if (trem1PodeDesvioAntes && !trem2PodeDesvioDepois) {
            return trem1;
        }
        if (trem2PodeDesvioAntes && !trem1PodeDesvioDepois) {
            return trem2;
        }
        if (trem1PodeDesvioDepois && !trem2PodeDesvioAntes) {
            return trem1;
        }
        if (trem2PodeDesvioDepois && !trem1PodeDesvioAntes) {
            return trem2;
        }

        if ((trem1PodeDesvioAntes || trem1PodeDesvioDepois)
                && (trem2PodeDesvioAntes || trem2PodeDesvioDepois)) {
            int prioridadeTrem1 = gerenciadorPrioridade.getPrioridade(trem1);
            int prioridadeTrem2 = gerenciadorPrioridade.getPrioridade(trem2);

            if (prioridadeTrem1 < prioridadeTrem2) {
                gerenciadorPrioridade.atualizarPrioridade(trem1);
                return trem2;
            } else {
                gerenciadorPrioridade.atualizarPrioridade(trem2);
                return trem1;
            }
        }

        return null;
    }

    private int calcularTempoEspera(Trem tremNaEstacao) {
        return tremNaEstacao.getTempoParado() + 1;
    }

    public List<Integer> calcularTrajetoria(Trem trem, int passos) {
        List<Integer> trajetoria = new ArrayList<>();

        // Se o trem já chegou ao destino, retorna lista vazia (não interfere em outros trens)
        if (trem.chegouAoDestinoFinal) {
            return trajetoria;
        }

        int incremento = trem.getSentido() ? 1 : -1;
        int pos = trem.getPosicao();

        for (int i = 0; i < passos; i++) {
            pos += incremento;

            if (pos < 0 || pos >= numEstacoes * 20) {
                incremento *= -1;
                pos += 2 * incremento;
            }

            trajetoria.add(pos);
        }
        return trajetoria;
    }
}