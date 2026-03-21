/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.handlearnproyect;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class PantallaPuntaje {
    // Cambia el nombre a PantallaPuntaje para la otra clase
    private VBox root;

    public PantallaPuntaje(App app) {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0A0F1E;");

        Label titulo = new Label("PRÓXIMAMENTE: MÓDULO DE CONVERSACIÓN");
        titulo.setStyle("-fx-text-fill: #FF6B35; -fx-font-size: 18; -fx-font-weight: bold;");

        root.getChildren().add(titulo);
        root.setVisible(false);
    }

    public Parent getRoot() {
        return root;
    }
}
