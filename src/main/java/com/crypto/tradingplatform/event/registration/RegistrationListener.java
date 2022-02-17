package com.crypto.tradingplatform.event.registration;

import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    ConfirmationMailSender confirmationMailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();

        confirmationMailSender.sendConfirmation(recipientAddress, event.getAppUrl(), token);
    }
}
