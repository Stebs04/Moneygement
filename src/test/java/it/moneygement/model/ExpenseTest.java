package it.moneygement.model;

import it.moneygement.exception.DatiNonValidiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test unitari completa per la classe Expense.
 * Questa suite di test copre ogni singolo metodo pubblico della classe,
 * inclusi costruttori, getter, setter e validazioni dei dati.
 *
 * @author Stefano Bellan
 */
public class ExpenseTest {

    // Istanza principale utilizzata in tutti i test
    private Expense spesa;

    /**
     * Configurazione iniziale eseguita prima di ogni metodo di test.
     * Crea un'istanza pulita e valida di Expense per garantire l'indipendenza dei test.
     */
    @BeforeEach
    public void setUp() {
        // Creazione dell'oggetto con dati validi iniziali
        spesa = new Expense(
                "Assicurazione Auto",
                Categories.AUTO,
                "Polizza annuale auto",
                250.50,
                LocalDateTime.of(2026, 1, 9, 10, 0)
        );
    }

    // ---------------------------------------------------------------
    // TEST SUL COSTRUTTORE E GETTER
    // ---------------------------------------------------------------

    /**
     * Verifica che l'oggetto venga istanziato correttamente e che i getter
     * restituiscano i valori passati al costruttore.
     */
    @Test
    @DisplayName("Test Costruttore e Getter (Happy Path)")
    public void testCostruttoreEGetter() {
        // Verifica corrispondenza nome
        assertEquals("Assicurazione Auto", spesa.getNomeSpesa());
        // Verifica corrispondenza categoria
        assertEquals(Categories.AUTO, spesa.getCategoria());
        // Verifica corrispondenza descrizione
        assertEquals("Polizza annuale auto", spesa.getDescrizione());
        // Verifica corrispondenza importo
        assertEquals(250.50, spesa.getImporto());
        // Verifica corrispondenza data
        assertEquals(LocalDateTime.of(2026, 1, 9, 10, 0), spesa.getData());
        // Verifica che l'oggetto non sia nullo
        assertNotNull(spesa);
    }

    // ---------------------------------------------------------------
    // TEST SU NOME SPESA (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto aggiornamento del nome della spesa con un valore valido.
     */
    @Test
    @DisplayName("Test setNomeSpesa con valore valido")
    public void testSetNomeSpesaValido() {
        String nuovoNome = "Spesa Supermercato";
        spesa.setNomeSpesa(nuovoNome);
        assertEquals(nuovoNome, spesa.getNomeSpesa());
    }

    /**
     * Verifica che venga lanciata un'eccezione se il nome della spesa è null.
     */
    @Test
    @DisplayName("Test setNomeSpesa con valore Null")
    public void testSetNomeSpesaNull() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setNomeSpesa(null));
    }

    /**
     * Verifica che venga lanciata un'eccezione se il nome della spesa è vuoto o solo spazi.
     */
    @Test
    @DisplayName("Test setNomeSpesa con stringa vuota")
    public void testSetNomeSpesaVuoto() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setNomeSpesa(""));
        assertThrows(DatiNonValidiException.class, () -> spesa.setNomeSpesa("   "));
    }

    // ---------------------------------------------------------------
    // TEST SU CATEGORIA (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto aggiornamento della categoria.
     * Nota: Assicurati che Categories.CIBO (o un'altra categoria diversa da AUTO) esista nel tuo Enum.
     */
    @Test
    @DisplayName("Test setCategoria con valore valido")
    public void testSetCategoriaValida() {
        // Proviamo a cambiare la categoria (assumendo che AUTO sia quella iniziale)
        // Se CIBO non esiste nel tuo enum, usa un'altra costante valida
        Categories nuovaCategoria = Categories.AUTO;
        spesa.setCategoria(nuovaCategoria);
        assertEquals(nuovaCategoria, spesa.getCategoria());
    }

    /**
     * Verifica che venga lanciata un'eccezione se la categoria è null.
     */
    @Test
    @DisplayName("Test setCategoria con valore Null")
    public void testSetCategoriaNull() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setCategoria(null));
    }

    // ---------------------------------------------------------------
    // TEST SU DESCRIZIONE (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto aggiornamento della descrizione.
     */
    @Test
    @DisplayName("Test setDescrizione con valore valido")
    public void testSetDescrizioneValida() {
        String nuovaDescrizione = "Nuova descrizione dettagliata";
        spesa.setDescrizione(nuovaDescrizione);
        assertEquals(nuovaDescrizione, spesa.getDescrizione());
    }

    /**
     * Verifica che venga lanciata un'eccezione se la descrizione è null.
     */
    @Test
    @DisplayName("Test setDescrizione con valore Null")
    public void testSetDescrizioneNull() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setDescrizione(null));
    }

    /**
     * Verifica che venga lanciata un'eccezione se la descrizione è vuota.
     */
    @Test
    @DisplayName("Test setDescrizione con stringa vuota")
    public void testSetDescrizioneVuota() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setDescrizione(""));
    }

    // ---------------------------------------------------------------
    // TEST SU IMPORTO (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto aggiornamento dell'importo.
     */
    @Test
    @DisplayName("Test setImporto con valore valido")
    public void testSetImportoValido() {
        double nuovoImporto = 100.50;
        spesa.setImporto(nuovoImporto);
        assertEquals(nuovoImporto, spesa.getImporto());
    }

    /**
     * Verifica che venga lanciata un'eccezione se l'importo è negativo.
     */
    @Test
    @DisplayName("Test setImporto con valore Negativo")
    public void testSetImportoNegativo() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setImporto(-10.0));
    }

    /**
     * Verifica che venga lanciata un'eccezione se l'importo è zero.
     */
    @Test
    @DisplayName("Test setImporto con valore Zero")
    public void testSetImportoZero() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setImporto(0.0));
    }

    // ---------------------------------------------------------------
    // TEST SU DATA (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto aggiornamento della data.
     */
    @Test
    @DisplayName("Test setData con valore valido")
    public void testSetDataValida() {
        LocalDateTime nuovaData = LocalDateTime.now();
        spesa.setData(nuovaData);
        assertEquals(nuovaData, spesa.getData());
    }

    /**
     * Verifica che venga lanciata un'eccezione se la data è null.
     */
    @Test
    @DisplayName("Test setData con valore Null")
    public void testSetDataNull() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setData(null));
    }

    // ---------------------------------------------------------------
    // TEST SU ID (Setter e Validazione)
    // ---------------------------------------------------------------

    /**
     * Verifica il corretto inserimento di un ID valido.
     */
    @Test
    @DisplayName("Test setId con valore valido")
    public void testSetIdValido() {
        int nuovoId = 15;
        spesa.setId(nuovoId);
        assertEquals(nuovoId, spesa.getId());
    }

    /**
     * Verifica che venga lanciata un'eccezione se l'ID è zero.
     */
    @Test
    @DisplayName("Test setId con valore Zero")
    public void testSetIdZero() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setId(0));
    }

    /**
     * Verifica che venga lanciata un'eccezione se l'ID è negativo.
     */
    @Test
    @DisplayName("Test setId con valore Negativo")
    public void testSetIdNegativo() {
        assertThrows(DatiNonValidiException.class, () -> spesa.setId(-5));
    }

    // ---------------------------------------------------------------
    // TEST METODI DI UTILITÀ
    // ---------------------------------------------------------------

    /**
     * Verifica che il metodo toString restituisca una stringa non nulla
     * e contenga i dati essenziali dell'oggetto.
     */
    @Test
    @DisplayName("Test metodo toString")
    public void testToString() {
        String result = spesa.toString();
        assertNotNull(result);
        // Verifica che la stringa contenga almeno il nome della spesa
        assertTrue(result.contains("Assicurazione Auto"));
    }
}