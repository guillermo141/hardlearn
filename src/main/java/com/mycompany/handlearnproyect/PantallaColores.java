
package com.mycompany.handlearnproyect;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class PantallaColores {
    private VBox root;
    private Label labelColor;

    public PantallaColores(App app) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0A0F1E;");

        Label titulo = new Label("MÓDULO DE RECONOCIMIENTO DE COLORES");
        titulo.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 14; -fx-font-weight: bold;");

        // Espacio para la cámara
        StackPane areaCamara = new StackPane();
        areaCamara.setPrefSize(640, 480);
        areaCamara.setMaxSize(640, 480);
        areaCamara.setStyle("-fx-background-color: #111827; -fx-border-color: #00D4AA; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-width: 2;");

        labelColor = new Label("Esperando color...");
        labelColor.setStyle("-fx-text-fill: #00D4AA; -fx-font-size: 50; -fx-font-family: 'Segoe UI Bold';");

        root.getChildren().addAll(titulo, areaCamara, labelColor);
        root.setVisible(false);
    }

    public void actualizarResultado(String color) {
        labelColor.setText(color.toUpperCase());
    }

    public Parent getRoot() { return root; }
}