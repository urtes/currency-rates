package com.urte.currencyrates.data;

import com.urte.currencyrates.domain.CurrencyByDate;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CurrencyRepository extends CrudRepository<CurrencyByDate, UUID> {
}
