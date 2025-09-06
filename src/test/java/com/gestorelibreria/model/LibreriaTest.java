package com.gestorelibreria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LibreriaTest {

    private Libreria libreria;
    private Libro libro1;
    private Libro libro2;

    @BeforeEach 
    @SuppressWarnings("unused")
    void setUp() {
        libreria = Libreria.INSTANCE;
        libreria.sostituisciCollezione(null);
        libro1 = new Libro.Builder("Il Signore degli Anelli", "J.R.R. Tolkien")
                .conIsbn("12345")
                .build();
        libro2 = new Libro.Builder("Dune", "Frank Herbert")
                .conIsbn("67890")
                .build();
    }

    @Test
    @DisplayName("Dovrebbe aggiungere un libro correttamente")
    void testAggiungiLibro() {
        libreria.aggiungiLibro(libro1);
        Libro libroTrovato = libreria.trovaLibro("12345");

        assertNotNull(libroTrovato, "Il libro dovrebbe essere trovato nella libreria.");
        assertEquals("Il Signore degli Anelli", libroTrovato.getTitolo(), "Il titolo del libro non corrisponde.");
    }

    @Test
    @DisplayName("Dovrebbe rimuovere un libro correttamente")
    void testRimuoviLibro() {
        libreria.aggiungiLibro(libro1);
        libreria.aggiungiLibro(libro2);
        libreria.rimuoviLibro(libro1);

        assertNull(libreria.trovaLibro("12345"), "Il libro 1 dovrebbe essere stato rimosso.");
        assertNotNull(libreria.trovaLibro("67890"), "Il libro 2 dovrebbe essere ancora presente.");
    }

    @Test
    @DisplayName("Dovrebbe trovare un libro tramite ISBN")
    void testTrovaLibro() {
        libreria.aggiungiLibro(libro1);
        Libro libroTrovato = libreria.trovaLibro("12345");
        Libro libroNonTrovato = libreria.trovaLibro("99999");
        
        assertNotNull(libroTrovato);
        assertEquals(libro1, libroTrovato);
        assertNull(libroNonTrovato);
    }
}