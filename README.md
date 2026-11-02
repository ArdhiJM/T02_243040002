# T02_NIM - Aplikasi Pendaftaran Kegiatan (Modul 09)
**Mata Kuliah:** IF21W0508 – Praktikum Pemrograman II  
**Dosen Pengampu:** Wanda Gusdya Purnama, ST., MT.  
**Program Studi:** Teknik Informatika, Universitas Pasundan  

---

## 📌 Deskripsi Proyek
Aplikasi desktop GUI berbasis **Java Swing** untuk sistem pendaftaran kegiatan. Aplikasi ini menerapkan arsitektur **Single Window** menggunakan `CardLayout`, validasi masukan berlapis, pelacakan perubahan data (*Dirty State Tracking*), serta penanganan kesalahan yang responsif sesuai dengan **4 Kriteria Rubrik Penilaian Modul 09**.

---

## 🛠️ Spesifikasi Lingkungan Pengembang (Environment)
- **Bahasa Pemrograman:** Java (JDK 11 / JDK 17 / JDK 21 LTS)
- **Pustaka GUI:** Java Swing (`javax.swing.*`) & AWT (`java.awt.*`)
- **IDE yang Didukung:** Visual Studio Code, IntelliJ IDEA, NetBeans, atau Eclipse
- **Sistem Operasi:** Windows / macOS / Linux

---

## 📂 Struktur Folder Proyek
```text
T02_NIM/
├── src/
│   └── p09/
│       └── AplikasiPendaftaran.java
├── README.md
└── Laporan_Tugas2_Modul09.html
```

---

## 🚀 Petunjuk Kompilasi dan Eksekusi (Build & Run)

### Via Terminal / Command Prompt:
1. Buka terminal lalu navigasikan ke direktori utama proyek `T02_NIM`:
   ```bash
   cd T02_NIM
   ```
2. Kompilasi file kode sumber Java:
   ```bash
   javac -d bin src/p09/AplikasiPendaftaran.java
   ```
3. Jalankan aplikasi:
   ```bash
   java -cp bin p09.AplikasiPendaftaran
   ```

### Via VS Code / IntelliJ IDEA:
1. Buka folder `T02_NIM` pada IDE Anda.
2. Navigasikan ke file `src/p09/AplikasiPendaftaran.java`.
3. Klik tombol **Run** atau tekan `Shift + F10` (IntelliJ) / `F5` (VS Code).

---

## 📋 Pemenuhan Rubrik Penilaian Praktikum

| Kriteria Rubrik | Bobot | Fitur & Implementasi Kode |
| :--- | :---: | :--- |
| **1. Event & Perubahan Keadaan** | **30%** | • `DocumentListener` mendeteksi ketikan, hapus (*backspace/delete*), dan *paste*.<br>• Mencegah penutupan aplikasi langsung saat data belum disimpan (`windowClosing` & `JOptionPane`).<br>• Flag `isResetting` menjaga status *dirty state* tetap konsisten. |
| **2. Validasi Input** | **35%** | • **Nama:** Wajib diisi, menolak string kosong atau spasi.<br>• **Email:** Regex RFC-5322 (`^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`).<br>• **Umur:** Integer parsing & cek rentang $15 - 100$ tahun.<br>• **Kegiatan:** Menolak indeks default `-- Pilih Kegiatan --`. |
| **3. Navigasi & Retensi Input** | **20%** | • CardLayout mengelola kartu `FORM`, `RINGKASAN`, dan `BANTUAN` dalam 1 window.<br>• Data masukan tidak hilang saat navigasi bolak-balik antara Form, Bantuan, dan Ringkasan. |
| **4. Pesan & Pemulihan Kesalahan** | **15%** | • Dialog pesan kesalahan spesifik (`JOptionPane.WARNING_MESSAGE`).<br>• Pemfokusan kursor (`requestFocusInWindow()`) & sorot teks otomatis (`selectAll()`). |

---

## 🧪 Matriks Skenario Uji Singkat
1. **Input Valid:** Nama `Ardhi`, Email `ardhi@pasundan.ac.id`, Umur `23`, Kegiatan `Workshop Web Dev` $\rightarrow$ Berhasil masuk ke Ringkasan.
2. **Nama Kosong:** Dialog muncul $\rightarrow$ Fokus kembali ke field Nama.
3. **Email Salah:** `alex.com` $\rightarrow$ Dialog "Email tidak valid" $\rightarrow$ Teks tersorot otomatis.
4. **Umur String/Rentang:** `dua puluh` atau `120` $\rightarrow$ Dialog peringatan format/rentang.
5. **Tombol Reset:** Mengosongkan formulir & mengembalikan fokus ke field Nama.
6. **Batal Keluar:** Menekan tombol Keluar lalu memilih "Tidak" $\rightarrow$ Aplikasi tetap terbuka.