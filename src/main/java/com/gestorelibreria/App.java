package com.gestorelibreria;

import java.io.IOException;

import com.gestorelibreria.controller.MainController;
import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.view.MainView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe principale che avvia l'applicazione JavaFX.
 * Il suo compito è inizializzare e collegare i componenti MVC.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Crea il Model (ottiene l'istanza Singleton)
            Libreria libreria = Libreria.INSTANCE;

            // 2. Carica la View dal file FXML
            // Assicurati che il file MainView.fxml si trovi in: src/main/resources/com/gestorelibreria/view/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestorelibreria/view/MainView.fxml"));
            
            // Crea manualmente l'istanza della View (il controller FXML)
            MainView view = new MainView();
            // Imposta il controller sul loader PRIMA di caricare l'FXML
            loader.setController(view);
            
            Parent root = loader.load();

            // 4. Crea il Controller dell'applicazione, passandogli View e Model
            MainController controller = new MainController(view, libreria);

            // 5. "Inietta" il riferimento al controller nella View
            view.setController(controller);

            // 6. Registra la View come Observer del Model
            libreria.registraObserver(view);

            try {
                // Carica l'icona dalle risorse
                Image appIcon = new Image(getClass().getResourceAsStream("/com/gestorelibreria/icons/icona.png"));
                // Imposta l'icona sulla finestra principale
                primaryStage.getIcons().add(appIcon);
            } catch (Exception e) {
                System.err.println("Errore durante il caricamento dell'icona dell'applicazione: " + e.getMessage());
            }

            // 7. Configura e mostra la finestra principale
            primaryStage.setTitle("Gestore Libreria Personale");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            // 8. Carica i dati salvati all'avvio dell'applicazione
            controller.gestisciCarica();

        } catch (IOException e) {
            System.err.println("Errore durante l'avvio dell'applicazione: impossibile caricare MainView.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Si è verificato un errore imprevisto durante l'avvio.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
