package com.urte.currencyrates.service;

import com.urte.currencyrates.wsdl.GetFxRates;
import com.urte.currencyrates.wsdl.GetFxRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Slf4j
public class CurrencyClient extends WebServiceGatewaySupport {

    public GetFxRatesResponse getCurrencyList() {
        GetFxRates request = new GetFxRates();
        request.setTp("EU");
        request.setDt("2019-12-21");

//        log.debug("TP: " + request.getTp());

        return (GetFxRatesResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://www.lb.lt/webservices/FxRates/FxRates.asmx",
                        request,
                        new SoapActionCallback("http://www.lb.lt/WebServices/FxRates/getFxRates"));
    }
}
