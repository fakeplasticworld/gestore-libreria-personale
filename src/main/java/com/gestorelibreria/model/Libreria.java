package com.gestorelibreria.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gestorelibreria.model.iterator.Aggregate;
import com.gestorelibreria.model.iterator.LibriIterator;
import com.gestorelibreria.model.observer.Observer;
import com.gestorelibreria.model.observer.Subject;
import com.gestorelibreria.model.strategy.CriterioDiFiltro;
import com.gestorelibreria.model.strategy.CriterioDiOrdinamento;

public class Libreria implements Subject, Aggregate {

    /**
     * Singleton per la classe Libreria.
     * Questa istanza è utilizzata per garantire che ci sia una sola libreria
     * gestita in tutta l'applicazione.
     */
    private static Libreria instance;

    /**
     * Costruttore privato per impedire l'istanza diretta della libreria.
     * Utilizza il pattern Singleton per garantire un'unica istanza.
     */
    private Libreria() {
    }

    /**
     * Metodo per ottenere l'istanza singleton della libreria.
     * 
     * @return L'istanza della libreria.
     */
    public static Libreria getInstance() {
        if (instance == null) {
            instance = new Libreria();
        }
        return instance;
    }

    private final List<Libro> libri = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Aggiunge un libro alla collezione e notifica gli observer.
     * 
     * @param libro Il libro da aggiungere.
     */
    public void aggiungiLibro(Libro libro) {
        if (libro != null) {
            this.libri.add(libro);
            notifyObservers();
        }
    }

    /**
     * Rimuove un libro dalla collezione e notifica gli observer.
     * 
     * @param libro Il libro da rimuovere.
     */
    public void rimuoviLibro(Libro libro) {
        if (libro != null) {
            this.libri.remove(libro);
            notifyObservers();
        }
    }

    /**
     * Crea e restituisce un iteratore su tutti i libri della collezione, senza
     * filtri.
     * 
     * @return un iteratore sulla collezione completa.
     */
    @Override
    public Iterator<Libro> createIterator() {
        // Restituisce l'iteratore standard di Java su una copia della lista per
        // sicurezza.
        return new LibriIterator(new ArrayList<>(this.libri));
    }

    @Override
    public void registraObserver(Observer observer) {
        if (observer != null) {
            this.observers.add(observer);
        }
    }

    @Override
    public void rimuoviObserver(Observer observer) {
        if (observer != null) {
            this.observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : this.observers) {
            observer.update();
        }
    }

    /**
     * Filtra e ordina la collezione di libri in base alle strategie fornite
     * e restituisce un iteratore sui risultati.
     *
     * @param criterio    La strategia di filtro da applicare.
     * @param ordinamento La strategia di ordinamento da applicare.
     * @return Un iteratore sulla lista filtrata e ordinata.
     */
    public Iterator<Libro> filtraLibri(CriterioDiFiltro criterio, CriterioDiOrdinamento ordinamento) {
        List<Libro> risultati = new ArrayList<>();

        for (Libro libro : this.libri) {
            if (criterio.isMatch(libro)) {
                risultati.add(libro);
            }
        }
        if (ordinamento != null) {
            risultati.sort(ordinamento);
        }

        return new LibriIterator(risultati);
    }

    /**
     * Trova un libro per ISBN.
     * 
     * @param isbn L'ISBN del libro da cercare.
     * @return Il libro corrispondente all'ISBN, o null se non trovato.
     */
    public Libro trovaLibro(String isbn) {
        for (Libro libro : this.libri) {
            if (libro.getIsbn() != null && libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    /**
     * Sostituisce l'intera collezione di libri con una nuova lista
     * e notifica gli observer.
     * 
     * @param nuovaCollezione La nuova lista di libri.
     */
    public void sostituisciCollezione(List<Libro> nuovaCollezione) {
        this.libri.clear();
        if (nuovaCollezione != null) {
            this.libri.addAll(nuovaCollezione);
        }
        notifyObservers();
    }
}