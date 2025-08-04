package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class RicercaPerAutore implements CriterioDiFiltro {
    private final String autore;

    /**
     * Costruttore che accetta l'autore da utilizzare per la ricerca.
     * @param autore L'autore da utilizzare per la ricerca.
     */
    public RicercaPerAutore(String autore) {
        this.autore = autore.toLowerCase();
    }

    /**
     * Verifica se un libro corrisponde al criterio di ricerca.
     * @param libro Il libro da verificare.
     * @return true se il libro corrisponde al criterio, false altrimenti.
     */
    @Override
    public boolean isMatch(Libro libro) {
        // Controlla se l'autore del libro contiene il testo cercato (non case-sensitive)
        return libro.getAutore().toLowerCase().contains(this.autore.toLowerCase());
    }
}
