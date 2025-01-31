package com.prueba.demo.principal;

import com.prueba.demo.model.Usuario;
import com.prueba.demo.principal.Principal;
import com.prueba.demo.repository.UsuarioRepository;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.Random;

@Component
public class LoginFrame extends JFrame {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    IEmailService iEmailService;

    // Componentes Swing
    private JTextField nombreField;
    private JTextField codigoField;
    private JButton generarCodigoButton;
    private JButton loginButton;
    private String codigoVerificacion;
    private boolean codigoGenerado = false;

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Crear los componentes
        nombreField = new JTextField(20);
        codigoField = new JTextField(10);
        generarCodigoButton = new JButton("Generar Código");
        loginButton = new JButton("Verificar Código");

        // Panel para los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Código de Verificación:"));
        panel.add(codigoField);
        panel.add(generarCodigoButton);
        panel.add(loginButton);

        // Añadir los componentes al JFrame
        add(panel);

        // Evento del botón de generar código
        generarCodigoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generarCodigoVerificacion();  // Solo se genera el código al hacer clic
                } catch (MessagingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Evento del botón de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!codigoGenerado) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, genera el código primero.");
                } else {
                    verificarCodigo();
                }
            }
        });
    }

    @PostConstruct
    public void verificarUsuarioValido() {
        // Verificar si ya existe un usuario validado en la base de datos
        Optional<Usuario> usuarioValido = usuarioRepository.findAll().stream()
                .filter(Usuario::isValidacion)
                .findFirst();

        if (usuarioValido.isPresent()) {
            System.out.println("me");
            // Si hay un usuario validado, abrir la ventana principal directamente
            new Principal().setVisible(true);
            //dispose();  // Cerrar la ventana de login
        } else {
            setVisible(true);
        }
    }

    // Método para generar un código de verificación aleatorio de 5 dígitos
    private void generarCodigoVerificacion() throws MessagingException {
        Random random = new Random();
        int codigo = 10000 + random.nextInt(90000);  // Generar un código de 5 dígitos
        codigoVerificacion = String.valueOf(codigo);
        codigoGenerado = true;  // Marcar que el código fue generado
        JOptionPane.showMessageDialog(this, "Código de verificación generado. Enviale mensaje a tu nutriologa");

        Context context = new Context();
        context.setVariable("verificationCode", codigoVerificacion);
        String contentHTML = templateEngine.process("email", context);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setDestinatario("nutriappunison@gmail.com"); //cambiar por correo de la nutriologa
        emailDTO.setAsunto("Código de verificación");
        emailDTO.setMensaje(contentHTML);

        iEmailService.sendMail(emailDTO);

    }

    // Método para verificar el código ingresado por el usuario
    private void verificarCodigo() {
        String nombre = nombreField.getText();
        String codigoIngresado = codigoField.getText();

        // Aquí debes buscar el usuario en la base de datos
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);

        // Validar si el código ingresado es correcto
        if (codigoIngresado.equals(codigoVerificacion)) {
            usuario.setValidacion(true);
            // Guardar el usuario con validación en la base de datos
            JOptionPane.showMessageDialog(this, "¡Usuario validado exitosamente!");
            usuarioRepository.save(usuario);

            // Abrir la ventana principal de búsqueda de alimentos
            new Principal().setVisible(true);
            dispose();  // Cerrar la ventana de login
        } else {
            JOptionPane.showMessageDialog(this, "Código incorrecto. Intenta de nuevo.");
        }
    }


}
