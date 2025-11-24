package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class COD implements Pembayaran {
    private String alamatPengiriman;
    private String kurir;

    public COD(String alamatPengiriman, String kurir) {
        this.alamatPengiriman = alamatPengiriman;
        this.kurir = kurir;
    }

    /**
     * Memproses logika pembayaran simulasi.
     * 
     * @param amount Total tagihan.
     * @return true (selalu berhasil dalam simulasi ini).
     */
    @Override
    public boolean prosesPembayaran(double amount) {
        System.out.println("Memproses pesanan COD...");
        System.out.println("Alamat pengiriman: " + alamatPengiriman);
        System.out.println("Kurir: " + kurir);
        System.out.println("Total yang harus dibayar saat barang diterima: " + amount);
        System.out.println("Pesanan COD berhasil dibuat");
        // COD selalu berhasil karena pembayaran dilakukan nanti
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "COD (" + kurir + ")";
    }
}