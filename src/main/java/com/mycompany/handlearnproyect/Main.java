/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.handlearnproyect;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // En Swing, lanzamos la aplicación así para que sea segura entre hilos
        SwingUtilities.invokeLater(() -> {
            try {
                // Creamos la instancia de tu App (la versión Swing)
                new App(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
