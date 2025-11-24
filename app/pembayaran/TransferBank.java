package app.pembayaran;

public class TransferBank implements Pembayaran {
    private String namaBank;
    private long noRek;

    public TransferBank(String namaBank, long noRek) {
        this.namaBank = namaBank;
        this.noRek = noRek;
    }

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