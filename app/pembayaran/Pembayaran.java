package app.pembayaran;

public interface Pembayaran {
    boolean prosesPembayaran(double amount);
    String getNamaMetode();
}