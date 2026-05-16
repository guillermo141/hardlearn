package com.mycompany.handlearnproyect;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class PantallaConversacion extends JPanel {

    private JTextArea areaTexto;
    private JPanel areaCamara;

    public PantallaConversacion() {
        // Configuración del panel (Fondo oscuro #0A0F1E)
        this.setBackground(new Color(10, 15, 30));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Título del Módulo
        JLabel titulo = new JLabel("MÓDULO DE CONVERSACIÓN (LSM)");
        titulo.setForeground(new Color(136, 149, 179)); // #8895B3
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 0;
        this.add(titulo, gbc);

        // 2. Recuadro de la Cámara (Donde se visualiza la seña)
        areaCamara = new JPanel();
        areaCamara.setPreferredSize(new Dimension(640, 480));
        areaCamara.setBackground(new Color(17, 24, 39)); // #111827
        areaCamara.setBorder(new LineBorder(new Color(0, 212, 170), 2, true)); // Borde Turquesa
        gbc.gridy = 1;
        this.add(areaCamara, gbc);

        // 3. Área de visualización de texto (Traducción)
        areaTexto = new JTextArea(2, 20);
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setBackground(new Color(10, 15, 30));
        areaTexto.setForeground(new Color(0, 212, 170)); // #00D4AA
        areaTexto.setFont(new Font("Segoe UI Semibold", Font.BOLD, 32));
        areaTexto.setText("Traducción en tiempo real...");
        
        // Scroll invisible para que el texto fluya si es largo
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(640, 100));
        
        gbc.gridy = 2;
        this.add(scroll, gbc);
    }

    /**
     * Método para actualizar el texto desde la clase App
     * Se activa cuando Python envía el prefijo correspondiente
     */
    public void actualizar(String textoDetectado) {
        areaTexto.setText(textoDetectado);
        // Aseguramos que se vea el cambio inmediatamente
        this.revalidate();
        this.repaint();
    }
}