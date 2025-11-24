package app.models;

import java.util.List;
import app.models.ProdukElektronik;
import app.models.Transaksi;
import app.DataStore;

/**
 * Merepresentasikan pengguna dengan hak akses Administrator.
 * Memiliki kemampuan untuk mengelola produk dan memvalidasi transaksi.
 */

public class Admin extends User {

    public Admin(String userID, String username, String password, String nama) {
        super(userID, username, password, nama);
    }

    public String getId() {
        return super.getUserID();
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // Admin operations (simple implementations interacting with DataStore)
    public void tambahProduk(ProdukElektronik p) {
        DataStore.getListProduk().add(p);
    }

    public boolean hapusProduk(String idProduk) {
        return DataStore.getListProduk().removeIf(p -> p.getIdProduk().equals(idProduk));
    }

    public ProdukElektronik cariProdukById(String id) {
        return DataStore.getListProduk().stream().filter(p -> p.getIdProduk().equals(id)).findFirst().orElse(null);
    }

    public List<Transaksi> lihatTransaksi() {
        return DataStore.getListTransaksi();
    }

    public boolean terimaTransaksi(String idTransaksi) {
        for (Transaksi t : DataStore.getListTransaksi()) {
            if (t.getIdTransaksi().equals(idTransaksi) && t.getStatus().equals("PENDING")) {
                t.setStatus("DITERIMA");
                // kurangi stok setiap item
                t.reduceStock();
                return true;
            }
        }
        return false;
    }
}