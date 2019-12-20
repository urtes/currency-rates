package com.urte.currencyrates;

import com.urte.currencyrates.service.CurrencyClient;
import com.urte.currencyrates.wsdl.GetCurrentFxRatesResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CurrencyRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CurrencyClient currencyClient) {
        return args -> {
//            GetCurrentFxRatesResponse response = currencyClient.getCurrencyList();
            Object response = currencyClient.getCurrencyList();

            System.out.println(((GetCurrentFxRatesResponse) response).getGetCurrentFxRatesResult().getContent());
        };
    }
}