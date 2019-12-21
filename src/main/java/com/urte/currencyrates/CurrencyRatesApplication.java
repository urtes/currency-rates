package com.urte.currencyrates;

import com.urte.currencyrates.service.CurrencyClient;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.wsdl.FxRatesHandling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.JAXBElement;
import java.time.LocalDate;

@SpringBootApplication
public class CurrencyRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CurrencyClient currencyClient, CurrencyService currencyService) {
        return args -> {
            JAXBElement element = (JAXBElement) currencyClient
                    .getCurrencyList(LocalDate.now().minusMonths(1))
                    .getGetFxRatesResult()
                    .getContent()
                    .get(0);
            FxRatesHandling handling = (FxRatesHandling) element.getValue();
            currencyService.save(handling.getFxRate());
        };
    }
}