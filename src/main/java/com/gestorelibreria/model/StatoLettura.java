package com.gestorelibreria.model;

/**
 * Enum che rappresenta i possibili stati di lettura di un libro.
 */
public enum StatoLettura {
    DA_LEGGERE("Da leggere"),
    IN_LETTURA("In lettura"),
    LETTO("Letto");

    private final String descrizione;

    StatoLettura(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
