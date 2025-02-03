package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class DashboardFrame {

    @FXML private VBox dashboardPane;
    @FXML private VBox profilePane;
    @FXML private VBox dietPane;
    @FXML private Button dashboardButton;
    @FXML private Button profileButton;
    @FXML private Button dietButton;



    @FXML
    private void initialize() {
        // Asigna el evento manualmente al botón
        profilePane.setVisible(false);
        dietPane.setVisible(false);

        profileButton.setOnAction(event -> showProfile());
        dashboardButton.setOnAction(event -> showDashboard());
        dietButton.setOnAction(event -> showDiet());
    }
    //Funcionalidades visuales
    @FXML
    private void mouseEntered(javafx.scene.input.MouseEvent event) {
        // Aquí puedes cambiar el color de fondo del HBox, por ejemplo
        Node node = (Node) event.getSource();  // Obtiene la referencia al HBox clickeado

        node.setStyle("-fx-background-color: #404040;");  // Cambia el color de fondo a gris
    }
    @FXML
    private void mouseExited(javafx.scene.input.MouseEvent event) {
        // Aquí puedes cambiar el color de fondo del HBox, por ejemplo
        Node node = (Node) event.getSource();  // Obtiene la referencia al HBox clickeado
        node.setStyle("-fx-background-color: #262626;");  // Cambia el color de fondo a gris
    }


    @FXML
    private void showDashboard() {
        hideAll();
        dashboardPane.setVisible(true);
    }

    @FXML
    private void showProfile() {
        hideAll();
        profilePane.setVisible(true);
    }
    @FXML
    private void showDiet() {
        hideAll();
        dietPane.setVisible(true);
    }

    private void hideAll() {
        dashboardPane.setVisible(false);
        profilePane.setVisible(false);
        dietPane.setVisible(false);
    }
}
