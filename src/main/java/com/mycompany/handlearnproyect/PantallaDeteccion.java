package com.mycompany.handlearnproyect;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class PantallaDeteccion extends JPanel {

    private JTextArea areaTexto;
    private JPanel areaCamara;

    public PantallaDeteccion() {
        this.setBackground(new Color(10, 15, 30)); // #0A0F1E
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Título
        JLabel titulo = new JLabel("MÓDULO DE CONVERSACIÓN LIBRE");
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

        // 3. Área de Texto (Para frases largas)
        areaTexto = new JTextArea(2, 20);
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setBackground(new Color(10, 15, 30));
        areaTexto.setForeground(new Color(0, 212, 170)); // #00D4AA
        areaTexto.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
        
        // Un scroll por si la frase es muy larga
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(640, 80));
        scroll.setBackground(new Color(10, 15, 30));
        
        gbc.gridy = 2;
        this.add(scroll, gbc);
    }

    /**
     * Este método lo llamarás desde App.java cuando Python mande 
     * texto de conversación general.
     */
    public void actualizar(String texto) {
        areaTexto.setText(texto);
        this.revalidate();
        this.repaint();
    }
}