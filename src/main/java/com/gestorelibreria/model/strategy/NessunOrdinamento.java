package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

/**
 * Implementazione di CriterioDiOrdinamento che non applica alcun ordinamento.
 * Utilizzata quando non si desidera ordinare i libri.
 */
public class NessunOrdinamento implements CriterioDiOrdinamento {
    /**
     * Metodo che restituisce 0 per indicare che non c'è alcun ordinamento.
     * @param libro1 Il primo libro da confrontare.
     * @param libro2 Il secondo libro da confrontare.
     * @return 0, poiché non si applica alcun ordinamento.
     */
    @Override
    public int compare(Libro libro1, Libro libro2) {
        return 0;
    }
}