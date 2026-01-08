package it.moneygement.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Classe di utilità per la gestione della sicurezza e della crittografia.
 * <p>
 * Questa classe fornisce metodi statici per operazioni comuni come l'hashing
 * delle password, evitando la duplicazione di codice sensibile in giro per l'applicazione.
 * </p>
 *
 * @author Stefano Bellan
 * @version 1.0
 */
public class SecurityUtils {

    /**
     * Costruttore privato per nascondere quello pubblico implicito.
     * <p>
     * Essendo una classe di utilità ("Utility Class"), tutti i suoi metodi sono statici
     * e non dovrebbe mai essere istanziata (non faremo mai "new SecurityUtils()").
     * </p>
     */
    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Esegue l'hashing di una password in chiaro utilizzando l'algoritmo SHA-256.
     * <p>
     * L'algoritmo SHA-256 produce un output irreversibile (digest) di 256 bit.
     * Questo metodo converte il risultato in una stringa esadecimale leggibile.
     * </p>
     *
     * @param password La password in chiaro da criptare (es. "MiaPassword123").
     * @return Una stringa contenente la rappresentazione esadecimale dell'hash (lunghezza 64 caratteri).
     * @throws RuntimeException Se l'algoritmo SHA-256 non è disponibile nell'ambiente Java corrente.
     */
    public static String hashPassword(String password) {
        try {
            // Otteniamo un'istanza del motore di digest per l'algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 1. Convertiamo la stringa della password in un array di byte.
            // Usiamo UTF_8 per garantire che venga trattata allo stesso modo su Windows, Mac e Linux.
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // 2. Eseguiamo l'hashing effettivo.
            // Il risultato è un array di byte illeggibile (es. [12, -45, 0, 112...]).
            byte[] encodedhash = digest.digest(passwordBytes);

            // 3. Convertiamo l'array di byte in una stringa esadecimale leggibile.
            return HexFormat.of().formatHex(encodedhash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore critico: Algoritmo SHA-256 non trovato.", e);
        }
    }
}