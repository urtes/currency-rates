package com.urte.currencyrates;

import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyClient;
import com.urte.currencyrates.service.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CurrencyRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CurrencyClient currencyClient, CurrencyService currencyService) {
        return args -> {
            List<CurrencyByDate> currenciesByDate = currencyClient.getCurrenciesByDate(LocalDate.now());
            currencyService.save(currenciesByDate);
        };
    }
}