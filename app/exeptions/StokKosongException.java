package app.exceptions;

/**
 * Exception kustom yang dilempar saat pelanggan mencoba membeli barang
 * melebihi stok yang tersedia.
 */

public class StokKosongException extends Exception {
    public StokKosongException(String message) {
        super(message);
    }
}