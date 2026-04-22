package com.mycompany.handlearnproyect;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.scene.Group;

public class PantallaCuentaRegresiva {

    private VBox root;
    private Timeline timeline;
    private int segundosRestantes = 5;

    private Label lblNumero;
    private Label lblMensaje;
    private Arc arco;
    private Button btnCancelar;
    private Label lblListo;
    private Label lblSeccion;

    private String destinoPagina;
    private App app;

    public PantallaCuentaRegresiva(App app) {
        this.app = app;

        root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0A0F1E;"); // Fondo sólido para ver mejor el contraste
        root.setVisible(false);

        lblSeccion = new Label("");
        lblSeccion.setStyle("-fx-text-fill: #00D4AA; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblSub = new Label("El juego comenzará en...");
        lblSub.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 16px;");

        // --- ANILLO CON CORRECCIÓN GEOMÉTRICA ---
        Group contenedorDibujo = new Group(); // Usamos Group para que no haya márgenes invisibles
        double radio = 70;

        Circle pista = new Circle(radio);
        pista.setFill(Color.TRANSPARENT);
        pista.setStroke(Color.web("#1A2235"));
        pista.setStrokeWidth(12);
        // Forzamos el centro en 0,0
        pista.setCenterX(0);
        pista.setCenterY(0);

        arco = new Arc();
        arco.setCenterX(0); // Forzamos el centro exacto en 0
        arco.setCenterY(0);
        arco.setRadiusX(radio);
        arco.setRadiusY(radio);
        arco.setStartAngle(90);
        arco.setLength(-360);
        arco.setType(ArcType.OPEN);
        arco.setFill(Color.TRANSPARENT);
        arco.setStroke(Color.web("#00D4AA"));
        arco.setStrokeWidth(12);
        arco.setStrokeLineCap(StrokeLineCap.ROUND);

        lblNumero = new Label("5");
        lblNumero.setStyle("-fx-text-fill: white; -fx-font-size: 55px; -fx-font-weight: bold;");
        // Ajuste manual para que el texto flote justo en medio del Group
        lblNumero.setLayoutX(-35); 
        lblNumero.setLayoutY(-40);

        contenedorDibujo.getChildren().addAll(pista, arco, lblNumero);

        // Ponemos el Group dentro de un StackPane para darle espacio
        StackPane frame = new StackPane(contenedorDibujo);
        frame.setPrefSize(200, 200);

        lblMensaje = new Label("Prepárate...");
        lblMensaje.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 16px;");

        lblListo = new Label("¡COMENZA!");
        lblListo.setStyle("-fx-background-color: #00D4AA; -fx-text-fill: #0A0F1E; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 15 40; -fx-background-radius: 12;");
        lblListo.setVisible(false);

        btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-border-color: #1E2D45; -fx-text-fill: #8895B3; -fx-border-radius: 8; -fx-padding: 10 30; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> cancelar());

        root.getChildren().addAll(lblSeccion, lblSub, frame, lblMensaje, lblListo, btnCancelar);
    }

    public void preparar(String nombreSeccion, String destinoPagina) {
        this.destinoPagina = destinoPagina;
        segundosRestantes = 5;
        lblSeccion.setText(nombreSeccion.toUpperCase());
        lblNumero.setText("5");
        lblMensaje.setText("Prepárate...");
        lblMensaje.setVisible(true);
        lblListo.setVisible(false);
        btnCancelar.setVisible(true);
        arco.setStroke(Color.web("#00D4AA"));
        arco.setLength(-360);

        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(5);
        timeline.setOnFinished(e -> terminar());
        timeline.play();
    }

    private void tick() {
        segundosRestantes--;
        lblNumero.setText(String.valueOf(segundosRestantes));
        
        // Ajuste de posición del texto si es un solo dígito para que no se mueva
        if(segundosRestantes < 10) lblNumero.setLayoutX(-15);

        double proporcion = (double) segundosRestantes / 5;
        arco.setLength(-360 * proporcion);

        if (segundosRestantes <= 2) arco.setStroke(Color.web("#FF6B35"));
        else if (segundosRestantes <= 6) arco.setStroke(Color.web("#EF9F27"));
        else arco.setStroke(Color.web("#00D4AA"));
    }

    private void terminar() {
        lblNumero.setText("✓");
        lblNumero.setLayoutX(-20);
        lblMensaje.setVisible(false);
        btnCancelar.setVisible(false);
        lblListo.setVisible(true);

        PauseTransition pausa = new PauseTransition(Duration.millis(1000));
        pausa.setOnFinished(e -> app.navegarA(destinoPagina));
        pausa.play();
    }

    private void cancelar() {
        if (timeline != null) timeline.stop();
        app.navegarA("inicio");
    }

    public Parent getRoot() { return root; }
}