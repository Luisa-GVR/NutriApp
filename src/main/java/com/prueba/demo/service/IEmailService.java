package com.prueba.demo.service;

import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {

    public void sendMail(EmailDTO emailDTO) throws MessagingException;
}
