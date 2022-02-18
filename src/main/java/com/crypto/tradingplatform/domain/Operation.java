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

    @Column(name = "amount", precision = 19, scale = 8)
    @NotNull
    private BigDecimal amount;

    @Column(name = "volume", precision = 19, scale = 2)
    @NotNull
    private BigDecimal volume;

    @Column(name = "crypto_saldo", precision = 19, scale = 8)
    @NotNull
    private BigDecimal cryptoSaldo;

    @Column(name = "funds_saldo", precision = 19, scale = 2)
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

    public Date getDate() {
        return date;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getCryptoSaldo() {
        return cryptoSaldo;
    }

    public BigDecimal getFundsSaldo() {
        return fundsSaldo;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
