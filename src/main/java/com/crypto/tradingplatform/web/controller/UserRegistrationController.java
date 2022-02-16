package com.crypto.tradingplatform.web.controller;

import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.domain.VerificationToken;
import com.crypto.tradingplatform.event.registration.ConfirmationMailSender;
import com.crypto.tradingplatform.event.registration.OnRegistrationCompleteEvent;
import com.crypto.tradingplatform.service.UserServiceImpl;
import com.crypto.tradingplatform.web.dto.UserRegistrationDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import java.util.Calendar;
import java.util.UUID;

@Controller
public class UserRegistrationController {

    private UserServiceImpl userService;
    private ApplicationEventPublisher eventPublisher;
    private ServletContext servletContext;
    private ConfirmationMailSender confirmationMailSender;

    public UserRegistrationController(UserServiceImpl userService, ApplicationEventPublisher eventPublisher, ServletContext servletContext, ConfirmationMailSender confirmationMailSender) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.servletContext = servletContext;
        this.confirmationMailSender = confirmationMailSender;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
        String url = "redirect:/registration";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        boolean status = true;

        if (userService.findByName(userRegistrationDto.getName()) != null) {
            uriComponentsBuilder.queryParam("takenName");
            status = false;
        }
        if (userService.findByEmail(userRegistrationDto.getEmail()) != null) {
            uriComponentsBuilder.queryParam("takenEmail");
            status = false;
        }

        if(status) {
            User user = userService.save(userRegistrationDto);
            String appUrl = servletContext.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, user));
            if(user != null)
                return "redirect:/registration/confirm/verification?success";
            else
                uriComponentsBuilder.queryParam("fail");
        }

        return uriComponentsBuilder.build().toUriString();
    }

    @GetMapping("/registration/confirm")
    public String confirmRegistration (@RequestParam("token") String token) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null) {
            return "redirect:/registration/confirm/verification?error=invalid";
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return "redirect:/registration/confirm/verification?error=expired&token=" + token;
        }

        user.setEnabled(true);
        userService.save(user);

        userService.deleteVerificationToken(verificationToken);
        return "redirect:/registration/confirm/verification?activated";
    }

    @GetMapping("/registration/confirm/verification")
    public String showVerificationPage() {
        return "verification";
    }

    @GetMapping("/registration/confirm/resendLink")
    public String resendLink(@RequestParam("token") String token) {
        VerificationToken existingToken = userService.getVerificationToken(token);
        if (existingToken != null) {
            User user = existingToken.getUser();
            String appUrl = servletContext.getContextPath();
            String newToken = UUID.randomUUID().toString();
            userService.generateNewVerificationToken(user, existingToken, newToken);
            String email = user.getEmail();

            confirmationMailSender.sendConfirmation(email, appUrl, newToken);

            return "redirect:/registration/confirm/verification?resent=true";
        }

        return "redirect:/registration/confirm/verification?resent=false";
    }
}
