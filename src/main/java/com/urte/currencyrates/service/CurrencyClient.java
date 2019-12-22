package com.urte.currencyrates.service;

import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.wsdl.FxRatesHandling;
import com.urte.currencyrates.wsdl.GetFxRates;
import com.urte.currencyrates.wsdl.GetFxRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyClient extends WebServiceGatewaySupport {

    public List<CurrencyByDate> getCurrenciesForPeriod() {
        List<CurrencyByDate> currenciesForPeriod = new ArrayList<>();
        LocalDate end  = LocalDate.now();
        LocalDate start = end.minusDays(5);
        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            currenciesForPeriod.addAll(getCurrenciesByDate(date));
        }
        return currenciesForPeriod;
    }

    public List<CurrencyByDate> getCurrenciesByDate(LocalDate fromDate) {
        List<CurrencyByDate> currenciesByDate = new ArrayList<>();
        JAXBElement element = (JAXBElement) getCurrencyList(fromDate)
                .getGetFxRatesResult()
                .getContent()
                .get(0);
        FxRatesHandling handling = (FxRatesHandling) element.getValue();
        handling.getFxRate().forEach(fxRateHandling -> currenciesByDate.add(new CurrencyByDate(
                LocalDate.parse(fxRateHandling.getDt().toString()),
                fxRateHandling.getCcyAmt().get(1).getCcy().toString(),
                fxRateHandling.getCcyAmt().get(1).getAmt()
        )));

        return currenciesByDate;
    }

    private GetFxRatesResponse getCurrencyList(LocalDate fromDate) {
        GetFxRates request = new GetFxRates();
        request.setTp("EU");
        request.setDt(fromDate.toString());

        return (GetFxRatesResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://www.lb.lt/webservices/FxRates/FxRates.asmx",
                        request,
                        new SoapActionCallback("http://www.lb.lt/WebServices/FxRates/getFxRates"));
    }
}
