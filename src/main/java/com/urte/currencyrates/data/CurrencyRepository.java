package com.urte.currencyrates.data;

import com.urte.currencyrates.domain.CurrencyByDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<CurrencyByDate, UUID> {

}
