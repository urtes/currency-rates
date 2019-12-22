package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ResultRequest;
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

    @Scheduled(cron = "${period.cron.daily}")
    public void addTodayRates() {
        List<CurrencyByDate> todayRates = currencyClient.getCurrenciesByDate(LocalDate.now());
        save(todayRates);
    }

    public void save(List<CurrencyByDate> currenciesByDate) {
        currenciesByDate.forEach(currencyByDate -> currencyRepository.save(currencyByDate));
    }

    public BigDecimal calculate(ResultRequest resultRequest) {
        BigDecimal amount = new BigDecimal(resultRequest.getAmount());
        CurrencyByDate currencyByDate = currencyRepository.findFirstByCodeOrderByDateDesc(resultRequest.getCode());
        BigDecimal result = currencyByDate.getRate().multiply(amount);
        return result;
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
