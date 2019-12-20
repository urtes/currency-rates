package com.urte.currencyrates.service;

import com.urte.currencyrates.wsdl.GetCurrentFxRates;
//import com.urte.currencyrates.wsdl.GetCurrentFxRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Slf4j
public class CurrencyClient extends WebServiceGatewaySupport {

//    public GetCurrentFxRatesResponse getCurrencyList() {
    public Object getCurrencyList() {
        GetCurrentFxRates request = new GetCurrentFxRates();
        request.setTp("EU");

        log.debug("TP: " + request.getTp());

//        GetCurrentFxRatesResponse response = (GetCurrentFxRatesResponse) getWebServiceTemplate()
//                .marshalSendAndReceive("http://www.lb.lt/webservices/fxrates/fxrates.asmx", request,
//                        new SoapActionCallback("http://www.lb.lt/WebServices/FxRates/getCurrentFxRates"));
//        return response;

        return getWebServiceTemplate()
                .marshalSendAndReceive("http://www.lb.lt/webservices/fxrates/fxrates.asmx", request,
                        new SoapActionCallback("http://www.lb.lt/WebServices/FxRates/getCurrentFxRates"));
    }
}
