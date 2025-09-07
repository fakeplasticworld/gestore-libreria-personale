package com.gestorelibreria.controller;

import java.util.Iterator;
import java.util.List;

import com.gestorelibreria.controller.command.AggiungiLibroCommand;
import com.gestorelibreria.controller.command.Command;
import com.gestorelibreria.controller.command.ModificaLibroCommand;
import com.gestorelibreria.controller.command.RimuoviLibroCommand;
import com.gestorelibreria.controller.dto.LibroDTO;
import com.gestorelibreria.controller.dto.RichiestaDTO;
import com.gestorelibreria.model.Libreria;
import com.gestorelibreria.model.Libro;
import com.gestorelibreria.model.strategy.CriterioDiFiltro;
import com.gestorelibreria.model.strategy.CriterioDiOrdinamento;
import com.gestorelibreria.model.strategy.FiltraPerGenere;
import com.gestorelibreria.model.strategy.FiltraPerStatoLettura;
import com.gestorelibreria.model.strategy.FiltraPerValutazione;
import com.gestorelibreria.model.strategy.FiltroUniversale;
import com.gestorelibreria.model.strategy.NessunOrdinamento;
import com.gestorelibreria.model.strategy.OrdinaPerTitolo;
import com.gestorelibreria.model.strategy.OrdinaPerValutazione;
import com.gestorelibreria.model.strategy.RicercaPerAutore;
import com.gestorelibreria.model.strategy.RicercaPerTitolo;
import com.gestorelibreria.persistence.GestorePersistenza;
import com.gestorelibreria.view.MainView;

public class MainController {

    private final MainView view;
    private final Libreria libreria;
    private final GestorePersistenza gestorePersistenza;

    public MainController(MainView view, Libreria libreria) {
        this.view = view;
        this.libreria = libreria;
        this.gestorePersistenza = new GestorePersistenza();
    }

    /**
     * Esegue un comando e salva immediatamente lo stato della libreria.
     * 
     * @param comando Il comando da eseguire.
     */
    private void eseguiComandoEsalva(Command comando) {
        comando.execute();
        gestorePersistenza.salva(this.libreria);
    }

    /**
     * Gestisce l'aggiunta di un nuovo libro.
     * 
     * @param dto Il DTO contenente i dati del libro da aggiungere.
     */
    public void gestisciAggiungiLibro(LibroDTO dto) {
        Libro nuovoLibro = new Libro.Builder(dto.titolo(), dto.autore())
                .conIsbn(dto.isbn())
                .conGenere(dto.genere())
                .conValutazione(dto.valutazione())
                .conStatoLettura(dto.statoLettura())
                .build();

        Command comando = new AggiungiLibroCommand(libreria, nuovoLibro);
        eseguiComandoEsalva(comando);
        view.mostraMessaggio("Libro aggiunto con successo: " + nuovoLibro.getTitolo());
    }

    /**
     * Gestisce la rimozione di un libro.
     * 
     * @param dto Il DTO contenente i dati del libro da rimuovere.
     */
    public void gestisciRimuoviLibro(LibroDTO dto) {
        // 1. Converte il DTO in un oggetto di dominio
        Libro libroDaRimuovere = libreria.trovaLibro(dto.isbn());
        if (libroDaRimuovere != null) {
            Command comando = new RimuoviLibroCommand(libreria, libroDaRimuovere);
            eseguiComandoEsalva(comando);
            view.mostraMessaggio("Libro rimosso con successo: " + libroDaRimuovere.getTitolo());
        } else {
            view.mostraMessaggio("Libro non trovato.");
        }
    }

    /**
     * Gestisce la modifica di un libro.
     * 
     * @param nuovoLibroDTO Il DTO contenente i nuovi dati del libro.
     * @param libroDaModificareDTO Il DTO del libro da modificare.
     */
    public void gestisciModificaLibro(LibroDTO nuovoLibroDTO, LibroDTO libroDaModificareDTO) {
        Libro libroDaModificare = libreria.trovaLibro(libroDaModificareDTO.isbn());
        if (libroDaModificare != null) {
            Libro libroAggiornato = new Libro.Builder(nuovoLibroDTO.titolo(), nuovoLibroDTO.autore())
                    .conIsbn(nuovoLibroDTO.isbn())
                    .conGenere(nuovoLibroDTO.genere())
                    .conValutazione(nuovoLibroDTO.valutazione())
                    .conStatoLettura(nuovoLibroDTO.statoLettura())
                    .build();

            Command comando = new ModificaLibroCommand(this.libreria, libroDaModificare, libroAggiornato);
            eseguiComandoEsalva(comando);
            view.mostraMessaggio("Libro modificato con successo: " + libroAggiornato.getTitolo());
        } else {
            view.mostraMessaggio("Libro non trovato.");
        }
    }

    /**
     * Gestisce una richiesta di filtro e ordinamento.
     * 
     * @param dto Il DTO contenente i criteri di filtro e ordinamento.
     */
    public void gestisciRichiesta(RichiestaDTO dto) {
        // 1. Traduce il DTO nelle strategie corrette
        CriterioDiFiltro filtro;
        try {
            filtro = switch (dto.tipoFiltro().toUpperCase()) {
                case "AUTORE" -> new RicercaPerAutore(dto.valoreFiltro());
                case "TITOLO" -> new RicercaPerTitolo(dto.valoreFiltro());
                case "GENERE" -> new FiltraPerGenere(dto.valoreFiltro());
                case "STATO LETTURA" -> new FiltraPerStatoLettura(dto.valoreFiltro());
                case "VALUTAZIONE" -> new FiltraPerValutazione(Integer.parseInt(dto.valoreFiltro()));
                default -> new FiltroUniversale();
            };
        } catch (NumberFormatException e) {
            view.mostraErrore("Valore non valido per il filtro valutazione. Inserire un numero.");
            filtro = new FiltroUniversale(); // Mostra tutti i libri in caso di errore
        }

        CriterioDiOrdinamento ordinamento = switch (dto.tipoOrdinamento().toUpperCase()) {
            case "TITOLO" -> new OrdinaPerTitolo();
            case "VALUTAZIONE" -> new OrdinaPerValutazione();
            default -> new NessunOrdinamento();
        };

        Iterator<Libro> risultati = libreria.filtraLibri(filtro, ordinamento);

        view.mostraRisultati(risultati);
    }

    /**
     * Gestisce il caricamento della libreria.
     */
    public void gestisciCarica() {
        List<Libro> libriCaricati = gestorePersistenza.carica();
        libreria.sostituisciCollezione(libriCaricati);
        gestorePersistenza.salva(libreria);
        view.mostraMessaggio("Libreria caricata con successo.");
    }

    /**
     * Gestisce l'importazione della libreria.
     * 
     * @param percorso Il percorso del file da importare.
     */
    public void gestisciImporta(String percorso) {
    List<Libro> libriImportati = gestorePersistenza.importa(percorso);
    if (libriImportati != null) {
        boolean isDataValid = true;
        for (Libro libro : libriImportati) {
            if (libro.getTitolo() == null || libro.getTitolo().trim().isEmpty() ||
                libro.getAutore() == null || libro.getAutore().trim().isEmpty() ||
                libro.getIsbn() == null || libro.getIsbn().trim().isEmpty() ||
                libro.getGenere() == null || libro.getGenere().trim().isEmpty() ||
                libro.getStatoLettura() == null) {
                isDataValid = false;
                break;
            }
        }
        if (isDataValid && !libriImportati.isEmpty()) {
            libreria.sostituisciCollezione(libriImportati);
            view.mostraMessaggio("Libreria importata con successo da: " + percorso);
        } else if (!isDataValid) {
            view.mostraErrore("Importazione fallita: il file JSON non è valido o contiene dati incompleti (es. titolo, autore o stato di lettura mancanti).");
        } 
        else {
            view.mostraMessaggio("Nessun libro trovato nel file specificato.");
        }
    } else {
        view.mostraErrore("Importazione fallita: impossibile leggere il file.");
    }
}

    /**
     * Gestisce l'esportazione della libreria.
     * 
     * @param percorso Il percorso del file da esportare.
     */
    public void gestisciEsporta(String percorso) {
        gestorePersistenza.esporta(libreria, percorso);
        view.mostraMessaggio("Libreria esportata con successo in: " + percorso);
    }

    /**
     * Gestisce il salvataggio della libreria.
     */
    public void gestisciSalva() {
        gestorePersistenza.salva(libreria);
        view.mostraMessaggio("Libreria salvata con successo.");
    }

    /**
     * Gestisce l'uscita dal programma, salvando la libreria.
     */
    public void gestisciEsci() {
        gestorePersistenza.salva(libreria);
        view.mostraMessaggio("Uscita dal programma. Libreria salvata.");
        System.exit(0);
    }

}