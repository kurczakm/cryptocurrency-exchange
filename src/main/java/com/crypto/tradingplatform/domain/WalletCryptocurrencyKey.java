package com.crypto.tradingplatform.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WalletCryptocurrencyKey implements Serializable {

    private static final long serialVersionUID = 6018859393780421827L;

    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "cryptocurrency_id")
    private Long cryptocurrencyId;

    public WalletCryptocurrencyKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WalletCryptocurrencyKey that = (WalletCryptocurrencyKey) o;

        if (!walletId.equals(that.walletId)) return false;
        return cryptocurrencyId.equals(that.cryptocurrencyId);
    }

    @Override
    public int hashCode() {
        int result = walletId.hashCode();
        result = 31 * result + cryptocurrencyId.hashCode();
        return result;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getCryptocurrencyId() {
        return cryptocurrencyId;
    }

    public void setCryptocurrencyId(Long cryptocurrencyId) {
        this.cryptocurrencyId = cryptocurrencyId;
    }
}
