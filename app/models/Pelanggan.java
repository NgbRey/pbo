package app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representasi pengguna tipe Pelanggan (Customer).
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class Pelanggan extends User {
    private String alamat;
    private String noTelepon;
    private List<Transaksi> historyTransaksi;

    public Pelanggan(String userID, String username, String password, String nama, String alamat, String noTelepon) {
        super(userID, username, password, nama);
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.historyTransaksi = new ArrayList<>();
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public List<Transaksi> getHistoryTransaksi() {
        return historyTransaksi;
    }

    /**
     * Menambahkan transaksi ke dalam riwayat belanja pribadi pelanggan.
     * 
     * @param t Objek transaksi yang baru dilakukan.
     */
    public void addToHistory(Transaksi t) {
        this.historyTransaksi.add(t);
    }

    public String getId() {
        return super.getUserID();
    }

    @Override
    public String getRole() {
        return "PELANGGAN";
    }
}