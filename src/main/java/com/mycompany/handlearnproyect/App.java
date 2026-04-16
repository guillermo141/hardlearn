package com.mycompany.handlearnproyect;
 
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.application.Platform;
import java.io.*;
import java.net.*;
import javafx.scene.image.ImageView;
 
public class App {
 
    private final Stage stage;
    private StackPane contentArea;
    private Button[] navBtns = new Button[4];
 
    // Clases de las pantallas
    private PantallaInicio pantallaInicio;
    private PantallaDeteccion pantallaDeteccion;
    private PantallaConversacion pantallaConversacion;
    private PantallaPuntaje pantallaPuntaje;
 
    // ── NUEVO: pantalla de cuenta regresiva ──────────────────────────────
    private PantallaCuentaRegresiva pantallaCuenta;
 
    public App(Stage stage) {
        this.stage = stage;
    }
 
    public void mostrar() {
        HBox root = new HBox();
        root.setStyle("-fx-background-color:#0A0F1E;");
 
        // 1. Inicializar pantallas
        pantallaInicio      = new PantallaInicio(this);
        pantallaDeteccion   = new PantallaDeteccion(this);
        pantallaConversacion = new PantallaConversacion(this);
        pantallaPuntaje     = new PantallaPuntaje(this);
 
        // ── NUEVO ────────────────────────────────────────────────────────
        pantallaCuenta = new PantallaCuentaRegresiva(this);
 
        VBox sidebar = buildSidebar();
 
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:#0A0F1E;");
        HBox.setHgrow(contentArea, Priority.ALWAYS);
 
        // 2. Agregar raíces al contenedor principal
        //    ── NUEVO: agrega pantallaCuenta también ──
        contentArea.getChildren().addAll(
            pantallaPuntaje.getRoot(),
            pantallaConversacion.getRoot(),
            pantallaDeteccion.getRoot(),
            pantallaInicio.getRoot(),
            pantallaCuenta.getRoot()   // <-- NUEVO
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
 
    // --- SISTEMA DE COMUNICACIÓN CON PYTHON (sin cambios) ---
    public void conectarConPython() {
        Thread hiloEscucha = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5005);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String linea;
                while ((linea = entrada.readLine()) != null) {
                    final String senaRecibida = linea;
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
        VBox sb = new VBox(15);
        sb.setPrefWidth(260);
        sb.setPadding(new Insets(20, 15, 18, 15));
        sb.setStyle("-fx-background-color:#111827; -fx-border-color:#1E2D45; -fx-border-width:0 1 0 0;");
 
        HBox logo = new HBox(15);
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setPadding(new Insets(10, 0, 30, 10));
 
        Label icono = new Label("👋");
        icono.setStyle("-fx-font-size: 24;");
 
        Label texto = new Label("HAND-LEARN");
        texto.setStyle("-fx-font-family:'Segoe UI Bold', 'Arial'; -fx-font-size: 24; -fx-text-fill:#00D4AA; -fx-letter-spacing: 1px; -fx-effect: dropshadow(gaussian, rgba(0, 212, 170, 0.5), 10, 0, 0, 0);");
 
        logo.getChildren().addAll(icono, texto);
        sb.getChildren().add(logo);
 
        // ── CAMBIO: los botones del menú ahora llaman a mostrarCuenta() ──
        // Solo "Inicio" navega directo, los demás pasan por la cuenta regresiva
        String[][] items = {
            {"inicio",      "Inicio"},
            {"deteccion",   "Detectar seña"},
            {"conversacion","Conversar"},
            {"puntaje",     "Mi progreso"}
        };
 
        for (int i = 0; i < items.length; i++) {
            final String key    = items[i][0];
            final String nombre = items[i][1];
            Button btn = crearNavBtn(nombre);
 
            if (key.equals("inicio")) {
                // Inicio va directo, sin cuenta regresiva
                btn.setOnAction(e -> navegarA("inicio"));
            } else {
                // Las demás secciones pasan por la cuenta regresiva
                btn.setOnAction(e -> mostrarCuenta(nombre, key));
            }
 
            navBtns[i] = btn;
            sb.getChildren().add(btn);
        }
 
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
 
        VBox stats = buildStatsPanel();
 
        Button salir = new Button("Salir");
        salir.setMaxWidth(Double.MAX_VALUE);
        salir.setStyle("-fx-background-color:transparent; -fx-border-color:#FF6B35; -fx-border-radius:10; -fx-text-fill:#FF6B35; -fx-font-weight:bold; -fx-padding:10; -fx-cursor:hand;");
        salir.setOnAction(e -> Platform.exit());
 
        sb.getChildren().addAll(spacer, new Separator(), stats, new Separator(), salir);
        return sb;
    }
 
    /**
     * ── NUEVO MÉTODO ─────────────────────────────────────────────────────
     * Muestra la cuenta regresiva antes de entrar a una sección.
     *
     * @param nombreSeccion  Texto visible en la pantalla (ej. "Detectar seña")
     * @param destinoPagina  Clave de navegación al terminar (ej. "deteccion")
     */
    private void mostrarCuenta(String nombreSeccion, String destinoPagina) {
        // Ocultar todo
        pantallaInicio.getRoot().setVisible(false);
        pantallaDeteccion.getRoot().setVisible(false);
        pantallaConversacion.getRoot().setVisible(false);
        pantallaPuntaje.getRoot().setVisible(false);
 
        // Resaltar el botón correspondiente en el sidebar
        String[] keys = {"inicio", "deteccion", "conversacion", "puntaje"};
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(destinoPagina)) {
                navBtns[i].setStyle("-fx-background-color:#00D4AA; -fx-background-radius:10; -fx-text-fill:#0A0F1E; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            } else {
                navBtns[i].setStyle("-fx-background-color:transparent; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            }
        }
 
        // Mostrar y arrancar la cuenta regresiva
        pantallaCuenta.getRoot().setVisible(true);
        pantallaCuenta.preparar(nombreSeccion, destinoPagina);
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
 
        String estiloNormal = "-fx-background-color:transparent; -fx-background-radius:10; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14; -fx-cursor:hand;";
        String estiloHover  = "-fx-background-color:rgba(0, 212, 170, 0.1); -fx-background-radius:10; -fx-text-fill:#00D4AA; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14; -fx-cursor:hand;";
 
        b.setStyle(estiloNormal);
 
        b.setOnMouseEntered(e -> {
            if (!b.getStyle().contains("-fx-background-color:#00D4AA")) {
                b.setStyle(estiloHover);
                b.setTranslateX(10);
            }
        });
        b.setOnMouseExited(e -> {
            if (!b.getStyle().contains("-fx-background-color:#00D4AA")) {
                b.setStyle(estiloNormal);
                b.setTranslateX(0);
            }
        });
 
        return b;
    }
 
    // navegarA() sin cambios — sigue siendo llamado por PantallaCuentaRegresiva
    // al terminar la cuenta, y por el botón Inicio directamente.
    public void navegarA(String pagina) {
        pantallaInicio.getRoot().setVisible(false);
        pantallaDeteccion.getRoot().setVisible(false);
        pantallaConversacion.getRoot().setVisible(false);
        pantallaPuntaje.getRoot().setVisible(false);
        pantallaCuenta.getRoot().setVisible(false);  // <-- NUEVO: ocultar cuenta también
 
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