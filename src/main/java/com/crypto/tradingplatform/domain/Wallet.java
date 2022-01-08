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

    @Column(name = "funds")
    @NotNull
    private BigDecimal funds;

    @ElementCollection
    @CollectionTable(name = "cryptocurrency_amount_mapping",
                    joinColumns = {@JoinColumn(name = "wallet_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "cryptocurrency_id")
    @Column(name = "amount")
    private Map<Cryptocurrency, BigDecimal> ownedCrypto;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Set<Operation> operations;

    @Transient
    private BigDecimal value;

    public Wallet() {
        ownedCrypto = new HashMap<>();
        operations = new LinkedHashSet<>();
    }

    public Wallet(BigDecimal funds, Map<Cryptocurrency, BigDecimal> ownedCrypto, Set<Operation> operations) {
        this.funds = funds;
        this.ownedCrypto = ownedCrypto;
        this.operations = operations;
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

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFunds() {
        return funds;
    }

    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }

    public Map<Cryptocurrency, BigDecimal> getOwnedCrypto() {
        return ownedCrypto;
    }

    public void setOwnedCrypto(Map<Cryptocurrency, BigDecimal> ownedCrypto) {
        this.ownedCrypto = ownedCrypto;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
