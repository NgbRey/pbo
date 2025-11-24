package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class EWallet implements Pembayaran {
    private String provider;
    private String idAkun;

    public EWallet(String provider, String idAkun) {
        this.provider = provider;
        this.idAkun = idAkun;
    }

    /**
     * Memproses logika pembayaran simulasi.
     * 
     * @param amount Total tagihan.
     * @return true (selalu berhasil dalam simulasi ini).
     */
    @Override
    public boolean prosesPembayaran(double amount) {
        System.out.println("Memproses pembayaran e-wallet " + provider + " id " + idAkun + " sebesar " + amount);
        System.out.println("Pembayaran e-wallet sukses.");
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "E-Wallet (" + provider + ")";
    }
}
