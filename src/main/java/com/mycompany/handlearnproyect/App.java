package com.mycompany.handlearnproyect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class App {

    private JFrame frame;
    private JPanel contentArea;
    private CardLayout cardLayout;
    
    // Arreglo para gestionar los botones del sidebar y sus estilos
    private JButton[] navBtns = new JButton[5];
    private String[] keys = {"inicio", "deteccion", "abecedario", "completar", "colores"};
    private String[] nombres = {"Inicio", "Conversación", "Abecedario", "Completar Palabras", "Colores"};

    // Clases de las pantallas 
    private PantallaInicio pantallaInicio;
    private PantallaDeteccion pantallaDeteccion;
    private PantallaConversacion pantallaConversacion;
    private PantallaPuntaje pantallaPuntaje;
    private PantallaColores pantallaColores;
    
    private String puntosActuales = "0";

    // Guardamos la llave de la pantalla activa para controlar el repintado
    private String paginaActiva = "inicio";

    public App() {
        initGUI();
        conectarConPython();
    }

    private void initGUI() {
        frame = new JFrame("HAND-LEARN - LSM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLayout(new BorderLayout());

        // 1. Sidebar (Panel Izquierdo)
        JPanel sidebar = buildSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        // 2. Área de Contenido (Centro)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(10, 15, 30));

        // Inicializamos las pantallas
        pantallaInicio = new PantallaInicio();
        pantallaDeteccion = new PantallaDeteccion();
        pantallaConversacion = new PantallaConversacion();
        pantallaPuntaje = new PantallaPuntaje();
        pantallaColores = new PantallaColores();

        // Las agregamos al CardLayout con su "llave"
        contentArea.add(pantallaInicio, "inicio");
        contentArea.add(pantallaDeteccion, "deteccion");
        contentArea.add(pantallaConversacion, "abecedario");
        contentArea.add(pantallaPuntaje, "completar");
        contentArea.add(pantallaColores, "colores");

        frame.add(contentArea, BorderLayout.CENTER);
        
        // --- TRUCO MAESTRO: CORRECCIÓN GLOBAL DE LA BARRA BLANCA ---
        // Personaliza el diseño de todas las JScrollBar del sistema para que sean oscuras y premium
        UIManager.put("ScrollBar.thumb", new Color(30, 41, 59)); // Color de la barrita que se desliza
        UIManager.put("ScrollBar.track", new Color(10, 15, 30)); // Fondo de la barra (Mismo del contenido)
        UIManager.put("ScrollBar.width", 10); // Más delgada y discreta
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Mostrar inicio por defecto y aplicar estilo inicial
        navegarA("inicio");
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setPreferredSize(new Dimension(260, 700));
        sb.setBackground(new Color(17, 24, 39)); // #111827
        sb.setLayout(new GridBagLayout());
        sb.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(30, 45, 69)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- 1. LOGO "HAND LEARN" ---
        JLabel logo = new JLabel("<html><div style='color:#00D4AA; font-family:Segoe UI; font-size:32px; font-weight:bold;'>HAND<br>LEARN</div></html>");
        logo.setBorder(BorderFactory.createEmptyBorder(30, 25, 25, 0));
        gbc.gridy = 0;
        sb.add(logo, gbc);

        // --- 2. BOTONES DE NAVEGACIÓN ---
        for (int i = 0; i < keys.length; i++) {
            final String key = keys[i];
            
            navBtns[i] = new JButton(nombres[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fondo oscuro base para mantener limpio el diseño
                    g2.setColor(new Color(17, 24, 39));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    
                    // Si la llave coincide con la página activa, pinto el borde turquesa de resaltado
                    if (paginaActiva.equals(key)) {
                        g2.setColor(new Color(0, 212, 170)); // Turquesa #00D4AA
                        g2.setStroke(new BasicStroke(2));    // Grosor del contorno
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                    }
                    
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            
            navBtns[i].setContentAreaFilled(false); 
            navBtns[i].setOpaque(false);
            navBtns[i].setFocusPainted(false);
            navBtns[i].setBorderPainted(false); 
            
            navBtns[i].setHorizontalAlignment(SwingConstants.LEFT);
            navBtns[i].setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0)); 
            
            // Propiedades de la tipografía grande (19px)
            navBtns[i].setForeground(Color.WHITE);
            navBtns[i].setFont(new Font("Segoe UI Semibold", Font.PLAIN, 19)); 
            
            navBtns[i].setPreferredSize(new Dimension(240, 48)); 
            navBtns[i].setMinimumSize(new Dimension(240, 48));
            navBtns[i].setMaximumSize(new Dimension(240, 48));
            navBtns[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            navBtns[i].addActionListener(e -> {
                navegarA(key); 
            });

            gbc.gridy = i + 1;
            gbc.insets = new Insets(5, 12, 5, 12); 
            sb.add(navBtns[i], gbc);
        }

        // --- 3. ESPACIADOR ---
        gbc.gridy = keys.length + 1;
        gbc.weighty = 1.0;
        sb.add(Box.createVerticalGlue(), gbc);

        // --- 4. PANEL INFERIOR PARA "VER PROGRESO" Y "SALIR" ---
        JPanel panelInferior = new JPanel();
        panelInferior.setOpaque(false);
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));

        // Botón Ver Progreso
        JButton btnStats = new JButton("Ver Progreso");
        btnStats.setPreferredSize(new Dimension(200, 40));
        btnStats.setMinimumSize(new Dimension(200, 40));
        btnStats.setMaximumSize(new Dimension(200, 40));
        btnStats.setBackground(new Color(26, 34, 53));
        btnStats.setForeground(new Color(0, 212, 170));
        btnStats.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btnStats.setFocusPainted(false);
        btnStats.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStats.addActionListener(e -> PantallaEstadisticas.mostrar(frame, puntosActuales));
        
        // Botón Salir
        JButton btnSalir = new JButton("Salir") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(new Color(239, 68, 68));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSalir.setContentAreaFilled(false);
        btnSalir.setOpaque(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setPreferredSize(new Dimension(200, 40));
        btnSalir.setMinimumSize(new Dimension(200, 40));
        btnSalir.setMaximumSize(new Dimension(200, 40));
        btnSalir.setBackground(new Color(17, 24, 39)); 
        btnSalir.setForeground(new Color(239, 68, 68)); 
        btnSalir.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));

        panelInferior.add(btnStats);
        panelInferior.add(Box.createRigidArea(new Dimension(0, 12))); 
        panelInferior.add(btnSalir);

        gbc.gridy = keys.length + 2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 25, 0); 
        sb.add(panelInferior, gbc);

        return sb;
    }

    public void navegarA(String pagina) {
        cardLayout.show(contentArea, pagina);
        
        // Actualizamos la variable global de control
        this.paginaActiva = pagina;
        
        // Cambiamos el color de la letra y lanzamos el repintado de inmediato
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(pagina)) {
                navBtns[i].setForeground(new Color(0, 212, 170)); // Letra turquesa activo
                navBtns[i].setFont(new Font("Segoe UI", Font.BOLD, 19));
            } else {
                navBtns[i].setForeground(Color.WHITE); // Letra blanca por defecto
                navBtns[i].setFont(new Font("Segoe UI Semibold", Font.PLAIN, 19));
            }
            navBtns[i].repaint();
        }
        
        // Buscamos si la pantalla actual tiene un JScrollPane oculto y forzamos su modo oscuro
        SwingUtilities.invokeLater(() -> {
            configurarScrollBarsInvisibles(contentArea);
        });
    }

    // Función auxiliar que busca JScrollPanes dentro de cualquier pantalla y los mimetiza al fondo oscuro
    private void configurarScrollBarsInvisibles(Component comp) {
        if (comp instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) comp;
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getVerticalScrollBar().setBackground(new Color(10, 15, 30));
            sp.getHorizontalScrollBar().setBackground(new Color(10, 15, 30));
            
            // Elimina las flechas molestas nativas de Windows y deja un recorrido sutil oscuro
            sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                @Override protected void configureScrollBarColors() { this.thumbColor = new Color(30, 41, 59); }
                @Override protected JButton createDecreaseButton(int orientation) { return crearBotonVacio(); }
                @Override protected JButton createIncreaseButton(int orientation) { return crearBotonVacio(); }
            });
        } else if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                configurarScrollBarsInvisibles(child);
            }
        }
    }

    private JButton crearBotonVacio() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        return b;
    }

    public void conectarConPython() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5005);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String linea;
                while ((linea = entrada.readLine()) != null) {
                    final String dato = linea;
                    SwingUtilities.invokeLater(() -> {
                        if (dato.startsWith("PUNTOS:")) {
                            puntosActuales = dato.replace("PUNTOS:", "");
                        } else if (dato.startsWith("[C]")) {
                            pantallaColores.actualizar(dato.replace("[C]", ""));
                        } else if (dato.startsWith("[L]")) {
                            pantallaConversacion.actualizar(dato.replace("[L]", ""));
                        } else if (dato.startsWith("[P]")) {
                            pantallaPuntaje.actualizar(dato.replace("[P]", ""));
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Esperando Python...");
            }
        }).start();
    }
}