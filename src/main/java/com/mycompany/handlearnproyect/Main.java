/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.handlearnproyect;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creamos la instancia de tu clase App y le pasamos el Stage
        App app = new App(primaryStage);
        
        // Llamamos al método que construimos para armar la interfaz
        app.mostrar();
    }

    public static void main(String[] args) {
        // Este es el punto de entrada que busca NetBeans
        launch(args);
    }
}
