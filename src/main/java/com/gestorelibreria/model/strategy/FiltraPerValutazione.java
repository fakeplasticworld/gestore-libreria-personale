package com.gestorelibreria.model.strategy;

import com.gestorelibreria.model.Libro;

public class FiltraPerValutazione  implements CriterioDiFiltro {
    /**
     * Valutazione minima per il filtro.
     */
    private final int valutazioneMinima;
    /**
     * Costruttore che accetta la valutazione minima per il filtro.
     * @param valutazioneMinima La valutazione minima da utilizzare per il filtro.
     */
    public FiltraPerValutazione(int valutazioneMinima) {
        this.valutazioneMinima = valutazioneMinima;
    }
    /**
     * Restituisce la valutazione minima utilizzata per il filtro.
     * @return La valutazione minima.
     */
    public int getValutazioneMinima() {
        return valutazioneMinima;
    }
    @Override
    public boolean isMatch(Libro libro) {
        throw new UnsupportedOperationException("Unimplemented method 'isMatch'");
    }
    
}
