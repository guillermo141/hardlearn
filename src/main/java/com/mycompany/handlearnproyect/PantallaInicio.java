package com.mycompany.handlearnproyect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class PantallaInicio extends JPanel {

    public PantallaInicio() {
        this.setBackground(new Color(10, 15, 30));
        this.setLayout(new BorderLayout()); // BorderLayout es más estable para separar el techo del centro

        // --- 1. CABEZAL INSTITUCIONAL ---
        JPanel panelCabezal = new JPanel(new GridLayout(1, 3));
        panelCabezal.setOpaque(false);
        
        // Aumentamos el margen superior a 60 para que bajen un poco y no se vean "aplastados"
        // El margen de 80 a los lados evita que se vean comprimidos contra las orillas
        panelCabezal.setBorder(new EmptyBorder(40, 70, 40, 80)); 

        // Logo Izquierda (TecNM) - Tamaño balanceado
        JPanel pnlIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlIzquierda.setOpaque(false);
        pnlIzquierda.add(crearLogoLabel("/logo_tecnm.png", 130, 60));

        // Logo Centro (Sistemas) - Un poco más pequeño para que no domine
        JPanel pnlCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlCentro.setOpaque(false);
        pnlCentro.add(crearLogoLabel("/ISC horizontal blanco.png", 140, 50));

        // Logo Derecha (ITSS Libres) - Tamaño balanceado
        JPanel pnlDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlDerecha.setOpaque(false);
        pnlDerecha.add(crearLogoLabel("/logo_transparente.png", 130, 60));

        panelCabezal.add(pnlIzquierda);
        panelCabezal.add(pnlCentro);
        panelCabezal.add(pnlDerecha);

        this.add(panelCabezal, BorderLayout.NORTH);

        // --- 2. ÁREA CENTRAL (SALUDO) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        
        JLabel labelHola = new JLabel("¡Hola, Amigo!");
        labelHola.setForeground(new Color(0, 212, 170));
        labelHola.setFont(new Font("Segoe UI", Font.BOLD, 55));
        
        gbc.gridy = 0;
        
        gbc.insets = new Insets(-100, 0, 0, 0); 
        panelCentral.add(labelHola, gbc);
        
        JLabel labelSub = new JLabel("Bienvenido a Hand Learn");
        labelSub.setForeground(new Color(136, 149, 179));
        labelSub.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // Reset de margen para el subtítulo
        panelCentral.add(labelSub, gbc);

        this.add(panelCentral, BorderLayout.CENTER);    }

    private JLabel crearLogoLabel(String ruta, int ancho, int alto) {
        URL imgURL = getClass().getResource(ruta);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
        }
        return new JLabel(""); 
    }
}
