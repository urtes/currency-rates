package com.urte.currencyrates.domain;

import com.urte.currencyrates.wsdl.CcyISO4217;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class CurrencyByDate {

    public CurrencyByDate(LocalDate date, CcyISO4217 code, BigDecimal rate) {
        this.date = date;
        this.code = code;
        this.rate = rate;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    private LocalDate date;
    private CcyISO4217 code;
    private BigDecimal rate;
}
