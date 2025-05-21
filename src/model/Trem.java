package model;

import java.util.Random;

public class Trem {

    private final int id;
    private final boolean sentidoHorario;
    private int posicao;
    private int passageiros;
    private int tempoParado = 0;
    private boolean noDesvio = false;
    private boolean aguardando = false;
    private int tempoAguardando = 0;
    private final Trilho trilho;
    private int ultimosQueEntraram = 0;
    private int ultimosQueSairam = 0;
    private int tempoParadoSegundos = 0;
    private final Estatisticas estatisticas;
    public boolean chegouAoDestinoFinal = false;

    public boolean chegouAoDestinoFinal() {
    return chegouAoDestinoFinal;
}

    public Trem(int id, boolean sentidoHorario, int posicaoInicial, Trilho trilho, Estatisticas estatisticas) {
        this.id = id;
        this.sentidoHorario = sentidoHorario;
        this.posicao = posicaoInicial;
        this.trilho = trilho;
        this.estatisticas = estatisticas;
        this.passageiros = new Random().nextInt(41) + 10;

        int estacaoAtual = getEstacaoAtual() - 1;
        if ((sentidoHorario && estacaoAtual == 0) || (!sentidoHorario && estacaoAtual == trilho.getNumEstacoes() - 1)) {
            estatisticas.registrarEmbarque(estacaoAtual, passageiros);
        }

        this.tempoParado = 1;
    }

    public int getId() {
        return id;
    }

    public int getPosicao() {
        return posicao;
    }

    public int getEstacaoAtual() {
        return (posicao / 20) + 1;
    }

    public int getPassageiros() {
        return passageiros;
    }

    public int getUltimosQueEntraram() {
        return ultimosQueEntraram;
    }

    public int getUltimosQueSairam() {
        return ultimosQueSairam;
    }

    public boolean getSentido() {
        return sentidoHorario;
    }

    public boolean isEmVia() {
        return tempoParado == 0 && !aguardando && !noDesvio;
    }

    public boolean estaNoDesvio() {
        return noDesvio;
    }

    public boolean estaNaAreaDeDesvio() {
        return Math.abs(posicao % 20) == 1;
    }

    public boolean estaNaEstacao() {
        return posicao % 20 == 0;
    }

    public int getTempoParado() {
        return tempoParado;
    }

    public String getStatus() {
    if (chegouAoDestinoFinal) {
        return "ESTACAO"; // Sempre mostra como estação se chegou ao destino
    }
    if (noDesvio) {
        return "DESVIO";
    }
    if (tempoParado > 0) {
        return "ESTACAO";
    }
    if (aguardando) {
        return "AGUARD.";
    }
    return "VIA";
}

    public String getTempoParadoFormatado() {
        if (tempoParado > 0 || tempoParadoSegundos > 0) {
            return String.format("%dmin %02ds", tempoParado, tempoParadoSegundos);
        }
        if (aguardando) {
            return "esperando";
        }
        return "-";
    }

    public void entrarDesvio(int minutos) {
        if (!noDesvio) {
            noDesvio = true;
            aguardando = true;
            tempoAguardando = minutos;
            estatisticas.registrarColisaoEvitada();
            estatisticas.registrarTempoDesvio(minutos);
        }
    }

    public void atualizar(int minutoAtual) {
        if (tempoParado > 0) {
            atualizarTempoParado();
            return;
        }

        if (aguardando) {
            atualizarTempoAguardando();
            return;
        }

        moverTrem();
    }

    private void atualizarTempoParado() {
        tempoParadoSegundos -= 60;
        if (tempoParadoSegundos <= 0) {
            tempoParado--;
            tempoParadoSegundos = 0;
        }

        if (tempoParado == 0 && noDesvio) {
            noDesvio = false;
        }
    }

    public void atualizarTempoAguardando() {
        tempoAguardando--;
        if (tempoAguardando <= 0) {
            aguardando = false;
            noDesvio = false;
        }
    }

    private void moverTrem() {
        int delta = sentidoHorario ? 1 : -1;
        int novaPosicao = posicao + delta;

        int maxPos = (trilho.getNumEstacoes() - 1) * 20;
        if (novaPosicao < 0 || novaPosicao > maxPos) {
            return;
        }

        if (novaPosicao % 20 == 0) {
            int estacaoId = novaPosicao / 20;
            if (estacaoId >= 0 && estacaoId < trilho.getNumEstacoes()) {
                posicao = novaPosicao;
                processarEstacao();
                return;
            }
        }

        posicao = novaPosicao;
    }

    public void processarEstacao() {
    int estacaoAtual = getEstacaoAtual() - 1;
    int ultimaEstacao = trilho.getNumEstacoes() - 1;

    if ((sentidoHorario && estacaoAtual == ultimaEstacao) || 
        (!sentidoHorario && estacaoAtual == 0)) {
        // Trem chegou ao destino final
        ultimosQueSairam = passageiros;
        passageiros = 0;
        chegouAoDestinoFinal = true;  // Aqui é definido
        estatisticas.registrarDesembarque(estacaoAtual, ultimosQueSairam);
    } else {
        processarPassageiros(estacaoAtual);
    }
    calcularTempoParado();
}

    private void processarPassageiros(int estacaoAtual) {
        Random rand = new Random();

        // Desembarque (máx 10)
    ultimosQueSairam = Math.min(passageiros, rand.nextInt(11)); // 0-10

    // Embarque (máx 10 e não ultrapassar 50)
    int capacidadeRestante = 50 - passageiros;
    ultimosQueEntraram = Math.min(capacidadeRestante, rand.nextInt(11)); // 0-10

        // Garante que a soma seja par
        if ((ultimosQueSairam + ultimosQueEntraram) % 2 != 0) {
            if (ultimosQueEntraram < 10 && capacidadeRestante > ultimosQueEntraram) {
                ultimosQueEntraram++;
            } else if (ultimosQueSairam > 0) {
                ultimosQueSairam--;
            }
        }

        // Atualiza o número de passageiros
        passageiros -= ultimosQueSairam;
        passageiros += ultimosQueEntraram;

        // Garante mínimo de 10 passageiros
        if (passageiros < 10) {
            int adicional = 10 - passageiros;
            ultimosQueEntraram += adicional;
            passageiros = 10;
        }

        // Registra estatísticas
        estatisticas.registrarDesembarque(estacaoAtual, ultimosQueSairam);
        estatisticas.registrarEmbarque(estacaoAtual, ultimosQueEntraram);
    }

    private void calcularTempoParado() {
        int totalPassageiros = ultimosQueSairam + ultimosQueEntraram;
        tempoParadoSegundos = totalPassageiros == 0 ? 60 : totalPassageiros * 30;
        tempoParado = tempoParadoSegundos / 60;
        tempoParadoSegundos = tempoParadoSegundos % 60;
    }
}