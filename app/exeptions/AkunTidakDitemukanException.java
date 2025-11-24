package app.exceptions;

/**
 * Custom Exception yang dilempar saat login gagal karena username tidak
 * ditemukan
 * atau password tidak cocok.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class AkunTidakDitemukanException extends Exception {
    public AkunTidakDitemukanException(String message) {
        super(message);
    }
}
