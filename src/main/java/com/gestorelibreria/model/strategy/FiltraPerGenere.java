package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class FiltraPerGenere implements CriterioDiFiltro {
    private final String genere;

    /**
     * Costruttore che accetta il genere da utilizzare per il filtro.
     * @param genere Il genere da utilizzare per il filtro.
     */
    public FiltraPerGenere(String genere) {
        this.genere = genere;
    }

    /**
     * Verifica se un libro corrisponde al criterio di filtro.
     * @param libro Il libro da verificare.
     * @return true se il libro corrisponde al criterio, false altrimenti.
     */
    @Override
    public boolean isMatch(Libro libro) {
        return libro.getGenere().equalsIgnoreCase(this.genere);
    }
}