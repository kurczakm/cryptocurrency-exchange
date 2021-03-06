package com.crypto.tradingplatform.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "cryptocurrency")
public class Cryptocurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol", unique = true)
    @NotNull
    private String symbol;

    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    public Cryptocurrency() {
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cryptocurrency that = (Cryptocurrency) o;

        if (!id.equals(that.id)) return false;
        if (!symbol.equals(that.symbol)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + symbol.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
