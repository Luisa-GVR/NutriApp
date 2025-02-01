package com.prueba.demo.principal;

import com.prueba.demo.model.User;
import com.prueba.demo.repository.UserRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ValidationFrame{


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @FXML private TextField codeField;
    @FXML private Button loginButton;


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

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private static String verificationCode; // Código recibido de LoginFrame

    public static void setVerificationCode(String code) {
        verificationCode = code;
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> verifyCode());
    }



    private void verifyCode() {
        String inputCode = codeField.getText();

        if (inputCode.equals(verificationCode)) {

            User user = new User();
            user.setName(getName());
            user.setEmail(getEmail());
            user.setValidation(true);

            userRepository.save(user);

            showAlert("Éxito", "Código validado correctamente.");
            openPrincipal();
        } else {
            showAlert("Error", "Código incorrecto. Intenta de nuevo.");
        }
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    private void openPrincipal() {
        Platform.runLater(() -> {
            try {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close(); // Cerrar ventana de validación
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Principal.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***

                Scene scene = new Scene(loader.load());

                Stage newStage = new Stage();
                newStage.setTitle("Principal");
                newStage.setScene(scene);
                newStage.setResizable(false);
                newStage.show();

            } catch (Exception e) {
                e.printStackTrace();
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
