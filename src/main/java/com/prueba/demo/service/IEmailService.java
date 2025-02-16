package com.prueba.demo.service;

import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;

import java.io.File;

public interface IEmailService {

    public void sendMail(EmailDTO emailDTO) throws MessagingException;
    public void sendMailWithAttachment(EmailDTO emailDTO, File attachment) throws MessagingException;

}
