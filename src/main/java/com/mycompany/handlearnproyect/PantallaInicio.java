
package com.mycompany.handlearnproyect;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PantallaInicio {
    private VBox root;

    public PantallaInicio(App app) {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0A0F1E;");

        Label saludo = new Label("¡Hola, Guillermo!");
        saludo.setStyle("-fx-text-fill: white; -fx-font-size: 32; -fx-font-weight: bold;");

        Label instruccion = new Label("Selecciona 'Detectar seña' para comenzar a practicar.");
        instruccion.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 16;");

        root.getChildren().addAll(saludo, instruccion);
        root.setVisible(false);
    }

    public Parent getRoot() {
        return root;
    }
}

