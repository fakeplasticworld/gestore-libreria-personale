package com.gestorelibreria.controller.command;

import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.model.Libro;

public class ModificaLibroCommand implements Command {
    private final Libreria libreria;
    private final Libro libroDaModificare;
    private final Libro nuovoLibro;

    /**
     * Costruttore per inizializzare il comando con la libreria, il libro da modificare e i nuovi dati del libro.
     *
     * @param libreria La libreria in cui si trova il libro da modificare.
     * @param libroDaModificare Il libro da modificare.
     * @param nuovoLibroDTO I nuovi dati del libro.
     */
    public ModificaLibroCommand(Libreria libreria, Libro libroDaModificare, Libro nuovoLibro) {
        this.libreria = libreria;
        this.libroDaModificare = libroDaModificare;
        this.nuovoLibro = nuovoLibro;
    }

    /**
     * Esegue il comando di modifica del libro nella libreria.
     * Chiama il metodo `modificaLibro` della libreria per aggiornare i dati del libro.
     */
    @Override
    public void execute() {
        libreria.rimuoviLibro(libroDaModificare);
        libreria.aggiungiLibro(nuovoLibro);
    }
    
}
