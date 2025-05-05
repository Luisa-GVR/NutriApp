
package com.prueba.demo.principal;

import com.prueba.demo.model.Account;
import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import com.prueba.demo.repositoryFreeSQL.AccountFreeSQLRepository;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

@Component
public class LoginAdminFrame {
    //---Variables LoginAdminFrame---
    //Botones
    @FXML private Button loginButton;
    @FXML private Button backButton;
    @FXML private Button forgotPasswordButton;
    //Labels
    @FXML private Label labelMessage;

    //Text Fields
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    private String originalStyleEmail;
    private String originalStylePassword;
    private String originalStyleLabelMessage;


    //AUTOWIRED
    @Autowired
    private ApplicationContext applicationContext;

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
    @FXML
    private void handleMouseEnteredB(MouseEvent event) {
        Button button = (Button) event.getSource();
        String currentStyle = button.getStyle();

        // Elimina cualquier estilo previo de background-color para evitar duplicados
        String styleWithoutBackground = currentStyle.replaceAll("-fx-background-color: *[^;]+;", "").trim();

        // Aplica el nuevo estilo agregando solo el background
        button.setStyle(styleWithoutBackground + " -fx-background-color: #404040;");
    }


        @FXML
        private void handleMouseExitedB(MouseEvent event) {
            Button button = (Button) event.getSource();
            String currentStyle = button.getStyle();

            // Elimina cualquier estilo previo de background-color para evitar duplicados
            String styleWithoutBackground = currentStyle.replaceAll("-fx-background-color: *[^;]+;", "").trim();

            // Aplica el nuevo estilo agregando solo el background
            button.setStyle(styleWithoutBackground + " -fx-background-color: #262626;");
        }


    private void handleFieldClick() {
        labelMessage.setText("Ingresa tus datos");
        labelMessage.setStyle(originalStyleLabelMessage);
        emailField.setStyle(originalStyleEmail);
        passwordField.setStyle(originalStylePassword);
    }

    @Autowired
    AccountFreeSQLRepository accountFreeSQLRepository;

    @FXML
    private boolean validateFields() {
        String inputPassword = passwordField.getText().trim();
        String email = emailField.getText().trim();

        Optional<AccountFreeSQL> existingAccount = accountFreeSQLRepository.findByEmail(email);

        if (existingAccount.isPresent()) {
            String storedEncryptedPassword = existingAccount.get().getName();

            if (compareEncryptedPassword(inputPassword, storedEncryptedPassword)){
                return true;
            } else {
                labelMessage.setStyle(originalStyleLabelMessage + "-fx-text-fill: #b30000;");
                labelMessage.setText("Contraseña incorrecta. Inténtalo nuevamente.");
                emailField.setStyle(originalStyleEmail + " -fx-border-color: #b30000;");
                passwordField.setStyle(originalStylePassword + " -fx-border-color: #b30000;");
                return false;
            }

        }
        labelMessage.setStyle(originalStyleLabelMessage +  "-fx-text-fill: #b30000;");
        labelMessage.setText("Correo o contraseña incorrectos. Inténtalo nuevamente.");
        emailField.setStyle(originalStyleEmail + " -fx-border-color: #b30000;");
        passwordField.setStyle(originalStylePassword + " -fx-border-color: #b30000;");
        return false;
    }

    @FXML
    private void initialize() {

        //Estilos originales
        originalStyleLabelMessage = labelMessage.getStyle();
        originalStyleEmail = emailField.getStyle();
        originalStylePassword = passwordField.getStyle();

        //Llamar metodos, para cambiar estilos mediante eventos
        passwordField.setOnMouseClicked(event -> handleFieldClick());
        emailField.setOnMouseClicked(event -> handleFieldClick());
        // Verificar si ya existe un usuario validado

        // Configurar evento en el campo de generación de código
        loginButton.setOnAction(event -> {

            if(validateFields()){
                closeCurrentWindow();
                openDashboardAdminFrame();
            } else {

            }

        });
        backButton.setOnAction(event ->{
            closeCurrentWindow();
            openLoginFrame();
        });
        forgotPasswordButton.setOnAction(event -> {
            emailField.setStyle(originalStyleEmail);
            passwordField.setStyle(originalStylePassword);
            labelMessage.setStyle(originalStyleLabelMessage);

            try {
                sendForgotPassword();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            labelMessage.setText("Se envió la contraseña a tu correo.");
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> labelMessage.setText("Ingresa tus datos")); // Borra el mensaje
            pause.play();
        });
    }

    @Autowired
    private IEmailService iEmailService;
    @Autowired
    private TemplateEngine templateEngine;
    private void sendForgotPassword() throws Exception {
        // Enviar email
        Context context = new Context();

        String encryptedPassword = accountFreeSQLRepository.findByEmail("nutriappunison@gmail.com").get().getName();
        String decryptedPassword = decrypt(encryptedPassword, getAESKey());

        context.setVariable("password", decryptedPassword);

        String contentHTML = templateEngine.process("forgotPassword", context);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAddressee("nutriappunison@gmail.com");
        emailDTO.setSubject("Recuperación de contraseña");
        emailDTO.setMessage(contentHTML);

        iEmailService.sendMail(emailDTO);

    }

    // Método para encriptar un texto con AES
    private String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private SecretKey getAESKey() throws Exception {
        String keyString = "1234567890123456"; // Clave de 16 caracteres (AES-128)
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private boolean compareEncryptedPassword(String inputPassword, String storedEncryptedPassword) {
        try {
            SecretKey key = getAESKey();
            String encryptedInput = encrypt(inputPassword, key);
            return encryptedInput.equals(storedEncryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //decrypt

    private String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    private void closeCurrentWindow() {
        Platform.runLater(() -> {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        });
    }

    private void openLoginFrame() {
        Platform.runLater(() -> {
            try {
                // Obtener la ventana actual desde el stage principal
                Stage stage = (Stage) loginButton.getScene().getWindow();

                if (stage != null) {
                    stage.close(); // Cerrar la ventana actual
                }

                // Cargar la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginFrame.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***


                Scene scene = new Scene(loader.load());


                // Crear un nuevo Stage para la ventana principal
                Stage newStage = new Stage();
                newStage.setTitle("Inicio");
                newStage.setScene(scene);

                // Establecer el tamaño mínimo de la ventana principal
                newStage.setMinWidth(1000);  // Ancho mínimo de la ventana
                newStage.setMinHeight(660); // Alto mínimo de la ventana

                // Mostrar la nueva ventana
                newStage.show();

            } catch (Exception e) {
                e.printStackTrace();  // Para obtener más detalles sobre el error
                showAlert("Error", "No se pudo abrir la ventana principal.");
            }
        });
    }
    private void openDashboardAdminFrame() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardAdmin.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                Stage dashboardAdminStage = new Stage();
                dashboardAdminStage.setTitle("Dashboard administración");
                dashboardAdminStage.setScene(scene);

                File propertiesFile = new File("preferencesState.properties");
                Properties properties = new Properties();

                try {
                    if (!propertiesFile.exists()) {
                        propertiesFile.createNewFile();
                    }

                    properties.setProperty("logedAdmin", "true");
                    try (OutputStream outputStream = new FileOutputStream(propertiesFile)) {
                        properties.store(outputStream, null);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                dashboardAdminStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo abrir la ventana de dashboard.");
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
