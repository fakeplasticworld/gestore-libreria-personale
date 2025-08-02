package com.gestorelibreria.view;

import java.io.IOException;
import java.util.Iterator;

import com.gestorelibreria.controller.MainController;
import com.gestorelibreria.controller.dto.RichiestaDTO;
import com.gestorelibreria.model.Libro;
import com.gestorelibreria.model.observer.Observer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane; // <-- Import necessario

public class MainView implements Observer {

    // --- RIFERIMENTI AI COMPONENTI FXML ---
    @FXML
    private TilePane bookContainer; // Il contenitore a griglia per le "card" dei libri
    @FXML
    private ComboBox<String> filtroTipoComboBox;
    @FXML
    private TextField filtroValoreField;
    @FXML
    private ComboBox<String> ordinamentoTipoComboBox;

    private MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    /**
     * Metodo speciale di JavaFX, chiamato automaticamente dopo che l'FXML è stato caricato.
     */
    @FXML
    public void initialize() {
        // Popola i ComboBox con le opzioni di filtro e ordinamento
        filtroTipoComboBox.getItems().addAll("TITOLO", "AUTORE", "GENERE");
        ordinamentoTipoComboBox.getItems().addAll("NESSUNO", "TITOLO", "VALUTAZIONE");
        
        // Imposta dei valori di default
        filtroTipoComboBox.setValue("TITOLO");
        ordinamentoTipoComboBox.setValue("NESSUNO");
    }

    // --- METODI CHIAMATI DAL CONTROLLER ---

    @Override
    public void update() {
        System.out.println("View: Ricevuto update dal Model. Ricarico i dati...");
        // Quando il Model notifica un cambiamento, riesegue l'ultima ricerca o una ricerca di default
        onFiltraClicked(); 
    }

    /**
     * Popola la griglia con le "card" dei libri ricevuti.
     * @param risultati Un iteratore sui libri da mostrare.
     */
    public void mostraRisultati(Iterator<Libro> risultati) {
        // Pulisci la vista precedente
        bookContainer.getChildren().clear();

        // Itera sui risultati e crea una "card" per ogni libro
        risultati.forEachRemaining(libro -> {
            try {
                // Carica il file FXML della singola card
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BookCard.fxml"));
                Parent bookCardNode = loader.load();

                // Ottieni il controller della card e passagli i dati del libro
                BookCardController cardController = loader.getController();
                cardController.setData(libro);

                // Aggiungi la card al contenitore a griglia
                bookContainer.getChildren().add(bookCardNode);

            } catch (IOException e) {
                System.err.println("Errore durante il caricamento di BookCard.fxml");
                e.printStackTrace();
            }
        });
        System.out.println("View: Griglia aggiornata.");
    }

    public void mostraMessaggio(String messaggio) {
        new Alert(Alert.AlertType.INFORMATION, messaggio).showAndWait();
    }

    public void mostraErrore(String errore) {
        new Alert(Alert.AlertType.ERROR, errore).showAndWait();
    }
    
    // --- GESTORI DI EVENTI FXML ---
    
    @FXML
    private void onFiltraClicked() {
        String tipoFiltro = filtroTipoComboBox.getValue();
        String valoreFiltro = filtroValoreField.getText();
        String tipoOrdinamento = ordinamentoTipoComboBox.getValue();

        // Crea il DTO e lo passa al controller
        RichiestaDTO richiesta = new RichiestaDTO(tipoFiltro, valoreFiltro, tipoOrdinamento);
        controller.gestisciRichiesta(richiesta);
    }
    
    @FXML
    private void onAggiungiClicked() {
        // Qui aprirai una nuova finestra (dialog) per inserire i dati del libro
        // e poi chiamerai controller.gestisciAggiungiLibro(dto);
        System.out.println("Pulsante Aggiungi cliccato");
        mostraMessaggio("Funzionalità 'Aggiungi' non ancora implementata.");
    }
    
    @FXML
    private void onSalvaClicked() {
        controller.gestisciSalva();
    }
}