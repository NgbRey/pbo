package app.models;

/**
 * Abstract class yang menjadi parent untuk semua tipe pengguna.
 * Menyimpan atribut kredensial dasar.
 *
 * @author Rajabi, Teuku Al, Azira, M Rayyanta
 */
public abstract class User {
    private String userID;
    private String username;
    private String password;
    private String nama;

    /**
     * Konstruktor untuk membuat user baru.
     *
     * @param userID   ID unik user (contoh: U001, A001).
     * @param username Username untuk login.
     * @param password Password untuk autentikasi.
     * @param nama     Nama lengkap pengguna.
     */
    public User(String userID, String username, String password, String nama) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    } // untuk demo CLI sederhana

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * Memeriksa kecocokan password yang diinputkan.
     *
     * @param pw Password yang diinput user.
     * @return true jika password cocok, false jika salah.
     */
    public boolean checkPassword(String pw) {
        return this.password.equals(pw);
    }

    /**
     * Mendapatkan peran (role) dari user.
     * 
     * @return String "ADMIN" atau "PELANGGAN".
     */
    public abstract String getRole();
}
