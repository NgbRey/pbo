package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class KartuKredit implements Pembayaran {
    private String namaPemilik;
    private String noKartu;

    public KartuKredit(String namaPemilik, String noKartu) {
        this.namaPemilik = namaPemilik;
        this.noKartu = noKartu;
    }

    /**
     * Memproses logika pembayaran simulasi.
     * 
     * @param amount Total tagihan.
     * @return true (selalu berhasil dalam simulasi ini).
     */
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
