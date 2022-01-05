package com.crypto.tradingplatform.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_cryptocurrency")
public class WalletCryptocurrency {

    @EmbeddedId
    private WalletCryptocurrencyKey id;

    @ManyToOne
    @MapsId("walletId")
    @JoinColumn(name = "wallet_id")
    @NotNull
    private Wallet wallet;

    @ManyToOne
    @MapsId("cryptocurrencyId")
    @JoinColumn(name = "cryptocurrency_id")
    @NotNull
    private Cryptocurrency cryptocurrency;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    public WalletCryptocurrency() {
        id = new WalletCryptocurrencyKey();
    }

    public WalletCryptocurrency(Wallet wallet, Cryptocurrency cryptocurrency, BigDecimal amount) {
        id = new WalletCryptocurrencyKey();
        this.wallet = wallet;
        this.cryptocurrency = cryptocurrency;
        this.amount = amount;
    }

    public WalletCryptocurrencyKey getId() {
        return id;
    }

    public void setId(WalletCryptocurrencyKey id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
