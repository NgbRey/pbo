package app.pembayaran;

/**
 * Interface strategi untuk berbagai metode pembayaran.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public interface Pembayaran {

    /**
     * Memproses pembayaran sejumlah nilai tertentu.
     *
     * @param amount Jumlah uang yang harus dibayar.
     * @return true jika pembayaran berhasil, false jika gagal.
     */
    boolean prosesPembayaran(double amount);

    /**
     * Mengambil nama metode pembayaran untuk keperluan display.
     * 
     * @return Nama metode (contoh: "Transfer Bank", "QRIS").
     */                                               
    String getNamaMetode();
}