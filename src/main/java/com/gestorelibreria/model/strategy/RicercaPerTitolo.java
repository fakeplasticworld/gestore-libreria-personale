package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class RicercaPerTitolo implements CriterioDiFiltro {
    private final String titolo;

    /**
     * Costruttore che accetta il titolo da utilizzare per la ricerca.
     * @param titolo Il titolo da utilizzare per la ricerca.
     */
    public RicercaPerTitolo(String titolo) {
        this.titolo = titolo.toLowerCase();
    }

    /**
     * Verifica se un libro corrisponde al criterio di ricerca.
     * @param libro Il libro da verificare.
     * @return true se il libro corrisponde al criterio, false altrimenti.
     */
    @Override
    public boolean isMatch(Libro libro) {
        // Controlla se il titolo del libro contiene il testo cercato (non case-sensitive)
        return libro.getTitolo().toLowerCase().contains(this.titolo);
    }
}