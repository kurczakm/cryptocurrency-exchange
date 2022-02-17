package com.crypto.tradingplatform.event.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationMailSender {

    private JavaMailSender mailSender;
    private final String SUBJECT = "Registration Confirmation";
    private final String MESSAGE = "Confirm your address email.";

    @Autowired
    public ConfirmationMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmation(String addressEmail, String appUrl, String token) {
        String confirmationUrl = appUrl + "/registration/confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(addressEmail);
        email.setSubject(SUBJECT);
        email.setText(MESSAGE + "\r\n" + "http://localhost:8080" + confirmationUrl);

        mailSender.send(email);
    }
}
