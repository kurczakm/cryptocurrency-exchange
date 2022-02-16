package com.crypto.tradingplatform.web.controller;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.market.Market;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.service.UserServiceImpl;
import com.crypto.tradingplatform.service.WalletService;
import com.crypto.tradingplatform.config.CustomUserDetails;
import com.crypto.tradingplatform.web.dto.OperationDto;
import com.crypto.tradingplatform.web.dto.UserUpdateDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/wallet")
    public String showWallet() {
        return "wallet";
    }

    @GetMapping("/trade")
    public String showTrade() {
        return "trade";
    }

    @GetMapping("/trade/cryptocurrency")
    public String showTradeCryptocurrency(@RequestParam(name = "symbol") String symbol, Model model) {
        Cryptocurrency cryptocurrency = cryptocurrencyRepository.findBySymbol(symbol);
        BigDecimal[] prices = cryptocurrencies().get(cryptocurrency);
        BigDecimal amount = user().getWallet().getOwnedCrypto().get(cryptocurrency);
        model.addAttribute("amount", amount);
        model.addAttribute("crypto", cryptocurrency);
        model.addAttribute("prices", prices);
        model.addAttribute("operation", new OperationDto());

        return "trade-cryptocurrency";
    }

    @PostMapping("/buy")
    public String buy(@ModelAttribute("operation") OperationDto operationDto, @RequestParam(name="symbol") String symbol) {
        walletService.tryMakeOperation(user().getWallet().getId(), operationDto, true);
        return "redirect:trade/cryptocurrency?symbol=" + symbol;
    }

    @PostMapping("/sell")
    public String sell(@ModelAttribute("operation") OperationDto operationDto, @RequestParam(name="symbol") String symbol) {
        walletService.tryMakeOperation(user().getWallet().getId(), operationDto, false);
        return "redirect:trade/cryptocurrency?symbol=" + symbol;
    }

    @GetMapping("/ranking")
    public String showRanking(Model model) {
        Map<String, BigDecimal> ranking = walletService.getSortedActiveWallets();
        model.addAttribute("wallets", ranking);
        return "ranking";
    }

    @GetMapping("/history")
    public String showHistory() {
        return "history";
    }

    @GetMapping("/settings")
    public String showAccount(Model model) {
        model.addAttribute("updateUser", new UserUpdateDto());
        return "settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@ModelAttribute("updateUser") UserUpdateDto updatedUser) {
        updatedUser.setName(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()); //try to change to user().getUsername()
        return userService.updatePassword(updatedUser) != null ? "redirect:/settings?password=success" : "redirect:/settings?password=fail";
    }

    @PostMapping("/settings/email")
    public String changeEmail(@ModelAttribute("updateUser") UserUpdateDto updatedUser) {
        updatedUser.setName(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()); //try to change to user().getUsername()
        return userService.updateEmail(updatedUser) != null ? "redirect:/settings?email=success" : "redirect:/settings?email=fail";
    }
}
