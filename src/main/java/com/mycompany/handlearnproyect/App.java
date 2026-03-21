package com.mycompany.handlearnproyect;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.application.Platform;
import java.io.*;
import java.net.*;

public class App {

    private final Stage stage;
    private StackPane contentArea;
    private Button[] navBtns = new Button[4];
    
    // Clases de las pantallas
    private PantallaInicio pantallaInicio;
    private PantallaDeteccion pantallaDeteccion;
    private PantallaConversacion pantallaConversacion;
    private PantallaPuntaje pantallaPuntaje;

    public App(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        HBox root = new HBox();
        root.setStyle("-fx-background-color:#0A0F1E;");

        // 1. Inicializar pantallas
        pantallaInicio = new PantallaInicio(this);
        pantallaDeteccion = new PantallaDeteccion(this);
        pantallaConversacion = new PantallaConversacion(this);
        pantallaPuntaje = new PantallaPuntaje(this);

        VBox sidebar = buildSidebar();
        
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:#0A0F1E;");
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // 2. Agregar raíces al contenedor principal
        contentArea.getChildren().addAll(
            pantallaPuntaje.getRoot(),
            pantallaConversacion.getRoot(),
            pantallaDeteccion.getRoot(),
            pantallaInicio.getRoot()
        );

        root.getChildren().addAll(sidebar, contentArea);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("HAND-LEARN - Lengua de Señas Mexicana");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        // 3. Iniciar navegación y conexión con Python
        navegarA("inicio");
        conectarConPython(); 
    }

    // --- SISTEMA DE COMUNICACIÓN CON PYTHON ---
    public void conectarConPython() {
        Thread hiloEscucha = new Thread(() -> {
            try {
                // Se conecta al servidor Python en el puerto 5005
                Socket socket = new Socket("localhost", 5005);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String linea;
                while ((linea = entrada.readLine()) != null) {
                    final String senaRecibida = linea;
                    
                    // Actualizamos la interfaz de forma segura
                    Platform.runLater(() -> {
                        pantallaDeteccion.actualizarResultado(senaRecibida);
                    });
                }
            } catch (IOException e) {
                System.out.println("Esperando servidor Python...");
            }
        });
        hiloEscucha.setDaemon(true);
        hiloEscucha.start();
    }

    private VBox buildSidebar() {
        VBox sb = new VBox(4);
        sb.setPrefWidth(250);
        sb.setPadding(new Insets(22, 12, 18, 12));
        sb.setStyle("-fx-background-color:#111827; -fx-border-color:#1E2D45; -fx-border-width:0 1 0 0;");

        // logo de la imagen
        HBox logo = new HBox(15); // Aumentamos el espacio entre icono y texto
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setPadding(new Insets(10, 0, 30, 10)); // Más espacio arriba y abajo

        Label icono = new Label("👋"); // Un emoji o icono más visible
        icono.setStyle("-fx-font-size: 24;"); 

        Label texto = new Label("HAND-LEARN");
        // Aumentamos tamaño a 18 y el espacio entre letras (letter-spacing)
        texto.setStyle("-fx-font-family:'Segoe UI Bold', 'Arial'; -fx-font-size: 24; -fx-text-fill:#00D4AA; -fx-letter-spacing: 1px; -fx-effect: dropshadow(gaussian, rgba(0, 212, 170, 0.5), 10, 0, 0, 0);");

        logo.getChildren().addAll(icono, texto);
        sb.getChildren().add(logo);    

        // Botones
        String[][] items = {
            {"inicio", "Inicio"},
            {"deteccion", "Detectar seña"},
            {"conversacion", "Conversar"},
            {"puntaje", "Mi progreso"}
        };

        for (int i = 0; i < items.length; i++) {
            final String key = items[i][0];
            Button btn = crearNavBtn(items[i][1]);
            btn.setOnAction(e -> navegarA(key));
            navBtns[i] = btn;
            sb.getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Stats
        VBox stats = buildStatsPanel();
        
        Button salir = new Button("Salir");
        salir.setMaxWidth(Double.MAX_VALUE);
        salir.setStyle("-fx-background-color:transparent; -fx-border-color:#FF6B35; -fx-border-radius:10; -fx-text-fill:#FF6B35; -fx-font-weight:bold; -fx-padding:10; -fx-cursor:hand;");
        salir.setOnAction(e -> Platform.exit());

        sb.getChildren().addAll(spacer, new Separator(), stats, new Separator(), salir);
        return sb;
    }

    private VBox buildStatsPanel() {
        VBox stats = new VBox(4);
        stats.setPadding(new Insets(12));
        stats.setStyle("-fx-background-color:#1A2235; -fx-background-radius:12;");
        
        Label lp = new Label("Puntos totales");
        lp.setStyle("-fx-text-fill:#8895B3; -fx-font-size:10; -fx-font-weight:bold;");
        Label vp = new Label("1,240");
        vp.setStyle("-fx-font-family:'Courier New'; -fx-font-size:18; -fx-font-weight:bold; -fx-text-fill:#00D4AA;");
        
        stats.getChildren().addAll(lp, vp);
        return stats;
    }

    private Button crearNavBtn(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        
        // Estilo cuando el botón está quieto
        String estiloNormal = "-fx-background-color:transparent; -fx-background-radius:10; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14; -fx-cursor:hand;";
        // Estilo cuando pasas el mouse
        String estiloHover = "-fx-background-color:rgba(0, 212, 170, 0.1); -fx-background-radius:10; -fx-text-fill:#00D4AA; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14; -fx-cursor:hand;";

        b.setStyle(estiloNormal);

        // --- ANIMACIÓN DE DESPLAZAMIENTO ---
        b.setOnMouseEntered(e -> {
            // Si el botón no está seleccionado, aplica el efecto
            if (!b.getStyle().contains("-fx-background-color:#00D4AA")) { 
                b.setStyle(estiloHover);
                b.setTranslateX(10); // <--- Aquí es donde "salta" a la derecha
            }
        });

        b.setOnMouseExited(e -> {
            // Al quitar el mouse, regresa a su lugar
            if (!b.getStyle().contains("-fx-background-color:#00D4AA")) {
                b.setStyle(estiloNormal);
                b.setTranslateX(0); 
            }
        });

        return b;
    }

    public void navegarA(String pagina) {
        pantallaInicio.getRoot().setVisible(false);
        pantallaDeteccion.getRoot().setVisible(false);
        pantallaConversacion.getRoot().setVisible(false);
        pantallaPuntaje.getRoot().setVisible(false);

        String[] keys = {"inicio", "deteccion", "conversacion", "puntaje"};
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(pagina)) {
                navBtns[i].setStyle("-fx-background-color:#00D4AA; -fx-background-radius:10; -fx-text-fill:#0A0F1E; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            } else {
                navBtns[i].setStyle("-fx-background-color:transparent; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            }
        }

        switch (pagina) {
            case "inicio":
                pantallaInicio.getRoot().setVisible(true);
                break;
            case "deteccion":
                pantallaDeteccion.getRoot().setVisible(true);
                break;
            case "conversacion":
                pantallaConversacion.getRoot().setVisible(true);
                break;
            case "puntaje":
                pantallaPuntaje.getRoot().setVisible(true);
                break;
        }
    }
}
