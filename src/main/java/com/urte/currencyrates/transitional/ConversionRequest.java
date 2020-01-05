package com.urte.currencyrates.transitional;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionRequest {

    private String code;
    private BigDecimal amount;
}
