package app.pembayaran;

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
