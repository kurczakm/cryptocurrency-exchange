package com.crypto.tradingplatform.domain;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @NotNull
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cryptocurrency_id", referencedColumnName = "id")
    @NotNull
    private Cryptocurrency cryptocurrency;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    @Column(name = "volume")
    @NotNull
    private BigDecimal volume;

    @Column(name = "crypto_saldo")
    @NotNull
    private BigDecimal cryptoSaldo;

    @Column(name = "funds_saldo")
    @NotNull
    private BigDecimal fundsSaldo;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @NotNull
    private Wallet wallet;

    public Operation() {
    }

    public Operation(Cryptocurrency cryptocurrency, BigDecimal amount, BigDecimal volume, BigDecimal cryptoSaldo, BigDecimal fundsSaldo, Wallet wallet) {
        this.cryptocurrency = cryptocurrency;
        this.amount = amount;
        this.volume = volume;
        this.cryptoSaldo = cryptoSaldo;
        this.fundsSaldo = fundsSaldo;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getCryptoSaldo() {
        return cryptoSaldo;
    }

    public void setCryptoSaldo(BigDecimal cryptoSaldo) {
        this.cryptoSaldo = cryptoSaldo;
    }

    public BigDecimal getFundsSaldo() {
        return fundsSaldo;
    }

    public void setFundsSaldo(BigDecimal fundsSaldo) {
        this.fundsSaldo = fundsSaldo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
