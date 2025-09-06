package com.gestorelibreria.model.strategy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.gestorelibreria.model.Libro;

class RicercaPerTitoloTest {

    @Test
    void isMatchDovrebbeRestituireTruePerCorrispondenza() {
        Libro libro = new Libro.Builder("Guerra e Pace", "Lev Tolstoj").build();
        RicercaPerTitolo criterio = new RicercaPerTitolo("guerra");

        assertTrue(criterio.isMatch(libro), "Dovrebbe trovare una corrispondenza parziale e case-insensitive.");
    }

    @Test
    void isMatchDovrebbeRestituireFalseSeNonCorrisponde() {
        Libro libro = new Libro.Builder("Guerra e Pace", "Lev Tolstoj").build();
        RicercaPerTitolo criterio = new RicercaPerTitolo("Promessi Sposi");
        
        assertFalse(criterio.isMatch(libro), "Non dovrebbe trovare una corrispondenza.");
    }
}