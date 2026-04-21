package com.mycompany.handlearnproyect;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PantallaInicio {
    private VBox root;

    public PantallaInicio(App app) {
        root = new VBox(20); // Espacio entre cabecera y cuerpo
        root.setAlignment(Pos.TOP_CENTER); // Alineamos todo al inicio superior
        root.setStyle("-fx-background-color: #0A0F1E;");
        root.setPadding(new Insets(20)); // Padding general para que nada toque la orilla

        // --- CONTENEDOR DE CABECERA (LOGOS EN LAS ORILLAS SUPERIORES) ---
        BorderPane cabeceraLogos = new BorderPane();
        cabeceraLogos.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho disponible
        cabeceraLogos.setPadding(new Insets(10, 30, 0, 30)); // Padding interno (Arriba, Derecha, Abajo, Izquierda)

        // Inicializamos los ImageViews
        ImageView imgTec = new ImageView();
        ImageView imgLibres = new ImageView();
        ImageView imgISC = new ImageView();

        try {
            // Logo TecNM
            Image logo1 = new Image(getClass().getResourceAsStream("/logo_tecnm.png"));
            imgTec.setImage(logo1);
            imgTec.setFitHeight(70);
            imgTec.setPreserveRatio(true);
            
            // --- CORRECCIÓN AQUÍ: Agregamos la "/" al inicio ---
            Image logo3 = new Image(getClass().getResourceAsStream("/ISC horizontal blanco.png"));
            imgISC.setImage(logo3);
            imgISC.setFitHeight(60); // Un poquito más pequeño para que no amontone el centro
            imgISC.setPreserveRatio(true);
            
            // Logo ITS Libres
            Image logo2 = new Image(getClass().getResourceAsStream("/logo-tec.png"));
            imgLibres.setImage(logo2);
            imgLibres.setFitHeight(70); 
            imgLibres.setPreserveRatio(true);
            
        } catch (Exception e) {
            // Esto te dirá exactamente qué falló en la consola
            System.out.println("Error al cargar logos: " + e.getMessage());
            e.printStackTrace(); 
        }

        // --- COLOCAR LOGOS EN LAS ORILLAS (BorderPane) ---
        cabeceraLogos.setCenter(imgISC);  // Logo ISC al centro supereior CENTRO
        cabeceraLogos.setLeft(imgTec);    // Logo TecNM a la esquina superior IZQUIERDA
        cabeceraLogos.setRight(imgLibres); // Logo ITS Libres a la esquina superior DERECHA

        // Alineación interna para que se peguen a las orillas superiores
        BorderPane.setAlignment(imgTec, Pos.TOP_LEFT);
        BorderPane.setAlignment(imgISC, Pos.TOP_CENTER);
        BorderPane.setAlignment(imgLibres, Pos.TOP_RIGHT);


        // --- CUERPO DE LA PANTALLA (SALUDO E INSTRUCCIONES) ---
        VBox cuerpo = new VBox(30); // Espacio entre saludo e instrucciones
        cuerpo.setAlignment(Pos.CENTER);
        VBox.setVgrow(cuerpo, javafx.scene.layout.Priority.ALWAYS); // Ocupa el espacio central restante

        // Crear el saludo central
        Label saludo = new Label("¡Hola, Amigo!");
        saludo.setStyle("-fx-text-fill: white; -fx-font-size: 36; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        // Texto de instrucciones
        Label instruccion = new Label("Exprésate con tus manos. Pulsa 'Detectar seña' para iniciar.");
        instruccion.setStyle("-fx-text-fill: #8895B3; -fx-font-size: 18; -fx-font-family: 'Segoe UI Semibold';");

        cuerpo.getChildren().addAll(saludo, instruccion);


        // --- AGREGAR TODO AL PANEL PRINCIPAL (root) ---
        root.getChildren().addAll(cabeceraLogos, cuerpo);
        root.setVisible(false);

        // --- ANIMACIÓN DE ENTRADA SUAVE ---
        // Animamos todo el root para que la cabecera aparezca elegantemente
        root.setOpacity(0); // Empezamos invisible
        FadeTransition ft = new FadeTransition(Duration.seconds(1.8), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public Parent getRoot() {
        return root;
    }
}

