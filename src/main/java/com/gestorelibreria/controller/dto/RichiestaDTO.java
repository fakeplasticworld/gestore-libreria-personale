package com.gestorelibreria.controller.dto;

/**
 * Data Transfer Object (DTO) per rappresentare una richiesta di filtro e ordinamento.
 * Utilizzato per trasferire i criteri di filtro e ordinamento tra il controller e la vista.
 */
public record RichiestaDTO(
    String tipoFiltro,
    String valoreFiltro,
    String tipoOrdinamento
) {}