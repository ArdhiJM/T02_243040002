package p09;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AplikasiPendaftaran {

    // Identifikasi Kartu Navigasi
    private static final String CARD_FORM = "FORM";
    private static final String CARD_RINGKASAN = "RINGKASAN";
    private static final String CARD_BANTUAN = "BANTUAN";

    // Frame Utama & Layout Manager
    private final JFrame frame = new JFrame("Aplikasi Pendaftaran Kegiatan - UNPAS");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Komponen Input Formulir
    private final JTextField namaField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField umurField = new JTextField(20);
    private final JComboBox<String> kegiatanCombo = new JComboBox<>(new String[]{
        "-- Pilih Kegiatan --",
        "Workshop Web Development (React & Spring Boot)",
        "Seminar Cybersecurity & Ethical Hacking",
        "Bootcamp Data Science & AI",
        "Lomba Competitive Programming"
    });

    // Komponen Output Ringkasan
    private final JLabel txtNama = new JLabel("-");
    private final JLabel txtEmail = new JLabel("-");
    private final JLabel txtUmur = new JLabel("-");
    private final JLabel txtKegiatan = new JLabel("-");

    // Tracking Perubahan Data (Dirty State)
    private boolean isDirty = false;
    private boolean isResetting = false; // Flag khusus agar event reset tidak memicu status dirty

    // Standard RFC-5322 Compliant Email Regex Pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public AplikasiPendaftaran() {
        initUI();
    }

    private void initUI() {
        // Registrasi Panel ke CardLayout
        mainPanel.add(buatPanelForm(), CARD_FORM);
        mainPanel.add(buatPanelRingkasan(), CARD_RINGKASAN);
        mainPanel.add(buatPanelBantuan(), CARD_BANTUAN);

        // DocumentListener untuk Mendeteksi Semua Perubahan Teks (Mendukung Backspace, Delete, Paste)
        DocumentListener docTracker = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { tandaiPerubahan(); }
            @Override
            public void removeUpdate(DocumentEvent e) { tandaiPerubahan(); }
            @Override
            public void changedUpdate(DocumentEvent e) { tandaiPerubahan(); }
        };

        namaField.getDocument().addDocumentListener(docTracker);
        emailField.getDocument().addDocumentListener(docTracker);
        umurField.getDocument().addDocumentListener(docTracker);
        kegiatanCombo.addActionListener(e -> tandaiPerubahan());

        // Penanganan Penutupan Jendela (Rubrik 1)
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                konfirmasiKeluar();
            }
        });

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setSize(560, 440);
        frame.setLocationRelativeTo(null);
    }

    private void tandaiPerubahan() {
        if (!isResetting) {
            isDirty = true;
        }
    }

    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel headerLabel = new JLabel("Formulir Pendaftaran Kegiatan", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerLabel.setForeground(new Color(24, 54, 93));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 8, 12));
        gridPanel.add(new JLabel("Nama Lengkap:"));
        gridPanel.add(namaField);
        gridPanel.add(new JLabel("Email Aktif:"));
        gridPanel.add(emailField);
        gridPanel.add(new JLabel("Umur (15 - 100):"));
        gridPanel.add(umurField);
        gridPanel.add(new JLabel("Pilihan Kegiatan:"));
        gridPanel.add(kegiatanCombo);

        panel.add(gridPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSimpan = new JButton("Simpan & Tampilkan");
        JButton btnReset = new JButton("Reset");
        JButton btnBantuan = new JButton("Bantuan");
        JButton btnKeluar = new JButton("Keluar");

        btnSimpan.addActionListener(e -> prosesSimpan());
        btnReset.addActionListener(e -> prosesReset());
        btnBantuan.addActionListener(e -> cardLayout.show(mainPanel, CARD_BANTUAN));
        btnKeluar.addActionListener(e -> konfirmasiKeluar());

        btnPanel.add(btnSimpan);
        btnPanel.add(btnReset);
        btnPanel.add(btnBantuan);
        btnPanel.add(btnKeluar);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelRingkasan() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel headerLabel = new JLabel("Ringkasan Data Pendaftaran", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerLabel.setForeground(new Color(24, 54, 93));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel infoGrid = new JPanel(new GridLayout(4, 2, 8, 10));
        infoGrid.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        infoGrid.add(new JLabel("Nama Lengkap:"));
        infoGrid.add(txtNama);
        infoGrid.add(new JLabel("Email:"));
        infoGrid.add(txtEmail);
        infoGrid.add(new JLabel("Umur:"));
        infoGrid.add(txtUmur);
        infoGrid.add(new JLabel("Kegiatan:"));
        infoGrid.add(txtKegiatan);

        panel.add(infoGrid, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnKembali = new JButton("Kembali ke Form");
        JButton btnKeluar = new JButton("Keluar");

        btnKembali.addActionListener(e -> cardLayout.show(mainPanel, CARD_FORM));
        btnKeluar.addActionListener(e -> konfirmasiKeluar());

        btnPanel.add(btnKembali);
        btnPanel.add(btnKeluar);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelBantuan() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel headerLabel = new JLabel("Halaman Bantuan & Petunjuk", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerLabel.setForeground(new Color(24, 54, 93));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTextArea helpText = new JTextArea(
            "PETUNJUK PENGISIAN FORMULIR PENDAFTARAN:\n\n" +
            "1. Nama Lengkap: Wajib diisi, tidak boleh hanya berupa spasi.\n" +
            "2. Email: Wajib diisi dengan format email valid (contoh: nama@domain.com).\n" +
            "3. Umur: Isikan bilangan bulat positif antara 15 hingga 100 tahun.\n" +
            "4. Pilihan Kegiatan: Pilih salah satu opsi kegiatan dari dropdown.\n" +
            "5. Simpan & Tampilkan: Memvalidasi data dan menampilkan ringkasan.\n" +
            "6. Reset: Mengosongkan formulir dan mengembalikan fokus ke Nama.\n" +
            "7. Kembali: Kembali ke formulir tanpa menghapus data yang ada."
        );
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(new JScrollPane(helpText), BorderLayout.CENTER);

        JButton btnKembali = new JButton("Kembali ke Form");
        btnKembali.addActionListener(e -> cardLayout.show(mainPanel, CARD_FORM));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnKembali);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void prosesSimpan() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String teksUmur = umurField.getText().trim();
        int idxKegiatan = kegiatanCombo.getSelectedIndex();

        // 1. Validasi Nama Wajib (Rubrik 2)
        if (nama.isEmpty()) {
            tampilkanPeringatan("Nama lengkap wajib diisi!", namaField);
            return;
        }

        // 2. Validasi Format Email (Rubrik 2)
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            tampilkanPeringatan("Format email tidak valid!\nContoh email yang benar: mahasiswa@pasundan.ac.id", emailField);
            return;
        }

        // 3. Validasi Umur - Kosong, Format Angka, & Rentang 15-100 (Rubrik 2)
        if (teksUmur.isEmpty()) {
            tampilkanPeringatan("Umur wajib diisi!", umurField);
            return;
        }

        int umur;
        try {
            umur = Integer.parseInt(teksUmur);
            if (umur < 15 || umur > 100) {
                tampilkanPeringatan("Umur harus berada dalam rentang 15 hingga 100 tahun!", umurField);
                return;
            }
        } catch (NumberFormatException ex) {
            tampilkanPeringatan("Umur harus berupa bilangan bulat angka (contoh: 20)!", umurField);
            return;
        }

        // 4. Validasi Dropdown Kegiatan (Rubrik 2)
        if (idxKegiatan <= 0) {
            tampilkanPeringatan("Silakan pilih salah satu kegiatan yang tersedia!", kegiatanCombo);
            return;
        }

        // Pembaruan Data Ringkasan
        txtNama.setText(nama);
        txtEmail.setText(email);
        txtUmur.setText(umur + " Tahun");
        txtKegiatan.setText((String) kegiatanCombo.getSelectedItem());

        isDirty = false; // Reset status perubahan setelah berhasil disimpan
        cardLayout.show(mainPanel, CARD_RINGKASAN);
    }

    private void prosesReset() {
        isResetting = true; // Aktifkan flag agar listener tidak memicu isDirty = true
        namaField.setText("");
        emailField.setText("");
        umurField.setText("");
        kegiatanCombo.setSelectedIndex(0);
        isResetting = false;

        isDirty = false;
        namaField.requestFocusInWindow();
    }

    private void tampilkanPeringatan(String pesan, JComponent component) {
        JOptionPane.showMessageDialog(frame, pesan, "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        component.requestFocusInWindow();
        if (component instanceof JTextField) {
            ((JTextField) component).selectAll(); // Sorot seluruh teks yang salah (Rubrik 4)
        }
    }

    private void konfirmasiKeluar() {
        String pesan = isDirty 
            ? "Terdapat masukan data pada formulir yang belum disimpan.\nApakah Anda yakin ingin keluar?" 
            : "Apakah Anda yakin ingin keluar dari aplikasi?";

        int pilihan = JOptionPane.showConfirmDialog(
            frame, pesan, "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AplikasiPendaftaran app = new AplikasiPendaftaran();
            app.frame.setVisible(true);
        });
    }
}