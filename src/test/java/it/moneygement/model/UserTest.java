package it.moneygement.model;

import it.moneygement.exception.DatiNonValidiException;
import it.moneygement.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite di test per verificare le funzionalità della classe User.
 * <p>
 * Questa classe utilizza JUnit 5 per assicurare che il modello Utente
 * gestisca correttamente i dati, inclusi il costruttore, i getter, i setter
 * e la validazione dei dati in ingresso.
 * </p>
 *
 * @author Stefano Bellan
 */
public class UserTest {

    private User user;

    // Password in chiaro usata per i test
    String passwordInChiaro = "Pippo123!";

    // Genero l'hash della password per simulare il comportamento reale del sistema
    String hash = SecurityUtils.hashPassword(passwordInChiaro);

    /**
     * Configurazione iniziale eseguita prima di ogni singolo test.
     * <p>
     * Questo metodo inizializza un oggetto User "pulito" con dati validi per garantire
     * che ogni test parta da uno stato noto e indipendente dagli altri.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        // Istanzio un nuovo utente valido prima di ogni test
        user = new User("Mario", "Rossi", hash, "mario@example.com", 25);
    }

    /**
     * Test per verificare la corretta creazione dell'oggetto User.
     * <p>
     * Controlla che tutti i campi passati al costruttore siano stati
     * assegnati correttamente alle proprietà dell'oggetto tramite i getter.
     * </p>
     */
    @Test
    @DisplayName("Test Creazione Utente Valido")
    void testCreazioneUtenteValido() {
        // Verifico che il nome restituito sia quello inserito nel costruttore
        assertEquals("Mario", user.getNome(), "Il nome dovrebbe corrispondere a Mario");

        // Verifico che il cognome restituito sia corretto
        assertEquals("Rossi", user.getCognome(), "Il cognome dovrebbe corrispondere a Rossi");

        // Verifico che la password salvata sia l'hash e non quella in chiaro
        assertEquals(hash, user.getPasswordHash(), "La password (hash) dovrebbe corrispondere");

        // Verifico che l'email sia stata salvata correttamente
        assertEquals("mario@example.com", user.getEmail(), "L'email dovrebbe corrispondere");

        // Verifico che l'età sia corretta
        assertEquals(25, user.getEta(), "L'età dovrebbe corrispondere a 25");
    }

    /**
     * Test per verificare il funzionamento dei setter per i dati anagrafici.
     */
    @Test
    @DisplayName("Test Setter dell'anagrafica")
    public void testSetAnagrafica() {
        // Modifico il nome e il cognome dell'oggetto esistente
        user.setNome("Luigi");
        user.setCognome("Bianchi");

        // Verifico che il getter restituisca i nuovi valori aggiornati
        assertEquals("Luigi", user.getNome(), "I nomi dovrebbero coincidere dopo la modifica");
        assertEquals("Bianchi", user.getCognome(), "I cognomi dovrebbero coincidere dopo la modifica");
    }

    /**
     * Test per verificare il funzionamento del setter dell'email.
     */
    @Test
    @DisplayName("Test Setter Email")
    public void testSetEmail() {
        // Imposto un nuovo indirizzo email valido
        user.setEmail("luigi.bianchi@example.com");

        // Controllo che il valore sia stato aggiornato correttamente
        assertEquals("luigi.bianchi@example.com", user.getEmail(), "Le email dovrebbero coincidere dopo la modifica");
    }

    /**
     * Test per verificare il funzionamento del setter della password (hash).
     */
    @Test
    @DisplayName("Test Setter della password")
    public void testSetPassword() {
        // Creo una nuova password e genero il relativo hash
        String pwdInChiaro = "Minnie123!";
        String nuovoHash = SecurityUtils.hashPassword(pwdInChiaro);

        // Aggiorno l'hash della password nell'oggetto utente
        user.setPasswordHash(nuovoHash);

        // Verifico che l'hash salvato corrisponda a quello nuovo
        assertEquals(nuovoHash, user.getPasswordHash(), "L'hash della password dovrebbe essere aggiornato");
    }

    /**
     * Test per verificare il funzionamento del setter dell'età.
     */
    @Test
    @DisplayName("Test Setter dell'età")
    public void testSetEta() {
        // Definisco una nuova età valida
        int nuovaEta = 18;

        // Aggiorno l'età dell'utente
        user.setEta(nuovaEta);

        // Verifico che l'età sia stata aggiornata
        assertEquals(nuovaEta, user.getEta(), "L'età dovrebbe essere aggiornata a 18");
    }

    /**
     * Test per verificare il funzionamento del setter dell'ID.
     * Questo è utile per testare l'assegnazione dell'ID che avverrà tramite Database.
     */
    @Test
    @DisplayName("Test Setter dell'id")
    public void testSetId() {
        // Simulo l'assegnazione di un ID (es. dopo il salvataggio su DB)
        int id = 2;
        user.setId(id);

        // Verifico che l'ID sia stato impostato correttamente
        assertEquals(id, user.getId(), "L'ID dovrebbe corrispondere a quello impostato");
    }

    /**
     * Verifica che il costruttore lanci un'eccezione se l'email non è valida.
     */
    @Test
    @DisplayName("Test Eccezione Email Non Valida")
    void testEmailNonValida() {
        // Mi aspetto che venga lanciata DatiNonValidiException se l'email non ha il formato corretto
        assertThrows(DatiNonValidiException.class, () -> {
            new User("Mario", "Rossi", "hash123", "email_sbagliata", 25);
        });
    }

    /**
     * Verifica che il costruttore lanci un'eccezione se l'età è inferiore al minimo consentito.
     */
    @Test
    @DisplayName("Test Eccezione Età Non Valida")
    void testEtaNonValida() {
        // Mi aspetto che venga lanciata DatiNonValidiException se l'età è troppo bassa (es. 10 anni)
        assertThrows(DatiNonValidiException.class, () -> {
            new User("Mario", "Rossi", "hash123", "mario@example.com", 10);
        });
    }

    /**
     * Verifica che il costruttore lanci un'eccezione se la password (hash) è vuota.
     */
    @Test
    @DisplayName("Test Eccezione Password Vuota")
    void testPasswordNonValida() {
        // Mi aspetto che venga lanciata DatiNonValidiException se la stringa hash è vuota
        assertThrows(DatiNonValidiException.class, () -> {
            new User("Mario", "Rossi", "", "mario@example.com", 18);
        });
    }

    /**
     * Verifica che il costruttore lanci un'eccezione se i campi anagrafici (nome/cognome) sono vuoti.
     */
    @Test
    @DisplayName("Test Eccezione Anagrafica Vuota")
    void testAnagraficaNonValida() {
        // Mi aspetto che venga lanciata DatiNonValidiException se nome e cognome sono stringhe vuote
        assertThrows(DatiNonValidiException.class, () -> {
            new User("", "", "hash123", "mario@example.com", 18);
        });
    }
}