package com.prueba.demo.principal;

import com.prueba.demo.model.User;
import com.prueba.demo.model.UserData;
import com.prueba.demo.repository.UserDataRepository;
import com.prueba.demo.repository.UserRepository;
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

@Component
public class ValidationFrame {
    //Variables Validation Frame
    //Botones
    @FXML
    private Button loginButton;
    //Labels
    @FXML
    private Label labelMessage;

    //TextFields
    @FXML
    private TextField codeField;
    private String originalStyleCode;


    //Autowired
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private static String verificationCode; // Código recibido de LoginFrame

    public static void setVerificationCode(String code) {
        verificationCode = code;
    }

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
    private void handleFieldClick() {
        labelMessage.setText("Ingresa el código de verificación enviado a tu nutrióloga");
        labelMessage.setStyle("-fx-text-fill: #7DA12D;");
        codeField.setStyle(originalStyleCode);
    }

    @FXML
    private void initialize() {
        //Variables de estilos originales
        originalStyleCode = codeField.getStyle();
        //Llamar metodos, para cambiar estilos mediante eventos
        codeField.setOnMouseClicked(event -> handleFieldClick());
        loginButton.setOnAction(event -> verifyCode());
    }

    private void verifyCode() {
        String inputCode = codeField.getText();

        if (inputCode.equals(verificationCode)) {

            // Crear el objeto User
            User user = new User();
            UserData userData = new UserData();

            user.setName(getName());
            user.setEmail(getEmail());

            // Si tienes datos para UserData, crearlos y asociarlos
            userData.setWeight(0.0);
            userData.setHeight(0.0);
            userData.setGender(false);
            userData.setAbdomen(null);
            userData.setHips(null);
            userData.setWaist(null);
            userData.setArm(null);
            userData.setChest(null);
            userData.setGoal(null);

            // Establece la relación con el User
            user.setUserData(userData);

            userRepository.save(user);

            // Guarda UserData después de haber asociado

            labelMessage.setStyle("-fx-text-fill: #7da12d;");
            labelMessage.setText("Código verificado. ¡Bienvenido!");
            openProfileFrame();
        } else {

            labelMessage.setStyle("-fx-text-fill: b30000;");
            labelMessage.setText("Código de verificación incorrecto. Inténtalo nuevamente.");
            codeField.setStyle(originalStyleCode + " -fx-border-color: #b30000;");
        }
    }


    private void openProfileFrame() {
        Platform.runLater(() -> {
            try {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close(); // Cerrar ventana de validación
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileFrame.fxml"));
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
