package com.urte.currencyrates.config;

import com.urte.currencyrates.service.CurrencyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CurrencyConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.urte.currencyrates.wsdl");
        return marshaller;
    }

    @Bean
    public CurrencyClient currencyClient(Jaxb2Marshaller marshaller) {
        CurrencyClient currencyClient = new CurrencyClient();
        currencyClient.setDefaultUri("http://www.lb.lt/WebServices/FxRates/getCurrentFxRates");
        currencyClient.setMarshaller(marshaller);
        currencyClient.setUnmarshaller(marshaller);
        return currencyClient;
    }
}
