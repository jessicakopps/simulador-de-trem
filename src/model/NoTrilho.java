package model;

class NoTrilho {

    private ElementoTrilho elemento;
    private NoTrilho anterior;
    private NoTrilho proximo;

    public NoTrilho(ElementoTrilho elemento) {
        this.elemento = elemento;
    }

    // Getters e Setters
    public ElementoTrilho getElemento() {
        return elemento;
    }

    public NoTrilho getAnterior() {
        return anterior;
    }

    public NoTrilho getProximo() {
        return proximo;
    }

    public void setAnterior(NoTrilho anterior) {
        this.anterior = anterior;
    }

    public void setProximo(NoTrilho proximo) {
        this.proximo = proximo;
    }
}