/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.handlearnproyect;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PantallaEstadisticas {
    public static void mostrar(String puntos) {
        Stage ventana = new Stage();
        
        // Bloquea la ventana principal hasta que se cierre esta
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Mi Progreso");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #0A0F1E; -fx-padding: 20; -fx-alignment: center; " +
                        "-fx-border-color: #00D4AA; -fx-border-width: 2; -fx-border-radius: 10;");

        Label txt = new Label("PUNTOS ACUMULADOS");
        txt.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 12; -fx-font-weight: bold;");

        Label num = new Label(puntos);
        num.setStyle("-fx-text-fill: #00D4AA; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: 'Courier New';");

        layout.getChildren().addAll(txt, num);
        
        Scene escena = new Scene(layout, 280, 180);
        ventana.setScene(escena);
        ventana.show();
    }
}
