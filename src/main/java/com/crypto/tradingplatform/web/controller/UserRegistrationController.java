package com.crypto.tradingplatform.web.controller;

import com.crypto.tradingplatform.service.UserServiceImpl;
import com.crypto.tradingplatform.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserServiceImpl userService;

    public UserRegistrationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
        String url = "redirect:/registration";
        boolean status = true;

        if (userService.findByName(userRegistrationDto.getName()) != null) {
            url += "?takenName";
            status = false;
        }
        if (userService.findByEmail(userRegistrationDto.getEmail()) != null) {
            if(status)
                url += "?takenEmail";
            else
                url += "&takenEmail";
            status = false;
        }

        if(status) {
            userService.save(userRegistrationDto);
            url += "?success";
        }

        return url;
    }
}
