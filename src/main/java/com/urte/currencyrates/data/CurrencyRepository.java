package com.urte.currencyrates.data;

import com.urte.currencyrates.domain.CurrencyByDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<CurrencyByDate, UUID> {

    @Query(value = "SELECT * FROM CURRENCY_BY_DATE  WHERE (CODE , DATE ) IN (SELECT CODE, Max(DATE ) FROM " +
            "CURRENCY_BY_DATE GROUP BY CODE ) ORDER BY CODE", nativeQuery = true)
    public Iterable<CurrencyByDate> selectAllForToday();

}
