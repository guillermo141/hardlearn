package com.mycompany.handlearnproyect;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class PantallaPuntaje extends JPanel {

    private JLabel labelPalabra;
    private JPanel areaCamara;

    public PantallaPuntaje() {
        this.setBackground(new Color(10, 15, 30)); // #0A0F1E
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Título
        JLabel titulo = new JLabel("MÓDULO: COMPLETAR PALABRAS");
        titulo.setForeground(new Color(136, 149, 179)); // #8895B3
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 0;
        this.add(titulo, gbc);

        // 2. Área de la Cámara
        areaCamara = new JPanel();
        areaCamara.setPreferredSize(new Dimension(640, 480));
        areaCamara.setBackground(new Color(17, 24, 39)); // #111827
        areaCamara.setBorder(new LineBorder(new Color(0, 212, 170), 2, true));
        gbc.gridy = 1;
        this.add(areaCamara, gbc);

        // 3. Resultado de la Palabra
        labelPalabra = new JLabel("Formando palabra...");
        labelPalabra.setForeground(new Color(0, 212, 170)); // #00D4AA
        labelPalabra.setFont(new Font("Segoe UI Bold", Font.BOLD, 50));
        gbc.gridy = 2;
        this.add(labelPalabra, gbc);
    }

    public void actualizar(String texto) {
        labelPalabra.setText(texto.toUpperCase());
        this.revalidate();
        this.repaint();
    }
}