package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class OrdinaPerTitolo implements CriterioDiOrdinamento {

    /**
     * Metodo che confronta due libri in base al loro titolo.
     * @param libro1 Il primo libro da confrontare.
     * @param libro2 Il secondo libro da confrontare.
     * @return Un intero negativo, zero o positivo se il titolo del primo libro è
     *         rispettivamente minore, uguale o maggiore di quello del secondo libro.
     */
    @Override
    public int compare(Libro libro1, Libro libro2) {
        return libro1.getTitolo().compareToIgnoreCase(libro2.getTitolo());
    }
}
