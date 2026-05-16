
package com.mycompany.handlearnproyect;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class PantallaColores extends JPanel {

    private JLabel labelColor;
    private JPanel areaCamara;

    public PantallaColores() {
        // Configuramos el panel principal (equivalente al VBox)
        this.setBackground(new Color(10, 15, 30)); // #0A0F1E
        this.setLayout(new GridBagLayout()); // Para centrar todo fácilmente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Título
        JLabel titulo = new JLabel("MÓDULO DE RECONOCIMIENTO DE COLORES");
        titulo.setForeground(new Color(136, 149, 179)); // #8895B3
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 0;
        this.add(titulo, gbc);

        // 2. Área de la Cámara (El recuadro donde Python proyecta)
        areaCamara = new JPanel();
        areaCamara.setPreferredSize(new Dimension(640, 480));
        areaCamara.setBackground(new Color(17, 24, 39)); // #111827
        areaCamara.setBorder(new LineBorder(new Color(0, 212, 170), 2, true)); // Borde Turquesa
        gbc.gridy = 1;
        this.add(areaCamara, gbc);

        // 3. Etiqueta de Resultado
        labelColor = new JLabel("Esperando color...");
        labelColor.setForeground(new Color(0, 212, 170)); // #00D4AA
        labelColor.setFont(new Font("Segoe UI Semibold", Font.BOLD, 45));
        gbc.gridy = 2;
        this.add(labelColor, gbc);
    }

    /**
     * Método que llamará App.java cuando reciba el dato "[C]" de Python
     */
    public void actualizar(String colorDetectado) {
        // Swing requiere que actualicemos el texto así
        labelColor.setText(colorDetectado.toUpperCase());
        
        // Efecto visual: Cambiar el color del texto según el nombre
        String color = colorDetectado.toLowerCase();
        if (color.contains("rojo")) labelColor.setForeground(Color.RED);
        else if (color.contains("azul")) labelColor.setForeground(new Color(30, 144, 255));
        else if (color.contains("verde")) labelColor.setForeground(Color.GREEN);
        else if (color.contains("amarillo")) labelColor.setForeground(Color.YELLOW);
        else labelColor.setForeground(new Color(0, 212, 170)); // Volver al turquesa original
        
        // Forzar a que la interfaz se repinte
        this.revalidate();
        this.repaint();
    }
}