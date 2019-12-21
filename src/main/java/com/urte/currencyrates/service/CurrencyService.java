package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.wsdl.FxRateHandling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void save(List<FxRateHandling> fxRateHandlings) {

        for(FxRateHandling fxRateHandling: fxRateHandlings) {
            currencyRepository.save(new CurrencyByDate(
                    LocalDate.parse(fxRateHandling.getDt().toString()),
                    fxRateHandling.getCcyAmt().get(1).getCcy(),
                    fxRateHandling.getCcyAmt().get(1).getAmt()
            ));
        }
    }
}
