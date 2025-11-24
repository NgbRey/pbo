package app.exceptions;

/**
 * Custom Exception yang dilempar saat user mencoba membeli barang melebihi
 * jumlah stok yang tersedia.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class StokKosongException extends Exception {
    public StokKosongException(String message) {
        super(message);
    }
}