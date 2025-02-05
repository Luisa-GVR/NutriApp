package com.prueba.demo.principal;

import com.prueba.demo.model.User;
import com.prueba.demo.repository.UserRepository;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;

import java.util.Optional;
import java.util.Random;

@Component
public class LoginFrame {
    //---Variables LoginFrame---
    //Botones
    @FXML private Button generateCodeField;
    //Labels
    @FXML private Label labelMessage;

    //Text Fields
    @FXML private TextField nameField;
    private String originalStyleName;
    @FXML private TextField emailField;
    @FXML private String originalStyleEmail;

    //AUTOWIRED
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IEmailService iEmailService;
    @Autowired

    private TemplateEngine templateEngine;
    @Autowired
    private UserRepository userRepository;


    private String verificationCode;
    private boolean generatedCode = false;

    //Métodos front
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
    private void handleFieldClick() {
        labelMessage.setText("Ingresa tus datos");
        labelMessage.setStyle("-fx-text-fill: #7DA12D;");
        nameField.setStyle(originalStyleName);
        emailField.setStyle(originalStyleEmail);
    }

    @FXML
    private void validarCampos() {
//---CHECAR SI LA LOGICA DE ERRORES DEL REGISTRO ESTA COMPLETA---
        if (emailField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
            labelMessage.setStyle("-fx-text-fill: b30000;");
            labelMessage.setText("Nombre o correo electrónico inválidos. Por favor, verifica e intenta nuevamente");
            nameField.setStyle(originalStyleName + " -fx-border-color: #b30000;");
            emailField.setStyle(originalStyleName + " -fx-border-color: #b30000;");
        }
    }

    @FXML
    private void initialize() {
        //Variables de estilos originales
        originalStyleEmail = emailField.getStyle();
        originalStyleName = nameField.getStyle();
        //Llamar metodos, para cambiar estilos mediante eventos
        emailField.setOnMouseClicked(event -> handleFieldClick());
        nameField.setOnMouseClicked(event -> handleFieldClick());
        // Verificar si ya existe un usuario validado
        Optional<User> existingUser = userRepository.findAll().stream().findFirst();
        if (existingUser.isPresent()) {
            openPrincipal();
        }
        // Configurar evento en el campo de generación de código
        generateCodeField.setOnAction(event -> {
            try {
                // Validar campos antes de continuar
                validarCampos();

                if (!isValidEmail(emailField.getText())) {
                    showAlert("Error", "Correo electrónico inválido. Debe terminar en @gmail.com, @hotmail.com u @outlook.com");
                    return;
                }
                generateCodeVerification();
            } catch (MessagingException e) {
                showAlert("Error", "No se pudo enviar el correo.");
            }
        });
    }
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[\\w-\\.]+@(?:gmail\\.com|hotmail\\.com|outlook\\.com)$");
    }

    private void generateCodeVerification() throws MessagingException {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        verificationCode = String.valueOf(code);
        generatedCode = true;

        showAlert("Código Generado", "Se ha enviado el código a tu nutrióloga.");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String contentHTML = templateEngine.process("email", context);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAddressee("nutriappunison@gmail.com");
        emailDTO.setSubject("Código de verificación");
        emailDTO.setMessage(contentHTML);

        iEmailService.sendMail(emailDTO);

        // Cerrar la ventana actual y abrir la de validación
        closeCurrentWindow();
        openValidationFrame();
    }
    private void closeCurrentWindow() {
        Platform.runLater(() -> {
            Stage stage = (Stage) generateCodeField.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        });
    }

    private void openValidationFrame() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ValidationFrame.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***
                Scene scene = new Scene(loader.load());

                ValidationFrame validationFrame = loader.getController();

                validationFrame.setVerificationCode(verificationCode);
                validationFrame.setName(nameField.getText());
                validationFrame.setEmail(emailField.getText());

                Stage validationStage = new Stage();
                validationStage.setTitle("Validación");
                validationStage.setScene(scene);

                validationStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo abrir la ventana de validación.");
            }
        });
    }

    private void openPrincipal() {
        Platform.runLater(() -> {
            try {
                // Obtener la ventana actual desde el stage principal
                Stage stage = (Stage) generateCodeField.getScene().getWindow();

                if (stage != null) {
                    stage.close(); // Cerrar la ventana actual
                }

                // Cargar la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***

                Scene scene = new Scene(loader.load());

                // Crear un nuevo Stage para la ventana principal
                Stage newStage = new Stage();
                newStage.setTitle("Principal");
                newStage.setScene(scene);

                // Establecer el tamaño mínimo de la ventana principal
                newStage.setMinWidth(900);  // Ancho mínimo de la ventana
                newStage.setMinHeight(520); // Alto mínimo de la ventana

                // Mostrar la nueva ventana
                newStage.show();

            } catch (Exception e) {
                e.printStackTrace();  // Para obtener más detalles sobre el error
                showAlert("Error", "No se pudo abrir la ventana principal.");
            }
        });
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
