package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public interface CriterioDiFiltro {
    /**
     * Metodo per verificare se un libro soddisfa il criterio di filtro specificato.
     * @param libro Il libro da verificare.
     * @return true se il libro soddisfa il criterio, false altrimenti.
     */
    boolean isMatch(Libro libro);
    
}
