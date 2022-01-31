package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.Operation;
import com.crypto.tradingplatform.domain.Wallet;
import com.crypto.tradingplatform.market.Market;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.repository.UserRepository;
import com.crypto.tradingplatform.repository.WalletRepository;
import com.crypto.tradingplatform.web.dto.OperationDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WalletService {

    private WalletRepository walletRepository;
    private Market market;
    private UserRepository userRepository;
    private CryptocurrencyRepository cryptocurrencyRepository;

    public WalletService(WalletRepository walletRepository, Market market, UserRepository userRepository, CryptocurrencyRepository cryptocurrencyRepository) {
        this.walletRepository = walletRepository;
        this.market = market;
        this.userRepository = userRepository;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    public Map<String, BigDecimal> getSortedWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        Map<Cryptocurrency, BigDecimal[]> prices = market.getPrices();

        for (Wallet wallet : wallets) {
            wallet.setValue(wallet.getFunds());
            wallet.getOwnedCrypto().forEach((k,v) -> wallet.setValue(wallet.getValue().add(prices.get(k)[1].multiply(v))));
        }

        wallets.sort((w1, w2) -> w2.getValue().compareTo(w1.getValue()));

        Map<String, BigDecimal> ranking = new LinkedHashMap<>();

        for (Wallet wallet : wallets) {
            ranking.put(userRepository.findByWalletId(wallet.getId()).getName(), wallet.getValue());
        }

        return ranking;
    }

    public boolean makeOperation(Long walletId, Cryptocurrency cryptocurrency, BigDecimal amount, BigDecimal volume) {
        boolean status = true;

        Wallet wallet = walletRepository.getById(walletId);

        BigDecimal ownedFunds = wallet.getFunds();
        BigDecimal ownedCryptoAmount = wallet.getOwnedCrypto().get(cryptocurrency);

        BigDecimal newFunds = ownedFunds.add(volume);
        BigDecimal newCryptoAmount = ownedCryptoAmount.add(amount);

        if (newFunds.compareTo(BigDecimal.ZERO) >= 0 && newCryptoAmount.compareTo(BigDecimal.ZERO) >= 0) {
            Operation operation = new Operation();
            operation.setCryptocurrency(cryptocurrency);
            operation.setAmount(amount);
            operation.setVolume(volume);
            operation.setCryptoSaldo(newCryptoAmount);
            operation.setFundsSaldo(newFunds);
            operation.setWallet(wallet);
            wallet.setFunds(newFunds);
            wallet.getOwnedCrypto().put(cryptocurrency, newCryptoAmount);
            wallet.getOperations().add(operation);
            walletRepository.save(wallet);

        } else {
            status = false;
        }

        return status;
    }

    //mode true = buy crypto / false = sell crypto
    public boolean tryMakeOperation(Long walletId, OperationDto operationDto, boolean mode) {
        boolean status = true;
        if (operationDto.isCorrectInput()) {
            BigDecimal amount = new BigDecimal(operationDto.getAmount());
            if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.scale() <= 8) {
                BigDecimal volume = amount.multiply(operationDto.getPrice());
                if (mode)
                    volume = volume.negate();
                else
                    amount = amount.negate();

                //rounding
                volume = volume.setScale(2, RoundingMode.FLOOR);

                System.out.println("VOL: " + volume);

                makeOperation(
                        walletId,
                        cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
                        amount,
                        volume
                );
            } else
                status = false;
        } else
            status = false;
        return status;
    }
}
