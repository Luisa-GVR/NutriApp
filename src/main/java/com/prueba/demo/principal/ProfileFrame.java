package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

@Component
public class ProfileFrame {
    //VBox principal
    @FXML
    private VBox rootPane;
    @FXML
    private Button profileButton;
    @FXML
    private ChoiceBox sexChoiceBox;
    @FXML
    private ChoiceBox allergiesChoiceBox;

    @FXML
    private TextArea ageTextArea;
    @FXML
    private TextArea heightTextArea;
    @FXML
    private TextArea weightTextArea;
    @FXML
    private TextArea abdomenTextArea;
    @FXML
    private TextArea hipTextArea;
    @FXML
    private TextArea waistTextArea;
    @FXML
    private TextArea neckTextArea;
    @FXML
    private TextArea armTextArea;
    @FXML
    private TextArea chestTextArea;



    //Metodos front
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #A3D13C;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #7DA12D;");
    }

    @FXML
    private void initialize() {
        //Variables de estilos originales

    }
}



