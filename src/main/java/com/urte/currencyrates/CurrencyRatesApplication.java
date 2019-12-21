package com.urte.currencyrates;

import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyClient;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.wsdl.FxRateHandling;
import com.urte.currencyrates.wsdl.FxRatesHandling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.JAXBElement;
import java.util.List;

@SpringBootApplication
public class CurrencyRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CurrencyClient currencyClient, CurrencyService currencyService) {
        return args -> {
            JAXBElement element =
                    (JAXBElement) currencyClient.getCurrencyList().getGetFxRatesResult().getContent().get(0);
            FxRatesHandling handling = (FxRatesHandling) element.getValue();

            List<FxRateHandling> fxRateHandlings = handling.getFxRate();

            currencyService.save(fxRateHandlings);

            for(FxRateHandling fxRateHandling: handling.getFxRate()) {
                System.out.format("%s %s %s\n",
                        fxRateHandling.getDt().toString(),
                        fxRateHandling.getCcyAmt().get(1).getCcy(),
                        fxRateHandling.getCcyAmt().get(1).getAmt());
            }
        };
    }
}