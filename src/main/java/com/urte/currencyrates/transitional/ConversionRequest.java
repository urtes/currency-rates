package com.urte.currencyrates.transitional;

import lombok.Data;

@Data
public class ConversionRequest {

    private String code;
    private Long amount;
}
