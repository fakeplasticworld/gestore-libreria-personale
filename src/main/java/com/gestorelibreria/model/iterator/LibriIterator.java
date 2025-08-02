package com.gestorelibreria.model.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.gestorelibreria.model.Libro;

/**
 * Un'implementazione concreta dell'interfaccia Iterator per una lista di Libri.
 */
public class LibriIterator implements Iterator<Libro> {

    private final List<Libro> libri;
    private int posizioneCorrente;

    /**
     * Costruttore che accetta la lista di libri su cui iterare.
     * @param libriDaIterare la lista di risultati.
     */
    public LibriIterator(List<Libro> libriDaIterare) {
        this.libri = libriDaIterare;
        this.posizioneCorrente = 0;
    }

    /**
     * Controlla se ci sono ancora elementi nella lista.
     * @return true se ci sono altri elementi, altrimenti false.
     */
    @Override
    public boolean hasNext() {
        return posizioneCorrente < libri.size();
    }

    /**
     * Restituisce l'elemento successivo nella lista.
     * @return il prossimo Libro.
     * @throws NoSuchElementException se non ci sono più elementi.
     */
    @Override
    public Libro next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // Restituisce l'elemento corrente e poi avanza l'indice
        return libri.get(posizioneCorrente++);
    }
}