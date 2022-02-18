package com.crypto.tradingplatform.web.dto;

import java.math.BigDecimal;

public class OperationDto {

    private String amount;
    private Long cryptocurrencyId;
    private BigDecimal price;

    public OperationDto() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getCryptocurrencyId() {
        return cryptocurrencyId;
    }

    public void setCryptocurrencyId(Long cryptocurrencyId) {
        this.cryptocurrencyId = cryptocurrencyId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
