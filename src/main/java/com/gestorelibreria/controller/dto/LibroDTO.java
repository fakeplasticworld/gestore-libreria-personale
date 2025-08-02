package com.gestorelibreria.controller.dto;

import com.gestorelibreria.model.StatoLettura;

/**
 * Data Transfer Object (DTO) per rappresentare un libro.
 * Utilizzato per trasferire i dati del libro tra il controller e la vista.
 */
public record LibroDTO(
    String titolo,
    String autore,
    String isbn,
    String genere,
    int valutazione,
    StatoLettura statoLettura
) {}