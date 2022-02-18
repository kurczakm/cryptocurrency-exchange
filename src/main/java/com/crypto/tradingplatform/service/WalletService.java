package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.domain.Operation;
import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.domain.Wallet;
import com.crypto.tradingplatform.market.Market;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.repository.UserRepository;
import com.crypto.tradingplatform.repository.WalletRepository;
import com.crypto.tradingplatform.web.dto.OperationDto;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.math.NumberUtils;

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

    public Map<String, BigDecimal> getSortedActiveWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        Map<Cryptocurrency, BigDecimal[]> prices = market.getPrices();

        for (Wallet wallet : wallets) {
            wallet.setValue(wallet.getFunds());
            wallet.getOwnedCrypto().forEach((k,v) -> wallet.setValue(wallet.getValue().add(prices.get(k)[1].multiply(v))));
        }

        wallets.sort((w1, w2) -> w2.getValue().compareTo(w1.getValue()));

        Map<String, BigDecimal> ranking = new LinkedHashMap<>();

        for (Wallet wallet : wallets) {
            User user = userRepository.findByWalletId(wallet.getId());
            if (user.isEnabled())
                ranking.put(user.getName(), wallet.getValue());
        }

        return ranking;
    }

    //mode true = buy crypto / false = sell crypto
    public boolean makeOperation(Long walletId, Cryptocurrency cryptocurrency, BigDecimal amount, BigDecimal volume, Boolean mode) {
        boolean status = true;

        Wallet wallet = walletRepository.getById(walletId);
        BigDecimal ownedFunds = wallet.getFunds();
        BigDecimal ownedCryptoAmount = wallet.getOwnedCrypto().get(cryptocurrency);

        if(mode && volume.compareTo(ownedFunds) <= 0) {
            wallet.subtractFunds(volume);
            wallet.addCrypto(cryptocurrency, amount);

            Operation operation = new Operation(
                    cryptocurrency,
                    amount,
                    volume.negate(),
                    wallet.getOwnedCrypto().get(cryptocurrency),
                    wallet.getFunds(),
                    wallet
            );

            wallet.addOperation(operation);
            walletRepository.save(wallet);
        }
        else if(!mode && amount.compareTo(ownedCryptoAmount) <= 0) {
            wallet.subtractCrypto(cryptocurrency, amount);
            wallet.addFunds(volume);

            Operation operation = new Operation(
                    cryptocurrency,
                    amount.negate(),
                    volume,
                    wallet.getOwnedCrypto().get(cryptocurrency),
                    wallet.getFunds(),
                    wallet
            );

            wallet.addOperation(operation);
            walletRepository.save(wallet);
        }
        else {
            status = false;
        }

        return status;
    }

    //mode true = buy crypto / false = sell crypto
    public boolean tryMakeOperation(Long walletId, OperationDto operationDto, boolean mode) {
        boolean status = NumberUtils.isCreatable(operationDto.getAmount());
        if (status) {
            BigDecimal amount = new BigDecimal(operationDto.getAmount());
            if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.scale() <= 8) {
                BigDecimal volume = amount.multiply(operationDto.getPrice());

                //rounding
                if(mode)
                    volume = volume.setScale(2, RoundingMode.CEILING);
                else
                    volume = volume.setScale(2, RoundingMode.FLOOR);

                makeOperation(
                        walletId,
                        cryptocurrencyRepository.getById(operationDto.getCryptocurrencyId()),
                        amount,
                        volume,
                        mode
                );
            } else
                status = false;
        } else
            status = false;

        return status;
    }
}
