package com.mycompany.handlearnproyect;

import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.Animation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class PantallaDeteccion {
    private VBox root;
    private Label labelSena;

    public PantallaDeteccion(App app) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0A0F1E;");

        Label titulo = new Label("MÓDULO DE DETECCIÓN EN TIEMPO REAL");
        titulo.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 14; -fx-font-weight: bold;");

        // Área visual (Simulación de cámara)
        StackPane areaCamara = new StackPane();
        areaCamara.setPrefSize(640, 480);
        areaCamara.setMaxSize(640, 480);
        areaCamara.setStyle("-fx-background-color: #111827; -fx-border-color: #00D4AA; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-width: 2;");
        
        // EN PantallaDeteccion.java, DEBAJO DE areaCamara.setStyle:
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(1.5), areaCamara);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.setCycleCount(javafx.animation.Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        
        Label placeholder = new Label("conectando a pyhton...");
        placeholder.setStyle("-fx-text-fill: #1E2D45; -fx-font-size: 20;");
        areaCamara.getChildren().add(placeholder);

        // Etiqueta donde se mostrará la letra detectada
        labelSena = new Label("Esperando...");
        labelSena.setStyle("-fx-text-fill: #00D4AA; -fx-font-size: 60; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");

        root.getChildren().addAll(titulo, areaCamara, labelSena);
        root.setVisible(false);
    }

    // Este método lo llama la clase App cada vez que Python manda un dato
    public void actualizarResultado(String sena) {
        labelSena.setText(sena);
    }

    public Parent getRoot() {
        return root;
    }
}