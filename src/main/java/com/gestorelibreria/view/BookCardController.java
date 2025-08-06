package com.gestorelibreria.view;

import java.util.Arrays;
import java.util.List;

import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.model.Libro;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BookCardController {
    @FXML
    private ImageView copertina;
    @FXML
    private Label titolo;
    @FXML
    private Label autore;
    @FXML
    private ImageView statoLettura;
    @FXML
    private HBox valutazione;
    @FXML
    private Button modifica;
    @FXML
    private Button rimuovi;

    private Libro libro;
    private MainView mainView;

    /**
     * Colori pastello per le card dei libri.
     * Questi colori sono scelti in modo da essere facilmente distinguibili
     */
    private static final List<Color> COLORI = Arrays.asList(
            Color.web("#d4a373"),
            Color.web("#c5a05a"),
            Color.web("#c49a88"),
            Color.web("#a98467"),
            Color.web("#a0938f"),
            Color.web("#dde5b6"),
            Color.web("#adc178"),
            Color.web("#728168"),
            Color.web("#83c5be"),
            Color.web("#b5ada5"),
            Color.web("#7a8c99"),
            Color.web("#cad2d9"),
            Color.web("#a999ab"),
            Color.web("#965f5f"),
            Color.web("#62768b"),
            Color.web("#8d8883"));

    /**
     * Imposta i dati del libro e aggiorna la visualizzazione della card.
     *
     * @param libro    Il libro da visualizzare nella card.
     * @param mainView La vista principale per interazioni future.
     */
    public void setData(Libro libro, MainView mainView) {
        this.libro = libro;
        this.mainView = mainView;
        titolo.setText(libro.getTitolo());
        autore.setText(libro.getAutore());
        impostaStelleValutazione(libro.getValutazione());

        try {
            String iconPath = libro.getStatoLettura().getIconPath();
            Image icon = new Image(getClass().getResourceAsStream(iconPath));
            statoLettura.setImage(icon);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'icona: " + e.getMessage());
        }

        // Crea un canvas per disegnare la copertina del libro
        int width = 150;
        int height = 230;
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int colorIndex = Math.abs(libro.getTitolo().hashCode()) % COLORI.size();
        Color deterministicColor = COLORI.get(colorIndex);
        gc.setFill(deterministicColor);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("System Bold", 18));
        gc.setTextAlign(TextAlignment.CENTER);
        gestisciTestoACapo(gc, libro.getTitolo(), width / 2.0, 50, width - 20);
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        copertina.setImage(writableImage);
    }

    /**
     * Gestisce il click sul titolo del libro.
     * Mostra i dettagli del libro nella vista principale.
     */
    @FXML
    private void onModificaClicked() {
        LibroDTO dto = new LibroDTO(
                libro.getTitolo(), libro.getAutore(), libro.getIsbn(),
                libro.getGenere(), libro.getValutazione(), libro.getStatoLettura());
        mainView.mostraModificaDialog(dto);
    }

    /**
     * Gestisce il click sull'immagine della copertina del libro.
     * Mostra i dettagli del libro nella vista principale.
     */
    @FXML
    private void onRimuoviClicked() {
        LibroDTO dto = new LibroDTO(
                libro.getTitolo(), libro.getAutore(), libro.getIsbn(),
                libro.getGenere(), libro.getValutazione(), libro.getStatoLettura());
        if (mainView.mostraConfermaRimozione(dto)) {
            mainView.getController().gestisciRimuoviLibro(dto);
            mainView.mostraMessaggio("Libro rimosso con successo.");
        }
    }

    /**
     * Disegna il testo su più righe se non entra nella larghezza massima.
     * 
     * @param gc       il GraphicsContext su cui disegnare
     * @param testo    il testo da disegnare
     * @param x        la coordinata x del centro del testo
     * @param y        la coordinata y della prima riga
     * @param maxWidth la larghezza massima permessa per una riga
     */
    private void gestisciTestoACapo(GraphicsContext gc, String testo, double x, double y, double maxWidth) {
        String[] parole = testo.split(" ");
        StringBuilder riga = new StringBuilder(parole[0]);
        double altezza = gc.getFont().getSize();

        for (int i = 1; i < parole.length; i++) {
            if (gc.getFont().getSize() * (riga.length() + parole[i].length()) > maxWidth * 1.5) {
                gc.fillText(riga.toString(), x, y);
                y += altezza;
                riga = new StringBuilder(parole[i]);
            } else {
                riga.append(" ").append(parole[i]);
            }
        }
        gc.fillText(riga.toString(), x, y);
    }

    /**
     * Pulisce e popola il contenitore della valutazione con le stelle.
     * 
     * @param valutazione Il punteggio da 1 a 5, o -1 se non presente.
     */
    private void impostaStelleValutazione(int val) {
        valutazione.getChildren().clear();

        if (val > 0) {
            valutazione.setVisible(true);
            valutazione.setStyle(
                    "-fx-background-color: rgba(0, 0, 0, 0.75); -fx-padding: 2 5 2 5; -fx-background-radius: 20;");

            for (int i = 1; i <= 5; i++) {
                Label stella = new Label(i <= val ? "★" : "☆");
                stella.setStyle("-fx-font-size: 12px; -fx-text-fill: #ffc107;");
                valutazione.getChildren().add(stella);
            }
        } else {
            valutazione.setVisible(false);
        }
    }

}