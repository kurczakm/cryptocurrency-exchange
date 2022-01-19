package com.crypto.tradingplatform.web.controller;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.market.Market;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.service.UserServiceImpl;
import com.crypto.tradingplatform.service.WalletService;
import com.crypto.tradingplatform.web.detail.CustomUserDetails;
import com.crypto.tradingplatform.web.dto.OperationDto;
import com.crypto.tradingplatform.web.dto.UserUpdateDto;
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
        return userService.findByName(userDetails.getUsername());
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
        return "wallet";
    }

    @GetMapping("/trade")
    public String showTrade(Model model) {
        model.addAttribute("operation", new OperationDto());
        return "trade";
    }

    @PostMapping("/buy")
    public String buy(@ModelAttribute("operation") OperationDto operationDto) {
//        walletService.makeOperation(
//                user().getWallet().getId(),
//                cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
//                operationDto.getAmount(),
//                operationDto.getAmount().multiply(operationDto.getPrice()).negate()
//        );
//        walletService.buyCrypto(user().getWallet().getId(), operationDto);
        walletService.tryMakeOperation(user().getWallet().getId(), operationDto, true);
        return "redirect:trade?success";
    }

    @PostMapping("/sell")
    public String sell(@ModelAttribute("operation") OperationDto operationDto) {
//        walletService.makeOperation(
//                user().getWallet().getId(),
//                cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
//                operationDto.getAmount().negate(),
//                operationDto.getAmount().multiply(operationDto.getPrice())
//        );
//        System.out.println(operationDto);
//        walletService.sellCrypto(user().getWallet().getId(), operationDto);
        walletService.tryMakeOperation(user().getWallet().getId(), operationDto, false);
        return "redirect:trade?success";
    }

    @GetMapping("/ranking")
    public String showRanking(Model model) {
        Map<String, BigDecimal> ranking = walletService.getSortedWallets();
        model.addAttribute("wallets", ranking);
        return "ranking";
    }

    @GetMapping("/history")
    public String showHistory() {
        return "history";
    }

    @GetMapping("/account")
    public String showAccount() {
        return "account";
    }

    @GetMapping("/account/password")
    public String showPasswordForm(Model model) {
        model.addAttribute("updateUser", new UserUpdateDto());
        return "password";
    }

    @PostMapping("/account/password")
    public String changePassword(@ModelAttribute("updateUser") UserUpdateDto updatedUser) {
        updatedUser.setName(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return userService.updatePassword(updatedUser) != null ? "redirect:password?success" : "redirect:password?fail";
    }

    @GetMapping("/account/email")
    public String showEmailForm(Model model) {
        model.addAttribute("updateUser", new UserUpdateDto());
        return "email";
    }

    @PostMapping("/account/email")
    public String changeEmail(@ModelAttribute("updateUser") UserUpdateDto updatedUser) {
        updatedUser.setName(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return userService.updateEmail(updatedUser) != null ? "redirect:email?success" : "redirect:email?fail";
    }
}
