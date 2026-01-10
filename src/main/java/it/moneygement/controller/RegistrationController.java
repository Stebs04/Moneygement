package it.moneygement.controller;

import it.moneygement.exception.DatiNonValidiException;
import it.moneygement.exception.UserAlreadyExistException;
import it.moneygement.service.MoneygementService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {

    // --- VARIABILI FXML ---
    // Queste variabili si collegheranno alle caselle che disegnerai in SceneBuilder.
    // Assicurati che gli fx:id nel file FXML coincidano con questi nomi!
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField pwdField;
    @FXML private TextField ageField; // Ho corretto il nome in CamelCase (era agefield)

    @FXML private Label statusLabel;
    @FXML private Button submitButton;

    // Riferimento al "Cameriere" che porta l'ordine allo Chef
    private MoneygementService service;

    /**
     * Metodo di inizializzazione.
     * Viene chiamato automaticamente da JavaFX appena la finestra si apre.
     */
    @FXML
    public void initialize() {
        // Prendiamo l'istanza del Service (Singleton)
        this.service = MoneygementService.getInstance();
    }

    /**
     * Gestisce il click sul bottone "Registrati".
     * Esegue tutti i controlli (validazione) prima di provare a creare l'utente.
     */
    @FXML
    private void handleRegistration() {
        // 1. RECUPERO DATI GREZZI (RAW DATA)
        // Leggiamo tutto come stringhe, così non rischiamo crash immediati.
        String nome = nameField.getText();
        String cognome = surnameField.getText();
        String email = emailField.getText();
        String password = pwdField.getText();
        String etaString = ageField.getText();

        // 2. VALIDAZIONE CAMPI VUOTI (EMPTY CHECK)
        // Dobbiamo farlo SUBITO. Se provassimo a convertire l'età prima di questo controllo,
        // e l'età fosse vuota, il programma crasherebbe.
        if (nome.isBlank() || cognome.isBlank() || email.isBlank() ||
                password.isBlank() || etaString.isBlank()) {

            statusLabel.setText("Tutti i campi sono obbligatori!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return; // STOP! Non andiamo oltre.
        }

        // 3. VALIDAZIONE NUMERICA (PARSING)
        // Ora sappiamo che 'etaString' non è vuota. Ma è un numero?
        // Se l'utente ha scritto "venti", Integer.parseInt lancerà un'eccezione.
        int eta;
        try {
            eta = Integer.parseInt(etaString);
        } catch (NumberFormatException e) {
            // Se siamo qui, l'utente ha scritto del testo nel campo età.
            statusLabel.setText("L'età deve essere un numero valido!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return; // STOP!
        }

        // 4. LOGICA DI BUSINESS (IL SERVIZIO)
        // Se siamo arrivati qui, abbiamo tutti i dati pronti e corretti formalmente.
        // Possiamo chiamare lo Chef.
        try {
            // Chiamiamo il metodo del service.
            // Lui farà controlli più approfonditi (es. età < 14, email duplicata, password debole)
            service.registerUser(nome, cognome, email, password, eta);

            // 5. SUCCESSO!
            // Se il service non lancia eccezioni, è andato tutto bene.
            statusLabel.setText("Registrazione completata con successo!");
            statusLabel.setStyle("-fx-text-fill: green;");

            // TODO: In futuro qui useremo SceneManager per mandare l'utente al Login.
            System.out.println("Utente registrato: " + email);

        } catch (UserAlreadyExistException e) {
            // Caso: Email già presente nel DB
            statusLabel.setText("Errore: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");

        } catch (DatiNonValidiException  e) {
            // Caso: Password troppo corta, Età < 14, Email non valida, ecc.
            // Nota: Catturiamo sia la nostra eccezione che IllegalArgumentException (di SecurityUtils)
            statusLabel.setText("Dati non validi: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}