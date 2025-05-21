package model;

public class Estacao extends ElementoTrilho {
    public Estacao(int posicao) {
        super(posicao);
    }

    @Override
    public void processarTrem(Trem trem) {
        trem.processarEstacao();
    }
}