package com.prueba.demo.principal;

import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import com.prueba.demo.repositoryFreeSQL.AccountFreeSQLRepository;
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
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class LoginAdminFrame {
    //---Variables LoginAdminFrame---
    //Botones
    @FXML private Button loginButton;
    @FXML private Button backButton;
    //Labels
    @FXML private Label labelMessage;

    //Text Fields
    @FXML private TextField emailField;
    private String originalStyleName;
    @FXML private TextField passwordField;
    @FXML private String originalStyleEmail;

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
    private void handleFieldClick() {
        labelMessage.setText("Ingresa tus datos");
        labelMessage.setStyle("-fx-text-fill: #7DA12D;");
        emailField.setStyle(originalStyleName);
        passwordField.setStyle(originalStyleEmail);
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
            return compareEncryptedPassword(inputPassword, storedEncryptedPassword);
        }

        return false;
    }



    @FXML
    private void initialize() {

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
                //aqui pon el errorlabel con el "contraseña o correo incorrecto" / setvisible uwu
            }

        });
        backButton.setOnAction(event ->{
            closeCurrentWindow();
            openLoginFrame();
        });
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
