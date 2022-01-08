package com.crypto.tradingplatform.web.dto;

import com.crypto.tradingplatform.domain.Cryptocurrency;

import java.math.BigDecimal;

public class OperationDto {

    private BigDecimal amount;
    private Long cryptocurrencyId;
    private String price;

    public OperationDto() {
        price = "0";
    }

    public OperationDto(BigDecimal amount, Long cryptocurrencyId, String price) {
        this.amount = amount;
        this.cryptocurrencyId = cryptocurrencyId;
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCryptocurrencyId() {
        return cryptocurrencyId;
    }

    public void setCryptocurrencyId(Long cryptocurrencyId) {
        this.cryptocurrencyId = cryptocurrencyId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BigDecimal getPriceNumber() {
        return new BigDecimal(price);
    }

    @Override
    public String toString() {
        return "OperationDto{" +
                "amount=" + amount +
                ", cryptocurrencyId=" + cryptocurrencyId +
                ", price='" + price + '\'' +
                '}';
    }
}
