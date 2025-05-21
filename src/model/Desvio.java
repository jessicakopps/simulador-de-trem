package model;

public class Desvio extends ElementoTrilho {
    public Desvio(int posicao) {
        super(posicao);
    }

    @Override
    public void processarTrem(Trem trem) {
        if (trem.estaNoDesvio()) {
            trem.atualizarTempoAguardando();
        }
    }
}