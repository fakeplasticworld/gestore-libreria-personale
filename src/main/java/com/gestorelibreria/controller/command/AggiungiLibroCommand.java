package com.gestorelibreria.controller.command;

import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.model.Libro;

/**
 * Comando per aggiungere un libro alla libreria.
 * Implementa il pattern Command per incapsulare l'azione di aggiunta di un libro.
 */
public class AggiungiLibroCommand implements Command {
    private final Libreria libreria;
    private final Libro libroDaAggiungere;

    /**
     * Costruttore per inizializzare il comando con la libreria e il libro da aggiungere.
     *
     * @param libreria La libreria in cui aggiungere il libro.
     * @param libro Il libro da aggiungere alla libreria.
     */
    public AggiungiLibroCommand(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libroDaAggiungere = libro;
    }
    /**
     * Esegue il comando di aggiunta del libro alla libreria.
     * Chiama il metodo `aggiungiLibro` della libreria per aggiungere il libro.
     */
    @Override
    public void execute() {
        libreria.aggiungiLibro(libroDaAggiungere);
    }
}