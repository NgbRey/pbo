package app.models;

import java.util.List;
import app.models.ProdukElektronik;
import app.models.Transaksi;
import app.DataStore;

/**
 * Representasi pengguna dengan hak akses Administrator.
 * Memiliki wewenang mengelola data produk dan transaksi.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
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

    /**
     * Menambahkan produk baru ke inventaris toko.
     * 
     * @param p Objek produk yang akan ditambahkan.
     */
    public void tambahProduk(ProdukElektronik p) {
        DataStore.getListProduk().add(p);
    }

    /**
     * Menghapus produk dari inventaris berdasarkan ID.
     * 
     * @param idProduk ID produk yang akan dihapus.
     * @return true jika berhasil dihapus, false jika ID tidak ditemukan.
     */
    public boolean hapusProduk(String idProduk) {
        return DataStore.getListProduk().removeIf(p -> p.getIdProduk().equals(idProduk));
    }

    public ProdukElektronik cariProdukById(String id) {
        return DataStore.getListProduk().stream().filter(p -> p.getIdProduk().equals(id)).findFirst().orElse(null);
    }

    public List<Transaksi> lihatTransaksi() {
        return DataStore.getListTransaksi();
    }

    /**
     * Menerima transaksi yang berstatus PENDING dan mengubahnya menjadi DITERIMA.
     * Proses ini juga akan mengurangi stok barang secara otomatis.
     *
     * @param idTransaksi ID transaksi yang akan diproses.
     * @return true jika transaksi ditemukan dan berhasil diproses.
     */
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