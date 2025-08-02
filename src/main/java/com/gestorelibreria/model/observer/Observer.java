package com.gestorelibreria.model.observer;

public interface Observer {
    /**
     * Metodo chiamato quando l'oggetto osservato cambia stato.
     * Gli observer implementano questo metodo per aggiornarsi in base al nuovo stato.
     */
    void update();

}
