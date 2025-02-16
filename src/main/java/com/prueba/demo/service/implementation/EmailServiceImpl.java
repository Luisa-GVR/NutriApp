package com.prueba.demo.service.implementation;

import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(EmailDTO emailDTO) throws MessagingException {
        // Método para enviar solo correo con HTML
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDTO.getAddressee());
            helper.setSubject(emailDTO.getSubject());

            String contentHTML = emailDTO.getMessage();
            helper.setText(contentHTML, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }

    public void sendMailWithAttachment(EmailDTO emailDTO, File attachment) throws MessagingException {
        // Método para enviar correo con adjunto
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDTO.getAddressee());
            helper.setSubject(emailDTO.getSubject());

            String contentHTML = emailDTO.getMessage();
            helper.setText(contentHTML, true);

            // Añadir el archivo adjunto
            if (attachment != null && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo con adjunto: " + e.getMessage(), e);
        }
    }
}
