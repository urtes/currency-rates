package com.urte.currencyrates.transitional;

import com.urte.currencyrates.validation.CurrencyCodeConstraint;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
public class ConversionRequest {

    @CurrencyCodeConstraint
    private String code;

    @NotNull
    @Min(1L)
    private BigDecimal amount;
}
