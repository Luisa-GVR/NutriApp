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
    private UserRepository userRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private IEmailService iEmailService;
    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private SpringFXMLoader fxmlLoader; // Inject the loader


    @FXML private TextField nameField;
    @FXML private TextField emailField; // Add email field
    @FXML private TextField codeField;
    @FXML private Button generateCodeField;
    @FXML private Button loginButton;

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
                generateCodeVerification();
            } catch (MessagingException e) {
                showAlert("Error", "No se pudo enviar el correo.");
            }
        });

    }

    private void generateCodeVerification() throws MessagingException {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        verificationCode = String.valueOf(code);
        generatedCode = true;

        showAlert("Código Generado", "Envía el código a tu nutrióloga.");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String contentHTML = templateEngine.process("email", context);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAddressee("nutriappunison@gmail.com");
        emailDTO.setSubject("Código de verificación");
        emailDTO.setMessage(contentHTML);

        iEmailService.sendMail(emailDTO);

        closeCurrentWindow();

        // *** KEY CHANGE: Create and populate UserData ***
        UserData userData = applicationContext.getBean(UserData.class); // Get a new UserData instance
        userData.setVerificationCode(verificationCode);
        userData.setName(nameField.getText()); // Get name from TextField
        userData.setEmail(emailField.getText()); // Get email from TextField

        openValidationFrame(userData); // Pass UserData

    }    private void closeCurrentWindow() {
        Platform.runLater(() -> {
            Stage stage = (Stage) generateCodeField.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        });
    }
    private void openValidationFrame(UserData userData) { // Take UserData as argument
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = fxmlLoader.load("/ValidationFrame.fxml");
                ValidationFrame validationFrame = loader.getController();
                validationFrame.setUserData(userData); // *** THIS IS THE KEY! ***

                Stage validationStage = new Stage();
                validationStage.setTitle("Validación");
                validationStage.setScene(new Scene(loader.getRoot()));
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
