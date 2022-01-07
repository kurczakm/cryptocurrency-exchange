package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.Operation;
import com.crypto.tradingplatform.domain.Wallet;
import com.crypto.tradingplatform.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public boolean makeOperation(Long walletId, Cryptocurrency cryptocurrency, BigDecimal amount, BigDecimal volume) {
        boolean status = true;

        Wallet wallet = walletRepository.getById(walletId);

        BigDecimal ownedFunds = wallet.getFunds();
        BigDecimal ownedCryptoAmount = wallet.getOwnedCrypto().get(cryptocurrency);

        BigDecimal newFunds = ownedFunds.add(volume);
        BigDecimal newCryptoAmount = ownedCryptoAmount.add(amount);

//        System.out.println("ownedFunds: " + ownedFunds);
//        System.out.println("ownedCryptoAmount: " + ownedCryptoAmount);

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
            walletRepository.save(wallet);
        } else {
            status = false;
        }

        return status;
    }

}
