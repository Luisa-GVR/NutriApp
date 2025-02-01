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


    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private IEmailService iEmailService;

    @FXML private TextField nameField;
    @FXML private TextField codeField;
    @FXML private Button generateCodeField;
    @FXML private Button loginButton;

    @FXML private TextField emailField;

    private String verificationCode;
    private boolean generatedCode = false;


    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #5E7922;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #7DA12D;");
    }

    @Autowired
    private UserRepository userRepository;
    @FXML
    private void initialize() {



        // Verificar si ya existe un usuario validado
        Optional<User> validUser = userRepository.findAll().stream()
                .filter(User::isValidation)
                .findFirst();

        if (validUser.isPresent()) {
            openPrincipal();
        }

        generateCodeField.setOnAction(event -> {
            try {

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

    @Autowired
    private ApplicationContext applicationContext;

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
                validationStage.setResizable(false);
                validationStage.setMaximized(false);
                validationStage.setIconified(false);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Principal.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***

                Scene scene = new Scene(loader.load());

                // Crear un nuevo Stage para la ventana principal
                Stage newStage = new Stage();
                newStage.setTitle("Principal");
                newStage.setScene(scene);

                // Desactivar redimensionamiento, maximización y minimización
                newStage.setResizable(false);  // Desactiva redimensionamiento
                newStage.setMaximized(false);  // Desactiva maximización
                newStage.setIconified(false);  // Desactiva minimización

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
