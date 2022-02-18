package com.crypto.tradingplatform.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "funds", precision = 19, scale = 2)
    @NotNull
    private BigDecimal funds;

    @ElementCollection
    @CollectionTable(name = "cryptocurrency_amount_mapping",
                    joinColumns = {@JoinColumn(name = "wallet_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "cryptocurrency_id")
    @Column(name = "amount", precision = 19, scale = 8)
    private Map<Cryptocurrency, BigDecimal> ownedCrypto;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    @OrderBy(value = "date DESC")
    private Set<Operation> operations;

    @Transient
    private BigDecimal value;

    public Wallet() {
        ownedCrypto = new HashMap<>();
        operations = new LinkedHashSet<>();
    }

    public Wallet(BigDecimal funds, List<Cryptocurrency> cryptocurrencies) {
        this.funds = funds;
        this.ownedCrypto = new HashMap<>();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            ownedCrypto.put(cryptocurrency, BigDecimal.ZERO);
        }
        this.operations = new LinkedHashSet<>();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getFunds() {
        return funds;
    }

    public void addFunds(BigDecimal amountToAdd) {
        if(amountToAdd.compareTo(BigDecimal.ZERO) > 0)
            this.funds = this.funds.add(amountToAdd);
        else
            throw new IllegalArgumentException("Amount of funds to add must be greater than 0");
    }

    public void subtractFunds(BigDecimal amountToSubtract) {
        if(amountToSubtract.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount of funds to subtract must be greater than 0");

        if(amountToSubtract.compareTo(this.funds) <= 0)
            this.funds = this.funds.subtract(amountToSubtract);
        else
            throw new RuntimeException("Not enough funds");
    }

    public Map<Cryptocurrency, BigDecimal> getOwnedCrypto() {
        return ownedCrypto;
    }

    public void addCrypto(Cryptocurrency cryptocurrency, BigDecimal amountToAdd) {
        if(amountToAdd.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentAmount = this.ownedCrypto.get(cryptocurrency);
            this.ownedCrypto.put(cryptocurrency, currentAmount.add(amountToAdd));
        }
        else
            throw new IllegalArgumentException("Amount of cryptocurrency to add must be greater than 0");
    }

    public void subtractCrypto(Cryptocurrency cryptocurrency, BigDecimal amountToSubtract) {
        if(amountToSubtract.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount of crypto to subtract must be greater than 0");

        BigDecimal currentAmount = this.ownedCrypto.get(cryptocurrency);
        if(amountToSubtract.compareTo(currentAmount) <= 0)
            this.ownedCrypto.put(cryptocurrency, currentAmount.subtract(amountToSubtract));
        else
            throw new RuntimeException("Not enough crypto");
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
