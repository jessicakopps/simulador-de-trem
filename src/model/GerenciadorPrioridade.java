package model;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorPrioridade {
    private final Map<Integer, Integer> ultimaPrioridadePorTrem = new HashMap<>();
    private int contadorGlobal = 0;

    public int getPrioridade(Trem trem) {
        int id = trem.getId();
        if (!ultimaPrioridadePorTrem.containsKey(id)) {
            int prioridade = (id % 2 == 1) ? contadorGlobal - 1000 : contadorGlobal + 1000;
            ultimaPrioridadePorTrem.put(id, prioridade);
        }
        return ultimaPrioridadePorTrem.get(id);
    }

    public void atualizarPrioridade(Trem trem) {
        ultimaPrioridadePorTrem.put(trem.getId(), contadorGlobal++);
    }
}