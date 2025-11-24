# Digitala Store

**Digitala Store** adalah aplikasi berbasis Java (mendukung CLI dan GUI) untuk manajemen toko elektronik. Aplikasi ini menjembatani interaksi antara Admin (pengelola toko) dan Pelanggan (pembeli).

##  Fitur

### 1. Admin
* **Manajemen Produk:** Tambah, Edit, Hapus, dan Lihat daftar produk elektronik.
* **Manajemen Transaksi:** Melihat transaksi status 'PENDING' dan melakukan konfirmasi (Terima Transaksi).
* **Laporan:** Melihat riwayat barang yang terjual.
* **Manajemen User:** Menambah Admin baru dan menghapus User.

### 2. Pelanggan
* **Katalog:** Melihat daftar produk beserta spesifikasi dan stok.
* **Keranjang Belanja:** Menambah barang ke keranjang dan melihat total harga.
* **Checkout & Pembayaran:** Melakukan pemesanan dengan berbagai metode pembayaran (Transfer Bank, COD, E-Wallet, Kartu Kredit, QRIS).
* **Riwayat:** Melihat riwayat pembelian status pesanan.

### 3. Sistem
* **Persistensi Data:** Data akun, produk, dan transaksi disimpan secara permanen dalam file `.txt` (Auto-save & Load).
* **GUI & CLI:** Tersedia dalam dua mode antarmuka: Command Line Interface dan Graphical User Interface (Swing).

##  How to Use

Pastikan Anda memiliki JDK (Java Development Kit) terinstal.

### Menjalankan Mode GUI (Disarankan)
Jalankan file `app/MainAppGUI.java`.
```bash
javac app/MainAppGUI.java
java app.MainAppGUI
