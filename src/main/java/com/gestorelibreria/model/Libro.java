package com.gestorelibreria.model;

public class Libro {
    private final String titolo;
    private final String autore;
    private final String genere;
    private final String isbn;
    private final int valutazione;
    private final StatoLettura statoLettura;

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getGenere() {
        return genere;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getValutazione() {
        return valutazione;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    /**
     * Costruttore privato per il pattern Builder.
     * 
     * @param builder Il builder che contiene i parametri per creare un Libro.
     */
    private Libro(Builder builder) {
        this.titolo = builder.titolo;
        this.autore = builder.autore;
        this.genere = builder.genere;
        this.isbn = builder.isbn;
        this.valutazione = builder.valutazione;
        this.statoLettura = builder.statoLettura;
    }

    public static class Builder {
        private final String titolo;
        private final String autore;
        private String genere;
        private String isbn;
        private int valutazione;
        private StatoLettura statoLettura;

        /**
         * Costruttore del Builder che richiede titolo e autore.
         * 
         * @param titolo Il titolo del libro.
         * @param autore L'autore del libro.
         */
        public Builder(String titolo, String autore) {
            if (titolo == null || titolo.trim().isEmpty() || autore == null || autore.trim().isEmpty()) {
                throw new IllegalArgumentException("Titolo e autore non possono essere vuoti.");
            }
            this.titolo = titolo;
            this.autore = autore;
        }

        public Builder conGenere(String genere) {
            this.genere = genere;
            return this;
        }

        public Builder conIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder conValutazione(int valutazione) {
            this.valutazione = valutazione;
            return this;
        }

        public Builder conStatoLettura(StatoLettura statoLettura) {
            this.statoLettura = statoLettura;
            return this;
        }

        public Libro build() {
            return new Libro(this);
        }
    }

}
