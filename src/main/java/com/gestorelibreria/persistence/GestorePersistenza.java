package com.gestorelibreria.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.model.Libro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Gestisce il salvataggio e il caricamento della libreria su file JSON.
 */
public class GestorePersistenza {

    // Nome del file di default per il salvataggio rapido
    private static final String DEFAULT_FILENAME = "libreria.json";
    private final Gson gson;

    public GestorePersistenza() {
        // Inizializza Gson con l'opzione "pretty printing" per rendere il JSON
        // leggibile
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Salva lo stato attuale della libreria nel file di default.
     * 
     * @param libreria L'istanza della libreria da salvare.
     */
    public void salva(Libreria libreria) {
        List<Libro> listaDaSalvare = new ArrayList<>();
        Iterator<Libro> iterator = libreria.createIterator();
        while (iterator.hasNext()) {
            Libro libro = iterator.next();
            listaDaSalvare.add(libro);
        }
        try (FileWriter writer = new FileWriter(DEFAULT_FILENAME)) {
            gson.toJson(listaDaSalvare, writer);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Carica lo stato della libreria dal file di default.
     * 
     * @return L'istanza della libreria caricata, o una nuova istanza se il file non
     *         esiste.
     */
    // Dentro la classe GestorePersistenza
    public List<Libro> carica() {
        try (FileReader reader = new FileReader(DEFAULT_FILENAME)) {
            Type tipoListaLibri = new TypeToken<ArrayList<Libro>>() {
            }.getType();
            List<Libro> libriCaricati = gson.fromJson(reader, tipoListaLibri);
            return libriCaricati != null ? libriCaricati : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Nessun file di salvataggio trovato.");
            return new ArrayList<>();
        }
    }

    /**
     * Esporta la libreria in un percorso file specificato dall'utente.
     * 
     * @param libreria L'istanza della libreria da esportare.
     * @param percorso Il percorso completo del file di destinazione.
     */
    public void esporta(Libreria libreria, String percorso) {
        List<Libro> listaDaEsportare = new ArrayList<>();
        Iterator<Libro> iterator = libreria.createIterator();
        while (iterator.hasNext()) {
            Libro libro = iterator.next();
            listaDaEsportare.add(libro);
        }
        try (FileWriter writer = new FileWriter(percorso)) {
            gson.toJson(listaDaEsportare, writer);
        } catch (IOException e) {
            System.err.println("Errore durante l'esportazione: " + e.getMessage());
        }
    }

    /**
     * Importa una libreria da un percorso file specificato dall'utente.
     * 
     * @param percorso Il percorso completo del file da cui importare.
     * @return L'istanza della libreria importata.
     */
    public List<Libro> importa(String percorso) {
        try (FileReader reader = new FileReader(percorso)) {
            Type tipoListaLibri = new TypeToken<ArrayList<Libro>>(){}.getType();
            List<Libro> libriImportati = gson.fromJson(reader, tipoListaLibri);
            return libriImportati != null ? libriImportati : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore durante l'importazione: " + e.getMessage());
            return null;
        }
    }
}