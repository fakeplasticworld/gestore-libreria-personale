package com.gestorelibreria.view;

import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.model.StatoLettura;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AggiungiLibroDialogController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titoloField;
    @FXML
    private TextField autoreField;
    @FXML
    private TextField genereField;
    @FXML
    private TextField valutazioneField;
    @FXML
    private ComboBox<StatoLettura> statoLetturaComboBox;

    private Stage dialogStage;
    private LibroDTO libroDTO;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public LibroDTO getLibroDTO() {
        return libroDTO;
    }

    @FXML
    private void initialize() {
        // Popola il ComboBox con i valori dell'enum
        statoLetturaComboBox.getItems().setAll(StatoLettura.values());
        statoLetturaComboBox.setValue(StatoLettura.DA_LEGGERE);
    }

     @FXML
    private void onOkClicked() {
        if (isInputValid()) {
            libroDTO = new LibroDTO(
                titoloField.getText(),
                autoreField.getText(),
                isbnField.getText(),
                genereField.getText(),
                Integer.parseInt(valutazioneField.getText()),
                statoLetturaComboBox.getValue()
            );
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void onAnnullaClicked() {
        dialogStage.close();
    }

    /**
     * Valida l'input dell'utente nei campi di testo.
     */
    private boolean isInputValid() {
        // Aggiungi qui una logica di validazione più robusta!
        // Per ora, controlliamo solo che i campi non siano vuoti.
        if (isbnField.getText() == null || isbnField.getText().isEmpty() ||
                titoloField.getText() == null || titoloField.getText().isEmpty() ||
                autoreField.getText() == null || autoreField.getText().isEmpty()) {
            // Mostra un messaggio di errore (da implementare)
            System.err.println("Errore: I campi ISBN, Titolo e Autore non possono essere vuoti.");
            return false;
        }
        try {
            Integer.valueOf(valutazioneField.getText());
        } catch (NumberFormatException e) {
            System.err.println("Errore: La valutazione deve essere un numero.");
            return false;
        }
        return true;
    }
}