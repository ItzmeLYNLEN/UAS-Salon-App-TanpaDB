/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.salonapp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author M Daffa A
 */
public class Kasir {
    private final HashMap<String, Integer> layananMap = new HashMap<>();
    
    public Kasir() {
        // Inisialisasi daftar layanan dan harga
        layananMap.put("Potong Rambut", 25000);
        layananMap.put("Creambath", 40000);
        layananMap.put("Hair Coloring", 60000);
        layananMap.put("Waxing", 50000);
    }
    
    public HashMap<String, Integer> getLayananMap() {
        return layananMap;
    }
    
    public String formatRupiah(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
    
    public String buatStruk(String nama, DefaultTableModel model, boolean isMember, double uangDibayar) throws Exception {
        if (nama.isEmpty()) {
            throw new Exception("Nama pelanggan harus diisi!");
        }
        if (model.getRowCount() == 0) {
            throw new Exception("Tambahkan minimal satu layanan terlebih dahulu!");
        }

        double totalHarga = 0;
        StringBuilder detailLayanan = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            String layanan = model.getValueAt(i, 0).toString();
            int harga = (int) model.getValueAt(i, 1);
            totalHarga += harga;
            detailLayanan.append(" ").append(String.format("%-13s : %s\n", layanan, formatRupiah(harga)));
        }

        double diskon = isMember ? 0.1 : 0.0;
        double totalSetelahDiskon = totalHarga - (totalHarga * diskon);

        if (uangDibayar < totalSetelahDiskon) {
            throw new Exception("Uang yang dibayarkan tidak cukup!");
        }

        double kembalian = uangDibayar - totalSetelahDiskon;

        LocalDateTime sekarang = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
        String waktuTransaksi = sekarang.format(formatter);

        // Format struk
        StringBuilder output = new StringBuilder();
        output.append("       SALON KECANTIKAN CERIA        \n");
        output.append("|--------------------------------------|\n");
        output.append(" Waktu         : ").append(waktuTransaksi).append("\n");
        output.append(" Nama          : ").append(nama).append("\n");
        output.append(detailLayanan);
        output.append(" Member        : ").append(isMember ? "Ya" : "Tidak").append("\n");
        output.append(" Total Bayar   : ").append(formatRupiah(totalSetelahDiskon)).append("\n");
        output.append(" Jumlah Bayar  : ").append(formatRupiah(uangDibayar)).append("\n");
        output.append(" Kembalian     : ").append(formatRupiah(kembalian)).append("\n");
        output.append("|--------------------------------------|\n");
        output.append("       Terima kasih atas kunjungan Anda!\n");

        // Simpan ke file log setelah semua validasi berhasil
        simpanKeFile(nama, model, isMember, totalSetelahDiskon, uangDibayar, kembalian, waktuTransaksi);

        return output.toString();
    }
    
    private void simpanKeFile(String nama, DefaultTableModel model, boolean isMember, double total, double uangDibayar, double kembalian, String waktuTransaksi) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transaksi_salon.txt", true))) {
            writer.write("Waktu Transaksi: " + waktuTransaksi);
            writer.newLine();
            writer.write("Nama: " + nama);
            writer.newLine();
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write("Layanan: " + model.getValueAt(i, 0) + ", Harga: " + formatRupiah((int) model.getValueAt(i, 1)));
                writer.newLine();
            }
            writer.write("Member: " + (isMember ? "Ya" : "Tidak"));
            writer.newLine();
            writer.write("Total Bayar: " + formatRupiah(total));
            writer.newLine();
            writer.write("Jumlah Bayar: " + formatRupiah(uangDibayar));
            writer.newLine();
            writer.write("Kembalian: " + formatRupiah(kembalian));
            writer.newLine();
            writer.write("------------");
            writer.newLine();
        }
    }
     public String bacaRiwayat() throws IOException {
        StringBuilder isiRiwayat = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("transaksi_salon.txt"))) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                isiRiwayat.append(baris).append("\n");
            }
        }
        return isiRiwayat.toString();
    }
}
