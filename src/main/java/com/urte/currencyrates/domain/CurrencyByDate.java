package com.urte.currencyrates.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@IdClass(CurrencyByDateId.class)
public class CurrencyByDate {

    @Id
    private LocalDate date;

    @Id
    private String code;

    @Column(precision = 20, scale = 10)
    private BigDecimal rate;

    public CurrencyByDate(LocalDate date, String code, BigDecimal rate) {
        this.date = date;
        this.code = code;
        this.rate = rate;
    }
}
