package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

/**
 * Implementazione Null Object che accetta qualsiasi libro.
 */
public class FiltroUniversale implements CriterioDiFiltro {
    /**
     * Metodo che verifica se il libro corrisponde al criterio di filtro.
     * In questo caso, accetta qualsiasi libro.
     *
     * @param libro Il libro da verificare.
     * @return Sempre true, poiché accetta qualsiasi libro.
     */
    @Override
    public boolean isMatch(Libro libro) {
        return true;
    }
}