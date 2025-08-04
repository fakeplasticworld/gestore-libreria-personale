package com.gestorelibreria.view;

import java.util.Arrays;
import java.util.List;

import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.model.Libro;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
    private Label valutazione;
    @FXML
    private ImageView statoLettura;

    private Libro libro;
    private MainView mainView;

    private static final List<Color> PASTEL_COLORS = Arrays.asList(
            Color.web("#ffadad"), // Rosso Pastello
            Color.web("#ffd6a5"), // Arancione Pastello
            Color.web("#fdffb6"), // Giallo Pastello
            Color.web("#caffbf"), // Verde Pastello
            Color.web("#9bf6ff"), // Ciano Pastello
            Color.web("#a0c4ff"), // Blu Pastello
            Color.web("#bdb2ff"), // Viola Pastello
            Color.web("#ffc6ff"), // Magenta Pastello
            Color.web("#e6e6e6"), // Grigio Chiaro
            Color.web("#d4a373")  // Marroncino Sobrio
    );

    // Metodo per popolare la card con i dati di un libro
    public void setData(Libro libro, MainView mainView) {
        this.libro = libro;
        this.mainView = mainView;
        titolo.setText(libro.getTitolo());
        autore.setText(libro.getAutore());
        if (libro.getValutazione() >= 0) {
            valutazione.setText(libro.getValutazione() + "/5");
            valutazione.setVisible(true);
        } else {
            valutazione.setVisible(false);
        }
        try {
            String iconPath = libro.getStatoLettura().getIconPath();
            Image icon = new Image(getClass().getResourceAsStream(iconPath));
            statoLettura.setImage(icon);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'icona: " + e.getMessage());
        }
        int width = 150;
        int height = 230;

        // Crea un canvas su cui disegnare
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Genera un colore di sfondo casuale
        int colorIndex = Math.abs(libro.getTitolo().hashCode()) % PASTEL_COLORS.size();
        Color deterministicColor = PASTEL_COLORS.get(colorIndex);
        
        // Imposta il colore di sfondo
        gc.setFill(deterministicColor);
        gc.fillRect(0, 0, width, height);

        // Imposta il colore del testo
        gc.setFill(Color.BLACK);

        // Impostazioni del font e allineamento del testo
        gc.setFont(new Font("System Bold", 18));
        gc.setTextAlign(TextAlignment.CENTER);

        drawTextWithLineBreaks(gc, libro.getTitolo(), width / 2.0, 50, width - 20);

        // Crea un'immagine dal canvas
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);

        // Imposta l'immagine generata sulla ImageView della copertina
        copertina.setImage(writableImage);
    }

    @FXML
    private void onModificaClicked() {
        LibroDTO dto = new LibroDTO(
            libro.getTitolo(), libro.getAutore(), libro.getIsbn(),
            libro.getGenere(), libro.getValutazione(), libro.getStatoLettura()
        );
        mainView.mostraModificaDialog(dto);
    }

    @FXML
    private void onRimuoviClicked() {
        LibroDTO dto = new LibroDTO(
            libro.getTitolo(), libro.getAutore(), libro.getIsbn(),
            libro.getGenere(), libro.getValutazione(), libro.getStatoLettura()
        );
        if (mainView.mostraConfermaRimozione(dto)) {
            mainView.getController().gestisciRimuoviLibro(dto);
            mainView.mostraMessaggio("Libro rimosso con successo.");
        }
    }

     /**
     * Disegna il testo su più righe se non entra nella larghezza massima.
     * @param gc il GraphicsContext su cui disegnare
     * @param text il testo da disegnare
     * @param x la coordinata x del centro del testo
     * @param y la coordinata y della prima riga
     * @param maxWidth la larghezza massima permessa per una riga
     */
    private void drawTextWithLineBreaks(GraphicsContext gc, String text, double x, double y, double maxWidth) {
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder(words[0]);
        double lineHeight = gc.getFont().getSize(); // Ottiene l'altezza della riga basata sulla dimensione del font

        for (int i = 1; i < words.length; i++) {
            // Se aggiungere la prossima parola supera la larghezza massima...
            if (gc.getFont().getSize() * (currentLine.length() + words[i].length()) > maxWidth * 1.5) { // Moltiplicatore per approssimare la larghezza
                // ...disegna la riga corrente e vai a capo.
                gc.fillText(currentLine.toString(), x, y);
                y += lineHeight;
                currentLine = new StringBuilder(words[i]);
            } else {
                // ...altrimenti, aggiungi la parola alla riga corrente.
                currentLine.append(" ").append(words[i]);
            }
        }
        // Disegna l'ultima riga rimasta
        gc.fillText(currentLine.toString(), x, y);
    }

}