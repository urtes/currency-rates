package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ConversionRequest;
import com.urte.currencyrates.transitional.ConversionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class CurrencyService {

    private CurrencyRepository currencyRepository;
    private CurrencyClient currencyClient;

    @Value("${period.days}")
    private int period;

    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyClient currencyClient) {
        this.currencyRepository = currencyRepository;
        this.currencyClient = currencyClient;
    }

    @PostConstruct
    public void loadInitialData() {
        List<CurrencyByDate> currenciesByDate = getCurrenciesForPeriod();
        save(currenciesByDate);
    }

    @Scheduled(cron = "${period.cron}")
    public void addTodayRates() {
        List<CurrencyByDate> todayRates = currencyClient.getCurrenciesByDate(LocalDate.now());
        save(todayRates);
    }

    public void save(List<CurrencyByDate> currenciesByDate) {
        currenciesByDate.forEach(currencyByDate -> currencyRepository.save(currencyByDate));
    }

    public ConversionResult convert(ConversionRequest conversionRequest) {
        BigDecimal amount = new BigDecimal(conversionRequest.getAmount());
        CurrencyByDate currencyByDate = currencyRepository.findFirstByCodeOrderByDateDesc(conversionRequest.getCode());
        BigDecimal rate = currencyByDate.getRate();
        BigDecimal count = rate.multiply(amount);
        return new ConversionResult(rate, count);
    }

    private List<CurrencyByDate> getCurrenciesForPeriod() {
        List<CurrencyByDate> currenciesForPeriod = new ArrayList<>();
        LocalDate end  = LocalDate.now();
        LocalDate start = end.minusDays(period);
        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            currenciesForPeriod.addAll(currencyClient.getCurrenciesByDate(date));
        }
        return currenciesForPeriod;
    }
}
