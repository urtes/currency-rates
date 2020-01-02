package com.urte.currencyrates.service;

import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.wsdl.FxRatesHandling;
import com.urte.currencyrates.wsdl.GetFxRates;
import com.urte.currencyrates.wsdl.GetFxRatesResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CurrencyClient extends WebServiceGatewaySupport {

    private static final String URI = "http://www.lb.lt/webservices/FxRates/FxRates.asmx";
    private static final String SOAP_ACTION = "http://www.lb.lt/WebServices/FxRates/getFxRates";

    public List<CurrencyByDate> getCurrenciesByDate(LocalDate localDate) {
        List<CurrencyByDate> currenciesByDate = new ArrayList<>();
        JAXBElement element = (JAXBElement) getCurrencyList(localDate)
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
                .marshalSendAndReceive(URI,
                        request,
                        new SoapActionCallback(SOAP_ACTION));
    }
}
