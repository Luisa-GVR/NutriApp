package com.prueba.demo.principal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class ValidationFrame {

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
            showAlert("Éxito", "Código validado correctamente.");
            openPrincipal();
        } else {
            showAlert("Error", "Código incorrecto. Intenta de nuevo.");
        }
    }

    private void openPrincipal() {
        Platform.runLater(() -> {
            try {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close(); // Cerrar ventana de validación
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Principal.fxml"));
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
