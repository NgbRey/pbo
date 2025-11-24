package app.pembayaran;

/**
 * Interface untuk strategi pembayaran.
 * Mendefinisikan kontrak metode yang harus dimiliki oleh setiap jenis pembayaran.
 */

public interface Pembayaran {
    boolean prosesPembayaran(double amount);
    String getNamaMetode();
}