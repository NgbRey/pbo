package app;

import java.util.Scanner;
import java.util.UUID;
import java.io.File;
import java.util.Map;

import app.models.*;
import app.pembayaran.*;
import app.exceptions.*;

public class MainApp {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // LOAD DATA SAAT PROGRAM START
        DataStoreSaver.loadAkun();
        DataStoreSaver.loadProduk();
        DataStoreSaver.loadTransaksi();
        boolean running = true;

        while (running) {
            System.out.println("=== SELAMAT DATANG DI DIGITALA ===");
            System.out.println("1. Registrasi User");
            System.out.println("2. Login");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            String pilih = sc.nextLine().trim();

            switch (pilih) {
                case "1":
                    registerPelanggan();
                    break;

                case "2":
                    try {
                        User user = prosesLogin();
                        if (user instanceof Admin) {
                            menuAdmin((Admin) user);
                        } else if (user instanceof Pelanggan) {
                            menuPelanggan((Pelanggan) user);
                        }
                    } catch (AkunTidakDitemukanException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;

                case "3":
                    DataStoreSaver.saveAkun();
                    DataStoreSaver.saveProduk();
                    DataStoreSaver.saveTransaksi(); // ← TAMBAHKAN INI
                    running = false;
                    System.out.println("Data disimpan. Terima kasih sudah menggunakan DigitalA.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static User prosesLogin() throws AkunTidakDitemukanException {
        System.out.print("Username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();

        User user = DataStore.findUserByUsername(u);
        if (user == null)
            throw new AkunTidakDitemukanException("Username tidak ditemukan.");
        if (!user.checkPassword(p))
            throw new AkunTidakDitemukanException("Password salah.");
        System.out.println("Login berhasil. Halo, " + user.getNama());
        return user;
    }

    // --- MENU ADMIN ---
    private static void menuAdmin(Admin admin) {
        boolean keluar = false;
        while (!keluar) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Produk");
            System.out.println("2. Tambah Produk");
            System.out.println("3. Edit Produk"); // ← TAMBAH INI
            System.out.println("4. Hapus Produk"); // ← NOMOR BERUBAH (dulu 3)
            System.out.println("5. Lihat Transaksi (PENDING)"); // ← NOMOR BERUBAH (dulu 4)
            System.out.println("6. Terima Transaksi"); // ← NOMOR BERUBAH (dulu 5)
            System.out.println("7. Lihat Barang Terjual"); // ← NOMOR BERUBAH (dulu 6)
            System.out.println("8. Tambah Admin Baru"); // ← NOMOR BERUBAH (dulu 7)
            System.out.println("9. Hapus User"); // ← NOMOR BERUBAH (dulu 8)
            System.out.println("10. Logout"); // ← NOMOR BERUBAH (dulu 9)
            System.out.print("Pilih: ");
            String m = sc.nextLine().trim();

            switch (m) {
                case "1":
                    listProduk();
                    break;

                case "2":
                    try {
                        tambahProdukFlow(admin);
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "\n❌ Terjadi kesalahan input! Harap masukkan angka yang valid untuk harga dan stok.");
                    } catch (Exception e) {
                        System.out.println("\n❌ Terjadi kesalahan tak terduga saat menambahkan produk.");
                    }
                    break;

                case "3": // ← CASE BARU: EDIT PRODUK
                    editProdukFlow(admin);
                    break;

                case "4": // ← HAPUS PRODUK (dulu case 3)
                    System.out.print("Masukkan ID produk yang ingin dihapus: ");
                    String idHapus = sc.nextLine().trim();
                    boolean ok = admin.hapusProduk(idHapus);
                    System.out.println(ok ? "Produk dihapus." : "Produk tidak ditemukan.");
                    if (ok)
                        DataStoreSaver.saveProduk();
                    break;

                case "5": // ← LIHAT TRANSAKSI PENDING (dulu case 4)
                    System.out.println("=== TRANSAKSI PENDING ===");
                    boolean adaPending = false;
                    for (Transaksi t : DataStore.getListTransaksi()) {
                        if (t.getStatus().equalsIgnoreCase("PENDING")) {
                            adaPending = true;
                            System.out.println(t);
                        }
                    }
                    if (!adaPending) {
                        System.out.println("Belum ada transaksi yang pending.");
                    }
                    break;

                case "6": // ← TERIMA TRANSAKSI (dulu case 5)
                    System.out.print("Masukkan ID Transaksi untuk diterima: ");
                    String idTr = sc.nextLine().trim();
                    boolean diterima = admin.terimaTransaksi(idTr);
                    System.out.println(diterima ? "Transaksi diterima dan invoice tercetak."
                            : "Transaksi tidak ditemukan / sudah diproses.");
                    if (diterima) {
                        Transaksi t = DataStore.getListTransaksi().stream()
                                .filter(x -> x.getIdTransaksi().equals(idTr))
                                .findFirst()
                                .orElse(null);
                        if (t != null) {
                            t.printInvoice();
                            DataStoreSaver.saveTransaksi();
                            DataStoreSaver.saveProduk();
                        }
                    }
                    break;

                case "7": // LIHAT BARANG TERJUAL
                    System.out.println("=== Barang Terjual ===");
                    boolean adaTerjual = false;

                    for (Transaksi t : DataStore.getListTransaksi()) {
                        if (!t.getStatus().equals("PENDING")) {
                            for (Map.Entry<ProdukElektronik, Integer> e : t.getItems().entrySet()) {
                                System.out.printf("| %-22s | %-5d |%n",
                                        e.getKey().getNama(), e.getValue());
                                adaTerjual = true;
                            }
                        }
                    }

                    if (!adaTerjual) {
                        System.out.println("Belum ada barang yang terjual.");
                    }

                    // SAVE DATA KE FILE TXT
                    DataStoreSaver.saveBarangTerjual();
                    System.out.println(">> Data barang terjual telah disimpan ke 'barang_terjual.txt'");

                    break;

                case "8": // ← TAMBAH ADMIN BARU
                    System.out.println("=== TAMBAH ADMIN BARU ===");

                    // VALIDASI NAMA
                    String namaAdmin = "";
                    while (namaAdmin.isEmpty()) {
                        System.out.print("Nama: ");
                        namaAdmin = sc.nextLine().trim();
                        if (namaAdmin.isEmpty()) {
                            System.out.println("❌ Nama tidak boleh kosong! Silakan coba lagi.");
                        }
                    }

                    // VALIDASI USERNAME (tidak boleh kosong dan harus unik)
                    String username = "";
                    boolean usernameValid = false;
                    while (!usernameValid) {
                        System.out.print("Username: ");
                        username = sc.nextLine().trim();

                        if (username.isEmpty()) {
                            System.out.println("❌ Username tidak boleh kosong! Silakan coba lagi.");
                        } else if (DataStore.findUserByUsername(username) != null) {
                            System.out.println("❌ Username sudah digunakan! Coba yang lain.");
                        } else {
                            usernameValid = true;
                        }
                    }

                    // VALIDASI PASSWORD
                    String pass = "";
                    while (pass.isEmpty()) {
                        System.out.print("Password: ");
                        pass = sc.nextLine().trim();
                        if (pass.isEmpty()) {
                            System.out.println("❌ Password tidak boleh kosong! Silakan coba lagi.");
                        }
                    }

                    String idAdmin = "A" + String.format("%03d", DataStore.getListAkun().size() + 1);
                    DataStore.getListAkun().add(new Admin(idAdmin, username, pass, namaAdmin));
                    DataStoreSaver.saveAkun();
                    System.out.println("✅ Admin berhasil ditambahkan.");
                    break;

                case "9": // ← HAPUS USER
                    System.out.println("============================= DAFTAR USER =============================");
                    System.out.printf("| %-5s | %-12s | %-18s | %-15s |%n", "No", "ID User", "Username", "Nama");
                    System.out.println("------------------------------------------------------------------------");

                    int no = 1;
                    boolean adaUser = false;

                    for (User a : DataStore.getListAkun()) {
                        if (a instanceof Pelanggan) {
                            Pelanggan p = (Pelanggan) a;
                            System.out.printf(
                                    "| %-5d | %-12s | %-18s | %-15s |%n",
                                    no++, p.getId(), p.getUsername(), p.getNama());
                            adaUser = true;
                        }
                    }

                    System.out.println("------------------------------------------------------------------------");

                    if (!adaUser) {
                        System.out.println("❌ Tidak ada user yang bisa dihapus.");
                        break;
                    }

                    // VALIDASI INPUT USERNAME
                    String userHapus = "";
                    boolean usernameDitemukan = false;

                    while (!usernameDitemukan) {
                        System.out.print("Username user yang ingin dihapus: ");
                        userHapus = sc.nextLine().trim();

                        if (userHapus.isEmpty()) {
                            System.out.println("❌ Username tidak boleh kosong! Silakan coba lagi.");
                            continue;
                        }

                        // Cek apakah username ada
                        for (User a : DataStore.getListAkun()) {
                            if (a instanceof Pelanggan && a.getUsername().equalsIgnoreCase(userHapus)) {
                                usernameDitemukan = true;
                                break;
                            }
                        }

                        if (!usernameDitemukan) {
                            System.out.println(
                                    "❌ User dengan username '" + userHapus + "' tidak ditemukan! Silakan coba lagi.");
                        }
                    }

                    // KONFIRMASI HAPUS
                    System.out.print("⚠️  Yakin ingin menghapus user '" + userHapus + "'? (y/n): ");
                    String konfirmasi = sc.nextLine().trim();

                    if (!konfirmasi.equalsIgnoreCase("y") && !konfirmasi.equalsIgnoreCase("Y")) {
                        System.out.println("✅ Penghapusan dibatalkan.");
                        break;
                    }

                    // PROSES HAPUS
                    User target = null;
                    for (User a : DataStore.getListAkun()) {
                        if (a instanceof Pelanggan && a.getUsername().equalsIgnoreCase(userHapus)) {
                            target = a;
                            break;
                        }
                    }

                    if (target != null) {
                        DataStore.getListAkun().remove(target);
                        DataStoreSaver.saveAkun();
                        System.out.println("✅ User '" + userHapus + "' berhasil dihapus.");
                    } else {
                        System.out.println("❌ User tidak ditemukan.");
                    }
                    break;

                case "10": // ← LOGOUT (dulu case 9)
                    keluar = true;
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void tambahProdukFlow(Admin admin) {
        System.out.println("\n=== TAMBAH PRODUK BARU ===");

        // VALIDASI ID PRODUK
        String id = "";
        while (id.isEmpty()) {
            System.out.print("ID Produk: ");
            id = sc.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("❌ ID Produk tidak boleh kosong!");
            } else if (DataStore.findProdukById(id) != null) {
                System.out.println("❌ ID Produk sudah digunakan! Silakan gunakan ID lain.");
                id = ""; // Reset untuk meminta input ulang
            }
        }

        // VALIDASI NAMA PRODUK
        String nama = "";
        while (nama.isEmpty()) {
            System.out.print("Nama Produk: ");
            nama = sc.nextLine().trim();
            if (nama.isEmpty()) {
                System.out.println("❌ Nama Produk tidak boleh kosong!");
            }
        }

        // VALIDASI DESKRIPSI
        String deskripsi = "";
        while (deskripsi.isEmpty()) {
            System.out.print("Deskripsi: ");
            deskripsi = sc.nextLine().trim();
            if (deskripsi.isEmpty()) {
                System.out.println("❌ Deskripsi tidak boleh kosong!");
            }
        }

        // VALIDASI HARGA BELI
        double hb = 0;
        while (hb <= 0) {
            System.out.print("Harga Beli: ");
            try {
                hb = Double.parseDouble(sc.nextLine().trim());
                if (hb <= 0) {
                    System.out.println("❌ Harga Beli harus lebih dari 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Harap masukkan angka yang valid untuk Harga Beli!");
            }
        }

        // VALIDASI HARGA JUAL
        double hj = 0;
        while (hj <= 0) {
            System.out.print("Harga Jual: ");
            try {
                hj = Double.parseDouble(sc.nextLine().trim());
                if (hj <= 0) {
                    System.out.println("❌ Harga Jual harus lebih dari 0!");
                } else if (hj < hb) {
                    System.out.println("❌ Harga Jual tidak boleh lebih kecil dari Harga Beli!");
                    hj = 0; // Reset untuk meminta input ulang
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Harap masukkan angka yang valid untuk Harga Jual!");
            }
        }

        // VALIDASI STOK
        int stok = -1;
        while (stok < 0) {
            System.out.print("Stok: ");
            try {
                stok = Integer.parseInt(sc.nextLine().trim());
                if (stok < 0) {
                    System.out.println("❌ Stok tidak boleh negatif!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Harap masukkan angka yang valid untuk Stok!");
            }
        }

        ProdukGeneric produkBaru = new ProdukGeneric(id, nama, hb, hj, stok, deskripsi);
        admin.tambahProduk(produkBaru);

        System.out.println("\n✅ Produk berhasil ditambahkan!");
        DataStoreSaver.saveProduk();
    }

    /**
     * Method untuk mengedit produk yang sudah ada
     * Admin dapat mengubah nama, harga beli, harga jual, stok, dan deskripsi produk
     * 
     * @param admin Admin yang melakukan edit
     */
    private static void editProdukFlow(Admin admin) {
        System.out.println("\n=== EDIT PRODUK ===");

        // Tampilkan daftar produk
        listProduk();

        System.out.print("\nMasukkan ID Produk yang ingin diedit: ");
        String id = sc.nextLine().trim();

        ProdukElektronik produk = DataStore.findProdukById(id);

        if (produk == null) {
            System.out.println("❌ Produk dengan ID " + id + " tidak ditemukan!");
            return;
        }

        // Tampilkan info produk saat ini
        System.out.println("\n=== INFO PRODUK SAAT INI ===");
        System.out.println("ID         : " + produk.getIdProduk());
        System.out.println("Nama       : " + produk.getNama());
        System.out.println("Deskripsi  : " + produk.getDeskripsi());
        System.out.println("Harga Beli : Rp " + produk.getHargaBeli());
        System.out.println("Harga Jual : Rp " + produk.getHargaJual());
        System.out.println("Stok       : " + produk.getStok());

        System.out.println("\n=== PILIH DATA YANG INGIN DIEDIT ===");
        System.out.println("1. Nama Produk");
        System.out.println("2. Deskripsi");
        System.out.println("3. Harga Beli");
        System.out.println("4. Harga Jual");
        System.out.println("5. Stok");
        System.out.println("6. Edit Semua");
        System.out.println("0. Batal");
        System.out.print("Pilih: ");

        String pilihan = sc.nextLine().trim();

        try {
            switch (pilihan) {
                case "1":
                    System.out.print("Nama baru: ");
                    String namaBaru = sc.nextLine().trim();
                    if (!namaBaru.isEmpty()) {
                        produk.setNama(namaBaru);
                        System.out.println("✓ Nama berhasil diubah!");
                    }
                    break;

                case "2":
                    System.out.print("Deskripsi baru: ");
                    String deskripsiBaru = sc.nextLine().trim();
                    if (!deskripsiBaru.isEmpty()) {
                        produk.setDeskripsi(deskripsiBaru);
                        System.out.println("✓ Deskripsi berhasil diubah!");
                    }
                    break;

                case "3":
                    System.out.print("Harga Beli baru: ");
                    double hargaBeliBaru = Double.parseDouble(sc.nextLine().trim());
                    if (hargaBeliBaru > 0) {
                        // Akses langsung karena tidak ada setter untuk hargaBeli
                        // Atau kita bisa tambahkan setter di ProdukElektronik
                        System.out.println("⚠️ Harga beli tidak bisa diubah untuk menjaga konsistensi data.");
                        System.out.println(
                                "   Silakan hapus produk dan tambah produk baru jika perlu mengubah harga beli.");
                    } else {
                        System.out.println("❌ Harga harus lebih dari 0!");
                    }
                    break;

                case "4":
                    System.out.print("Harga Jual baru: ");
                    double hargaJualBaru = Double.parseDouble(sc.nextLine().trim());
                    if (hargaJualBaru > 0) {
                        produk.setHargaJual(hargaJualBaru);
                        System.out.println("✓ Harga jual berhasil diubah!");
                    } else {
                        System.out.println("❌ Harga harus lebih dari 0!");
                    }
                    break;

                case "5":
                    System.out.print("Stok baru: ");
                    int stokBaru = Integer.parseInt(sc.nextLine().trim());
                    if (stokBaru >= 0) {
                        produk.setStok(stokBaru);
                        System.out.println("✓ Stok berhasil diubah!");
                    } else {
                        System.out.println("❌ Stok tidak boleh negatif!");
                    }
                    break;

                case "6":
                    // Edit semua sekaligus
                    System.out.print("Nama baru: ");
                    String nama = sc.nextLine().trim();

                    System.out.print("Deskripsi baru: ");
                    String deskripsi = sc.nextLine().trim();

                    System.out.print("Harga Jual baru: ");
                    double hj = Double.parseDouble(sc.nextLine().trim());

                    System.out.print("Stok baru: ");
                    int stok = Integer.parseInt(sc.nextLine().trim());

                    if (!nama.isEmpty() && !deskripsi.isEmpty() && hj > 0 && stok >= 0) {
                        produk.setNama(nama);
                        produk.setDeskripsi(deskripsi);
                        produk.setHargaJual(hj);
                        produk.setStok(stok);
                        System.out.println("✓ Semua data berhasil diubah!");
                    } else {
                        System.out.println("❌ Input tidak valid!");
                    }
                    break;

                case "0":
                    System.out.println("Edit dibatalkan.");
                    return;

                default:
                    System.out.println("Pilihan tidak valid.");
                    return;
            }

            // Simpan perubahan
            DataStoreSaver.saveProduk();

            // Tampilkan info produk setelah diedit
            System.out.println("\n=== INFO PRODUK SETELAH DIEDIT ===");
            System.out.println("ID         : " + produk.getIdProduk());
            System.out.println("Nama       : " + produk.getNama());
            System.out.println("Deskripsi  : " + produk.getDeskripsi());
            System.out.println("Harga Jual : Rp " + produk.getHargaJual());
            System.out.println("Stok       : " + produk.getStok());

        } catch (NumberFormatException e) {
            System.out.println("❌ Input tidak valid! Harap masukkan angka yang benar.");
        } catch (Exception e) {
            System.out.println("❌ Terjadi kesalahan: " + e.getMessage());
        }
    }

    // --- MENU PELANGGAN ---
    private static void menuPelanggan(Pelanggan p) {
        Keranjang keranjang = new Keranjang();
        boolean keluar = false;
        while (!keluar) {
            System.out.println("\n=== MENU PELANGGAN ===");
            System.out.println("1. Lihat Produk");
            System.out.println("2. Tambah ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("4. Checkout");
            System.out.println("5. History Belanja");
            System.out.println("6. Logout");
            System.out.print("Pilih: ");
            String m = sc.nextLine().trim();

            switch (m) {
                case "1":
                    listProduk();
                    break;
                case "2":
                    listProduk();
                    System.out.print("Masukkan ID produk: ");
                    String id = sc.nextLine().trim();
                    ProdukElektronik produk = DataStore.findProdukById(id);
                    if (produk == null) {
                        System.out.println("Produk tidak ditemukan.");
                        break;
                    }

                    // VALIDASI INPUT JUMLAH DENGAN ERROR HANDLING
                    int qty = 0;
                    boolean validInput = false;

                    while (!validInput) {
                        System.out.print("Jumlah: ");
                        String inputQty = sc.nextLine().trim();

                        try {
                            qty = Integer.parseInt(inputQty);

                            if (qty <= 0) {
                                System.out.println("❌ Jumlah harus lebih dari 0. Silakan coba lagi.");
                            } else if (produk.getStok() < qty) {
                                System.out.println("❌ Stok tidak cukup. Stok tersedia: " + produk.getStok()
                                        + ". Silakan coba lagi.");
                            } else {
                                validInput = true; // Input valid, keluar dari loop
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "❌ Input tidak valid! Harap masukkan angka yang benar. Silakan coba lagi.");
                        }
                    }

                    keranjang.addItem(produk, qty);
                    System.out.println("✅ Berhasil ditambahkan ke keranjang.");
                    break;
                case "3":
                    if (keranjang.isEmpty()) {
                        System.out.println("Keranjang kosong.");
                    } else {
                        System.out.println("Isi Keranjang:");
                        for (Map.Entry<ProdukElektronik, Integer> e : keranjang.getItems().entrySet()) {
                            System.out.println("- " + e.getKey().getNama() + " x " + e.getValue() + " = "
                                    + (e.getKey().getHargaJual() * e.getValue()));
                        }
                        System.out.println("Total: " + keranjang.getTotalHarga());
                    }
                    break;
                case "4":
                    if (keranjang.isEmpty()) {
                        System.out.println("Keranjang kosong. Tidak dapat checkout.");
                        break;
                    }

                    // PILIH KURIR DULU (UNTUK SEMUA METODE)
                    System.out.println("\n=== PILIH KURIR ===");
                    System.out.println("1. JNE");
                    System.out.println("2. J&T");
                    System.out.println("3. SiCepat");
                    System.out.println("4. AnterAja");
                    System.out.print("Pilih kurir: ");

                    String pilihKurir = sc.nextLine().trim();
                    String kurir = "";

                    switch (pilihKurir) {
                        case "1":
                            kurir = "JNE";
                            break;
                        case "2":
                            kurir = "J&T";
                            break;
                        case "3":
                            kurir = "SiCepat";
                            break;
                        case "4":
                            kurir = "AnterAja";
                            break;
                        default:
                            System.out.println("Pilihan tidak valid! Otomatis menggunakan JNE.");
                            kurir = "JNE";
                    }

                    // PILIH METODE PEMBAYARAN
                    System.out.println("\n=== METODE PEMBAYARAN ===");
                    System.out.println("1. Transfer Bank");
                    System.out.println("2. Kartu Kredit");
                    System.out.println("3. E-Wallet");
                    System.out.println("4. COD (Bayar Di Tempat)");
                    System.out.println("5. QRIS");
                    System.out.print("Pilih Opsi: ");
                    String mp = sc.nextLine().trim();
                    Pembayaran metode = null;

                    switch (mp) {
                        case "1":
                            System.out.print("Nama Bank: ");
                            String nb = sc.nextLine();

                            long noRekLong = 0;
                            boolean valid = false;

                            while (!valid) {
                                System.out.print("No Rek: ");
                                String nr = sc.nextLine();

                                try {
                                    noRekLong = Long.parseLong(nr);
                                    valid = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Nomor rekening harus berupa angka! Silakan coba lagi.");
                                }
                            }

                            metode = new TransferBank(nb, noRekLong);
                            break;

                        case "2":
                            System.out.print("Nama pemilik: ");
                            String np = sc.nextLine().trim();
                            System.out.print("No Kartu: ");
                            String nk = sc.nextLine().trim();
                            metode = new KartuKredit(np, nk);
                            break;

                        case "3":
                            System.out.print("Provider: ");
                            String prov = sc.nextLine().trim();
                            System.out.print("ID Akun: ");
                            String idA = sc.nextLine().trim();
                            metode = new EWallet(prov, idA);
                            break;

                        case "4":
                            metode = new COD(kurir, p.getAlamat());
                            break;

                        case "5":
                            metode = new QRIS();
                            break;

                        default:
                            System.out.println("Metode tidak valid.");
                            continue;
                    }

                    System.out.println("\n📦 Kurir yang dipilih: " + kurir);

                    double total = keranjang.getTotalHarga();
                    boolean paid = metode.prosesPembayaran(total);

                    if (paid) {
                        String idTrans = "TR-" + UUID.randomUUID().toString().substring(0, 8);
                        Transaksi t = new Transaksi(idTrans, p, keranjang, metode);
                        DataStore.addTransaksi(t);
                        p.addToHistory(t);
                        DataStoreSaver.saveTransaksi();

                        System.out.println("\n✓ Checkout berhasil!");
                        System.out.println("Transaksi ID: " + idTrans + " (status PENDING)");
                        System.out.println("Menunggu konfirmasi admin.");
                        System.out.println("Pengiriman via: " + kurir);

                        keranjang = new Keranjang();
                    } else {
                        System.out.println("Pembayaran gagal.");
                    }
                    break;
                case "5":
                    System.out.println("History Belanja:");
                    if (p.getHistoryTransaksi().isEmpty()) {
                        System.out.println("Anda belum memiliki history transaksi.");
                        break;
                    }
                    for (Transaksi t : p.getHistoryTransaksi()) {
                        System.out.println("- " + t.getIdTransaksi() + " | " + t.getStatus() + " | " + t.getTotal());
                    }
                    break;
                case "6":
                    keluar = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void registerPelanggan() {
        System.out.println("=== REGISTRASI PELANGGAN ===");

        // VALIDASI NAMA
        String nama = "";
        while (nama.isEmpty()) {
            System.out.print("Nama: ");
            nama = sc.nextLine().trim();
            if (nama.isEmpty()) {
                System.out.println("❌ Nama tidak boleh kosong! Silakan coba lagi.");
            }
        }

        // VALIDASI USERNAME
        String username = "";
        boolean usernameValid = false;
        while (!usernameValid) {
            System.out.print("Username: ");
            username = sc.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("❌ Username tidak boleh kosong! Silakan coba lagi.");
            } else if (DataStore.findUserByUsername(username) != null) {
                System.out.println("❌ Username sudah digunakan! Coba yang lain.");
            } else {
                usernameValid = true;
            }
        }

        // VALIDASI PASSWORD
        String password = "";
        while (password.isEmpty()) {
            System.out.print("Password: ");
            password = sc.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("❌ Password tidak boleh kosong! Silakan coba lagi.");
            }
        }

        // VALIDASI ALAMAT
        String alamat = "";
        while (alamat.isEmpty()) {
            System.out.print("Alamat: ");
            alamat = sc.nextLine().trim();
            if (alamat.isEmpty()) {
                System.out.println("❌ Alamat tidak boleh kosong! Silakan coba lagi.");
            }
        }

        // VALIDASI NOMOR TELEPON - HANYA ANGKA
        String phone = "";
        boolean validPhone = false;
        while (!validPhone) {
            System.out.print("No Telepon: ");
            phone = sc.nextLine().trim();

            if (phone.isEmpty()) {
                System.out.println("❌ Nomor telepon tidak boleh kosong! Silakan coba lagi.");
            } else if (phone.matches("\\d+")) {
                validPhone = true;
            } else {
                System.out.println("❌ Nomor telepon harus berupa angka saja! Silakan coba lagi.");
            }
        }

        String userID = "U" + String.format("%03d", DataStore.getListAkun().size() + 1);
        Pelanggan p = new Pelanggan(userID, username, password, nama, alamat, phone);
        DataStore.getListAkun().add(p);
        DataStoreSaver.saveAkun();

        System.out.println("✓ Registrasi berhasil! Silakan login.");
    }

    private static void listProduk() {
        System.out.println("\n================================== DAFTAR PRODUK ==================================");
        System.out.println("+------+------------------------+------------------------------+------------+-------+");
        System.out.printf("| %-4s | %-22s | %-28s | %-10s | %-5s |%n", "ID", "Nama", "Deskripsi", "Harga", "Stok");
        System.out.println("+------+------------------------+------------------------------+------------+-------+");

        for (ProdukElektronik p : DataStore.getListProduk()) {
            // Potong deskripsi jika terlalu panjang (max 40 karakter)
            String deskripsi = p.getDeskripsi();
            if (deskripsi.length() > 20) {
                deskripsi = deskripsi.substring(0, 17) + "...";
            }

            System.out.printf("| %-4s | %-22s | %-28s | %10.0f | %-5d |%n",
                    p.getIdProduk(),
                    p.getNama(),
                    deskripsi,
                    p.getHargaJual(),
                    p.getStok());
        }

        System.out.println("+------+------------------------+------------------------------+------------+-------+");
    }
}