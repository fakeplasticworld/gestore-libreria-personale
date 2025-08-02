package com.gestorelibreria.view;

import com.gestorelibreria.model.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class BookCardController {
    @FXML private ImageView copertina;
    @FXML private Label titolo;
    @FXML private Label autore;
    @FXML private Label valutazione;

    // Metodo per popolare la card con i dati di un libro
    public void setData(Libro libro) {
        titolo.setText(libro.getTitolo());
        autore.setText(libro.getAutore());
        valutazione.setText(libro.getValutazione() + "/5");
        // Carica l'immagine (dovrai gestire i percorsi delle immagini)
        // Image image = new Image(getClass().getResourceAsStream("/images/" + libro.getIsbn() + ".jpg"));
        // copertina.setImage(image);
    }
}