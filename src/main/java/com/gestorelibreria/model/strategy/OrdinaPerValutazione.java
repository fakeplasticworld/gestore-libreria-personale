package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class OrdinaPerValutazione implements CriterioDiOrdinamento {
    /**
     * Confronta due libri in base alla loro valutazione.
     * Restituisce un numero negativo se il primo libro ha una valutazione inferiore,
     * zero se sono equivalenti, e un numero positivo se il primo libro ha una valutazione superiore.
     *
     * @param libro1 Il primo libro da confrontare.
     * @param libro2 Il secondo libro da confrontare.
     * @return Un intero che indica l'ordine dei libri in base alla loro valutazione.
     */
    @Override
    public int compare(Libro libro1, Libro libro2) {
        return Integer.compare(libro2.getValutazione(), libro1.getValutazione());
    }

}
