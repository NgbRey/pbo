package app.pembayaran;

/**
 * Implementasi pembayaran placeholder.
 * Digunakan saat memuat data transaksi lama dari file teks
 * di mana logika pembayaran real-time tidak lagi diperlukan.
 */

public class PembayaranDummy implements Pembayaran {
    private String namaMetode;

    public PembayaranDummy(String namaMetode) {
        this.namaMetode = namaMetode;
    }

    @Override
    public boolean prosesPembayaran(double amount) {
        return true; // Sudah dibayar sebelumnya
    }

    @Override
    public String getNamaMetode() {
        return namaMetode;
    }
}