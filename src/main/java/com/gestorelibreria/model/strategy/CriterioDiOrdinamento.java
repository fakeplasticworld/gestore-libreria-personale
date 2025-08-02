package com.gestorelibreria.model.strategy;

import java.util.Comparator;

import com.gestorelibreria.model.Libro;

/**
 * Interfaccia Strategy per definire un algoritmo di ordinamento per oggetti Libro.
 * Estende l'interfaccia standard java.util.Comparator per integrarsi
 * nativamente con i metodi di ordinamento delle collezioni Java.
 */
public interface CriterioDiOrdinamento extends Comparator<Libro> {
    /**
     * Metodo per confrontare due libri e determinare il loro ordine.
     * @param libro1 Il primo libro da confrontare.
     * @param libro2 Il secondo libro da confrontare.
     * @return Un valore negativo se libro1 precede libro2, zero se sono uguali,
     *         un valore positivo se libro1 segue libro2.
     */
    @Override
    int compare(Libro libro1, Libro libro2);
}
