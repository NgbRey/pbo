package app.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Kelas yang mengelola item belanjaan sementara sebelum checkout.
 * Menggunakan HashMap untuk menyimpan produk dan kuantitasnya.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public class Keranjang {
    // Produk -> qty
    private HashMap<ProdukElektronik, Integer> items = new HashMap<>();

    /**
     * Menambahkan produk ke dalam keranjang.
     * Jika produk sudah ada, kuantitas akan ditambahkan.
     *
     * @param p   Produk yang akan dibeli.
     * @param qty Jumlah yang ingin dibeli.
     */
    public void addItem(ProdukElektronik p, int qty) {
        items.put(p, items.getOrDefault(p, 0) + qty);
    }

    public void removeItem(ProdukElektronik p) {
        items.remove(p);
    }

    public HashMap<ProdukElektronik, Integer> getItems() {
        return items;
    }

    /**
     * Menghitung total harga seluruh item di dalam keranjang.
     * 
     * @return Total harga dalam double.
     */
    public double getTotalHarga() {
        double total = 0.0;
        for (Map.Entry<ProdukElektronik, Integer> e : items.entrySet()) {
            total += e.getKey().getHargaJual() * e.getValue();
        }
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}