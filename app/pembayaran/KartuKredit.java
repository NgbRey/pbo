package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 * Menangani logika pemrosesan pembayaran dan format nama metode.
 */

public class KartuKredit implements Pembayaran {
    private String namaPemilik;
    private String noKartu;

    public KartuKredit(String namaPemilik, String noKartu) {
        this.namaPemilik = namaPemilik;
        this.noKartu = noKartu;
    }

    @Override
    public boolean prosesPembayaran(double amount) {
        System.out.println("Memproses pembayaran kartu kredit " + noKartu + " sebesar " + amount);
        System.out.println("Pembayaran kartu sukses (simulasi).");
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "Kartu Kredit";
    }
}
