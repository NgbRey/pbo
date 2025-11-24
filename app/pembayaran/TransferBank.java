package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class TransferBank implements Pembayaran {
    private String namaBank;
    private long noRek;

    public TransferBank(String namaBank, long noRek) {
        this.namaBank = namaBank;
        this.noRek = noRek;
    }

    /**
     * Memproses logika pembayaran simulasi.
     * 
     * @param amount Total tagihan.
     * @return true (selalu berhasil dalam simulasi ini).
     */
    @Override
    public boolean prosesPembayaran(double amount) {
        System.out.println("Proses transfer ke " + namaBank + " no rek " + noRek + " sebesar " + amount);
        System.out.println("Transfer berhasil");
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "Transfer Bank (" + namaBank + ") - Rek: " + noRek;
    }
}