/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.handlearnproyect;

import javax.swing.*;
import java.awt.*;

public class PantallaEstadisticas {

    public static void mostrar(JFrame parent, String puntos) {
        JDialog ventana = new JDialog(parent, "Mi Progreso", true);
        ventana.setSize(300, 200);
        ventana.setLocationRelativeTo(parent);

        JPanel layout = new JPanel(new GridBagLayout());
        layout.setBackground(new Color(10, 15, 30));
        layout.setBorder(BorderFactory.createLineBorder(new Color(0, 212, 170), 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("PUNTOS TOTALES");
        titulo.setForeground(new Color(136, 149, 179));
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 0;
        layout.add(titulo, gbc);

        JLabel valor = new JLabel(puntos);
        valor.setForeground(new Color(0, 212, 170));
        valor.setFont(new Font("Courier New", Font.BOLD, 50));
        gbc.gridy = 1;
        layout.add(valor, gbc);

        ventana.add(layout);
        ventana.setVisible(true);
    }
}