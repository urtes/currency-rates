package com.urte.currencyrates.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CurrencyByDateId implements Serializable {

    public CurrencyByDateId(LocalDate date, String code) {
        this.date = date;
        this.code = code;
    }

    private LocalDate date;
    private String code;
}
