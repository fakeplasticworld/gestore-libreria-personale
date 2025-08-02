package com.gestorelibreria.controller.command;
/**
 * Interfaccia Command per il pattern Command.
 * Definisce un metodo per eseguire un comando.
 * Le classi che implementano questa interfaccia rappresentano comandi specifici
 * che possono essere eseguiti, come l'aggiunta o la rimozione di libri.
 */
public interface Command {
    /**
     * Esegue il comando.
     * Questo metodo deve essere implementato da tutte le classi che
     * rappresentano un comando specifico.
     */
    void execute();
}
