package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;
import com.gestorelibreria.model.StatoLettura;

public class FiltraPerStatoLettura implements CriterioDiFiltro {
    private final StatoLettura statoLettura;

    /**
     * Costruttore che accetta lo stato di lettura da utilizzare per il filtro.
     * 
     * @param statoLettura Lo stato di lettura da utilizzare per il filtro.
     */
    public FiltraPerStatoLettura(String statoLettura) {
        this.statoLettura = StatoLettura.valueOf(statoLettura.toUpperCase());
    }

    /**
     * Verifica se un libro corrisponde al criterio di filtro.
     * 
     * @param libro Il libro da verificare.
     * @return true se il libro corrisponde al criterio, false altrimenti.
     */
    @Override
    public boolean isMatch(Libro libro) {
        return libro.getStatoLettura() == this.statoLettura;
    }
}