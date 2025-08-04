package com.gestorelibreria.model;

/**
 * Enum che rappresenta i possibili stati di lettura di un libro.
 */
public enum StatoLettura {
    DA_LEGGERE("Da leggere", "/com/gestorelibreria/icons/da_leggere.png"),
    IN_LETTURA("In lettura", "/com/gestorelibreria/icons/in_lettura.png"),
    LETTO("Letto", "/com/gestorelibreria/icons/letto.png");

    private final String descrizione;
    private final String iconPath;

    StatoLettura(String descrizione, String iconPath) {
        this.descrizione = descrizione;
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}