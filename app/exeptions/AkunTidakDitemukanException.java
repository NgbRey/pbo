package app.exceptions;

/**
 * Exception kustom yang dilempar saat proses login gagal
 * (Username tidak ada atau password salah).
 */

public class AkunTidakDitemukanException extends Exception {
    public AkunTidakDitemukanException(String message) {
        super(message);
    }
}
