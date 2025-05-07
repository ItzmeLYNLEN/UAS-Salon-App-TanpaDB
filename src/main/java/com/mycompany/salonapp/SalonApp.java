package com.mycompany.salonapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



/**
 *
 * @author M Daffa A
 */
public class SalonApp {

    public static void main(String[] args) {
        Salon();
    }

    private static void Salon() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Salon frame = new Salon(); // ganti dengan nama form kamu
            frame.setVisible(true);
        });
    }
}
