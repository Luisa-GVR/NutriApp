package com.prueba.demo.principal;

import com.prueba.demo.model.Account;
import com.prueba.demo.model.AccountData;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.AccountRepository;
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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
    private AccountRepository accountRepository;

    @Autowired
    private AccountDataRepository accountDataRepository;

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

        if (verificationCode == null || verificationCode.isEmpty()) {
            verificationCode = readAndDecryptCode();
        }

        if (inputCode.equals(verificationCode)) {

            // Crear el objeto User
            Account account = new Account();
            AccountData accountData = new AccountData();

            account.setName(getName());
            account.setEmail(getEmail());

            // Si tienes datos para UserData, crearlos y asociarlos
            accountData.setWeight(0.0);
            accountData.setHeight(0.0);
            accountData.setGender(false);
            accountData.setAbdomen(null);
            accountData.setHips(null);
            accountData.setWaist(null);
            accountData.setArm(null);
            accountData.setChest(null);
            accountData.setGoal(null);

            // Establece la relación con el User
            account.setAccountData(accountData);

            accountRepository.save(account);

            // Guarda UserData después de haber asociado

            labelMessage.setStyle("-fx-text-fill: #7da12d;");
            labelMessage.setText("Código verificado. ¡Bienvenido!");
            deleteEncryptedCodeFiles();

            openProfileFrame();
        } else {

            labelMessage.setStyle("-fx-text-fill: b30000;");
            labelMessage.setText("Código de verificación incorrecto. Inténtalo nuevamente.");
            codeField.setStyle(originalStyleCode + " -fx-border-color: #b30000;");
        }
    }

    //borrar archivos
    private void deleteEncryptedCodeFiles() {
        try {
            File encryptedCodeFile = new File("src/main/resources/encrypted_code.txt");
            File keyFile = new File("src/main/resources/encryption_key.txt");

            // Elimina el archivo de código encriptado si existe
            if (encryptedCodeFile.exists()) {
                boolean deletedCode = encryptedCodeFile.delete();
                if (!deletedCode) {
                    System.out.println("No se pudo eliminar el archivo encrypted_code.txt");
                }
            }

            // Elimina el archivo de clave si existe
            if (keyFile.exists()) {
                boolean deletedKey = keyFile.delete();
                if (!deletedKey) {
                    System.out.println("No se pudo eliminar el archivo encryption_key.txt");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //leer el .txt

    private static final String KEY_FILE = "src/main/resources/encryption_key.txt";

    private SecretKey getOrCreateKey() {
        try {
            if (Files.exists(Paths.get(KEY_FILE))) {
                String encodedKey = new String(Files.readAllBytes(Paths.get(KEY_FILE)), StandardCharsets.UTF_8).trim();
                byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
                return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            }

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();

            // Guardar la clave en el archivo
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            Files.write(Paths.get(KEY_FILE), encodedKey.getBytes(StandardCharsets.UTF_8));

            return secretKey;

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String readAndDecryptCode() {
        try {
            File file = new File("src/main/resources/encrypted_code.txt");
            if (file.exists()) {
                SecretKey secretKey = getOrCreateKey(); // Obtiene o crea la clave de desencriptación
                String encryptedCode = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8).trim();
                return decrypt(encryptedCode, secretKey); // Desencripta el código
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedBytes));
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
