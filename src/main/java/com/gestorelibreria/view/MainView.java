package com.gestorelibreria.view;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import com.gestorelibreria.controller.MainController;
import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.controller.dto.RichiestaDTO;
import com.gestorelibreria.model.Libro;
import com.gestorelibreria.model.StatoLettura;
import com.gestorelibreria.model.observer.Observer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainView implements Observer {

    // --- RIFERIMENTI AI COMPONENTI FXML ---
    @FXML
    private TilePane bookContainer; // Il contenitore a griglia per le "card" dei libri
    @FXML
    private ComboBox<String> filtroTipoComboBox;
    @FXML
    private TextField filtroValoreField;
    @FXML
    private ComboBox<StatoLettura> filtroStatoLetturaComboBox;
    @FXML
    private ComboBox<String> ordinamentoTipoComboBox;

    private MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public MainController getController() {
        return controller;
    }

    private void setIconaStage(Stage stage) {
        try {
            Image appIcon = new Image(getClass().getResourceAsStream("/com/gestorelibreria/icons/icona.png"));
            stage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'icona per il dialog: " + e.getMessage());
        }
    }

    /**
     * Metodo speciale di JavaFX, chiamato automaticamente dopo che l'FXML è stato
     * caricato.
     */
    @FXML
    public void initialize() {
        // Popola i ComboBox con le opzioni di filtro e ordinamento
        filtroTipoComboBox.getItems().addAll("TITOLO", "AUTORE", "GENERE", "STATO LETTURA");
        ordinamentoTipoComboBox.getItems().addAll("NESSUNO", "TITOLO", "VALUTAZIONE");

        // Popola il ComboBox per lo stato di lettura
        filtroStatoLetturaComboBox.getItems().setAll(StatoLettura.values());
        filtroStatoLetturaComboBox.setValue(StatoLettura.DA_LEGGERE);

        // Imposta dei valori di default
        filtroTipoComboBox.setValue("TITOLO");
        ordinamentoTipoComboBox.setValue("NESSUNO");

        // Gestione della visibilità del campo di filtro in base al tipo selezionato
        filtroTipoComboBox.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if ("STATO LETTURA".equals(newValue)) {
                filtroValoreField.setVisible(false);
                filtroValoreField.setManaged(false);
                filtroStatoLetturaComboBox.setVisible(true);
                filtroStatoLetturaComboBox.setManaged(true);
            } else {
                filtroStatoLetturaComboBox.setVisible(false);
                filtroStatoLetturaComboBox.setManaged(false);
                filtroValoreField.setVisible(true);
                filtroValoreField.setManaged(true);
            }
        });
    }

    @Override
    public void update() {
        System.out.println("View: Ricevuto update dal Model. Ricarico i dati...");
        // Quando il Model notifica un cambiamento, riesegue l'ultima ricerca o una
        // ricerca di default
        onFiltraClicked();
    }

    /**
     * Popola la griglia con le "card" dei libri ricevuti.
     * 
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
                cardController.setData(libro, this);

                // Aggiungi la card al contenitore a griglia
                bookContainer.getChildren().add(bookCardNode);

            } catch (IOException e) {
                System.err.println("Errore durante il caricamento di BookCard.fxml");
            }
        });
        System.out.println("View: Griglia aggiornata.");
    }

    public void mostraModificaDialog(LibroDTO libroDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModificaLibroDialog.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Libro");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            setIconaStage(dialogStage);
            dialogStage.setResizable(false);

            dialogStage.setScene(scene);

            ModificaLibroDialogController dialogController = loader.getController();

            Window owner = bookContainer.getScene().getWindow();
            dialogStage.initOwner(owner);
            dialogController.setDialogStage(dialogStage);
            dialogController.setLibro(libroDTO);

            dialogStage.showAndWait();

            if (dialogController.isOkClicked()) {
                LibroDTO nuovoLibroDTO = dialogController.getLibroDTO();
                controller.gestisciModificaLibro(nuovoLibroDTO, libroDTO);
            }

        } catch (IOException e) {
            mostraErrore("Impossibile aprire la finestra per modificare il libro.");
        }
    }

    public boolean mostraConfermaRimozione(LibroDTO libro) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Rimozione");
        alert.setHeaderText("Sei sicuro di voler rimuovere il libro?");
        alert.setContentText("Libro: " + libro.titolo());
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void mostraMessaggio(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, messaggio);
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        alert.showAndWait();
    }

    public void mostraErrore(String errore) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errore);
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        alert.showAndWait();
    }

    // --- GESTORI DI EVENTI FXML ---

    @FXML
    private void onFiltraClicked() {
        String tipoFiltro = filtroTipoComboBox.getValue();
        String valoreFiltro;
        String tipoOrdinamento = ordinamentoTipoComboBox.getValue();

        if ("STATO LETTURA".equals(tipoFiltro)) {
            valoreFiltro = filtroStatoLetturaComboBox.getValue().name();
        } else {
            valoreFiltro = filtroValoreField.getText();
        }

        // Crea il DTO e lo passa al controller
        RichiestaDTO richiesta = new RichiestaDTO(tipoFiltro, valoreFiltro, tipoOrdinamento);
        controller.gestisciRichiesta(richiesta);
    }

    @FXML
    private void onAggiungiClicked() {
        try {
            // Carica il file FXML del dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibroDialog.fxml"));
            GridPane page = loader.load();

            // Crea una nuova finestra (Stage) per il dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Nuovo Libro");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookContainer.getScene().getWindow());
            dialogStage.setResizable(false);
            Window owner = bookContainer.getScene().getWindow();
            dialogStage.initOwner(owner);
            // Crea la scena e imposta l'icona
            Scene scene = new Scene(page);
            setIconaStage(dialogStage);
            dialogStage.setScene(scene);

            // Ottieni il controller del dialog e passagli lo Stage
            AggiungiLibroDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);

            // Mostra il dialog e attendi che venga chiuso
            dialogStage.showAndWait();

            // Se l'utente ha cliccato "Aggiungi", passa il DTO al controller principale
            if (dialogController.isOkClicked()) {
                LibroDTO nuovoLibro = dialogController.getLibroDTO();
                this.controller.gestisciAggiungiLibro(nuovoLibro);
            }

        } catch (IOException e) {
            System.err.println("Errore durante l'apertura del dialog di aggiunta libro.");
            mostraErrore("Impossibile aprire la finestra per aggiungere il libro.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSalvaClicked() {
        controller.gestisciSalva();
    }

    @FXML
    private void onImportaClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importa Libreria");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File JSON", "*.json"));

        File file = fileChooser.showOpenDialog(bookContainer.getScene().getWindow());
        if (file != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Importazione");
            alert.setHeaderText("L'importazione sostituirà la libreria corrente.");
            alert.setContentText("Tutte le modifiche non salvate andranno perse. Sei sicuro di voler continuare?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.gestisciImporta(file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void onEsportaClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Esporta Libreria");
        fileChooser.setInitialFileName("libreria.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File JSON", "*.json"));

        File file = fileChooser.showSaveDialog(bookContainer.getScene().getWindow());
        if (file != null) {
            controller.gestisciEsporta(file.getAbsolutePath());
        }
    }

    @FXML
    private void onEsciClicked() {
        controller.gestisciEsci();
    }
}