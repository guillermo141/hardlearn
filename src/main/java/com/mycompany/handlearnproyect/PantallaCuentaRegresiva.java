package com.mycompany.handlearnproyect;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class PantallaCuentaRegresiva {

    private VBox root;
    private Timeline timeline;
    private int segundosRestantes = 10;

    // Nodos que se actualizan durante la animación
    private Label lblNumero;
    private Label lblMensaje;
    private Arc arco;
    private Button btnCancelar;
    private Label lblListo;
    private Label lblSeccion;

    // Guardamos a dónde navegar al terminar y al cancelar
    private String destinoPagina;
    private App app;

    public PantallaCuentaRegresiva(App app) {
        this.app = app;

        root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: transparent;");
        root.setVisible(false);

        // Nombre de la sección
        lblSeccion = new Label("");
        lblSeccion.setStyle(
            "-fx-text-fill: #00D4AA;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;"
        );

        // Subtítulo
        Label lblSub = new Label("El juego comenzará en...");
        lblSub.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 15px;");

        // ── Anillo ────────────────────────────────────────────────────────
        double radio = 70;

        Circle pista = new Circle(radio);
        pista.setFill(Color.TRANSPARENT);
        pista.setStroke(Color.web("#1A2235"));
        pista.setStrokeWidth(10);

        arco = new Arc(0, 0, radio, radio, 90, -360);
        arco.setType(ArcType.OPEN);
        arco.setFill(Color.TRANSPARENT);
        arco.setStroke(Color.web("#00D4AA"));
        arco.setStrokeWidth(10);
        arco.setStrokeLineCap(StrokeLineCap.ROUND);

        lblNumero = new Label("10");
        lblNumero.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 52px;" +
            "-fx-font-weight: bold;"
        );

        StackPane anillo = new StackPane(pista, arco, lblNumero);
        anillo.setPrefSize(160, 160);
        anillo.setMaxSize(160, 160);

        // Mensaje dinámico
        lblMensaje = new Label("Prepárate...");
        lblMensaje.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 14px;");

        // Badge "¡A jugar!" (oculto al inicio)
        lblListo = new Label("¡A jugar!");
        lblListo.setStyle(
            "-fx-background-color: #00D4AA;" +
            "-fx-text-fill: #0A0F1E;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 32 12 32;" +
            "-fx-background-radius: 10;"
        );
        lblListo.setVisible(false);

        // Botón cancelar
        btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #1E2D45;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #8895B3;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 9 28 9 28;" +
            "-fx-cursor: hand;"
        );
        btnCancelar.setOnMouseEntered(e -> btnCancelar.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #FF6B35;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #FF6B35;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 9 28 9 28;" +
            "-fx-cursor: hand;"
        ));
        btnCancelar.setOnMouseExited(e -> btnCancelar.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #1E2D45;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #8895B3;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 9 28 9 28;" +
            "-fx-cursor: hand;"
        ));
        btnCancelar.setOnAction(e -> cancelar());

        root.getChildren().addAll(
            lblSeccion, lblSub, anillo,
            lblMensaje, lblListo, btnCancelar
        );
    }

    /**
     * Llama este método antes de hacer visible la pantalla.
     *
     * @param nombreSeccion  Texto que se muestra arriba (ej. "DETECTAR SEÑA")
     * @param destinoPagina  Clave de navegación al terminar (ej. "deteccion")
     */
    public void preparar(String nombreSeccion, String destinoPagina) {
        this.destinoPagina = destinoPagina;

        // Resetear estado visual
        segundosRestantes = 10;
        lblSeccion.setText(nombreSeccion.toUpperCase());
        lblNumero.setText("10");
        lblMensaje.setText("Prepárate...");
        lblMensaje.setVisible(true);
        lblListo.setVisible(false);
        btnCancelar.setVisible(true);
        arco.setStroke(Color.web("#00D4AA"));
        arco.setLength(-360);

        // Iniciar cuenta regresiva
        if (timeline != null) timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(10);
        timeline.setOnFinished(e -> terminar());
        timeline.play();
    }

    public void detener() {
        if (timeline != null) timeline.stop();
    }

    public Parent getRoot() {
        return root;
    }

    // ── Lógica interna ────────────────────────────────────────────────────

    private void tick() {
        segundosRestantes--;
        lblNumero.setText(String.valueOf(segundosRestantes));

        // Actualizar arco
        double proporcion = (double) segundosRestantes / 10;
        arco.setLength(-360 * proporcion);

        // Color según urgencia
        if (segundosRestantes <= 3) {
            arco.setStroke(Color.web("#FF6B35")); // rojo/naranja
        } else if (segundosRestantes <= 6) {
            arco.setStroke(Color.web("#EF9F27")); // amarillo
        } else {
            arco.setStroke(Color.web("#00D4AA")); // verde
        }

        // Mensaje dinámico
        if (segundosRestantes <= 1)      
            lblMensaje.setText("¡Ahora!");
        else if (segundosRestantes <= 3) 
            lblMensaje.setText("¡Ya casi!");
        else                             
            lblMensaje.setText("Prepárate...");
    }

    private void terminar() {
        lblNumero.setText("✓");
        lblMensaje.setVisible(false);
        btnCancelar.setVisible(false);
        lblListo.setVisible(true);

        // Pequeña pausa antes de navegar
        PauseTransition pausa = new PauseTransition(Duration.millis(800));
        pausa.setOnFinished(e -> app.navegarA(destinoPagina));
        pausa.play();
    }

    private void cancelar() {
        detener();
        app.navegarA("inicio");
    }
}