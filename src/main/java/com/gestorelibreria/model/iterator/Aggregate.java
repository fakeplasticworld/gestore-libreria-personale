package com.gestorelibreria.model.iterator;

import java.util.Iterator;

import com.gestorelibreria.model.Libro;

public interface Aggregate {
    /**
     * Crea un iteratore sui libri della collezione.
     * @return Un iteratore sui libri.
     */
    Iterator<Libro> createIterator();
}
