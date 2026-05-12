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
    // CORRECCIÓN 1: Cambiamos a tamaño 5 porque agregamos "Colores"
    private Button[] navBtns = new Button[5];
 
    private PantallaInicio pantallaInicio;
    private PantallaDeteccion pantallaDeteccion;
    private PantallaConversacion pantallaConversacion;
    private PantallaPuntaje pantallaPuntaje;
    private PantallaColores pantallaColores;
    private String puntosActuales = "0";
    private PantallaCuentaRegresiva pantallaCuenta;
 
    public App(Stage stage) {
        this.stage = stage;
    }
 
    public void mostrar() {
        HBox root = new HBox();
        root.setStyle("-fx-background-color:#0A0F1E;");
 
        // 1. Inicializar pantallas
        pantallaInicio       = new PantallaInicio(this);
        pantallaDeteccion    = new PantallaDeteccion(this);
        pantallaConversacion = new PantallaConversacion(this);
        pantallaPuntaje      = new PantallaPuntaje(this);
        pantallaColores      = new PantallaColores(this);
        pantallaCuenta       = new PantallaCuentaRegresiva(this);
 
        VBox sidebar = buildSidebar();
 
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:#0A0F1E;");
        HBox.setHgrow(contentArea, Priority.ALWAYS);
 
        // 2. Agregar raíces al contenedor
        contentArea.getChildren().addAll(
            pantallaPuntaje.getRoot(),
            pantallaConversacion.getRoot(),
            pantallaDeteccion.getRoot(),
            pantallaInicio.getRoot(),
            pantallaCuenta.getRoot(),
            pantallaColores.getRoot()
        );
 
        root.getChildren().addAll(sidebar, contentArea);
 
        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("HAND-LEARN - Lengua de Señas Mexicana");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
 
        navegarA("inicio");
        conectarConPython();
    }
 
    public void conectarConPython() {
        Thread hiloEscucha = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5005);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String linea;
                
                while ((linea = entrada.readLine()) != null) {
                    final String dato = linea;
                    Platform.runLater(() -> {
                        if (dato.startsWith("PUNTOS:")) {
                            this.puntosActuales = dato.replace("PUNTOS:", "");
                        } else if (dato.startsWith("[C]")) {
                            if (pantallaColores != null && pantallaColores.getRoot().isVisible()) {
                                pantallaColores.actualizarResultado(dato.replace("[C]", ""));
                            }
                        } else if (pantallaPuntaje.getRoot().isVisible()) {
                            pantallaPuntaje.actualizarResultado(dato.replace("[P]", ""));
                        } else if (pantallaConversacion.getRoot().isVisible()) {
                            pantallaConversacion.actualizarResultado(dato.replace("[L]", ""));
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Servidor Python no detectado.");
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

        VBox logoContainer = new VBox(-25); 
        logoContainer.setAlignment(Pos.CENTER_LEFT);
        logoContainer.setPadding(new Insets(10, 0, 40, 15));

        String estiloTexto = "-fx-font-family:'Segoe UI Bold'; -fx-font-size: 45; -fx-text-fill:#00D4AA; -fx-font-weight: bold;";

        Label labelHand = new Label("HAND");
        labelHand.setStyle(estiloTexto);
        Label labelLearn = new Label("LEARN");
        labelLearn.setStyle(estiloTexto);

        logoContainer.getChildren().addAll(labelHand, labelLearn);
        sb.getChildren().add(logoContainer);
        
        // CORRECCIÓN 2: El arreglo de items ahora tiene 5 elementos
        String[][] items = {
            {"inicio",      "Inicio"},
            {"deteccion",   "Conversación"},
            {"abecedario",  "Abecedario"},
            {"completar",   "Completar Palabras"},
            {"colores",     "Colores"}
        };

        for (int i = 0; i < items.length; i++) {
            final String key = items[i][0];
            final String nombre = items[i][1];
            Button btn = crearNavBtn(nombre);
            btn.setOnAction(e -> navegarA(key)); 

            navBtns[i] = btn; // Aquí era donde tronaba si el tamaño era 4
            sb.getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnEstadisticas = new Button("Ver Progreso");
        btnEstadisticas.setMaxWidth(Double.MAX_VALUE);
        btnEstadisticas.setStyle("-fx-background-color: #1A2235; -fx-text-fill: #00D4AA; -fx-border-color: #00D4AA; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-weight: bold; -fx-padding: 12; -fx-cursor: hand;");
        btnEstadisticas.setOnAction(e -> PantallaEstadisticas.mostrar(this.puntosActuales));

        Button salir = new Button("Salir");
        salir.setMaxWidth(Double.MAX_VALUE);
        salir.setStyle("-fx-background-color:transparent; -fx-border-color:#FF6B35; -fx-border-radius:10; -fx-text-fill:#FF6B35; -fx-font-weight:bold; -fx-padding:10; -fx-cursor:hand;");
        salir.setOnAction(e -> Platform.exit());

        sb.getChildren().addAll(spacer, new Separator(), btnEstadisticas, new Separator(), salir);
        return sb;
    }
 
    private Button crearNavBtn(String texto) {
        Button b = new Button(texto);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color:transparent; -fx-background-radius:10; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14; -fx-cursor:hand;");
        return b;
    }
 
    public void navegarA(String pagina) {
        pantallaInicio.getRoot().setVisible(false);
        pantallaDeteccion.getRoot().setVisible(false);
        pantallaConversacion.getRoot().setVisible(false);
        pantallaPuntaje.getRoot().setVisible(false);
        if (pantallaColores != null) pantallaColores.getRoot().setVisible(false);
        if (pantallaCuenta != null) pantallaCuenta.getRoot().setVisible(false);

        // CORRECCIÓN 3: Actualizamos las llaves de navegación a 5
        String[] keys = {"inicio", "deteccion", "abecedario", "completar", "colores"};
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(pagina)) {
                navBtns[i].setStyle("-fx-background-color:#00D4AA; -fx-background-radius:10; -fx-text-fill:#0A0F1E; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            } else {
                navBtns[i].setStyle("-fx-background-color:transparent; -fx-text-fill:#8895B3; -fx-font-size:13; -fx-font-weight:bold; -fx-padding:11 14;");
            }
        }

        switch (pagina) {
            case "inicio":      pantallaInicio.getRoot().setVisible(true); break;
            case "deteccion":   pantallaDeteccion.getRoot().setVisible(true); break;
            case "abecedario":  pantallaConversacion.getRoot().setVisible(true); break;
            case "completar":   pantallaPuntaje.getRoot().setVisible(true); break;
            case "colores":     pantallaColores.getRoot().setVisible(true); break;
        }
    }
}