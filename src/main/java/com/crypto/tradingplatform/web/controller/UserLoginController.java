package com.crypto.tradingplatform.web.controller;

import org.apache.catalina.util.ParameterMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class UserLoginController {

    @GetMapping
    public String showLoginForm() {
        return "login";
    }
}
