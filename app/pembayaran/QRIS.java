package app.pembayaran;

/**
 * Implementasi metode pembayaran spesifik.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class QRIS implements Pembayaran {
    private String merchantName;
    private String qrisId;

    public QRIS() {
        this.merchantName = "DIGITALA STORE";
        this.qrisId = "QRIS-DIGITALA-001";
    }

    /**
     * Memproses logika pembayaran simulasi.
     * 
     * @param amount Total tagihan.
     * @return true (selalu berhasil dalam simulasi ini).
     */
    @Override
    public boolean prosesPembayaran(double amount) {
        System.out.println("Memproses pembayaran QRIS...");
        System.out.println("Merchant: " + merchantName);
        System.out.println("QRIS ID: " + qrisId);
        System.out.println("Amount: Rp " + amount);
        System.out.println("Silakan scan QR code untuk melakukan pembayaran");
        System.out.println("Pembayaran QRIS berhasil");
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "QRIS";
    }
}