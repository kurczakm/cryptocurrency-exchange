package com.crypto.tradingplatform.web.dto;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

public class OperationDto {

    private String amount;
    private Long cryptocurrencyId;
    private BigDecimal price;
    //validation result of amount input
    private boolean correctInput;

    public OperationDto() {
        correctInput = true;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        checkAmount();
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

    public boolean isCorrectInput() {
        return correctInput;
    }

    public void setCorrectInput(boolean correctInput) {
        this.correctInput = correctInput;
    }

    private void checkAmount() {
        correctInput = NumberUtils.isCreatable(amount);
    }

    @Override
    public String toString() {
        return "OperationDto{" +
                "amount='" + amount + '\'' +
                ", cryptocurrencyId=" + cryptocurrencyId +
                ", price=" + price +
                ", correctInput=" + correctInput +
                '}';
    }
}
