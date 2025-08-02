package com.gestorelibreria.model.observer;

public interface Subject {
    /**
     * Registra un nuovo observer.
     * @param observer l'observer da aggiungere.
     */
    void registraObserver(Observer observer);

    /**
     * Rimuove un observer registrato.
     * @param observer l'observer da rimuovere.
     */
    void rimuoviObserver(Observer observer);

    /**
     * Notifica tutti gli observer registrati di un cambiamento di stato.
     */
    void notifyObservers();
}