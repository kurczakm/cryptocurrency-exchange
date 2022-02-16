package com.crypto.tradingplatform.repository;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    Cryptocurrency findBySymbol(String symbol);
}
