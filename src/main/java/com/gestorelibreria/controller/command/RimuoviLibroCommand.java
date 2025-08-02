package com.gestorelibreria.controller.command;

import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.model.Libro;

public class RimuoviLibroCommand implements Command {
    private final Libreria libreria;
    private final Libro libroDaRimuovere;

    /**
     * Costruttore per inizializzare il comando con la libreria e il libro da rimuovere.
     *
     * @param libreria La libreria da cui rimuovere il libro.
     * @param libro Il libro da rimuovere dalla libreria.
     */
    public RimuoviLibroCommand(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libroDaRimuovere = libro;
    }

    /**
     * Esegue il comando di rimozione del libro dalla libreria.
     * Chiama il metodo `rimuoviLibro` della libreria per rimuovere il libro.
     */
    @Override
    public void execute() {
        libreria.rimuoviLibro(libroDaRimuovere);
    }
    
}
