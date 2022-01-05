package com.crypto.tradingplatform.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "funds")
    @NotNull
    private BigDecimal funds;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet")
    private Set<WalletCryptocurrency> ownedCrypto;

    @OneToMany(mappedBy = "wallet")
    private Set<Operation> operations;

    public Wallet() {
        ownedCrypto = new HashSet<>();
    }

    public Wallet(BigDecimal funds, Set<WalletCryptocurrency> ownedCrypto, Set<Operation> operations) {
        this.funds = funds;
        this.ownedCrypto = ownedCrypto;
        this.operations = operations;
    }

    public Wallet(BigDecimal funds, List<Cryptocurrency> cryptocurrencies) {
        this.funds = funds;
        this.ownedCrypto = new HashSet<>();

        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            ownedCrypto.add(new WalletCryptocurrency(this, cryptocurrency, BigDecimal.ZERO));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFunds() {
        return funds;
    }

    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public Set<WalletCryptocurrency> getOwnedCrypto() {
        return ownedCrypto;
    }

    public void setOwnedCrypto(Set<WalletCryptocurrency> ownedCrypto) {
        this.ownedCrypto = ownedCrypto;
    }

    public void add(Cryptocurrency cryptocurrency, BigDecimal amount) {
        ownedCrypto.add(new WalletCryptocurrency(this, cryptocurrency, amount));
    }
}
