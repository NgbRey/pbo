package app.pembayaran;

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