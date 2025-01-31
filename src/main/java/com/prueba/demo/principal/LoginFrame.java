package com.prueba.demo.principal;

import com.prueba.demo.model.Usuario;
import com.prueba.demo.repository.UsuarioRepository;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;

import java.util.Optional;
import java.util.Random;

@Component
public class LoginFrame {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private IEmailService iEmailService;

    @FXML private TextField nombreField;
    @FXML private TextField codigoField;
    @FXML private Button generarCodigoButton;
    @FXML private Button loginButton;

    private String codigoVerificacion;
    private boolean codigoGenerado = false;

    @FXML
    private void initialize() {
        // Verificar si ya existe un usuario validado
        Optional<Usuario> usuarioValido = usuarioRepository.findAll().stream()
                .filter(Usuario::isValidacion)
                .findFirst();

        if (usuarioValido.isPresent()) {
            abrirPrincipal();
        }

        generarCodigoButton.setOnAction(event -> {
            try {
                generarCodigoVerificacion();
            } catch (MessagingException e) {
                mostrarAlerta("Error", "No se pudo enviar el correo.");
            }
        });

        loginButton.setOnAction(event -> verificarCodigo());
    }

    private void generarCodigoVerificacion() throws MessagingException {
        Random random = new Random();
        int codigo = 10000 + random.nextInt(90000);
        codigoVerificacion = String.valueOf(codigo);
        codigoGenerado = true;

        mostrarAlerta("Código Generado", "Envía el código a tu nutrióloga.");

        Context context = new Context();
        context.setVariable("verificationCode", codigoVerificacion);
        String contentHTML = templateEngine.process("email", context);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setDestinatario("nutriappunison@gmail.com");
        emailDTO.setAsunto("Código de verificación");
        emailDTO.setMensaje(contentHTML);

        iEmailService.sendMail(emailDTO);
    }

    private void verificarCodigo() {
        if (!codigoGenerado) {
            mostrarAlerta("Error", "Genera el código primero.");
            return;
        }

        String nombre = nombreField.getText();
        String codigoIngresado = codigoField.getText();

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);

        if (codigoIngresado.equals(codigoVerificacion)) {
            usuario.setValidacion(true);
            usuarioRepository.save(usuario);
            mostrarAlerta("Éxito", "Usuario validado correctamente.");
            abrirPrincipal();
        } else {
            mostrarAlerta("Error", "Código incorrecto.");
        }
    }

    private void abrirPrincipal() {
        Platform.runLater(() -> {
            try {
                // Obtener la ventana actual desde el stage principal
                Stage stage = (Stage) loginButton.getScene().getWindow();
                if (stage != null) {
                    stage.close(); // Cerrar la ventana actual
                }

                // Cargar la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Principal.fxml"));
                Scene scene = new Scene(loader.load());

                Stage newStage = new Stage();
                newStage.setTitle("Principal");
                newStage.setScene(scene);
                newStage.show();
            } catch (Exception e) {
                e.printStackTrace();  // Para obtener más detalles sobre el error
                mostrarAlerta("Error", "No se pudo abrir la ventana principal.");
            }
        });
    }



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
