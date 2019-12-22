package com.urte.currencyrates.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@IdClass(CurrencyByDateId.class)
public class CurrencyByDate {

    public CurrencyByDate(LocalDate date, String code, BigDecimal rate) {
        this.date = date;
        this.code = code;
        this.rate = rate;
    }

//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(updatable = false, nullable = false)
//    private UUID id;

    @Id
    private LocalDate date;

    @Id
    private String code;

    @Column(precision = 20, scale = 10)
    private BigDecimal rate;
}
