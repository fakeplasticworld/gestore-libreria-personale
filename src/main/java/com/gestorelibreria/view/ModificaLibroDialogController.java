package com.gestorelibreria.view;

import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.model.StatoLettura;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ModificaLibroDialogController {

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
        statoLetturaComboBox.getItems().setAll(StatoLettura.values());
    }

    public void setLibro(LibroDTO libro) {
        this.libroDTO = libro;
        isbnField.setText(libro.isbn());
        titoloField.setText(libro.titolo());
        autoreField.setText(libro.autore());
        genereField.setText(libro.genere());
        if (libro.valutazione() == -1) {
            valutazioneField.setText("");
        } else {
            valutazioneField.setText(String.valueOf(libro.valutazione()));
        }
        statoLetturaComboBox.setValue(libro.statoLettura());
    }

    @FXML
    private void onOkClicked() {
         if (isInputValid()) {
            // Se il campo valutazione è vuoto, usa -1 come default
            int valutazione = valutazioneField.getText().isEmpty() ? -1 : Integer.parseInt(valutazioneField.getText());
            libroDTO = new LibroDTO(
                    titoloField.getText(),
                    autoreField.getText(),
                    isbnField.getText(),
                    genereField.getText(),
                    valutazione,
                    statoLetturaComboBox.getValue());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void onAnnullaClicked() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        if (isbnField.getText() == null || isbnField.getText().isEmpty() ||
                titoloField.getText() == null || titoloField.getText().isEmpty() ||
                autoreField.getText() == null || autoreField.getText().isEmpty()) {
            mostraErrore("I campi ISBN, Titolo e Autore non possono essere vuoti.");
            return false;
        }

        if (statoLetturaComboBox.getValue() == null) {
            mostraErrore("È obbligatorio selezionare uno stato di lettura.");
            return false;
        }
        if (genereField.getText() == null || genereField.getText().isEmpty()) {
            mostraErrore("Il campo Genere non può essere vuoto.");
            return false;
        }
        // Validazione della valutazione se presente
        if (!valutazioneField.getText().isEmpty()) {
            try {
                int valutazione = Integer.parseInt(valutazioneField.getText());
                if (valutazione < 0 || valutazione > 5) {
                    mostraErrore("La valutazione deve essere un numero tra 0 e 5.");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostraErrore("La valutazione deve essere un numero valido.");
                return false;
            }
        }
        return true;
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di Validazione");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        setIconaStage((Stage) alert.getDialogPane().getScene().getWindow());
        alert.showAndWait();
    }

    private void setIconaStage(Stage stage) {
        try {
            Image appIcon = new Image(getClass().getResourceAsStream("/com/gestorelibreria/icons/libreria_icon.png"));
            stage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'icona dell'applicazione: " + e.getMessage());
        }
    }
}