package com.prueba.demo;


import com.prueba.demo.repository.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Pruebita {

    // Usando la anotación @FXML para vincular los elementos con el FXML
    @FXML
    private Button startButton;

    @FXML
    private Button validateButton;

    @FXML
    private Label name;

    @FXML
    private TextField nameTextArea;

    @FXML
    private TextField validationCodeTextArea;

    @FXML
    private Label validationCode;

    @FXML
    private Label title;

    // Este método será ejecutado cuando el botón "Iniciar" sea presionado
    @FXML
    private void handleStartButtonAction() {
        // Aquí puedes añadir la lógica para el botón "Iniciar"
        System.out.println("Botón 'Iniciar' presionado.");
    }

    private UsuarioRepository repositorioUsuario;


    // Este método será ejecutado cuando el botón "Validar Código" sea presionado
    @FXML
    private void handleValidateButtonAction() {
        // Obtener el texto de los campos de texto
        String nameInput = nameTextArea.getText();
        String validationCodeInput = validationCodeTextArea.getText();

        // Mostrar los valores en la consola (puedes agregar la lógica de validación aquí)
        System.out.println("Nombre: " + nameInput);
        System.out.println("Código de validación: " + validationCodeInput);




    }
}
