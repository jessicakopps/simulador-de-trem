package model;

public abstract class ElementoTrilho {
    protected int posicao;

    public ElementoTrilho(int posicao) {
        this.posicao = posicao;
    }

    public abstract void processarTrem(Trem trem);

    public int getPosicao() {
        return posicao;
    }
}