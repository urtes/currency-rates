package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ResultRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void save(List<CurrencyByDate> currenciesByDate) {
        currenciesByDate.forEach(currencyByDate -> currencyRepository.save(currencyByDate));
    }

    public BigDecimal count(ResultRequest resultRequest) {
        BigDecimal amount = new BigDecimal(resultRequest.getAmount());
        CurrencyByDate currencyByDate = currencyRepository.findFirstByCodeOrderByDateDesc(resultRequest.getCode());
        BigDecimal result = currencyByDate.getRate().multiply(amount);
        return result;
    }
}
