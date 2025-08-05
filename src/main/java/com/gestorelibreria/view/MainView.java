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

    @FXML
    private TilePane bookContainer;
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

    /**
     * Imposta l'icona per il dialogo.
     * 
     * @param stage Lo stage del dialogo.
     */
    private void setIconaStage(Stage stage) {
        try {
            Image appIcon = new Image(getClass().getResourceAsStream("/com/gestorelibreria/icons/icona.png"));
            stage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'icona per il dialog: " + e.getMessage());
        }
    }

    /**
     * Inizializza la vista principale.
     * Popola i ComboBox e imposta i listener per le azioni dell'utente.
     */
  @FXML
    public void initialize() {
        filtroTipoComboBox.getItems().addAll("NESSUNO", "TITOLO", "AUTORE", "GENERE", "STATO LETTURA");
        ordinamentoTipoComboBox.getItems().addAll("NESSUNO", "TITOLO", "VALUTAZIONE");

        filtroStatoLetturaComboBox.getItems().setAll(StatoLettura.values());
        filtroStatoLetturaComboBox.setValue(StatoLettura.DA_LEGGERE);

        filtroTipoComboBox.setValue("NESSUNO");
        ordinamentoTipoComboBox.setValue("NESSUNO");
        filtroValoreField.setVisible(false);
        filtroValoreField.setManaged(false);
        filtroStatoLetturaComboBox.setVisible(false);
        filtroStatoLetturaComboBox.setManaged(false);

        filtroTipoComboBox.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            boolean isStatoLettura = "STATO LETTURA".equals(newValue);
            boolean isNessuno = "NESSUNO".equals(newValue);

            filtroStatoLetturaComboBox.setVisible(isStatoLettura);
            filtroStatoLetturaComboBox.setManaged(isStatoLettura);

            filtroValoreField.setVisible(!isStatoLettura && !isNessuno);
            filtroValoreField.setManaged(!isStatoLettura && !isNessuno);
        });
    }


    @Override
    public void update() {
        onFiltraClicked();
    }

    /**
     * Popola la griglia con le "card" dei libri ricevuti.
     * 
     * @param risultati Un iteratore sui libri da mostrare.
     */
    public void mostraRisultati(Iterator<Libro> risultati) {
        bookContainer.getChildren().clear();

        risultati.forEachRemaining(libro -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BookCard.fxml"));
                Parent bookCardNode = loader.load();

                BookCardController cardController = loader.getController();
                cardController.setData(libro, this);

                bookContainer.getChildren().add(bookCardNode);

            } catch (IOException e) {
                System.err.println("Errore durante il caricamento di BookCard.fxml");
            }
        });
        if (bookContainer.getChildren().isEmpty()) {
            mostraMessaggio("Nessun libro trovato");
        }
    }

    /**
     * Mostra un dialog per modificare un libro esistente.
     * 
     * @param libroDTO Il DTO del libro da modificare.
     */
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

    /**
     * Mostra un dialog di conferma per la rimozione di un libro.
     * 
     * @param libro Il libro da rimuovere.
     * @return true se l'utente conferma la rimozione, false altrimenti.
     */
    public boolean mostraConfermaRimozione(LibroDTO libro) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Rimozione");
        alert.setHeaderText("Sei sicuro di voler rimuovere il libro?");
        alert.setContentText("Libro: " + libro.titolo());
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Mostra un messaggio informativo all'utente.
     * 
     * @param messaggio Il messaggio da mostrare.
     */
    public void mostraMessaggio(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, messaggio);
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di errore all'utente.
     * 
     * @param errore Il messaggio di errore da mostrare.
     */
    public void mostraErrore(String errore) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errore);
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Gestisce il click sul pulsante di filtro.
     */
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

        RichiestaDTO richiesta = new RichiestaDTO(tipoFiltro, valoreFiltro, tipoOrdinamento);
        controller.gestisciRichiesta(richiesta);
    }

    /**
     * Gestisce il click sul pulsante di aggiunta di un nuovo libro.
     * Apre un dialog per inserire i dettagli del libro da aggiungere.
     */
    @FXML
    private void onAggiungiClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AggiungiLibroDialog.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Nuovo Libro");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookContainer.getScene().getWindow());
            dialogStage.setResizable(false);
            Window owner = bookContainer.getScene().getWindow();
            dialogStage.initOwner(owner);
            Scene scene = new Scene(page);
            setIconaStage(dialogStage);
            dialogStage.setScene(scene);

            AggiungiLibroDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (dialogController.isOkClicked()) {
                LibroDTO nuovoLibro = dialogController.getLibroDTO();
                this.controller.gestisciAggiungiLibro(nuovoLibro);
            }

        } catch (IOException e) {
            System.err.println("Errore durante l'apertura del dialog di aggiunta libro.");
            mostraErrore("Impossibile aprire la finestra per aggiungere il libro.");
        }
    }

    /**
     * Gestisce il click sul pulsante di salvataggio.
     */
    @FXML
    private void onSalvaClicked() {
        controller.gestisciSalva();
    }

    /**
     * Gestisce il click sul pulsante di importazione.
     */
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

    /**
     * Gestisce il click sul pulsante di esportazione.
     * Permette di salvare la libreria in un file JSON.
     */
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