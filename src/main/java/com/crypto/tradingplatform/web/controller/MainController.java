package com.crypto.tradingplatform.web.controller;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.market.Market;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.service.UserServiceImpl;
import com.crypto.tradingplatform.service.WalletService;
import com.crypto.tradingplatform.web.detail.CustomUserDetails;
import com.crypto.tradingplatform.web.dto.OperationDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class MainController {

    private Market market;
    private UserServiceImpl userService;
    private WalletService walletService;
    private CryptocurrencyRepository cryptocurrencyRepository;

    public MainController(UserServiceImpl userService, Market market, WalletService walletService, CryptocurrencyRepository cryptocurrencyRepository) {
        this.userService = userService;
        this.market = market;
        this.walletService = walletService;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @ModelAttribute("user")
    public User user() {
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(userDetails.getUsername());
    }

    @ModelAttribute("cryptocurrencies")
    public Map<Cryptocurrency, BigDecimal[]> cryptocurrencies() {
        return market.getPrices();
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/wallet")
    public String showWallet() {
        System.out.println(market.getPrices().toString());
        return "wallet";
    }

    @GetMapping("/trade")
    public String showTrade(Model model) {
//        walletService.makeOperation((long) 1, cryptocurrencyRepository.getById((long) 1), BigDecimal.ONE, new BigDecimal(-1));
        model.addAttribute("operation", new OperationDto());
        return "trade";
    }

    @PostMapping("/buy")
    public String buy(@ModelAttribute("operation") OperationDto operationDto) {
        walletService.makeOperation(
                user().getWallet().getId(),
                cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
                operationDto.getAmount(),
                operationDto.getAmount().multiply(operationDto.getPriceNumber()).negate()
        );
        System.out.println(operationDto);
        return "redirect:trade?success";
    }

    @PostMapping("/sell")
    public String sell(@ModelAttribute("operation") OperationDto operationDto) {
        walletService.makeOperation(
                user().getWallet().getId(),
                cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
                operationDto.getAmount().negate(),
                operationDto.getAmount().multiply(operationDto.getPriceNumber())
        );
        System.out.println(operationDto);
        return "redirect:trade?success";
    }
}
