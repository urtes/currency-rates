package com.urte.currencyrates.transitional;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionResult {

    private BigDecimal rate;
    private BigDecimal count;

    public ConversionResult(BigDecimal rate, BigDecimal count) {
        this.rate = rate;
        this.count = count;
    }
}
