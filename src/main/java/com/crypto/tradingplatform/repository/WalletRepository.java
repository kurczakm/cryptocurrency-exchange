package com.crypto.tradingplatform.repository;

import com.crypto.tradingplatform.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
